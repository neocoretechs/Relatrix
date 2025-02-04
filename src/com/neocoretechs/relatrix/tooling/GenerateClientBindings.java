package com.neocoretechs.relatrix.tooling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteIterator;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Call with args: classname, output interface name, statement and transport method names,and desired package declaration.<p/>
 * This is an adjunct to the {@link ServerInvokeMethod} class that generates the server side callable method bindings
 * for a given class, such that a remote client side transport can invoke those methods and receive returned responses.<p/>
 * Combinations of these tools simplifies the process of building and maintaining 2 tier client/server models from existing
 * class files.<p/>
 * The invokeMethod of ServerInvokeMethod can be used to call methods reflected from a supplied class on a supplied local Object. If that
 * local object is null, a static method is assumed. These requests come in the form of an encapsulated {@link RemoteRequestInterface}.<p/>
 * A core assumption is that the transport of remote iterators is through a the implementation of a {@link RemoteIterator} interface
 * that carries a method transport for "boolean hasNext()" and "Object next()" to be remotely invoked on a server-side concrete
 * Iterator subclass. In conjunction with that, a {@link RemoteStream} implementation of {@link java.util.stream.Stream} that
 * wraps that RemoteIterator is available in the same package as the one designated on the command line. In this way, local
 * Stream functionality is provided using the remote iterator transport.<p/>
 * Of course, the hardcoded params are specific to the Relatrix package, but can be changed to any code that uses the ServerInvokeMethod
 * reflection paradigm.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class GenerateClientBindings {
	public static String outputClass = "RelatrixClientInterface"; //RelatrixClientTransactionInterface (will add Impl to class in code processing)
	public static String inputClass = "com.neocoretechs.relatrix.Relatrix"; //com.neocoretechs.relatrix.RelatrixTransaction
	public static String statementInterface = "RelatrixStatementInterface"; //parameter of sendCommand abstract declaration, superclass of all statement that provides encapsulated method and parameter container class
	public static String statement = "RelatrixStatement"; //parameter of sendCommand concrete instance, statement that provides encapsulated method and parameter container class
	public static String command = "sendCommand"; // method used for wire transport in the client that extends generated bindings, will be abstract method: public abstract Object
	public static String packageDecl = "com.neocoretechs.relatrix.client"; // fully qualified name to be formed into package decl
	public static String[] imports = new String[] {	// prime this with best guess, system will fill in required fully qualified class names for import
		"java.io.IOException",
		"java.util.Iterator",
		"java.util.stream.Stream",
		"java.util.List"
	};
	// if you want the client to trap Exception(s) and generate a simplified version, indicate it here
	public static boolean exceptionOverride = false;
	public static String simplifiedException = "java.io.IOException";
	
	public GenerateClientBindings() {}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 1 || args.length > 7)
			throw new Exception("usage: java GenerateClientBindings <simplified exception name or false> [fully qualified input class name] [output interface/class and file names] [statement transport method name] [transport command method name] [transport command parameter statement superclass] [package decl]");
		if(!args[0].equals("false")) {
			exceptionOverride = true;
			simplifiedException = args[0];
			try {
				Class c = Class.forName(args[0]);
				if(!Exception.class.isAssignableFrom(c))
					throw new Exception("Simplified exception not a valid Exception");
			} catch(ClassNotFoundException cnfe) {
				throw new Exception("Simplified exception name is not a valid class");
			}
		}
		if(args.length > 1)
			inputClass = args[1];
		if(args.length > 2)
			outputClass = args[2];
		if(args.length > 3)
			statement = args[3];
		if(args.length > 4)
			command = args[4];
		if(args.length > 5)
			statementInterface = args[5];
		if(args.length > 6)
			packageDecl = args[6];
		ServerInvokeMethod sim = new ServerInvokeMethod(ClassLoader.getSystemClassLoader(), inputClass, 0, false);
		MethodNamesAndParams rmnap = sim.getMethodNamesAndParams();
		generateInterface(rmnap);
		generateImpl(rmnap);
		System.exit(0);
	}
	/**
	 * Generate the implementation of the interface for the client bindings to our processed server side source class
	 * @param rmnap
	 * @throws IOException
	 */
	public static void generateImpl(MethodNamesAndParams rmnap) throws IOException {
		//
		// Now generate implementation skeleton with the class name [fully qualified interface name]Impl.
		// methods for each interface decl something like:
		//@Override
		//public [Type] [Method]([Type1] arg1, [Type2] arg2, [Type3] arg3, [Type4] arg4) throws ...
		//  [statement] s = new [statement]("[Method]", arg1, arg2, arg3, arg4);
		//	return ([Type])[command](s);
		//}
		// statement and command are passed from command line
		FileOutputStream fos = new FileOutputStream(outputClass+"Impl.java");
		DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
		outStream.writeBytes("// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings\r\n");
		outStream.writeBytes("package ");
		//outStream.writeBytes(inputClass.substring(0,inputClass.lastIndexOf(".")));
		outStream.writeBytes(packageDecl);
		outStream.writeBytes(";\r\n\r\n");
		// collect types for import decls
		ArrayList<String> importsList = generateImports(rmnap);
		for(String imps: importsList) {
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
		outStream.writeBytes(statementInterface);
		outStream.writeBytes(" s) throws Exception;\r\n");
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
					// substitute ellipsis for object array?
					if(rmnap.methodParams[mnum][i].getSimpleName().contains("Object[]") && 
						!rmnap.methodParams[mnum][i].isInstance(Object[].class))
						outStream.writeBytes("Object...");
					else
						outStream.writeBytes(rmnap.methodParams[mnum][i].getSimpleName());
					outStream.writeBytes(" arg");
					outStream.writeBytes(String.valueOf(i+1));
					if(i < rmnap.methodParams[mnum].length-1)
						outStream.writeBytes(",");
				}
				int ithrows = rmnap.methodSigs[mnum].indexOf("throws");
				LinkedHashMap<Class,String> excepts = null;
				if(ithrows != -1) {
					outStream.writeBytes(") ");
					if(exceptionOverride) {
						outStream.writeBytes("throws ");
						outStream.writeBytes(simplifiedException);
						excepts = new LinkedHashMap<Class,String>();
						excepts.put(Exception.class, "\t\t\tthrow new "+simplifiedException+"(e);\r\n");
					} else {
						outStream.writeBytes(rmnap.methodSigs[mnum].substring(ithrows));
						excepts = generateExceptions(rmnap.methodSigs[mnum].substring(ithrows+7));
					}
				} else {
					outStream.writeBytes(")");
					excepts = new LinkedHashMap<Class,String>();
					if(!rmnap.returnTypes[mnum].equals(Void.class)) {
						excepts.put(Exception.class, "\t\t\treturn null;\r\n"); // just trap Exception with no action
					} else {
						excepts.put(Exception.class, ""); // just trap Exception with no action
					}
				}
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
				// if we have exceptions to trap, write the try first
				if(excepts != null && !excepts.isEmpty()) {
					outStream.writeBytes("\t\ttry {\r\n\t");
				}
				// If not void, set up cast to return type for transport call
				// If we are returning type of stream, get the remote iterator and wrap it in a remote stream thusly:
				//return new RemoteStream((RemoteIterator) sendCommand(s));
				// NOTE: WE ASSUME RemoteStream and RemoteIterator are in same package as generated class and interface!
				if(!rmnap.returnTypes[mnum].equals(Void.class)) {
					if(!Stream.class.isAssignableFrom(rmnap.returnTypes[mnum])) {
						outStream.writeBytes("\t\treturn ("); // cast return to return type of method
						outStream.writeBytes(rmnap.returnTypes[mnum].getSimpleName());
						outStream.writeBytes(")");
						outStream.writeBytes(command);
						outStream.writeBytes("(s);\r\n");
					} else {
						outStream.writeBytes("\t\treturn new RemoteStream((RemoteIterator)"); // cast return to remote stream
						outStream.writeBytes(command);
						outStream.writeBytes("(s));\r\n");
					}
				} else {
					outStream.writeBytes("\t\t");
					outStream.writeBytes(command);
					outStream.writeBytes("(s);\r\n");
				}
				// write exceptions, if any, trapped from call to transport
				// these are exceptions coming back from server and trapped as generic Exception
				// we now break it out into its possible subclasses which are the
				// thows clause of the method call we are currently writing out
				if(excepts != null && !excepts.isEmpty()) {
					outStream.writeBytes("\t\t} catch(Exception e) {\r\n");
					Set<Entry<Class,String>> eset = excepts.entrySet();
					Iterator<Entry<Class, String>> it = eset.iterator();
					while(it.hasNext()) {
						Entry<Class,String> e = it.next();
						// if its the last entry, just fall through
						if(it.hasNext()) {
							outStream.writeBytes("\t\t\tif(e instanceof ");
							outStream.writeBytes(e.getKey().getName());
							outStream.writeBytes(")\r\n");
							outStream.writeBytes("\t");
						}
						outStream.writeBytes(e.getValue());
					}
					outStream.writeBytes("\t\t}\r\n");
				}
				outStream.writeBytes("\t}\r\n");
			}
			outStream.flush();
		}
		outStream.writeBytes("}");
		outStream.writeBytes("\r\n\r\n");
		outStream.flush();
		fos.flush();
		outStream.close();
		fos.close();

	}
	/**
	 * Generate the list of exceptions and their repackaging as a linked map of
	 * exception classes key and repackaged throws clause string, ready to write, as values
	 * @param throwLine the line of comma separated throws from method signature
	 * @return the Linked map of exception class and repackage string
	 */
	public static LinkedHashMap<Class,String> generateExceptions(String throwLine) {
		LinkedHashMap<Class,String> lhm = new LinkedHashMap<Class,String>();
		String[] excepts = throwLine.split(",");
		Class[] cexcepts = new Class[excepts.length];
		//System.out.println(Arrays.toString(excepts));
		// parse string of exception types, create classes, get constructors, determine params
		nextExcept:
		for(int c = 0; c < excepts.length; c++) {
			try {
				cexcepts[c] = Class.forName(excepts[c]); // get exception class
				Constructor[] ctors = cexcepts[c].getConstructors();
				// see if we can construct this particular exception with thrown generic Exception, string, or no args
				// if no args, we wont find an entry in the LinkedHasMap
				for(Constructor ctor: ctors) {
					Class[] params = ctor.getParameterTypes();
					for(Class param: params) {	
						if(Throwable.class.isAssignableFrom(param) && 
								!excepts[c].contains("NoSuchElementException") &&
								!excepts[c].contains("ClassNotFoundException")) { // for some reason, this kludge is necessary
							lhm.put(cexcepts[c], "\t\t\tthrow new "+excepts[c]+"(e);\r\n"); // create from Exception
							continue nextExcept; // found an exception param, done here
						} else {
							if(String.class.isAssignableFrom(param)) {
								lhm.put(cexcepts[c], "\t\t\tthrow new "+excepts[c]+"(e.getMessage());\r\n"); // create from String
							}
						}
					}
				}
				// went through all ctors with continue at main loop, must be a no-arg ctor situation if we dont have entry
				if(!lhm.containsKey(cexcepts[c]))
					lhm.put(cexcepts[c], "\t\t\tthrow new "+excepts[c]+"();\r\n"); 
			} catch (ClassNotFoundException e) {}
		}
		return lhm;
	}
	/**
	 * Generate the interface file for each client method binding
	 * @param rmnap
	 * @throws IOException
	 */
	public static void generateInterface(MethodNamesAndParams rmnap) throws IOException {
		//for(String method : rmnap.methodNames) {
		//	int mnum = rmnap.getMethodIndex(method);
		//	if(rmnap.methodSigs[mnum].contains(args[0]))
		//	System.out.printf("%s %s %s %s%n", method,rmnap.methodSigs[mnum],Arrays.toString(rmnap.methodParams[mnum]),rmnap.returnTypes[mnum]);
		//}
		FileOutputStream fos = new FileOutputStream(outputClass+".java");
		DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
		outStream.writeBytes("// auto generated from com.neocoretechs.relatrix.tooling.GenerateClientBindings\r\n");
		outStream.writeBytes("package ");
		//outStream.writeBytes(inputClass.substring(0,inputClass.lastIndexOf(".")));
		outStream.writeBytes(packageDecl);
		outStream.writeBytes(";\r\n\r\n");
		// collect types for import decls
		ArrayList<String> importsList = generateImports(rmnap);
		for(String imps: importsList) {
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
					// substitute ellipsis for object array?
					if(rmnap.methodParams[mnum][i].getSimpleName().contains("Object[]") && 
						!rmnap.methodParams[mnum][i].isInstance(Object[].class))
						outStream.writeBytes("Object...");
					else
						outStream.writeBytes(rmnap.methodParams[mnum][i].getSimpleName());
					outStream.writeBytes(" arg");
					outStream.writeBytes(String.valueOf(i+1));
					if(i < rmnap.methodParams[mnum].length-1)
						outStream.writeBytes(",");
				}
				int ithrows = rmnap.methodSigs[mnum].indexOf("throws");
				if(ithrows != -1) {
					if(exceptionOverride) {
						outStream.writeBytes(") throws ");
						outStream.writeBytes(simplifiedException);
					} else {
						outStream.writeBytes(") ");
						outStream.writeBytes(rmnap.methodSigs[mnum].substring(ithrows));
					}
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
	}

	private static ArrayList<String> generateImports(MethodNamesAndParams rmnap) {
		List<String> abstractList = Arrays.asList(imports);
		ArrayList<String> importsList = new ArrayList<String>();
		importsList.addAll(abstractList);
		for(int i = 0; i < rmnap.methodParams.length; i++) {
			for(int j = 0; j < rmnap.methodParams[i].length; j++) {
				if(!importsList.contains(rmnap.methodParams[i][j].getName()) && 
						!rmnap.methodParams[i][j].isPrimitive() &&
						!rmnap.methodParams[i][j].isArray() &&
						!rmnap.methodParams[i][j].getName().startsWith("java.lang"))
					importsList.add(rmnap.methodParams[i][j].getName());
			}
		}
		for(int i = 0; i < rmnap.returnTypes.length; i++) {
			if(!importsList.contains(rmnap.returnTypes[i].getName()) && 
					!rmnap.returnTypes[i].isPrimitive() &&
					!rmnap.returnTypes[i].isArray() &&
					!rmnap.returnTypes[i].getName().startsWith("java.lang"))
				importsList.add(rmnap.returnTypes[i].getName());
		}
		return importsList;
	}
}
