package com.neocoretechs.relatrix.tooling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;

/**
 * Call with args: classname, output interface name.<p/>
 * This is an adjunct to the {@link ServerInvokeMethod} class that generates the server side callable method bindings
 * for a given class, such that a remote client side transport can invoke those methods and receive returned responses.<p/>
 * Combinations of these tools simplifies the process of building and maintaining 2 tier client/server models from existing
 * class files.<p/>
 * The invokeMethod of ServerInvokeMethod can be used to call methods reflected from a supplied class on a supplied local Object. If that
 * local object is null, a static method is assumed. These requests come in the form of an encapsulated {@link RemoteRequestInterface}.
 * 
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class GenerateClientBindings {
	public static String outputClass = "clientInterface";
	public static String inputClass = "InputClass";
	public static String statement = "transportStatement";
	public static String command = "sendCommand";
	public static String packageDecl = "package com.your.package;";
	public static ArrayList<String> imports = new ArrayList<String>();
	
	public GenerateClientBindings() {}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 5)
			throw new Exception("usage: java GenerateClientBindings [fully qualified input class name] [output interface/class and file names] [statement transport method name] [transport command method name] [package decl]");
		inputClass = args[0];
		outputClass = args[1];
		statement = args[2];
		command = args[3];
		packageDecl = args[4];
		ServerInvokeMethod sim = new ServerInvokeMethod(ClassLoader.getSystemClassLoader(), inputClass, 0, false);
		MethodNamesAndParams rmnap = sim.getMethodNamesAndParams();
		//for(String method : rmnap.methodNames) {
		//	int mnum = rmnap.getMethodIndex(method);
		//	if(rmnap.methodSigs[mnum].contains(args[0]))
		//	System.out.printf("%s %s %s %s%n", method,rmnap.methodSigs[mnum],Arrays.toString(rmnap.methodParams[mnum]),rmnap.returnTypes[mnum]);
		//}
		FileOutputStream fos = new FileOutputStream(args[1]+".java");
		DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
		outStream.writeBytes("package ");
		//outStream.writeBytes(inputClass.substring(0,inputClass.lastIndexOf(".")));
		outStream.writeBytes(packageDecl);
		outStream.writeBytes(";\r\n\r\n");
		// collect types for import decls
		for(int i = 0; i < rmnap.methodParams.length; i++) {
			for(int j = 0; j < rmnap.methodParams[i].length; j++) {
				if(!imports.contains(rmnap.methodParams[i][j].getName()) && 
						!rmnap.methodParams[i][j].isPrimitive() &&
						!rmnap.methodParams[i][j].isArray() &&
						!rmnap.methodParams[i][j].getName().startsWith("java.lang"))
					imports.add(rmnap.methodParams[i][j].getName());
			}
		}
		for(int i = 0; i < rmnap.returnTypes.length; i++) {
			if(!imports.contains(rmnap.returnTypes[i].getName()) && 
					!rmnap.returnTypes[i].isPrimitive() &&
					!rmnap.returnTypes[i].isArray() &&
					!rmnap.returnTypes[i].getName().startsWith("java.lang"))
				imports.add(rmnap.returnTypes[i].getName());
		}
		for(String imps: imports) {
			outStream.writeBytes("import ");
			outStream.writeBytes(imps);
			outStream.writeBytes(";\r\n");
		}
		outStream.writeBytes("\r\n\r\n");
		outStream.writeBytes("public interface ");
		outStream.writeBytes(outputClass);
		outStream.writeBytes("{");
		outStream.writeBytes("\r\n\r\n");
		for(int mnum = 0; mnum < rmnap.methodNames.size(); mnum++) {
			if(rmnap.methodNames.get(mnum).equals("main"))
				continue;
			if(rmnap.methodSigs[mnum].contains(inputClass)) { // only methods with fully qualified class name in signature
				outStream.writeBytes("\tpublic ");
				if(rmnap.returnTypes[mnum].equals(Void.class))
					outStream.writeBytes("void");
				else
					outStream.writeBytes(rmnap.returnTypes[mnum].getSimpleName());
				outStream.writeBytes(" ");
				outStream.writeBytes(rmnap.methodNames.get(mnum));
				outStream.writeBytes("(");
				for(int i = 0; i < rmnap.methodParams[mnum].length; i++) {
					outStream.writeBytes(rmnap.methodParams[mnum][i].getSimpleName());
					outStream.writeBytes(" arg");
					outStream.writeBytes(String.valueOf(i+1));
					if(i < rmnap.methodParams[mnum].length-1)
						outStream.writeBytes(",");
				}
				int ithrows = rmnap.methodSigs[mnum].indexOf("throws");
				if(ithrows != -1) {
					outStream.writeBytes(") ");
					outStream.writeBytes(rmnap.methodSigs[mnum].substring(ithrows));
				} else
					outStream.writeBytes(")");
				outStream.writeBytes(";\r\n\r\n");
			}
			outStream.flush();
		}
		outStream.writeBytes("}");
		outStream.writeBytes("\r\n\r\n");
		outStream.flush();
		fos.flush();
		outStream.close();
		fos.close();
		//
		// Now generate implementation skeleton with the class name [fully qualified interface name]Impl.
		// methods for each interface decl something like:
		//@Override
		//public [Type] [Method]([Type1] arg1, [Type2] arg2, [Type3] arg3, [Type4] arg4) throws ...
		//  [statement] s = new [statement]("[Method]", arg1, arg2, arg3, arg4);
		//	return ([Type])[command](s);
		//}
		// statement and command are passed from command line
		fos = new FileOutputStream(outputClass+"Impl.java");
		outStream = new DataOutputStream(new BufferedOutputStream(fos));
		outStream.writeBytes("package ");
		//outStream.writeBytes(inputClass.substring(0,inputClass.lastIndexOf(".")));
		outStream.writeBytes(packageDecl);
		outStream.writeBytes(";\r\n\r\n");
		for(String imps: imports) {
			outStream.writeBytes("import ");
			outStream.writeBytes(imps);
			outStream.writeBytes(";\r\n");
		}
		outStream.writeBytes("\r\n\r\n");
		// generate abstract method declaration for transport command using transport statement
		outStream.writeBytes("public abstract class ");
		outStream.writeBytes(outputClass);
		outStream.writeBytes("Impl implements ");
		outStream.writeBytes(outputClass);
		outStream.writeBytes("{");	
		outStream.writeBytes("\r\n\r\n\t");
		// assumption is transport command returns Object type
		outStream.writeBytes("public abstract Object ");
		outStream.writeBytes(command);
		outStream.writeBytes("(");
		outStream.writeBytes(statement);
		outStream.writeBytes(" s);\r\n");
		for(int mnum = 0; mnum < rmnap.methodNames.size(); mnum++) {
			if(rmnap.methodNames.get(mnum).equals("main"))
				continue;
			if(rmnap.methodSigs[mnum].contains(inputClass)) {
				outStream.writeBytes("\t@Override\r\n");
				outStream.writeBytes("\tpublic ");
				if(rmnap.returnTypes[mnum].equals(Void.class))
					outStream.writeBytes("void");
				else
					outStream.writeBytes(rmnap.returnTypes[mnum].getSimpleName());
				outStream.writeBytes(" ");
				outStream.writeBytes(rmnap.methodNames.get(mnum));
				outStream.writeBytes("(");
				for(int i = 0; i < rmnap.methodParams[mnum].length; i++) {
					outStream.writeBytes(rmnap.methodParams[mnum][i].getSimpleName());
					outStream.writeBytes(" arg");
					outStream.writeBytes(String.valueOf(i+1));
					if(i < rmnap.methodParams[mnum].length-1)
						outStream.writeBytes(",");
				}
				int ithrows = rmnap.methodSigs[mnum].indexOf("throws");
				if(ithrows != -1) {
					outStream.writeBytes(") ");
					outStream.writeBytes(rmnap.methodSigs[mnum].substring(ithrows));
				} else
					outStream.writeBytes(")");
				outStream.writeBytes(" {\r\n");
				outStream.writeBytes("\t\t");
				outStream.writeBytes(statement);
				outStream.writeBytes(" s = new ");
				outStream.writeBytes(statement);
				outStream.writeBytes("(\"");
				outStream.writeBytes(rmnap.methodNames.get(mnum));
				outStream.writeBytes("\",");
				if(rmnap.methodParams[mnum].length == 0) {
					outStream.writeBytes("new Object[]{}");
				} else
					for(int i = 0; i < rmnap.methodParams[mnum].length; i++) {
						outStream.writeBytes(" arg");
						outStream.writeBytes(String.valueOf(i+1));
						if(i < rmnap.methodParams[mnum].length-1)
							outStream.writeBytes(",");
					}
				outStream.writeBytes(");\r\n");
				// If not void, set up cast to return type
				if(!rmnap.returnTypes[mnum].equals(Void.class)) {
					outStream.writeBytes("\t\treturn ("); // cast return to return type of method
					outStream.writeBytes(rmnap.returnTypes[mnum].getSimpleName());
					outStream.writeBytes(")");
				} else
					outStream.writeBytes("\t\t");
				outStream.writeBytes(command);
				outStream.writeBytes("(s);\r\n\t}\r\n");
			}
			outStream.flush();
		}
		outStream.writeBytes("}");
		outStream.writeBytes("\r\n\r\n");
		outStream.flush();
		fos.flush();
		outStream.close();
		fos.close();
		System.exit(0);
	}
}
