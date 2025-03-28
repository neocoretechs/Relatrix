package com.neocoretechs.relatrix.server;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
/**
* The remote call mechanism depends on Java reflection to provide access to methods that can be
* remotely invoked via serializable arguments and method name. By designating the reflected classes at startup
* in the server module, remote calls have access to reflected methods designated with the {@link ServerMethod} annotation.
* This class handles reflection of the user requests to call designated methods in the server side classes.<p/>
* It utilizes helper class {@link MethodNamesAndParams} and attempts to find the best match between passed params and reflected
* method params and so takes polymorphic calls into account..
* It starts by populating a table of those methods, and at runtime, creates a method call transport for client,
* and provides for server-side invocation of those methods.
* Option to skip leading arguments for whatever reason is provided.
* @author Jonathan Groff Copyright (C) NeoCoreTechs 1998-2000, 2015, 2025
*/
public class ServerInvokeMethod {
	private static final boolean DEBUG = false;
    protected int skipArgs;
    int skipArgIndex;
    protected Method[] methods;
    protected MethodNamesAndParams pkmnap = new MethodNamesAndParams();
    HandlerClassLoader hcl;
    // TreeMap of method name mapping to list of indexes to arrays of name and params
    protected TreeMap<String, ArrayList<Integer>> methodLookup = new TreeMap<String, ArrayList<Integer>>();

    public MethodNamesAndParams getMethodNamesAndParams() { return pkmnap; }
    
