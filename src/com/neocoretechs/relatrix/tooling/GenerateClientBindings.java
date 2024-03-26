package com.neocoretechs.relatrix.tooling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
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
	public GenerateClientBindings() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws Exception {
		if(args.length < 2)
			throw new Exception("usage: java GenerateClientBindings [output file name] [fully qualified class name");
		ServerInvokeMethod sim = new ServerInvokeMethod(ClassLoader.getSystemClassLoader(),args[0], 0, false);
		MethodNamesAndParams rmnap = sim.getMethodNamesAndParams();
		//for(String method : rmnap.methodNames) {
		//	int mnum = rmnap.getMethodIndex(method);
		//	if(rmnap.methodSigs[mnum].contains(args[0]))
		//	System.out.printf("%s %s %s %s%n", method,rmnap.methodSigs[mnum],Arrays.toString(rmnap.methodParams[mnum]),rmnap.returnTypes[mnum]);
		//}
		FileOutputStream fos = new FileOutputStream(args[1]+".java");
		DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
		outStream.writeBytes("package ");
		outStream.writeBytes(args[0].substring(0,args[0].lastIndexOf(".")));
		outStream.writeBytes(";\r\n\r\n");
		outStream.writeBytes("public interface ");
		outStream.writeBytes(args[1]);
		outStream.writeBytes("{");
		outStream.writeBytes("\r\n\r\n");
		for(int mnum = 0; mnum < rmnap.methodNames.size(); mnum++) {
			if(rmnap.methodSigs[mnum].contains(args[0])) {
				outStream.writeBytes("\t");
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
		System.exit(0);
	}

}
