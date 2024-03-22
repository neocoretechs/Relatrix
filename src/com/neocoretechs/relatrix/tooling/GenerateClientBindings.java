package com.neocoretechs.relatrix.tooling;

import java.util.Arrays;

import com.neocoretechs.relatrix.client.RelatrixMethodNamesAndParams;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

public class GenerateClientBindings {

	public GenerateClientBindings() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws Exception {
		ServerInvokeMethod sim = new ServerInvokeMethod(args[0], 0);
		RelatrixMethodNamesAndParams rmnap = sim.getMethodNamesAndParams();
		for(String method : rmnap.methodNames) {
			int mnum = rmnap.getMethodIndex(method);
			System.out.printf("%s %s %s %s%n", method,rmnap.methodSigs[mnum],Arrays.toString(rmnap.methodParams[mnum]),rmnap.returnTypes[mnum]);
		}
	}

}
