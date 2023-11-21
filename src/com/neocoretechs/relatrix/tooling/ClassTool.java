package com.neocoretechs.relatrix.tooling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassTool {
	private static boolean DEBUG = true;
	private static String packageName;
	private static String className;
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, IOException {
		preprocess(args[0]);
		String fqn = packageName+"."+className;
		if(DEBUG)
			System.out.println("Processing class:"+fqn);
		Object classToTool = Class.forName(fqn).newInstance();
		InstrumentClass instrument = new InstrumentClass();
		instrument.process(args[0], classToTool);
	}

	private static List<String> preprocess(String javaFile) throws IOException, IllegalArgumentException {
		List<String> fileLines = readLines(javaFile);
		// extract fully qualified name
		int packLine = -1;
		int classLine = -1;
		int packPos = -1;
		int classPos = -1;
		int cnt = 0;
		for(String s: fileLines) {
			if(classPos == -1) {
				classPos = s.indexOf("class ");
				classLine = cnt;
			}
			if(packPos == -1) {
				packPos = s.indexOf("package ");
				packLine = cnt;
			}	
			if(classPos != -1 && packPos != -1)
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
			throw new IllegalArgumentException("Malformed package declaration");
		packageName = fileLines.get(packLine).substring(packPos+8,semi);
		int space = fileLines.get(classLine).indexOf(" ",classPos+6);
		if(space == -1)
			throw new IllegalArgumentException("Malformed class declaration");
		className = fileLines.get(classLine).substring(classPos+6,space);
		return fileLines;
	}
	
	private static List<String> readLines(String javaFile) throws IOException {
		BufferedReader reader;
		ArrayList<String> lines = new ArrayList<String>();
		reader = new BufferedReader(new FileReader(javaFile));
		String line = reader.readLine();
		while (line != null) {
			if(DEBUG)
				System.out.println(line);
			// read next line
			lines.add(line);
			line = reader.readLine();
		}
		reader.close();
		return lines;
	}
}
