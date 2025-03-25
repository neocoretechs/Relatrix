package com.neocoretechs.relatrix.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.Map.Entry;

import com.google.gson.internal.LinkedTreeMap;
import com.neocoretechs.relatrix.server.HandlerClassLoader;

public class JsonUtil {
	private static boolean DEBUG = true;
	static HandlerClassLoader hcl = null;

	// TreeMap of method name mapping to list of classes to arrays of name and params
	protected static TreeMap<String, MethodAccess> methodLookup = new TreeMap<String, MethodAccess>();
	
	public JsonUtil() {}
	
	public static void recurse(com.google.gson.internal.LinkedTreeMap map) {
		for(Object e: map.entrySet()) {
			Entry me = (Entry)e;
			if(me.getValue().getClass() == com.google.gson.internal.LinkedTreeMap.class) {
				System.out.println("--Recurse submap:"+me.getKey());
				recurse((LinkedTreeMap) me.getValue());
				System.out.println("--End recurse submap");
			} else {
				System.out.println(me.getKey()+" "+me.getValue().getClass()+" "+me.getValue());
				if(me.getValue().getClass() == Double.class) {
					String dval2 = String.format("[0x%16X]",Math.round((Double)(me.getValue())));
					System.out.println("double val:"+dval2);
				}
			}
		}
		System.out.println("--End of map");
	}
	
	public static Object jsonMapToObject(Class<?> returnClass, com.google.gson.internal.LinkedTreeMap map) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
			if(DEBUG) {
				System.out.println("JsonUtil return class:"+returnClass+" gson map: "+map);
				for(Object e: map.entrySet()) {
					Entry me = (Entry)e;
					System.out.println(e);
					if(me.getValue().getClass() == com.google.gson.internal.LinkedTreeMap.class) {
						System.out.println("**Submap:");
						recurse((LinkedTreeMap) me.getValue());
						System.out.println("**End recurse submap");
					} else {
						System.out.println(me.getKey()+" "+me.getValue().getClass()+" "+me.getValue());
						if(me.getValue().getClass() == Double.class) {
							String dval2 = String.format("[0x%16X]",Math.round((Double)(me.getValue())));
							System.out.println("double val:"+dval2);
						}
					}
				}
				System.out.println("-End of submap");
			}
			//if(hcl == null)
			//	hcl = new HandlerClassLoader();
			//Class<?> returnClazz = hcl.loadClass(returnClass, true);
	    	//MethodAccess ma = init(returnClass);
			// create the method param with constructor using Gson map attributes
			// start with the ctor of the main object that serves as param to method
			// paramArray matches params. we will xfer the gson map to param array
			Constructor<?> ctor = returnClass.getConstructor();
			// newinstance of param to method call
			Object o = ctor.newInstance();
			// extract the args from the map, it has field name, string value
			Set ltmSet = map.entrySet();
			Iterator<?> ltmIterator = ltmSet.iterator();
			// each field name and value in gson map
			while(ltmIterator.hasNext()) {
				Map.Entry ltmEntry = (Entry) ltmIterator.next();
				// get the field name from the param based on key in gson map
				Field[] fields = returnClass.getDeclaredFields();//.getDeclaredField((String) ltmEntry.getKey());
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
						System.out.println("ServerInvokeJson trying mutator "+setString);
					Method setMethod = returnClass.getDeclaredMethod(setString, new Class[] {String.class});
						if(DEBUG)
						System.out.println("ServerInvokeJson mutator "+setMethod+" for "+o);
					setMethod.invoke(o, ltmEntry.getValue());
					if(DEBUG)
						System.out.println("ServerInvokeJson mutator "+setMethod+" result "+o);
				} else {
					// get the constructor for the param, we have to assume string constructor
					Constructor fieldCtor = field.getType().getConstructor(String.class);
					if(DEBUG)
						System.out.println("ServerInvokeJson trying ctor "+fieldCtor);
					// make the new instance, call the string ctor with value from gson map
					Object ofield = fieldCtor.newInstance(ltmEntry.getValue());
					if(DEBUG)
						System.out.println("ServerInvokeJson field "+ofield);
					// set the value of the newinstance of param to method call
					field.set(o, ofield);
					if(DEBUG)
						System.out.println("ServerInvokeJson field "+ofield+" set for "+o);
					// set the actual param to new instance of field name from gson map, called with string ctor from gson map
					if(DEBUG)
						System.out.println("ServerInvokeJson field "+ofield+" set param array "+o);
				}
			}
			return o;
	}
	 /**
     * Build arrays and lists of method names and parameters to facilitate method lookup and invocation.
     * Methods are looked up by name, then parameters are compared such that overloaded methods can be invoked properly.
     * @param tclass The class to reflect
     * @throws ClassNotFoundException
     */
    private static MethodAccess init(String tclass) throws ClassNotFoundException {
    	MethodAccess ma = null;
    	ma = methodLookup.get(tclass);
    	if(ma != null)
    		return ma;
    	Method[] methods;
    	MethodNamesAndParams pkmnap = new MethodNamesAndParams();
    	pkmnap.classClass = Class.forName(tclass);// hcl.loadClass(tclass, true);
    	pkmnap.className = tclass;
    	methods = pkmnap.classClass.getMethods();
    	for(int i = methods.length-1; i >= 0 ; i--) {
    		pkmnap.methodNames.add(methods[i].getName());
    		System.out.println("JsonUtil "+tclass+" Method "+methods[i].toString());
    	}
    	// create arrays
    	pkmnap.methodParams = new Class[pkmnap.methodNames.size()][];
    	pkmnap.methodSigs = new String[pkmnap.methodNames.size()];
    	pkmnap.returnTypes = new Class[pkmnap.methodNames.size()];
    	int methCnt = 0;
    	//
    	for(int i = methods.length-1; i >= 0 ; i--) {
    		pkmnap.methodParams[methCnt] = methods[i].getParameterTypes();
    		pkmnap.methodSigs[methCnt] = methods[i].toString();
    		pkmnap.returnTypes[methCnt] = methods[i].getReturnType();
    		if( pkmnap.returnTypes[methCnt] == void.class ) 
    			pkmnap.returnTypes[methCnt] = Void.class;
    	}
    	ma = new MethodAccess(methods, pkmnap);
    	methodLookup.put(tclass,  ma);
    	return ma;	
    }
    
    public static class MethodAccess {
		public Method[] methods;
    	public MethodNamesAndParams namesAndPArams;
      	public MethodAccess(Method[] methods, MethodNamesAndParams namesAndPArams) {
    		this.methods = methods;
    		this.namesAndPArams = namesAndPArams;
    	}
    }

}
