package com.neocoretechs.relatrix.tooling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
/**
 * TODO: check to see if class extends class that implements compareTo, then call superclass compareTo if we
 * need to add Comparable interface.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2023
 *
 */
public class ClassTool {
	private static boolean DEBUG = true;
	private static String packageName;
	private static String className;
	private static int packLine = -1; // package line
	private static int classLine = -1; // class decl line
	private static int implPos = -1; // implements position if any
	private static int implLine = -1; // implements line if any
	private static int classLineEnd = -1; // location of space after class space name decl
	private static int classDeclPosEnd = -1; // location of curly at end of class decl
	private static int classDeclLineEnd = -1; // line containing curly at end of class decl
	private static int lineLast = -1; // last curly
	private static int packPos = -1; // position of package
	private static int classPos = -1; // position of class
	private static int serializeLine = -1;
	private static int serializePos = -1;
	private static int comparableLine = -1;
	private static int comparablePos = -1;
	private static int serialVerLine = -1;
	static int cnt = 0;

	private static List<String> fileLines;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, IOException {
		fileLines = readLines(args[0]);
		postprocess();
		if(DEBUG)
			debugParse();
		// write backup of original
		try {
			Files.copy(Paths.get(args[0]),Paths.get(args[0]+".bak"));
		} catch(FileAlreadyExistsException fie) {
			System.out.println("Backup file already exists, will not overwrite");
			return;
		}
		if(DEBUG)
			System.out.println("Processing class:"+packageName+"."+className);
		//Class targetClass = Class.forName(packageName+"."+className);
		//Object classToTool = targetClass.newInstance();
		Object classToTool = null;
		if(packageName == "")
			classToTool = compile(args[0],className);
		else
			classToTool = compile(args[0],packageName+"."+className);
		Class targetClass = classToTool.getClass();
		if(DEBUG)
			System.out.println("Compiled instance:"+classToTool+" of class:"+targetClass);
		// see if we need to create Serializable interface tooling
		if(implLine == -1) {
			if(DEBUG)
				System.out.println("NO detected 'implements'. class line "+classLine+" end class decl pos "+classLineEnd);
			// no implements
			String newImpl = fileLines.get(classLine).substring(0,classLineEnd)+
					" implements java.io.Serializable,java.lang.Comparable"+
					fileLines.get(classLine).substring(classLineEnd);
			fileLines.set(classLine, newImpl);
			// is curly on class line or line following?
			// insert serialVersionUID
			//fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
			InstrumentClass instrument = new InstrumentClass();
			// return with compateTo statement constructed
			String compareToStatement = instrument.process(args[0], classToTool);
			findLastLine();
			if(DEBUG)
				System.out.println("End line of class decl (start of body):"+classDeclLineEnd+" last line of class decl (EOF)"+lineLast);
			fileLines.add(lineLast, compareToStatement);
		} else {
			if(DEBUG)
				System.out.println("Detected 'implements' at line "+implLine+" position "+implPos+" class line "+classLine+" end class decl pos "+classLineEnd);
			if(implPos == -1) {
				System.out.println("System failed to locate implements decl though one was indicated to exist..");
				System.exit(1);
			}
			//if(!Serializable.class.isAssignableFrom(targetClass) && !Comparable.class.isAssignableFrom(targetClass)) {
			if(serializeLine == -1 && comparableLine == -1) {
				// implements, but not our interfaces
				if(DEBUG)
					System.out.println("Detected 'implements', but not with required interfaces.");
				String newImpl = fileLines.get(implLine).substring(0,implPos+12)+
						"java.io.Serializable,java.lang.Comparable,"+
						fileLines.get(implLine).substring(implPos+12);
				if(DEBUG)
					System.out.println("New impl:"+newImpl);
				fileLines.set(implLine, newImpl);
				// is curly on class line or line following?
				// insert serialVersionUID
				//fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
				InstrumentClass instrument = new InstrumentClass();
				// return with compateTo statement constructed
				String compareToStatement = instrument.process(args[0], classToTool);
				findLastLine();
				if(DEBUG)
					System.out.println("End line of class decl (start of body):"+classDeclLineEnd+" last line of class decl (EOF)"+lineLast);
				fileLines.add(lineLast, compareToStatement);
			} else {
				// just Serializable?
				//if(!Serializable.class.isAssignableFrom(targetClass)) {
				if(serializeLine == -1) {
					// implements, but not Serializable
					if(DEBUG)
						System.out.println("Detected 'implements', but not with Serializable interface.");
					String newImpl = fileLines.get(implLine).substring(0,implPos+12)+
							"java.io.Serializable,"+
							fileLines.get(implLine).substring(implPos+12);
					if(DEBUG)
						System.out.println("New impl:"+newImpl);
					fileLines.set(implLine, newImpl);
					// is curly on class line or line following?
					// insert serialVersionUID
					findLastLine();
					if(DEBUG)
						System.out.println("End line of class decl (start of body):"+classDeclLineEnd+" last line of class decl (EOF)"+lineLast);
					//fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
				} else {
					// just comparable?
					//if(!Comparable.class.isAssignableFrom(targetClass)) {
					if(comparableLine == -1) {
						if(DEBUG)
							System.out.println("Detected 'implements', but not with Comparable interface.");
						// implements, but not our Comparable
						String newImpl = fileLines.get(implLine).substring(0,implPos+12)+
								"java.lang.Comparable,"+
								fileLines.get(implLine).substring(implPos+12);
						if(DEBUG)
							System.out.println("New impl:"+newImpl);
						fileLines.set(implLine, newImpl);
						// is curly on class line or line following?
						InstrumentClass instrument = new InstrumentClass();
						// return with compateTo statement constructed
						String compareToStatement = instrument.process(args[0], classToTool);
						findLastLine();
						if(DEBUG)
							System.out.println("End line of class decl (start of body):"+classDeclLineEnd+" last line of class decl (EOF)"+lineLast);
						fileLines.add(lineLast, compareToStatement);
					} else {
						System.out.println("Seems as if all tooling is already complete...");
						return;
					}
				}
			}
		}
		writeLines(args[0]);
		System.out.println("Tooling complete, backup of original source file is in "+args[0]+".bak");
		// compile new class
		if(packageName == "")
			classToTool = compile(args[0],className);
		else
			classToTool = compile(args[0],packageName+"."+className);
		targetClass = classToTool.getClass();
		if(DEBUG)
			System.out.println("New Compiled instance:"+classToTool+" of class:"+targetClass);
	}
	
