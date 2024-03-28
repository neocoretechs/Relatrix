package com.neocoretechs.relatrix.server;

import java.lang.reflect.*;
import java.util.Arrays;

import com.neocoretechs.relatrix.client.MethodNamesAndParams;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
/**
* The remote call mechanism depends on Java reflection to provide access to methods that can be
* remotely invoked via serializable arguments and method name. By designating the reflected classes at startup
* in the server module, remote calls have access to reflected methods. 
* This class handles reflection of the user requests to call designated methods in the server side classes.<p/>
* It utilizes helper class {@link MethodNamesAndParams}.
* It starts by populating a table of those methods, and at runtime, creates a method call transport for client,
* and provides for server-side invocation of those methods.
* Option to skip leading arguments for  whatever reason is provided.
* @author Groff Copyright (C) NeoCoreTechs 1998-2000, 2015
*/
public final class ServerInvokeMethod {
	private static final boolean DEBUG = false;
    protected int skipArgs;
    int skipArgIndex;
    private Method[] methods;
    private MethodNamesAndParams pkmnap = new MethodNamesAndParams();
    HandlerClassLoader hcl;

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

    private void init(String tclass, int tskipArgs) throws ClassNotFoundException {
    	pkmnap.classClass = hcl.loadClass(tclass, true);
    	pkmnap.className = pkmnap.classClass.getName();
    	skipArgs = tskipArgs;
    	skipArgIndex = skipArgs;
    	Method m[];
    	m = pkmnap.classClass.getMethods();
    	for(int i = m.length-1; i >= 0 ; i--) {
    		//if( m[i].getName().startsWith("Relatrix_") ) {
    		pkmnap.methodNames.add(m[i].getName()/*.substring(9)*/);
    		System.out.println("Method :"+m[i].getName()/*.substring(9)*/);
    		//}
    	}
    	// create arrays
    	methods = new Method[pkmnap.methodNames.size()];
    	pkmnap.methodParams = new Class[pkmnap.methodNames.size()][];
    	pkmnap.methodSigs = new String[pkmnap.methodNames.size()];
    	pkmnap.returnTypes = new Class[pkmnap.methodNames.size()];
    	int methCnt = 0;
    	//
    	for(int i = m.length-1; i >= 0 ; i--) {
    		//if( m[i].getName().startsWith("Relatrix_") ) {
    		pkmnap.methodParams[methCnt] = m[i].getParameterTypes();
    		pkmnap.methodSigs[methCnt] = m[i].toString();
    		pkmnap.returnTypes[methCnt] = m[i].getReturnType();
    		if( pkmnap.returnTypes[methCnt] == void.class ) 
    			pkmnap.returnTypes[methCnt] = Void.class;
    		//int ind1 = pkmnap.methodSigs[methCnt].indexOf("Relatrix_");
    		//pkmnap.methodSigs[methCnt] = pkmnap.methodSigs[methCnt].substring(0,ind1)+pkmnap.methodSigs[methCnt].substring(ind1+9);
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
    		methods[methCnt++] = m[i];
    		// }
    	}
    }
       /**
    	 * Call invocation for static methods in target class
    	 * @param tmc
    	 * @return
    	 * @throws Exception
       */
       public synchronized Object invokeMethod(RemoteRequestInterface tmc) throws Exception {
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
       public synchronized Object invokeMethod(RemoteRequestInterface tmc, Object localObject) throws Exception {
                //NoSuchMethodException, InvocationTargetException, IllegalAccessException, PowerSpaceException  {               
                String targetMethod = tmc.getMethodName();
                if(DEBUG) {
                	System.out.println("ServerInvoke Target method:"+targetMethod+" remote request:"+tmc+" localObject:"+localObject);
                }
                int methodIndex = pkmnap.methodNames.indexOf(targetMethod);
                String whyNotFound = "No such method";
                while( methodIndex != -1 && methodIndex < pkmnap.methodNames.size()) {
                //        System.out.println(jj);
                        Class[] params = tmc.getParams();
                        //
                        //
                        if (DEBUG) {
                        	for(int iparm1 = 0; iparm1 < params.length ; iparm1++) {        
                                System.out.println("ServerInvoke Target method:"+targetMethod+" Calling param: "+params[iparm1]);
                        	}
                        	for(int iparm2 = skipArgIndex ; iparm2 < pkmnap.methodParams[methodIndex].length; iparm2++) {
                                System.out.println("ServerInvoke Target method:"+targetMethod+" Method param: "+pkmnap.methodParams[methodIndex][iparm2]);
                        	}
                        }
                        //
                        //
                        if( params.length == pkmnap.methodParams[methodIndex].length-skipArgIndex ) {
                                boolean found = true;
                                // if skipArgs, don't compare first 2
                                for(int paramIndex = 0 ; paramIndex < params.length; paramIndex++) {
                                        // can we cast it?
                                        if( params[paramIndex] != null && !pkmnap.methodParams[methodIndex][paramIndex+skipArgIndex].isAssignableFrom(params[paramIndex]) ) {
                                                found = false;
                                                whyNotFound = "Parameters do not match";
                                                break;
                                        }
                                }
                                if( found ) {
                                        if( skipArgs > 0) {
                                        	Object o1[] = tmc.getParamArray();
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
                                 		   Object oret = methods[methodIndex].invoke(localObject, tmc.getParamArray());
                                 		   System.out.println("ServerInvoke return from invocation:"+oret);
                                 		   return oret;
                                    	}
                                        return methods[methodIndex].invoke( localObject, tmc.getParamArray() );
                               }
                        } else
                               // tag for later if we find nothing matching
                               whyNotFound = "Wrong number of parameters";
                        methodIndex = pkmnap.methodNames.indexOf(targetMethod,methodIndex+1);
                }
                throw new NoSuchMethodException("Method "+targetMethod+" not found in "+pkmnap.className+" "+whyNotFound);
        }

}

