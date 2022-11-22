package com.neocoretechs.relatrix.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.session.RockSackAdapter;
import com.neocoretechs.rocksack.session.TransactionalMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteKeySetIterator;

/**
* This is a generic ClassLoader of which many examples abound.
* We do some tricks with resolution from a hashtable of names and bytecodes,
* and InputStreams as well.</p>
* If you want to find a class and throw an exception if its not in the available cache, used findClass.<br/>
* If you want to load a class by all means available and make it available, use loadClass.<p/>
* This is to support the Relatrix JAR load, and remote classloading.
* We can operate in embedded mode, or remote using a client to retrieve bytecode from server.
* @author Jonathan Groff (C) NeoCoreTechs 1999, 2000, 2020
*/
public class HandlerClassLoader extends ClassLoader {
	private static boolean DEBUG = false;
	private static boolean DEBUGSETREPOSITORY = true;
    private static ConcurrentHashMap<String,Class> cache = new ConcurrentHashMap<String,Class>();
    private static ConcurrentHashMap<String, byte[]> classNameAndBytecodes = new ConcurrentHashMap<String, byte[]>();
    private static boolean useEmbedded = false;
    public static String defaultPath = "/etc/"; // bytecode repository path
    public static RelatrixClientInterface remoteRepository = null;
    private ClassLoader parent = null;
	static int size;
   
