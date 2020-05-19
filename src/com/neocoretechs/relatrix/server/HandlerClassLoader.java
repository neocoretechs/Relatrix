package com.neocoretechs.relatrix.server;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteKeysetIterator;
/**
* This is a generic ClassLoader of which many examples abound.
* We do some tricks with resolution from a hashtable of names and bytecodes,
* and InputStreams as well.</p>
* If you want to find a class and throw an exception if its not in the available cache, used findClass.<br/>
* If you want to load a class by all means available and make it available, use loadClass.<p/>
* This is to support the Relatrix JAR load, and remote classloading.
* We can operate in embedded mode, or remote using a client to retrieve bytecode from server.
* @author Groff (C) NeoCoreTechs 1999, 2000, 2020
*/
public class HandlerClassLoader extends ClassLoader
{
    private static ConcurrentHashMap<String,Class> cache = new ConcurrentHashMap<String,Class>();
    private static ConcurrentHashMap<String, byte[]> classNameAndBytecodes = new ConcurrentHashMap<String, byte[]>();
    private static boolean useEmbedded = false;
    public static String defaultPath = "/etc/"; // bytecode repository path
    public static RelatrixKVClient remoteRepository = null;
 
    
    public HandlerClassLoader()
    {
//        System.out.println("HandlerClassLoader "+this);
    }
    
    public HandlerClassLoader(ClassLoader parent)
    {
//        System.out.println("HandlerClassLoader "+this);
    }
    
    public static void connectToRemoteRepository(String local, String remote, int port) throws IOException, IllegalAccessException {
    	useEmbedded = false;
    	remoteRepository = new RelatrixKVClient(local, remote, port);
    }
    