    public ServerInvokeMethod(String tclass, int tskipArgs) throws ClassNotFoundException {
    	hcl = new HandlerClassLoader();
    	init(tclass, skipArgs);
    }
    /**
     * This constructor populates this object with reflected methods from the
     * designated class.  Reflect hierarchy in reverse (to get proper
     * overload) and look for methods
     * @param tclass The class name we are targeting
     * @param skipArgs > 0 if we want to skip first args.
     */
    public ServerInvokeMethod(ClassLoader cl, String tclass, int tskipArgs, boolean hasRemote) throws ClassNotFoundException {  
    	hcl = new HandlerClassLoader(cl, hasRemote);
    	init(tclass,skipArgs);
    }
    /**
     * Build arrays and lists of method names and parameters to facilitate method lookup and invocation.
     * Methods are looked up by name, then parameters are compared such that overloaded methods can be invoked properly.
     * The {@link MethodNamesAndParams} class is used to build the arrays and lists. In this class, the methods array holds reflected
     * methods and a TreeMap of method name mapped to a list of indexes into the methods array allows us to look up candidate
     * overloaded methods.
     * @param tclass The class to reflect
     * @param tskipArgs The number of arguments to skip in each method for invocation, this allows us to keep reserved arguments for special cases. 0 for none.
     * @throws ClassNotFoundException
     */
    private void init(String tclass, int tskipArgs) throws ClassNotFoundException {
    	pkmnap.classClass = hcl.loadClass(tclass, true);
    	pkmnap.className = pkmnap.classClass.getName();
    	skipArgs = tskipArgs;
    	skipArgIndex = skipArgs;
    	Method m[];
    	m = pkmnap.classClass.getMethods();
    	for(int i = m.length-1; i >= 0 ; i--) {
    		if( m[i].isAnnotationPresent(ServerMethod.class) ) {
    			pkmnap.methodNames.add(m[i].getName());
    			System.out.println("Method "+m[i].toString());
    		}
    	}
    	// create arrays
    	methods = new Method[pkmnap.methodNames.size()];
    	pkmnap.methodParams = new Class[pkmnap.methodNames.size()][];
    	pkmnap.methodSigs = new String[pkmnap.methodNames.size()];
    	pkmnap.returnTypes = new Class[pkmnap.methodNames.size()];
    	int methCnt = 0;
    	//
    	for(int i = m.length-1; i >= 0 ; i--) {
    		if(m[i].isAnnotationPresent(ServerMethod.class)) {
    			pkmnap.methodParams[methCnt] = m[i].getParameterTypes();
    			pkmnap.methodSigs[methCnt] = m[i].toString();
    			pkmnap.returnTypes[methCnt] = m[i].getReturnType();
    			if( pkmnap.returnTypes[methCnt] == void.class ) 
    				pkmnap.returnTypes[methCnt] = Void.class;
    			if( skipArgs > 0) {
    				try {
    					int ind1 = pkmnap.methodSigs[methCnt].indexOf("(");
    					int ind2 = pkmnap.methodSigs[methCnt].indexOf(",",ind1);
    					ind2 = pkmnap.methodSigs[methCnt].indexOf(",",ind2+1);
    					ind2 = pkmnap.methodSigs[methCnt].indexOf(",",ind2+1);
    					pkmnap.methodSigs[methCnt] = pkmnap.methodSigs[methCnt].substring(0,ind1+1)+pkmnap.methodSigs[methCnt].substring(ind2+1);
    				} catch(StringIndexOutOfBoundsException sioobe) {
    					System.out.println("<<Relatrix: The method "+pkmnap.methodSigs[methCnt]+" contains too few arguments (first "+skipArgIndex+" skipped)");
    				}
    			}
    			ArrayList<Integer> mPos = methodLookup.get(m[i].getName());
    			if(mPos == null) {
    				mPos = new ArrayList<Integer>();
    				methodLookup.put(m[i].getName(), mPos);
    			}
    			mPos.add(methCnt);
    			methods[methCnt++] = m[i];
    		}
    	}
    }
    /**
     * Call invocation for static methods in target class
     * @param tmc
     * @return
     * @throws Exception
     */
    public Object invokeMethod(RemoteRequestInterface tmc) throws Exception {
    	if(DEBUG) {
    		System.out.println("ServerInvoke Invoking method:"+tmc);
    		Object oret = invokeMethod(tmc, null);
    		System.out.println("ServerInvoke return from invocation:"+oret);
    		return oret;
    	}
    	return invokeMethod(tmc, null);
    }
    /**
     * For an incoming RelatrixStatement, verify and invoke the proper
     * method.  We assume there is a table of class names and this and
     * it has been used to locate this object. 
     * @return Object of result of method invocation
     */
    public Object invokeMethod(RemoteRequestInterface tmc, Object localObject) throws Exception {
    	//NoSuchMethodException, InvocationTargetException, IllegalAccessException, PowerSpaceException  {               
    	String targetMethod = tmc.getMethodName();
    	if(DEBUG) {
    		System.out.println("ServerInvoke Target method:"+targetMethod+" remote request:"+tmc+" localObject:"+localObject);
    	}
    	//int methodIndex = pkmnap.methodNames.indexOf(targetMethod);
    	ArrayList<Integer> methodIndexList = methodLookup.get(targetMethod);
    	String whyNotFound = "No such method";
		Class[] params = tmc.getParams();
		//
		// We are going to reflect the method params and determine the best one to invoke
		// based on the parameters being assignable from the method parameters
		//
    	if(methodIndexList != null ) {
    		TreeMap<Integer,Integer> methodRank = new TreeMap<Integer,Integer>();
    		boolean found = false;
    		for(int methodIndexCtr = 0; methodIndexCtr < methodIndexList.size(); methodIndexCtr++) {
    			int methodIndex = methodIndexList.get(methodIndexCtr);
    			if (DEBUG) {
    				for(int iparm1 = 0; iparm1 < params.length ; iparm1++) {        
    					System.out.println("ServerInvoke Target method:"+targetMethod+" Calling param: "+params[iparm1]);
    				}
    				for(int iparm2 = skipArgIndex ; iparm2 < pkmnap.methodParams[methodIndex].length; iparm2++) {
    					System.out.println("ServerInvoke Target method:"+targetMethod+" Method param: "+pkmnap.methodParams[methodIndex][iparm2]);
    				}
    			}
    			int sumParamRank = 0;
    			if( params.length == pkmnap.methodParams[methodIndex].length-skipArgIndex ) {
    				found = true; // we found a method with required number of params
    				// if skipArgs, don't compare first skipArgs params
    				for(int paramIndex = 0 ; paramIndex < params.length; paramIndex++) {
    					// exact match? If passed param is null, count as 0 rank, below assignable or exact
    					if( params[paramIndex] != null ) {
    						if(pkmnap.methodParams[methodIndex][paramIndex+skipArgIndex] == params[paramIndex]) {
    							sumParamRank+=2;
    						} else {
    							// can we cast it?	
    							if(pkmnap.methodParams[methodIndex][paramIndex+skipArgIndex].isAssignableFrom(params[paramIndex])) {
    								sumParamRank+=1;
    							} else {
    								// parameter doesnt match, reduce to ineligible
    								sumParamRank = -1;
    								break;
    							}
    						}
    					}
    				}
    				methodRank.put(sumParamRank, methodIndex);
    			}
    		}
    		//
    		if(found) {
    			int methodIndex = methodRank.get(methodRank.lastKey());
    			if(methodIndex < 0) {
    				whyNotFound = "parameters do not match";
    			} else {
    				if( skipArgs > 0) {
    					Object o1[] = tmc.getParamArray();
    					tmc.setReturnClass(methods[methodIndex].getReturnType().getName());
    					if(DEBUG) {
    						System.out.println("ServerInvoke Invoking method:"+methods[methodIndex]+" on object "+localObject+" with params "+Arrays.toString(o1));			
    						Object oret = methods[methodIndex].invoke( localObject, o1 );
    						System.out.println("ServerInvoke return from invocation:"+oret);
    						return oret;
    					}
    					return methods[methodIndex].invoke( localObject, o1 );
    				} 
    				// invoke it for return
    				if(DEBUG) {
    					System.out.println("ServerInvoke Invoking method:"+methods[methodIndex]+" on object "+localObject+" with params "+Arrays.toString(tmc.getParamArray()));
    					tmc.setReturnClass(methods[methodIndex].getReturnType().getName());
    					Object oret = methods[methodIndex].invoke(localObject, tmc.getParamArray());
    					System.out.println("ServerInvoke return from invocation:"+oret);
    					return oret;
    				}
    				tmc.setReturnClass(methods[methodIndex].getReturnType().getName());
    				return methods[methodIndex].invoke( localObject, tmc.getParamArray() );
    			}
    		} else {
    			whyNotFound = "wrong number of parameters";
    		}
    	}
    	throw new NoSuchMethodException("Method "+targetMethod+" not found in "+pkmnap.className+" "+whyNotFound);
    }

}