    public HandlerClassLoader() { }
    /**
     * Variation when specified on command line as -Djava.system.class.loader=com.neocoretechs.relatrix.server.HandlerClassLoader 
     * System environment variable RemoteClassLoader is set as remote node name, port is assumed default of 9999
     * @param parent
     */
    public HandlerClassLoader(ClassLoader parent) {
    	super(parent);
    	this.parent = parent;
    	if(DEBUG)
    		System.out.println("DEBUG: c'tor: HandlerClassLoader with parent:"+parent);
       	try {
    		String remote = System.getenv("RemoteClassLoader");
    		if(remote != null) {
    			String hostName = InetAddress.getLocalHost().getHostName();
    			connectToRemoteRepository(hostName, remote, 9999);
    		}
		} catch (IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * Variation when local and remote are assumed to be this node and port is default 9999
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void connectToRemoteRepository() throws IOException, IllegalAccessException {
    	useEmbedded = false;
		String hostName = InetAddress.getLocalHost().getHostName();
    	remoteRepository = new RelatrixKVClient(hostName, hostName, 9999);
    } 
    /**
     * Variation when remote is located on a different node, port is still assumed the default of 9999
     * @param remote
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void connectToRemoteRepository(String remote) throws IOException, IllegalAccessException {
    	useEmbedded = false;
		String hostName = InetAddress.getLocalHost().getHostName();
    	remoteRepository = new RelatrixKVClient(hostName, remote, 9999);
    } 
    /**
     * Variation when remote is different node, and port has been set to something other than standard default
     * @param remote
     * @param port
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void connectToRemoteRepository(String remote, int port) throws IOException, IllegalAccessException {
    	useEmbedded = false;
		String hostName = InetAddress.getLocalHost().getHostName();
    	remoteRepository = new RelatrixKVClient(hostName, remote, port);
    } 
    /**
     * Variation when everything is different, somehow
     * @param local
     * @param remote
     * @param port
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void connectToRemoteRepository(String local, String remote, int port) throws IOException, IllegalAccessException {
    	useEmbedded = false;
    	remoteRepository = new RelatrixKVClient(local, remote, port);
    }
    /**
     * Local repository for embedded mode, no remote server
     * @param path Path to tablespace and log parent directories
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void connectToLocalRepository(String path) throws IOException, IllegalAccessException {
    	useEmbedded = true;
    	if(path != null) {
    		if(!path.endsWith("/"))
    			path += "/";
    		defaultPath = path;
    	}
    	RockSackAdapter.setTableSpaceDir(defaultPath+"BytecodeRepository/Bytecodes");
    }
    /**
    * Find a class by the given name
    */
    public synchronized Class findClass(String name) throws ClassNotFoundException {
        Class c;
        if ( (c = cache.get(name)) != null) {
        	if(DEBUG)
                System.out.println("DEBUG:"+this+".findClass("+name+") return cache.get");
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
    	if(DEBUG)
    		System.out.println("DEBUG:"+this+".loadClass("+name+")");
        Class c = null;
        try {
        	 c = Class.forName(name); // can it be loaded by normal means? and initialized?
        	 return c;
        } catch(Exception e) {
        	if(DEBUG) {
        		System.out.println("DEBUG:"+this+".loadClass Class.forName("+name+") exception "+e);
        		e.printStackTrace();
        	}
        } 
        try {
            c = findSystemClass(name);
        } catch (Exception e) {
        	if(DEBUG) {
              System.out.println("DEBUG:"+this+".loadClass findSystemClass("+name+") exception "+e);
              e.printStackTrace();
        	}
        }
        if (c == null) {
            c = cache.get(name);
        } else {
        	if(DEBUG)
        		System.out.println("DEBUG:"+this+".loadClass exit found sys class "+name+" resolve="+resolve);
            return c;
        }
        if (c == null) {
            c = findLoadedClass(name);
        } else {
        	if(DEBUG)
        		System.out.println("DBUG:"+this+".loadClass exit cache hit:"+c+" for "+name+" resolve="+resolve);
            return c;
        }
        // this is our last chance, otherwise noClassDefFoundErr and we're screwed
        if (c == null) {
                byte[] bytecodes = classNameAndBytecodes.get(name);
                if(DEBUG)
                	System.out.println("DEBUG: "+this+" Attempt to retrieve "+name+" from classNameAndBytecodes");              
                    // grab it from repository
                try {
                    bytecodes = getBytesFromRepository(name);
                    if( bytecodes == null) {
                    	  // blued and tattooed
                    	  if(DEBUG)
                    		  System.out.println(this+".LoadClass bytecode not found in repository for "+name);
                          throw new ClassNotFoundException("The requested class: "+name+" can not be found on any resource path");
                    }
                } catch(Exception e) {
                	throw new ClassNotFoundException("The requested class: "+name+" can not be found on any resource path");
                }
                c = defineClass(name, bytecodes, 0, bytecodes.length);
                if(DEBUG)
                	System.out.println("DEBUG:"+this+" Putting class "+name+" of class "+c+" to cache with "+bytecodes.length+" bytes");
                cache.put(name, c);
        } else {
        	if(DEBUG)
               System.out.println("DEBUG:"+this+".loadClass exit found loaded "+name+" resolve="+resolve);
            	return c;
        }
        //if (resolve)
            resolveClass(c);
            if(DEBUG)
        	   System.out.println("DEBUG:"+this+".loadClass exit resolved "+name+" resolve="+resolve);
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
    
    public synchronized void defineClasses(String jarFile) throws IOException {
    	defineClasses(new JarFile(jarFile));
    }
    
    public synchronized void defineClasses(JarFile jarFile) throws IOException {
        Enumeration<JarEntry> e = jarFile.entries();
        byte[] buffer = new byte[4096];
        while (e.hasMoreElements()) {
          JarEntry entry = e.nextElement();
          String entryname = entry.getName();
          if (!entry.isDirectory() && entryname.endsWith(".class")) {
            String classname = entryname.substring(0, entryname.length() - 6);
            if (classname.startsWith("/")) {
              classname = classname.substring(1);
            }
            classname = classname.replace('/', '.');
            //entry.getSize();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int num;
            try {
            	InputStream data = jarFile.getInputStream(entry);
                while ((num = data.read(buffer)) > 0) {
                  baos.write(buffer, 0, num);
                }
                baos.flush();
              Class<?> c = defineAClass(classname, baos.toByteArray(), 0, baos.size());
            } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {}
          }
        }
  } 

    /**
    * Define classes from byte array JAR
    * @param jarFile The JAR byte array
    */
    @Deprecated
    public synchronized void defineClasses(byte jarFile[]) {
           defineClassStream(new ByteArrayInputStream(jarFile));
    }
    /**
    * Define a set of classes from JAR input stream
    * @param in the inputstream with JAR format
    */
    @Deprecated
    public synchronized void defineClassStream(InputStream in) {
        // we can't seem to get size from JAR, so big buf
        byte[] bigbuf = new byte[500000];
        try {
            ZipInputStream zipFile = new ZipInputStream(in);
            for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry =zipFile.getNextEntry()) {
                if (!entry.isDirectory() || entry.getName().indexOf("META-INF") == -1 ) {
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
    * Retrieve the bytecodes from RockSack repository
    * @param name The class name to get
    * @return The byte array or null
    */
    public static byte[] getBytesFromRepository(String name) throws BytecodeNotFoundInRepositoryException {
        byte[] retBytes = null;
       	ClassNameAndBytes cnab = new ClassNameAndBytes(name, retBytes);
   	 	if(DEBUG)
	 		System.out.println("DEBUG: HandlerClassLoader.getBytesFromRepository Attempting get for "+name);
        try {
        	if(useEmbedded) {
        	 	BufferedMap localRepository = RockSackAdapter.getRockSackMap(String.class); // class type of key
        	 	if(DEBUG)
        	 		System.out.println("DEBUG: HandlerClassLoader.getBytesFromRepository Attempting get from local repository "+localRepository);
                cnab = (ClassNameAndBytes) localRepository.get(name);	
        	} else {
           	 	if(DEBUG)
        	 		System.out.println("DEBUG: HandlerClassLoader.getBytesFromRepository Attempting get from remote repository "+remoteRepository);
        		cnab = (ClassNameAndBytes) remoteRepository.get(name);
        	}
            if( cnab != null ) {
            	if(cnab.getBytes() == null) {
               	 	if(DEBUG)
            	 		System.out.println("DEBUG: HandlerClassLoader.getBytesFromRepository Bytecode payload from remote repository "+remoteRepository+" came back null");
            	} else {
               	 	if(DEBUG)
            	 		System.out.println("DEBUG: HandlerClassLoader.getBytesFromRepository Bytecode payload returned "+cnab.getBytes().length+" bytes from remote repository "+remoteRepository);
               	 	return cnab.getBytes();
            	}
            } else {
            	System.out.println("Failed to return bytecodes from remote repository "+remoteRepository);
            	throw new BytecodeNotFoundInRepositoryException("Failed to return bytecodes from remote repository "+remoteRepository);
            }
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
 	 	if(DEBUG)
	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository for "+name);
    	ClassNameAndBytes cnab = new ClassNameAndBytes(name, bytes);
 		BufferedMap localRepository = null;
        try {
        	if(useEmbedded) {
        	 		localRepository = RockSackAdapter.getRockSackMap(String.class); // class type of key
        			localRepository.put(name, cnab);
                   	//localRepository.Commit();
               	 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Stored and committed bytecode in local repository for class:"+name);
        	} else {
        		if(remoteRepository != null) {
					try {
						remoteRepository.transactionalStore(name, cnab);
						remoteRepository.transactionCommit(String.class);
	             	 	if(DEBUG || DEBUGSETREPOSITORY)
	            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Stored and committed bytecode in remote repository for class:"+name);
					} catch (DuplicateKeyException dce) {
	             	 	if(DEBUG || DEBUGSETREPOSITORY)
	            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Removing existing bytecode in remote repository prior to replace for class "+name);
						remoteRepository.remove(name);
						try {
							remoteRepository.transactionalStore(name, cnab);
							remoteRepository.transactionCommit(String.class);
		             	 	if(DEBUG || DEBUGSETREPOSITORY)
		            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Replaced and committed bytecode in remote repository for class:"+name);
						} catch (DuplicateKeyException e) {}
					}
        		} else {
        			System.out.println("REMOTE REPOSITORY HAS NOT BEEN DEFINED!, NO ADDITION POSSIBLE!");
        		}
        	}
        } catch(IOException | ClassNotFoundException | IllegalAccessException e ) {
                e.printStackTrace();
                if( useEmbedded ) {
					if(DEBUG || DEBUGSETREPOSITORY)
						System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Rolling back bytecode in local repository for class:"+name);
					//localRepository.Rollback(); 
                } else {
                	try {
	             	 	if(DEBUG || DEBUGSETREPOSITORY)
	            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Rolling back bytecode in remote repository for class:"+name);
                		remoteRepository.transactionRollback(String.class);
                	} catch (IOException e1) {
						e1.printStackTrace();
					}
                }
        }
 
   }
    /**
     * Remove all classes STARTING WITH the given name, use caution.
     * @param name The value that the class STARTS WITH, to remove packages at any level desired
     */
    public static void removeBytesInRepository(String name) {
    	BufferedMap localRepository = null;
 	 	if(DEBUG || DEBUGSETREPOSITORY)
	 		System.out.println("DEBUG: HandlerClassLoader.removeBytesInRepository for "+name);
      try {
      	if(useEmbedded) {
      		ArrayList<String> remo = new ArrayList<String>();
      	 	localRepository = RockSackAdapter.getRockSackMap(String.class); // class type of key
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
             	 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.removeBytesInRepository Removed bytecode for class:"+s);
      			}
                //localRepository.Commit();
      	} else {
      		if(remoteRepository != null) {
      	      		ArrayList<String> remo = new ArrayList<String>();
      	      			RemoteKeySetIterator it = remoteRepository.keySet(String.class);
      	      			while(remoteRepository.hasNext(it)) {
      	      				Comparable key = (Comparable) remoteRepository.next(it);
      	      				if( ((String)key).startsWith(name))
      	      					remo.add((String) key);
      	      			}
      	      			for(String s: remo) {
      	      				remoteRepository.remove(s);
      	     				classNameAndBytecodes.remove(s);
      	      				cache.remove(s);
		             	 	if(DEBUG || DEBUGSETREPOSITORY)
		            	 		System.out.println("DEBUG: HandlerClassLoader.removeBytesInRepository Removed bytecode for class:"+s);
      	      			}
      	                //remoteRepository.transactionCommit(String.class);
  
      		} else
	      		System.out.println("REMOTE REPOSITORY HAS NOT BEEN DEFINED!, NO REMOVAL POSSIBLE!");
      	}
      } catch(IOException | ClassNotFoundException | IllegalAccessException e ) {
              System.out.println(e);
              e.printStackTrace();
              //if( useEmbedded )
					//try {
						//localRepository.Rollback();
					//} catch (IOException e1) {
						//e1.printStackTrace();
					//}
              //else
              	//try {
              	//	remoteRepository.transactionRollback(String.class);
              	//} catch (IOException e1) {
				//		e1.printStackTrace();
				//}
      }

 }

    /**
    * Put the bytecodes to RockSack repository.  This function to be
    * performed outside of class loading cause it happens rarely.
    * @param name The class name to put
    * @param bytes The associated bytecode array
     * @throws FileNotFoundException 
    */
    public static void setBytesInRepositoryFromJar(String jarFile) throws IOException, FileNotFoundException {
 	 	if(DEBUG || DEBUGSETREPOSITORY)
	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar for JAR file:"+jarFile);
    	try (JarFile file = new JarFile(jarFile)) {
    	file.stream().forEach(entry-> {
                if (!entry.isDirectory() && !entry.getName().contains("META-INF") && entry.getName().endsWith(".class")) {
                 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar for JAR file entry:"+entry.getName());
                    String entryName = entry.getName().replace('/', '.');
             	 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar size:"+String.valueOf(entry.getSize()));
                    entryName = entryName.substring(0,entryName.length()-6);
                   	byte[] bigbuf = new byte[(int) entry.getSize()];
                   	InputStream istream = null;
					try {
						istream = file.getInputStream(entry);
					} catch (IOException e) {
						e.printStackTrace();
					}
                    int i = 0;
                    int itot = 0;
                    while( itot < bigbuf.length ) {
                        try {
							i = istream.read(bigbuf,itot,bigbuf.length-itot);
						} catch (IOException e) {
							e.printStackTrace();
						}
                        if( i != -1 ) 
                        	itot+=i;
                        else
                        	break;
                    }
             	 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar JAR Entry "+entryName+" read "+String.valueOf(itot));
                    // move it right size buffer cause it's staying around
                    //byte bytecode[] = new byte[itot];
                    //System.arraycopy(bigbuf, 0, bytecode, 0, itot);
                    setBytesInRepository(entryName, bigbuf);//bytecode);
             	 	if(DEBUG || DEBUGSETREPOSITORY)
            	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar Loading bytecode for JAR Entry "+entryName+" read "+bigbuf.length);//bytecode.length);
                }
    	});
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
 	 	if(DEBUG || DEBUGSETREPOSITORY)
	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Attempting to load class files for package:"+packg+" from path:"+dir);
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{class}")) {
	    	   if(stream != null) {
	    		   for (Path entry: stream) {
	        	 	if(DEBUG || DEBUGSETREPOSITORY)
	        	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Found file:"+entry);
	        	    String entryName = entry.getFileName().toString().replace('/', '.');
	        	 	if(DEBUG || DEBUGSETREPOSITORY)
	        	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository size:"+String.valueOf(entry.toFile().length()));
	        	    if(packg.length() > 1) // account for default package
	        		   entryName = packg+"."+entryName.substring(0,entryName.length()-6);
	        	    else
	        		   entryName = entryName.substring(0,entryName.length()-6);
	        	 	if(DEBUG || DEBUGSETREPOSITORY)
	        	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Processing class "+entryName);
	        	    byte[] bytes = Files.readAllBytes(entry);
	        	    setBytesInRepository(entryName,  bytes); // chicken and egg, egg, or chicken
	        	 	if(DEBUG || DEBUGSETREPOSITORY)
	        	 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepository Loading bytecode for File Entry "+entryName+" read "+bytes.length);
	    		   }
	    	   } else
	    	 	 	if(DEBUG || DEBUGSETREPOSITORY)
	    		 		System.out.println("DEBUG: HandlerClassLoader.setBytesInRepositoryFromJar No class files available from path "+dir);
	       } catch (DirectoryIteratorException ex) {
	           // I/O error encountered during the iteration, the cause is an IOException
	           throw ex.getCause();
	       }
	}
 	
    public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException, ClassNotFoundException {
    	//Path p = FileSystems.getDefault().getPath("C:/users/jg/workspace/volvex/bin/com/neocoretechs/volvex");
    	//setBytesInRepository("com.neocoretechs.volvex",p);
    	size = 0;
    	switch(args.length) {
    		case 0:
    			connectToRemoteRepository();
    			break;
    		case 1:
    			connectToRemoteRepository(args[0]);
    			break;
    		case 2:
    			connectToRemoteRepository(args[0], Integer.parseInt(args[1]));
    			break;
    		case 3:
    			connectToRemoteRepository(args[0], args[1], Integer.parseInt(args[2]));
    			break;
    		default:
    			System.out.println("Number of arguments is "+args.length+", using first 3 for local host, remote host, remote port...");
       			connectToRemoteRepository(args[0], args[1], Integer.parseInt(args[2]));
    			break;
    	}
    	remoteRepository.entrySetStream(String.class).of().forEach(e-> {
    		System.out.printf("Class: %s size:%d%n",((ClassNameAndBytes)((Map.Entry)e).getValue()).getName(),
    			((ClassNameAndBytes)((Map.Entry)e).getValue()).getBytes().length);
    		size += ((ClassNameAndBytes)((Map.Entry)e).getValue()).getBytes().length;
    	});
    	System.out.printf("Total size=%d%n",size);
    	remoteRepository.close();
    }
}