    public static void connectToLocalRepository(String path) throws IOException, IllegalAccessException {
    	useEmbedded = true;
    	if(path != null) {
    		if(!path.endsWith("/"))
    			path += "/";
    		defaultPath = path;
    	}
    	BigSackAdapter.setTableSpaceDir(defaultPath+"BytecodeRepository/Bytecodes");
    }
    /**
    * Find a class by the given name
    */
    public synchronized Class findClass(String name) throws ClassNotFoundException {
        Class c;
        if ( (c = cache.get(name)) != null) {
//                System.out.println(this+" HandlerClassLoader.findClass return cache.get "+name);
                return c;
        }
        throw new ClassNotFoundException(name+" not found in HandlerClassLoader.findClass()");
    }
    /**
    * loadClass will attempt to load the named class, If not found in cache
    * or system or user, will attempt to use Hastable of name and bytecodes
    * set up from defineClasses.  defineClass will call this on attempting
    * to resolve a class, so we have to be ready with the bytes.
    * @param name The name of the class to load
    * @param resolve true to call resolveClass()
    * @return The resolved Class Object
    * @throws ClassNotFoundException If we can't load the class from system, or loaded, or cache
    */
    public synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        System.out.println(this+" HandlerClassLoader.LoadClass enter "+name);
        Class c = null;
        try {
        	 c = Class.forName(name); // can it be loaded by normal means? and initialized?
        	 return c;
        } catch(Exception e) {
//          System.out.println("HandlerClassLoader Class.forName("+name+") exception "+e.getMessage());
        }
        try {
            c = findSystemClass(name);
        } catch (Exception e) {
//              System.out.println("HandlerClassLoader.findSystemClass exception "+e.getMessage());
        }
        if (c == null)
            c = cache.get(name);
        else {
//            System.out.println(this+" HandlerClassLoader.LoadClass exit found sys class "+name+" resolve="+resolve);
            return c;
        }
        if (c == null)
            c = findLoadedClass(name);
        else {
//            System.out.println(this+" HandlerClassLoader.LoadClass exit cache get "+name+" resolve="+resolve);
            return c;
        }
        // this is our last chance, otherwise noClassDefFoundErr and we're screwed
        if (c == null) {
                byte[] bytecodes = classNameAndBytecodes.get(name);
                System.out.println("Attempt to retrieve "+name+" from classNameAndBytecodes");              
                if( bytecodes == null ) {
                        // grab it from repository
                        try {
                                bytecodes = getBytesFromRepository(name);
                                if( bytecodes == null) {
                                        // blued and tattooed
                                        System.out.println(this+" HandlerClassLoader.LoadClass bytecode not found in repository for "+name);
                                        throw new ClassNotFoundException("The requested class: "+name+" can not be found on any resource path");
//                                      return null;
                                }
                        } catch(Exception e) {
                                throw new ClassNotFoundException("The requested class: "+name+" can not be found on any resource path");
                        }
                }
                c = defineClass(name, bytecodes, 0, bytecodes.length);
                System.out.println("Putting class "+name+" of class "+c+" to cache with "+bytecodes.length+" bytes");
                cache.put(name, c);
        } else {
               System.out.println(this+" HandlerClassLoader.LoadClass exit found loaded "+name+" resolve="+resolve);
               return c;
        }
        //if (resolve)
            resolveClass(c);
        System.out.println(this+" HandlerClassLoader.LoadClass exit resolved "+name+" resolve="+resolve);
        return c;
    }
    /**
    * Define a single class by name and byte array.
    * We will attempt to liberate it from the cache first; if it's not
    * there, we go defining.
    * @param name The class name
    * @param data The byte array to get bytecodes from
    */
    public Class defineAClass(String name, byte data[]) {
        return defineAClass(name, data, 0, data.length);
    }
    /**
    * Define a single class by name and position in byte array.
    * We will attempt to liberate it from the cache first; if it's not
    * there, we go defining.
    * @param name The class name
    * @param data The byte array to get bytecodes from
    * @param offset The offset to above array
    * @param length The length of bytecodes at offset
    */
    public synchronized Class defineAClass(String name, byte data[], int offset, int length) {
//        System.out.println("HandlerClassLoader.defineAClass enter "+name);
        Class c;
//        if ( (c = (Class)cache.get(name)) != null) {
//                System.out.println("HandlerClassLoader.defineAClass return cache.get "+name);
//                return c;
//        }
        // force an update
        classNameAndBytecodes.put(name, data); // fix later for offset
        c = defineClass(name, data, offset, length);
        cache.put(name, c);
//        System.out.println("HandlerClassLoader.defineAClass return cache.put "+name);
        return c;
    }
  
    /**
    * Define classes from byte array JAR
    * @param jarFile The JAR byte array
    */
    public synchronized void defineClasses(byte jarFile[]) {
           defineClassStream(new ByteArrayInputStream(jarFile));
    }
    /**
    * Define a set of classes from JAR input stream
    * @param in the inputstream with JAR format
    */
    public synchronized void defineClassStream(InputStream in) {
        // we can't seem to get size from JAR, so big buf
        byte[] bigbuf = new byte[500000];
        try
        {
            ZipInputStream zipFile = new ZipInputStream(in);
            for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry =zipFile.getNextEntry())
            {
                if (!entry.isDirectory() || entry.getName().indexOf("META-INF") == -1 )
                {
                	if( entry.getName().endsWith(".class") ) {
                		String entryName = entry.getName().replace('/', '.');
//                    System.out.println(String.valueOf(entry.getSize()));
                		entryName = entryName.substring(0,entryName.length()-6);
                		int i = 0;
                		int itot = 0;
                		while( i != -1 ) {
                			i= zipFile.read(bigbuf,itot,bigbuf.length-itot);
                			if( i != -1 ) itot+=i;
                		}
                        System.out.println("JAR Entry "+entryName+" read "+String.valueOf(itot));
                        // move it right size buffer cause it's staying around
                        byte bytecode[] = new byte[itot];
                        System.arraycopy(bigbuf, 0, bytecode, 0, itot);
                        defineAClass(entryName, bytecode);
                	}
                }
                zipFile.closeEntry();
            }

            //
        } catch(Exception e) {
                System.out.println("HandlerClassLoader.defineClassStream failed "+e.getMessage());
                e.printStackTrace();
        }
    }
    /**
    * Retrieve the bytecodes from BigSack repository
    * @param name The class name to get
    * @return The byte array or null
    */
    public static byte[] getBytesFromRepository(String name) {
        byte[] retBytes = null;
       	ClassNameAndBytes cnab = new ClassNameAndBytes(name, retBytes);
//        System.out.println("HandlerClassLoader.getBytesFromRepository "+name);
        try {
        	if(useEmbedded) {
        	 	TransactionalTreeMap localRepository = BigSackAdapter.getBigSackMapTransaction(String.class); // class type of key
           		System.out.println("Attempting get from local repository "+localRepository);
                cnab = (ClassNameAndBytes) localRepository.get(name);	
        	} else {
          		System.out.println("Attempting get from remote repository "+remoteRepository);
        		cnab = (ClassNameAndBytes) remoteRepository.get(name);
        	}
            if( cnab != null ) {
            	if(cnab.getBytes() == null)
            		System.out.println("Bytecode payload from remote repository "+remoteRepository+" came back null");
            	else
            		System.out.println("Bytecode payload returned "+cnab.getBytes().length+" bytes from remote repository "+remoteRepository);
            	return cnab.getBytes();
            } else
            	System.out.println("Failed to return bytecodes from remote repository "+remoteRepository);
        } catch(Exception e) {
                e.printStackTrace();
        }
        return null;
   }
    /**
    * Put the bytecodes to BigSack repository.  This function to be
    * performed outside of class loading cause it happens rarely.
    * @param name The class name to put
    * @param bytes The associated bytecode array
    */
    public static void setBytesInRepository(String name, byte[] bytes) {
//        System.out.println("HandlerClassLoader.setBytesInRepository "+name);
    	ClassNameAndBytes cnab = new ClassNameAndBytes(name, bytes);
        try {
        	if(useEmbedded) {
        	 		TransactionalTreeMap localRepository = BigSackAdapter.getBigSackMapTransaction(String.class); // class type of key
        			localRepository.put(name, cnab);
                   	BigSackAdapter.commitMap(String.class);
					System.out.println("Stored and committed bytecode in local repository for class:"+name);
        	} else {
        		if(remoteRepository != null)
					try {
						remoteRepository.transactionalStore(name, cnab);
						remoteRepository.transactionCommit(String.class);
						System.out.println("Stored and committed bytecode in remote repository for class:"+name);
					} catch (DuplicateKeyException dce) {
						System.out.println("Removing existing bytecode in remote repository prior to replace for class "+name);
						remoteRepository.remove(name);
						try {
							remoteRepository.transactionalStore(name, cnab);
							remoteRepository.transactionCommit(String.class);
							System.out.println("Replaced and committed bytecode in remote repository for class:"+name);
						} catch (DuplicateKeyException e) {}
					}
        		else
        			System.out.println("REMOTE REPOSITORY HAS NOT BEEN DEFINED!, NO ADDITION POSSIBLE!");
        	}
        } catch(IOException | ClassNotFoundException | IllegalAccessException e ) {
                e.printStackTrace();
                if( useEmbedded )
					try {
						System.out.println("Rolling back bytecode in local repository for class:"+name);
					 	BigSackAdapter.rollbackMap(String.class); // class type of key
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                else
                	try {
    					System.out.println("Rolling back bytecode in remote repository for class:"+name);
                		remoteRepository.transactionRollback(String.class);
                	} catch (IOException e1) {
						e1.printStackTrace();
					}
        }
 
   }
    /**
     * Remove all classes STARTING WITH the given name, use caution.
     * @param name The value that the class STARTS WITH, to remove packages at any level desired
     */
    public static void removeBytesInRepository(String name) {
//      System.out.println("HandlerClassLoader.removeBytesInRepository "+name);
      try {
      	if(useEmbedded) {
      		ArrayList<String> remo = new ArrayList<String>();
      	 	TransactionalTreeMap localRepository = BigSackAdapter.getBigSackMapTransaction(String.class); // class type of key
      			Iterator<?> it = localRepository.keySet();
      			while(it.hasNext()) {
      				Comparable key = (Comparable) it.next();
      				if( ((String)key).startsWith(name))
      					remo.add((String) key);
      			}
      			for(String s: remo) {
      				localRepository.remove(s);
      				classNameAndBytecodes.remove(s);
      				cache.remove(s);
      				System.out.println("Removed bytecode for class:"+s);
      			}
                BigSackAdapter.commitMap(String.class);
      	} else {
      		if(remoteRepository != null) {
      	      		ArrayList<String> remo = new ArrayList<String>();
      	      			RemoteKeysetIterator it = remoteRepository.keySet(String.class);
      	      			while(remoteRepository.hasNext(it)) {
      	      				Comparable key = (Comparable) remoteRepository.next(it);
      	      				if( ((String)key).startsWith(name))
      	      					remo.add((String) key);
      	      			}
      	      			for(String s: remo) {
      	      				remoteRepository.remove(s);
      	     				classNameAndBytecodes.remove(s);
      	      				cache.remove(s);
      	      				System.out.println("Removed bytecode for class:"+s);
      	      			}
      	                remoteRepository.transactionCommit(String.class);
  
      		} else
	      		System.out.println("REMOTE REPOSITORY HAS NOT BEEN DEFINED!, NO REMOVAL POSSIBLE!");
      	}
      } catch(IOException | ClassNotFoundException | IllegalAccessException e ) {
              System.out.println(e);
              e.printStackTrace();
              if( useEmbedded )
					try {
						BigSackAdapter.rollbackMap(String.class);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
              else
              	try {
              		remoteRepository.transactionRollback(String.class);
              	} catch (IOException e1) {
						e1.printStackTrace();
				}
      }

 }

    /**
    * Put the bytecodes to BigSack repository.  This function to be
    * performed outside of class loading cause it happens rarely.
    * @param name The class name to put
    * @param bytes The associated bytecode array
     * @throws FileNotFoundException 
    */
    public static void setBytesInRepositoryFromJar(String jarFile) throws FileNotFoundException {
//        System.out.println("HandlerClassLoader.setBytesInRepository jarFile");
    	File file = new File(jarFile);
    	byte[] bigbuf = new byte[500000];
        try (  	FileInputStream f = new FileInputStream(file);
        		ZipInputStream zipFile = new ZipInputStream(f)) {
            for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry())
            {
                if (!entry.isDirectory() || entry.getName().indexOf("META-INF") == -1 )
                {
                	if( entry.getName().endsWith(".class") ) {
                    String entryName = entry.getName().replace('/', '.');
//                    System.out.println(String.valueOf(entry.getSize()));
                    entryName = entryName.substring(0,entryName.length()-6);
                    int i = 0;
                    int itot = 0;
                    while( i != -1 ) {
                        i= zipFile.read(bigbuf,itot,bigbuf.length-itot);
                        if( i != -1 ) itot+=i;
                    }
//                        System.out.println("JAR Entry "+entryName+" read "+String.valueOf(itot));
                    // move it right size buffer cause it's staying around
                    byte bytecode[] = new byte[itot];
                    System.arraycopy(bigbuf, 0, bytecode, 0, itot);
                    setBytesInRepository(entryName, bytecode);
	                System.out.println("Loading bytecode for JAR Entry "+entryName+" read "+bytecode.length);
                	}
                }
                zipFile.closeEntry();
            }
            //
        } catch(Exception e) {
                System.out.println("HandlerClassLoader.setBytesInRepository failed "+e.getMessage());
                e.printStackTrace();
        }
   }
    /**
     * Load the classes in the designated directory path into the repository for defining classes.
     * The use case here is if we are running a server and wish to define new classes, we wont have to bounce it, or
     * copy files, or JAR files and copy them and bounce a server. Remember that when loading class, ALL dependencies must be loaded
     * at once, or various errors including NPEs from loadClass due to not locating bytecode can occur.
     * @param packg the package designation for loaded/loading classes
     * @param dir The directory to load class files
     * @throws IOException If the directory is not valid
     */
 	public static void setBytesInRepository(String packg, Path dir) throws IOException {
 		System.out.println("Attempting to load class files for package:"+packg+" from path:"+dir);
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{class}")) {
	    	   if(stream != null)
	           for (Path entry: stream) {
	        	   System.out.println("Found file:"+entry);
	        	   String entryName = entry.getFileName().toString().replace('/', '.');
//	              System.out.println(String.valueOf(entry.getSize()));
	        	   if(packg.length() > 1) // account for default package
	        		   entryName = packg+"."+entryName.substring(0,entryName.length()-6);
	        	   else
	        		   entryName = entryName.substring(0,entryName.length()-6);	
	        	   System.out.println("Processing class "+entryName);
	        	   //System.out.println(entryName);
	        	   byte[] bytes = Files.readAllBytes(entry);
	        	   setBytesInRepository(entryName,  bytes); // chicken and egg, egg, or chicken
	               System.out.println("Loading bytecode for File Entry "+entryName+" read "+bytes.length);
	           }
	    	   else
	    		   System.out.println("No class files available from path "+dir);
	       } catch (DirectoryIteratorException ex) {
	           // I/O error encounted during the iteration, the cause is an IOException
	           throw ex.getCause();
	       }
	}
 	
    public static void main(String[] args) throws IOException {
    	Path p = FileSystems.getDefault().getPath("C:/users/jg/workspace/volvex/bin/com/neocoretechs/volvex");
    	setBytesInRepository("com.neocoretechs.volvex",p);

    }
}