	private static void debugParse() {
	System.out.println("\r\nPackage:"+ packageName+
	 "\r\nClass:"+className+" "+
	 packLine+"= package line \r\n"+
	 classLine+"= class decl line \r\n"+
	 implPos+"= implements position \r\n "+
	 implLine+"= implements line \r\n"+
	 comparablePos+"= Comparable decl pos \r\n"+
	 comparableLine+"= Comparable decl line \r\n"+
	 serializePos+"= Serializable decl pos \r\n"+
	 serializeLine+"= Serializable decl line \r\n"+
	 serialVerLine+"= serialVersionUID line\r\n"+
	 classLineEnd+" = location of space after class name \r\n"+
	 classDeclPosEnd+" = location of curly at end of class \r\n"+
	 classDeclLineEnd+"= line containing curly at end of class decl\r\n"+
	 lineLast+" =  last curly,"+
	 packPos+" = position of package,"+
	 classPos+" = position of class");
	}
	/**
	 * Set all static vars that indicate positions of parsed entities of interest.
	 * We will parse until the first occurrence of "{" indicating end of class decl
	 * @param javaFile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private static void preprocess(String s) throws IOException, IllegalArgumentException {
		if(DEBUG)
			System.out.println("parse:"+s);
			if(classPos == -1) {
				classPos = s.indexOf("class ");
				if(classPos != -1)
					classLine = cnt;
			}
			if(packPos == -1) {
				packPos = s.indexOf("package ");
				if(packPos != -1)
					packLine = cnt;
			}
			if(implLine == -1) {
				implPos = s.indexOf(" implements ");
				if(implPos != -1)
					implLine = cnt;
			}
			if(serializePos == -1) {
				serializePos = s.indexOf("Serializable");
				if(serializePos != -1)
					serializeLine = cnt;
			}
			if(comparablePos == -1) {
				comparablePos = s.indexOf("Comparable");
				if(comparablePos != -1)
					comparableLine = cnt;
			}
			if(serialVerLine == -1) {
				serialVerLine = s.indexOf("serialVersionUID");
				if(serialVerLine != -1)
					serialVerLine = cnt;
			}
			
			classDeclPosEnd = s.indexOf("{");
			if(classDeclPosEnd != -1) {
				classDeclLineEnd = cnt;
			}
			cnt++;
	}
	
	private static void postprocess() {
		if(classPos == -1) {
			throw new IllegalArgumentException("Can't find class declaration");
		}
		if(packPos == -1) {
			packageName = "";
		} else {
			int semi = fileLines.get(packLine).indexOf(";");
			if(semi == -1)
				throw new IllegalArgumentException("Malformed package declaration '"+fileLines.get(packLine)+"'");
			packageName = fileLines.get(packLine).substring(packPos+8,semi);
		}
		classLineEnd = fileLines.get(classLine).indexOf(" ",classPos+6);
		if(classLineEnd == -1) { // no space after class name
				throw new IllegalArgumentException("Malformed class declaration '"+fileLines.get(classLine)+"'");
		}
		className = fileLines.get(classLine).substring(classPos+6,classLineEnd);
	}
	
	/**
	 * Find the last curl brace of class declaration to place our compareTo method
	 * If we cant find it, exit, if found, set lineLast variable at that line -1
	 */
	private static void findLastLine() {
		// find last curly in file for location of compareTo if necessary
		lineLast = fileLines.size()-1;
		while(lineLast > 0) {
			if(fileLines.get(lineLast).contains("}"))
				break;
			--lineLast;
		}

		if(lineLast <= 0) {
			System.out.println("System could not locate last curly brace of class declaration, will exit..");
			System.exit(1);
		}
	}
	/**
	 * Read and parse, if we detect comment embedded, send to method to parse and return array of
	 * elements interspersed with comments
	 * @param javaFile
	 * @return
	 * @throws IOException
	 */
	private static List<String> readLines(String javaFile) throws IOException {
		BufferedReader reader;
		String parseLine;
		boolean commentBlk = false;
		ArrayList<String> lines = new ArrayList<String>();
		reader = new BufferedReader(new FileReader(javaFile));
		String line = reader.readLine();
		while (line != null) {
			if(commentBlk) {
				if(line.contains("*/")) {
					commentBlk = false;
					parseLine = line.substring(line.indexOf("*/")+2);
					preprocess(parseLine);
				}
				lines.add(line);
				line = reader.readLine();
				continue;
			}
			if(line.contains("/*") && line.contains("*/")) {
				parseLine = line.substring(0,line.indexOf("/*"))+line.substring(line.indexOf("*/")+2);
				preprocess(parseLine);
				lines.add(line);
				line = reader.readLine();
				continue;
			}
			if(line.contains("/*")) {
				parseLine = line.substring(0,line.indexOf("/*"));
				preprocess(parseLine);
				commentBlk = true;
				lines.add(line);
				line = reader.readLine();
				continue;
			}
			if(line.contains("//")) {
				parseLine = line.substring(0,line.indexOf("//"));
				preprocess(parseLine);
				lines.add(line);
				line = reader.readLine();
			}
			// read next line
			lines.add(line);
			preprocess(line);
			line = reader.readLine();
		}
		reader.close();
		return lines;
	}
	
