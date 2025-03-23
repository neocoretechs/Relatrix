package com.neocoretechs.relatrix.server;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
* method params and so takes polymorphic calls into account.
* It starts by populating a table of those methods, and at runtime, creates a method call transport for client,
* and provides for server-side invocation of those methods.
* Option to skip leading arguments, for whatever reason, is provided.<p>
* For our Json version of this, we are going to intercept the params as they
* are passed from Gson reflection and massage them back to an object model
* for some reason Gson refuses to do anything but change some objects to its internal map form
* @author Jonathan Groff Copyright (C) NeoCoreTechs 1998-2000, 2015, 2025
*/
public final class ServerInvokeMethodJson extends ServerInvokeMethod {
	private static final boolean DEBUG = true;
    
    public ServerInvokeMethodJson(String tclass, int tskipArgs) throws ClassNotFoundException {
    	super(tclass, tskipArgs);
    }
    /**
     * This constructor populates this object with reflected methods from the
     * designated class.  Reflect hierarchy in reverse (to get proper
     * overload) and look for methods
     * @param tclass The class name we are targeting
     * @param skipArgs > 0 if we want to skip first args.
     */
    public ServerInvokeMethodJson(ClassLoader cl, String tclass, int tskipArgs, boolean hasRemote) throws ClassNotFoundException {  
    	super(cl, tclass, tskipArgs, hasRemote);
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
    		System.out.println("ServerInvokeJson Target method:"+targetMethod+" remote request:"+tmc+" localObject:"+localObject);
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
    					System.out.println("ServerInvokeJson Target method:"+targetMethod+" Calling param: "+params[iparm1]);
    				}
    				for(int iparm2 = skipArgIndex ; iparm2 < pkmnap.methodParams[methodIndex].length; iparm2++) {
    					System.out.println("ServerInvokeJson Target method:"+targetMethod+" Method param: "+pkmnap.methodParams[methodIndex][iparm2]);
    				}
    			}
    			int sumParamRank = 0;
    			if( params.length == pkmnap.methodParams[methodIndex].length-skipArgIndex ) {
    				found = true; // we found a method with required number of params
    				// if skipArgs, don't compare first skipArgs params
    				for(int paramIndex = 0 ; paramIndex < params.length; paramIndex++) {
    					// exact match? If passed param is null, count as 0 rank, below assignable or exact
    					// For our Json version of this, we are going to intercept the params as they
    					// are passed from Gson reflection and massage them back to an object model
    					// for some reason Gson refuses to do anything but change some objects to its internal map form
    					if( params[paramIndex] != null ) {
    						if(params[paramIndex] == com.google.gson.internal.LinkedTreeMap.class) {
    							if(DEBUG) {
    								System.out.println("ServerInvokeJson Target method:"+targetMethod+" Method param: "+pkmnap.methodParams[methodIndex][paramIndex]+" has gson map");
    							}
    							// set the calling param back to method param
    							params[paramIndex] = pkmnap.methodParams[methodIndex][paramIndex+skipArgIndex];
    							// create the method param with constructor using Gson map attributes
    							// start with the ctor of the main object that serves as param to method
    							// paramArray matches params. we will xfer the gson map to param array
    							Constructor ctor = params[paramIndex].getConstructor();
    							// newinstance of param to method call
    							Object o = ctor.newInstance();
    							// extract the args from the map, it has field name, string value
    							com.google.gson.internal.LinkedTreeMap ltm = (com.google.gson.internal.LinkedTreeMap) tmc.getParamArray()[0];
    							Set ltmSet = ltm.entrySet();
    							Iterator ltmIterator = ltmSet.iterator();
    							// each field name and value in gson map
    							while(ltmIterator.hasNext()) {
    								Map.Entry ltmEntry = (Entry) ltmIterator.next();
    								// get the field name from the param based on key in gson map
    								Field[] fields = params[paramIndex].getDeclaredFields();//.getDeclaredField((String) ltmEntry.getKey());
    								Field field = null;
    								for(int j = 0; j < fields.length; j++) {	
    									if(fields[j].getName().equals((String) ltmEntry.getKey()) && 
    											!Modifier.toString(fields[j].getModifiers()).contains("private")) {
    										field = fields[j];
    										break;
    									}
    								}
    								//
    								// try to either set the field directly if not private, or setFieldname mutator method if it is
    								//
    								if(field == null) {
    									StringBuilder sb = new StringBuilder("set");
    									sb.append(String.valueOf(((String)ltmEntry.getKey()).charAt(0)).toUpperCase());
    									sb.append(((String)ltmEntry.getKey()).substring(1));
    									String setString = sb.toString();
    									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" trying mutator "+setString);
    									Method setMethod = params[paramIndex].getDeclaredMethod(setString, new Class[] {String.class});
     									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" mutator "+setMethod+" for "+o);
    									setMethod.invoke(o, ltmEntry.getValue());
    									tmc.getParamArray()[paramIndex] = o;
    									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" mutator "+setMethod+" result "+tmc.getParamArray()[paramIndex] );
    								} else {
    									// get the constructor for the param, we have to assume string constructor
    									Constructor fieldCtor = field.getType().getConstructor(String.class);
    									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" trying ctor "+fieldCtor);
    									// make the new instance, call the string ctor with value from gson map
    									Object ofield = fieldCtor.newInstance(ltmEntry.getValue());
    									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" field "+ofield);
    									// set the value of the newinstance of param to method call
    									field.set(o, ofield);
    									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" field "+ofield+" set for "+o);
    									// set the actual param to new instance of field name from gson map, called with string ctor from gson map
    									tmc.getParamArray()[paramIndex] = o;
      									if(DEBUG)
    										System.out.println("ServerInvokeJson Target method:"+targetMethod+" field "+ofield+" set param array "+tmc.getParamArray()[paramIndex]);
    								}
    							}
    						}
    						//
    						// Establish our ranking of method params to parameters we passed for the call
    						//
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
    					if(DEBUG) {
    						System.out.println("ServerInvokeJson Invoking method:"+methods[methodIndex]+" on object "+localObject+" with params "+Arrays.toString(o1));
    						Object oret = methods[methodIndex].invoke( localObject, o1 );
    						System.out.println("ServerInvokeJson return from invocation:"+oret);
    						return oret;
    					}
    					return methods[methodIndex].invoke( localObject, o1 );
    				} 
    				// invoke it for return
    				if(DEBUG) {
    					System.out.println("ServerInvokeJson Invoking method:"+methods[methodIndex]+" on object "+localObject+" with params "+Arrays.toString(tmc.getParamArray()));
    					Object oret = methods[methodIndex].invoke(localObject, tmc.getParamArray());
    					System.out.println("ServerInvokeJson return from invocation:"+oret);
    					return oret;
    				}
    				return methods[methodIndex].invoke( localObject, tmc.getParamArray() );
    			}
    		} else {
    			whyNotFound = "wrong number of parameters";
    		}
    	}
    	throw new NoSuchMethodException("Method "+targetMethod+" not found in "+pkmnap.className+" "+whyNotFound);
    }

}

