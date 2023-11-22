package com.neocoretechs.relatrix.tooling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

	private static List<String> fileLines;
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, IOException {
		preprocess(args[0]);
		if(DEBUG)
			debugParse();
		// write backup of original
		try {
			Files.copy(Paths.get(args[0]),Paths.get(args[0]+".bak"));
		} catch(FileAlreadyExistsException fie) {
			System.out.println("Backup file already exists, will not overwrite");
			return;
		}
		String fqn = packageName+"."+className;
		if(DEBUG)
			System.out.println("Processing class:"+fqn);
		Class targetClass = Class.forName(fqn);
		Object classToTool = targetClass.newInstance();
		// see if we need to create Serializable interface tooling
		if(implLine == -1) {
			// no implements
			String newImpl = fileLines.get(classLine).substring(0,classLineEnd)+
					" implements java.io.Serializable,java.lang.Comparable"+
					fileLines.get(classLine).substring(classLineEnd);
			fileLines.set(classLine, newImpl);
			// is curly on class line or line following?
			// insert serialVersionUID
			fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
			InstrumentClass instrument = new InstrumentClass();
			// return with compateTo statement constructed
			String compareToStatement = instrument.process(args[0], classToTool);
			findLastLine();
			fileLines.add(lineLast, compareToStatement);
		} else {
			if(!Serializable.class.isAssignableFrom(targetClass) && !Comparable.class.isAssignableFrom(targetClass)) {
				// implements, but not our interfaces
				String newImpl = fileLines.get(implLine).substring(0,implPos+11)+
						"java.io.Serializable,java.lang.Comparable,"+
						fileLines.get(implLine).substring(implPos+11);
				if(DEBUG)
					System.out.println("New impl:"+newImpl);
				fileLines.set(implLine, newImpl);
				// is curly on class line or line following?
				// insert serialVersionUID
				fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
				InstrumentClass instrument = new InstrumentClass();
				// return with compateTo statement constructed
				String compareToStatement = instrument.process(args[0], classToTool);
				findLastLine();
				fileLines.add(lineLast, compareToStatement);
			} else {
				// just Serializable?
				if(!Serializable.class.isAssignableFrom(targetClass)) {
					// implements, but not Serializable
					String newImpl = fileLines.get(implLine).substring(0,implPos+11)+
							"java.io.Serializable,"+
							fileLines.get(implLine).substring(implPos+11);
					if(DEBUG)
						System.out.println("New impl:"+newImpl);
					fileLines.set(implLine, newImpl);
					// is curly on class line or line following?
					// insert serialVersionUID
					fileLines.add(classDeclLineEnd+1,InstrumentClass.resolveClass(targetClass));
				} else {
					// just comparable?
					if(!Comparable.class.isAssignableFrom(targetClass)) {
						// implements, but not our Comparable
						String newImpl = fileLines.get(implLine).substring(0,implPos+11)+
								"java.lang.Comparable,"+
								fileLines.get(implLine).substring(implPos+11);
						if(DEBUG)
							System.out.println("New impl:"+newImpl);
						fileLines.set(implLine, newImpl);
						// is curly on class line or line following?
						InstrumentClass instrument = new InstrumentClass();
						// return with compateTo statement constructed
						String compareToStatement = instrument.process(args[0], classToTool);
						findLastLine();
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
	}
	
	private static void debugParse() {
		System.out.println("Package:"+ packageName+
	 "\r\nClass:"+className+
	 packLine+"= package line \r\n"+
	 classLine+"= class decl line \r\n"+
	 implPos+"= implements position if any\r\n "+
	 implLine+"= implements line if any \r\n"+
	 classLineEnd+" = location of space after class space name decl \r\n"+
	 classDeclPosEnd+" = location of curly at end of class decl\r\n"+
	 classDeclLineEnd+"= line containing curly at end of class decl\r\n"+
	 lineLast+" =  last curly,"+
	 packPos+" = position of package,"+
	 classPos+" = position of class");
	}
	/**
	 * Set all static vars that indicate positions of parsed entities of interest
	 * @param javaFile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private static void preprocess(String javaFile) throws IOException, IllegalArgumentException {
		int cnt = 0;
		fileLines = readLines(javaFile);
		// extract fully qualified name
		for(String s: fileLines) {
			if(classPos == -1) {
				classPos = s.indexOf("class ");
				classLine = cnt;
			}
			if(packPos == -1) {
				packPos = s.indexOf("package ");
				packLine = cnt;
			}
			if(implLine == -1) {
				implPos = s.indexOf("implements ");	
				implLine = cnt;
			}
			classDeclPosEnd = s.indexOf("{");
			if(classDeclPosEnd != -1) {
				classDeclLineEnd = cnt;
			}
			if((classPos != -1 && packPos != -1) || classDeclLineEnd != -1)
				break;
			cnt++;
		}
		if(packPos == -1) {
			throw new IllegalArgumentException("Can't find package declaration");
		}
		if(classPos == -1) {
			throw new IllegalArgumentException("Can't find class declaration");
		}
		int semi = fileLines.get(packLine).indexOf(";");
		if(semi == -1)
			throw new IllegalArgumentException("Malformed package declaration '"+fileLines.get(packLine)+"'");
		packageName = fileLines.get(packLine).substring(packPos+8,semi);
		classLineEnd = fileLines.get(classLine).indexOf(" ",classPos+6);
		if(classLineEnd == -1) { // no space after class name
				throw new IllegalArgumentException("Malformed class declaration '"+fileLines.get(classLine)+"'");
		}
		className = fileLines.get(classLine).substring(classPos+6,classLineEnd);
	}
	
	private static void findLastLine() {
		// find last curly in file for location of compareTo if necessary
		lineLast = fileLines.size()-1;
		while(lineLast > 0) {
			if(fileLines.get(lineLast).contains("}"))
				break;
			--lineLast;
		}
	}
	
	private static List<String> readLines(String javaFile) throws IOException {
		BufferedReader reader;
		boolean commentBlk = false;
		ArrayList<String> lines = new ArrayList<String>();
		reader = new BufferedReader(new FileReader(javaFile));
		String line = reader.readLine();
		while (line != null) {
			if(line.contains("/*") && line.contains("*/")) {
				line = line.substring(0,line.indexOf("/*"))+line.substring(line.indexOf("*/")+2);
				if(DEBUG)
					System.out.println("parse:"+line);
				lines.add(line);
				line = reader.readLine();
				continue;
			}
			if(line.contains("/*")) {
				line = line.substring(0,line.indexOf("/*"));
				commentBlk = true;
				if(line.length() != 0) {
					if(DEBUG)
						System.out.println("parse:"+line);
					lines.add(line);
				}
			}
			if(commentBlk) {
				line = reader.readLine();
				if(line.contains("*/")) {
					commentBlk = false;
					line = line.substring(line.indexOf("*/")+2);
					if(line.length() != 0) {
						if(DEBUG)
							System.out.println("parse:"+line);
						lines.add(line);
					}
				}
				continue;
			}
			if(line.contains("//")) {
				line = line.substring(0,line.indexOf("//"));
				if(line.length() == 0) {
					line = reader.readLine();
					continue;
				}
			}
			if(DEBUG)
				System.out.println("parse:"+line);
			// read next line
			lines.add(line);
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
}