	private static void writeLines(String javaFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
		for(String s: fileLines){
			if(DEBUG)
				System.out.println(s+"\r\n");
			writer.write(s+"\r\n");
		}
		writer.flush();
		writer.close();
	}
	
	private static Object compile(String javaFileName, String fqn) {
		try {
		File javaFile = new File(javaFileName);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // This sets up the class path that the compiler will use.
        // I've added the .jar file that contains the DoStuff interface within in it...
        List<String> optionList = new ArrayList<String>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path") + ";./");

        Iterable<? extends JavaFileObject> compilationUnit
            = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(javaFile));
        JavaCompiler.CompilationTask task = compiler.getTask(
          null, 
          fileManager, 
          diagnostics, 
          optionList, 
          null, 
          compilationUnit);
        if (task.call()) {
          /* Load and execute */
          // Create a new custom class loader, pointing to the directory that contains the compiled
          // classes, this should point to the top of the package structure!
          URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
          // Load the class from the classloader by name....
          Class<?> loadedClass = classLoader.loadClass(fqn);
       // Create a new instance...
          return loadedClass.newInstance();
          /************************************************************************************************* Load and execute **/
        } else {
          for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.format("Error on line %d in %s%n",
                diagnostic.getLineNumber(),
                diagnostic.getSource().toUri());
          }
        }
        fileManager.close();
      } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
        exp.printStackTrace();
      }
		return null;
	}
}
