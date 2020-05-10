package com.neocoretechs.relatrix.server;
import java.io.*;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.*;

import com.neocoretechs.bigsack.session.BigSackSession;
import com.neocoretechs.bigsack.session.SessionManager;
/**
* This is a generic ClassLoader of which many examples abound.
* We do some tricks with resolution from a hashtable of names and bytecodes,
* and InputStreams as well.
* This is to support the Relatrix JAR load, and remote classloading.
* We can operate in embedded mode, or remote using a client to retrieve bytecode from server.
* @author Groff (C) NeoCoreTechs 1999, 2000, 2020
*/
public class HandlerClassLoader extends ClassLoader
{
    private ConcurrentHashMap<String,Class> cache = new ConcurrentHashMap<String,Class>();
    private Hashtable<String, byte[]> classNameAndBytecodes = new Hashtable<String, byte[]>();
    private Class retc;
    private static boolean useEmbedded = false;
    public static String path = "/jserv/"; // bytecode repository path

    public HandlerClassLoader()
    {
//        System.out.println("HandlerClassLoader "+this);
    }
    /**
    * Find a class by the given name
    */
    protected Class findClass(String name) throws ClassNotFoundException {
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
                if( useEmbedded && bytecodes == null ) {
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
                cache.put(name, c);
        } else {
//                System.out.println(this+" HandlerClassLoader.LoadClass exit found loaded "+name+" resolve="+resolve);
                return c;
        }
        if (resolve)
            resolveClass(c);
//        System.out.println(this+" HandlerClassLoader.LoadClass exit resolved "+name+" resolve="+resolve);
        return c;
    }
    /**
    * Define a single class by name and byte array.
    * We will attempt to liberate it from the cache first; if it's not
    * there, we go defining.
    * @param name The class name
    * @param data The byte array to get bytecodes from
    */
    public synchronized Class defineAClass(String name, byte data[]) {
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
    * Define classes by Hashtable of names and bytecodes.
    * @param tclassNameAndBytecodes the Hashtable of bytecodes with name as key
    * @return An array of Classes currently defined + 1 extra at end for main handler
    * @see loadClass
    */
    public synchronized Class[] defineClasses(Hashtable<String, byte[]> tclassNameAndBytecodes) {
        String name = null;
//        System.out.println("HandlerClassLoader.defineClasses enter ");
        Enumeration<byte[]> en = tclassNameAndBytecodes.elements();
        Enumeration<String> enk = tclassNameAndBytecodes.keys();
        // init return classes array
        Class[] retClasses = new Class[tclassNameAndBytecodes.size()+1];
        int iClass = 0;
        //
        while(en.hasMoreElements()) {
                name = enk.nextElement();
                byte[] b = en.nextElement();
                // force an update
//                if ( (retc = (Class)(cache.get(name))) == null) {
                        // line up the name
                        classNameAndBytecodes.put(name, b);
                        retc = defineClass(name, b, 0, b.length);
                        cache.put(name, retc);
//                        System.out.println("HandlerClassLoader.defineClasses defined "+name);
//                }
                // found it, and it's retc
                retClasses[iClass++] = retc;
        }
        return retClasses;
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
        Hashtable<String, byte[]> classNameAndBytes = new Hashtable<>();
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
                        classNameAndBytes.put(entryName, bytecode);
                	}
                }
                zipFile.closeEntry();
            }
            defineClasses(classNameAndBytes);
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
        BigSackSession DS = null;
//        System.out.println("HandlerClassLoader.getBytesFromRepository "+name);
        try {
        	if(useEmbedded) {
                DS = SessionManager.Connect(path+"PSClassRepository",null, true);
                ClassNameAndBytes cnab = (ClassNameAndBytes) DS.get(new ClassNameAndBytes(name));	
                if( cnab != null )
                	return cnab.getBytes();
        	}
        } catch(Exception e) {
                System.out.println(e);
                e.printStackTrace();
        }
        finally {
                try {
                        if( useEmbedded && DS != null ) DS.Close(false);
                } catch(Exception e) {}
        }
        return retBytes;
   }
    /**
    * Put the bytecodes to BigSack repository.  This function to be
    * performed outside of class loading cause it happens rarely.
    * @param name The class name to put
    * @param bytes The associated bytecode array
    */
    public static void setBytesInRepository(String name, byte[] bytes) {
        BigSackSession DS = null;
//        System.out.println("HandlerClassLoader.setBytesInRepository "+name);
        try {
        	if(useEmbedded) {
        		DS = SessionManager.Connect(path+"PSClassRepository",null,true);
        		DS.put(new ClassNameAndBytes(name));
        	}
        } catch(Exception e) {
                System.out.println(e);
                e.printStackTrace();
        }
        finally {
                try {
                        if( useEmbedded && DS != null ) DS.Close(false);
                } catch(Exception e) {}
        }
   }
    /**
    * Put the bytecodes to BigSack repository.  This function to be
    * performed outside of class loading cause it happens rarely.
    * @param name The class name to put
    * @param bytes The associated bytecode array
    */
    public static void setBytesInRepository(byte[] jarFile) {
        BigSackSession DS = null;
//        System.out.println("HandlerClassLoader.setBytesInRepository jarFile");
        // we can't seem to get size from JAR, so big buf
        byte[] bigbuf = new byte[500000];
        ClassNameAndBytes cnab = new ClassNameAndBytes();
        try {
            InputStream in = new ByteArrayInputStream(jarFile);
            ZipInputStream zipFile = new ZipInputStream(in);
            DS = SessionManager.Connect(path+"PSClassRepository",null,true);
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
                    cnab = new ClassNameAndBytes(entryName, bytecode);
                   if(useEmbedded)
                	   DS.put(cnab);
		}
                }
                zipFile.closeEntry();
            }
            //
        } catch(Exception e) {
                System.out.println("HandlerClassLoader.setBytesInRepository failed "+e.getMessage());
                e.printStackTrace();
        }
        finally {
                try {
                        if( useEmbedded && DS != null ) DS.Close(false);
                } catch(Exception e) {}
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
 	public void setBytesInRepository(String packg, Path dir) throws IOException {
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{class}")) {
	           for (Path entry: stream) {
	        	String entryName = entry.getFileName().toString().replace('/', '.');
//	              System.out.println(String.valueOf(entry.getSize()));
	          		entryName = packg+"."+entryName.substring(0,entryName.length()-6);
	          		System.out.println(entryName);
	          		byte[] bytes = Files.readAllBytes(entry);
	          		classNameAndBytecodes.put(entryName,  bytes); // chicken and egg, egg, or chicken
	                System.out.println("File Entry "+entryName+" read "+bytes.length);
	           } 
	       } catch (DirectoryIteratorException ex) {
	           // I/O error encounted during the iteration, the cause is an IOException
	           throw ex.getCause();
	       }
	}
 	
    public static void main(String[] args) throws IOException {
    	HandlerClassLoader hcl = new HandlerClassLoader();
    	Path p = FileSystems.getDefault().getPath("C:/users/jg/workspace/volvex/bin/com/neocoretechs/volvex");
    	hcl.setBytesInRepository("com.neocoretechs.volvex",p);

    }
}
