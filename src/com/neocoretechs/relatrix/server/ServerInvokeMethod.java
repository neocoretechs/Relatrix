package com.neocoretechs.relatrix.server;


import java.lang.reflect.*;

import com.neocoretechs.relatrix.client.RelatrixMethodNamesAndParams;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RemoteRequestInterface;
/**
* This class handles reflection of the user "handlers" for designated methods,
* populates a table of those methods, creates a method call transport for client,
* and provides for server-side invocation of those methods.
* Option to skip leading arguments for  whatever reason is provided.
* @author Groff Copyright (C) NeoCoreTechs 1998-2000
*/
public final class ServerInvokeMethod {
	private static final boolean DEBUG = true;
    protected int skipArgs;
    int skipArgIndex;
    private Method[] methods;
    private RelatrixMethodNamesAndParams pkmnap = new RelatrixMethodNamesAndParams();

    public RelatrixMethodNamesAndParams getMethodNamesAndParams() { return pkmnap; }
    /**
    * This constructor populates this object with reflected methods from the
    * designated class.  Reflect hierarchy in reverse (to get proper
    * overload) and look for methods
    * @param tclass The class name we are targeting
    * @param skipArgs > 0 if we want to skip first args.
    */
    public ServerInvokeMethod(String tclass, int tskipArgs) throws ClassNotFoundException {      
                pkmnap.classClass = Class.forName(tclass);
                //pkmnap.classClass = theClassLoader.loadClass(tclass, true);
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
       */
       public static RemoteRequestInterface makeMethodCall(String tClass, String methName, Object ... params) throws Exception {
    	   return new RelatrixStatement("session", tClass, methName, params);
       }
       /**
       * For an incoming RelatrixStatement, verify and invoke the proper
       * method.  We assume there is a table of class names and this and
       * it has been used to locate this object. 
       * @return Object of result of method invocation
       */
       public Object invokeMethod(RemoteRequestInterface tmc) throws Exception {
                //NoSuchMethodException, InvocationTargetException, IllegalAccessException, PowerSpaceException  {               
                String targetMethod = tmc.getMethodName();
                int methodIndex = pkmnap.methodNames.indexOf(targetMethod);
                String whyNotFound = "No such method";
                while( methodIndex != -1 && methodIndex < pkmnap.methodNames.size()) {
                //        System.out.println(jj);
                        Class[] params = tmc.getParams();
                        //
                        //
                        if (DEBUG) {
                        	for(int iparm1 = 0; iparm1 < params.length ; iparm1++) {        
                                System.out.println("Calling param: "+params[iparm1]);
                        	}
                        	for(int iparm2 = skipArgIndex ; iparm2 < pkmnap.methodParams[methodIndex].length; iparm2++) {
                                System.out.println("Method param: "+pkmnap.methodParams[methodIndex][iparm2]);
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
//                                                return methods[methodIndex].invoke( PKObjectTable.getObject(tmc.getSession(), tmc.getObjref()), o2 );
                                                return methods[methodIndex].invoke( null, o1 );
                                        } 
//                                        return methods[methodIndex].invoke( PKObjectTable.getObject(tmc.getSession(), tmc.getObjref()),tmc.getParamArray() );
                                        // invoke it for return
                                        return methods[methodIndex].invoke( null, tmc.getParamArray() );
                               }
                        } else
                               // tag for later if we find nothing matching
                               whyNotFound = "Wrong number of parameters";
                        methodIndex = pkmnap.methodNames.indexOf(targetMethod,methodIndex+1);
                }
                throw new NoSuchMethodException("Method "+targetMethod+" not found in "+pkmnap.className+" "+whyNotFound);
        }

}

