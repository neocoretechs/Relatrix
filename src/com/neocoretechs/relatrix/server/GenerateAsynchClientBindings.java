package com.neocoretechs.relatrix.server;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.Set;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Call with args: classname, output interface name, statement and transport method names,and desired package declaration.<p/>
 * This is an adjunct to the {@link ServerInvokeMethod} class that generates the server side callable method bindings
 * for a given class, such that a remote client side transport can invoke those methods and receive returned responses asynchronously.<p/>
 * The methods will specified via the {@link ServerMethod} annotation in the server-side source class.<p/>
 * Combinations of these tools simplifies the process of building and maintaining 2 tier client/server models from existing
 * class files.<p/>
 * The invokeMethod of ServerInvokeMethod can be used to call methods reflected from a supplied class on a supplied local Object. If that
 * local object is null, a static method is assumed. These requests come in the form of an encapsulated {@link RemoteRequestInterface}.<p/>
 * Of course, the hardcoded params are specific to the Relatrix package, but can be changed to any code that uses the ServerInvokeMethod
 * reflection paradigm.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class GenerateAsynchClientBindings {
	public static String outputClass = "AsynchRelatrixClientInterface"; //RelatrixClientTransactionInterface (will add Impl to class in code processing)
	public static String inputClass = "com.neocoretechs.relatrix.Relatrix"; //com.neocoretechs.relatrix.RelatrixTransaction
	public static String statementInterface = "RelatrixStatementInterface"; //parameter of sendCommand abstract declaration, superclass of all statement that provides encapsulated method and parameter container class
	public static String statement = "RelatrixStatement"; //parameter of sendCommand concrete instance, statement that provides encapsulated method and parameter container class
	public static String command = "queueCommand"; // method used for wire transport in the client that extends generated bindings, will be abstract method: public abstract Object
	public static String packageDecl = "com.neocoretechs.relatrix.client.asynch"; // fully qualified name to be formed into package decl
	public static String[] imports = new String[] {	// prime this with best guess, system will fill in required fully qualified class names for import
		"java.io.IOException",
		"java.util.Iterator",
		"java.util.stream.Stream",
		"java.util.List",
		"java.util.concurrent.CompletableFuture",
		"com.neocoretechs.rocksack.Alias",
		"com.neocoretechs.rocksack.TransactionId"
	};
	// append to return [command] for stream type
	public static String streamDecl = ".thenApply(result -> {\r\n"
			+ "	        try {\r\n"
			+ "	            return (Stream)(new RemoteStream((Iterator) result));\r\n"
			+ "	        } catch (Exception e) {\r\n"
			+ "	            throw new CompletionException(e);\r\n"
			+ "	        }\r\n"
			+ "	    }).exceptionally(ex -> {\r\n"
			+ "	        // Handle the exception, e.g., return an empty stream or throw a custom exception\r\n"
			+ "	        throw new RuntimeException(ex);\r\n"
			+ "	    });\r\n";
	// append to return [command] for Iterator type
	public static String iteratorDecl = ".thenApply(result -> (Iterator) result);\r\n";
	public static String castDecl = ".thenApply(result -> (%s) result);\r\n";
	public static String returnCastDecl = "		try {\r\n"
			+ "			return cf.get();\r\n"
			+ "		} catch (InterruptedException | ExecutionException e) {\r\n"
			+ "			throw new RuntimeException(e);\r\n"
			+ "		}\r\n";
	//
	// specify the interfaces that include methods that should be synchronous to comply with the other synchronous
	// implementations for compatibility. We will issue an asych call to queueCommand , then do a a get to return a completed instance
	// with an object rather than CompletableFuture
	private static String[] excludesInterfaces = new String[] {"ClientInterface"};
	private static MethodNamesAndParams excludedMethods = null;
	
	public GenerateAsynchClientBindings() {}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 1 || args.length > 7)
			throw new Exception("usage: java GenerateAsynchClientBindings <simplified exception name or false> [fully qualified input class name] [output interface/class and file names] [statement transport method name] [transport command method name] [transport command parameter statement superclass] [package decl]");
		if(!args[0].equals("false")) {
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
	 * @param rmnap reflected methods from input class
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
		outStream.writeBytes("// auto generated from com.neocoretechs.relatrix.server.GenerateAsynchClientBindings ");
		outStream.writeBytes((new Date()).toString());
		outStream.writeBytes("\r\n");
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
		// assumption is transport command returns CompletableFuture<Object> type
		outStream.writeBytes("public abstract CompletableFuture<Object> ");
		outStream.writeBytes(command);
		outStream.writeBytes("(");
		outStream.writeBytes(statementInterface);
		outStream.writeBytes(" s);\r\n");
		// start generating the methods from the reflected input class
		for(int mnum = 0; mnum < rmnap.methodNames.size(); mnum++) {
			if(rmnap.methodNames.get(mnum).equals("main"))
				continue;
			if(rmnap.methodSigs[mnum].contains(inputClass)) {
				outStream.writeBytes("\t@Override\r\n");
				outStream.writeBytes("\tpublic ");
				if(isMethodExcluded(rmnap.methodSigs[mnum])) {
					if(rmnap.returnTypes[mnum].equals(Void.class))
						outStream.writeBytes("void");
					else
						outStream.writeBytes(rmnap.returnTypes[mnum].getSimpleName());
				} else {
					if(rmnap.returnTypes[mnum].equals(Void.class))
						outStream.writeBytes("CompletableFuture<Void>");
					else
						outStream.writeBytes("CompletableFuture<"+rmnap.returnTypes[mnum].getSimpleName()+">");
				}
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
				outStream.writeBytes(") {\r\n");
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
				// If not void, set up cast to return type for transport call
				// If we are returning type of Stream or Iterator, add special logic
				// NOTE: WE ASSUME RemoteStream and RemoteIterator are in same package as generated class and interface!
				if(isMethodExcluded(rmnap.methodSigs[mnum])) {
					outStream.writeBytes("\t\tCompletableFuture<Object> cf = "); // cast return to return type of method
					outStream.writeBytes(command);
					outStream.writeBytes("(s);\r\n");
					outStream.writeBytes(returnCastDecl);
					outStream.writeBytes("\t}\r\n");
				} else {	
					// ...method not excluded
					if(!rmnap.returnTypes[mnum].equals(Void.class)) {
						if(!Stream.class.isAssignableFrom(rmnap.returnTypes[mnum])) {
							if(!Iterator.class.isAssignableFrom(rmnap.returnTypes[mnum])) {
								outStream.writeBytes("\t\treturn "); // cast return to return type of method
								outStream.writeBytes(command);
								outStream.writeBytes("(s)");
								outStream.writeBytes(String.format(castDecl, rmnap.returnTypes[mnum].getSimpleName()));
								outStream.writeBytes(";\r\n");
							} else {
								outStream.writeBytes("\t\treturn "); // cast return to remote stream
								outStream.writeBytes(command);
								outStream.writeBytes("(s)");
								outStream.writeBytes(iteratorDecl);
								outStream.writeBytes("\r\n");
							}
						} else {
							outStream.writeBytes("\t\treturn "); // cast return to remote stream
							outStream.writeBytes(command);
							outStream.writeBytes("(s)");
							outStream.writeBytes(streamDecl);
							outStream.writeBytes("\r\n");
						}
					} else {
						outStream.writeBytes("\t\t");
						outStream.writeBytes(command);
						outStream.writeBytes("(s);\r\n");
					}
					outStream.writeBytes("\t}\r\n");
				}
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
		outStream.writeBytes("// auto generated from com.neocoretechs.relatrix.server.GenerateClientBindings ");
		outStream.writeBytes((new Date()).toString());
		outStream.writeBytes("\r\n");
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
	/**
	 * Generate a list of methods from the stated interfaces that are processed as regular synchronous
	 * methods to maintain compatibility with other implementations
	 * @throws ClassNotFoundException
	 */
	private static void generateExclusions() throws ClassNotFoundException {
		excludedMethods = new MethodNamesAndParams();
		for(String ifaces: excludesInterfaces) {
			Class clazz = Class.forName(ifaces);
			Method[] m = clazz.getMethods();
			for(Method me : m) {
				excludedMethods.methodNames.add(me.getName());
			}
		}
		excludedMethods.methodParams = new Class[excludedMethods.methodNames.size()][];
		excludedMethods.methodSigs = new String[excludedMethods.methodNames.size()];
		excludedMethods.returnTypes = new Class[excludedMethods.methodNames.size()];
		int methCnt = 0;
		for(String ifaces: excludesInterfaces) {
			Class<?> clazz = Class.forName(ifaces);
			Method[] m = clazz.getMethods();
			for(int i = 0; i < m.length; i++) {
				excludedMethods.methodParams[methCnt] = m[i].getParameterTypes();
				excludedMethods.methodSigs[methCnt] = m[i].toString();
				excludedMethods.returnTypes[methCnt++] = m[i].getReturnType();
			}
		}
	}
	private static boolean isMethodExcluded(String methodSig) {
		for(int i = 0; i < excludedMethods.methodSigs.length; i++) {
			if(excludedMethods.methodSigs[i].equals(methodSig))
				return true;
		}
		return false;
	}
}
