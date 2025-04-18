package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.stream.SackStream;
import com.neocoretechs.rocksack.KeyValue;

import com.neocoretechs.relatrix.iterator.IteratorWrapper;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;

/**
 * The following class allows the transport of RelatrixKV method calls to the server.
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) neoCoreTechs 2021
 *
 */
public class RelatrixKVStatement implements Serializable, RelatrixStatementInterface {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    protected String session = null;
    protected String methodName;
    protected Object[] paramArray;
    protected String[] paramTypes;
    protected transient Class<?>[] params = null;
    private Object objectReturn;
    private String returnClass;
    private transient CountDownLatch latch;
    
    public RelatrixKVStatement() {
    }
    
    public RelatrixKVStatement(String session) {
    	this.session = session;
 		this.paramArray = new Object[0];
 		this.paramTypes = new String[0];
		this.params = new Class[0];
    }
    /**
     * Prep RelatrixStatement to send remote method call
     */
    public RelatrixKVStatement(String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = UUID.randomUUID().toString();
		this.paramTypes = new String[o1.length];
 		this.params = new Class<?>[o1.length];
 		for(int i = 0; i < o1.length; i++) {
 			paramTypes[i] = o1[i].getClass().getName();
 			params[i] = o1[i].getClass();
 		}
    }
   
    @Override
	public synchronized String getSession() {
    	return session; 
    }
    
    public synchronized void setSession(String session) { this.session = session; }
    
    @Override
	public synchronized String getMethodName() { return methodName; }
    
    public synchronized void setMethodName(String methodName) {
    	this.methodName = methodName;
    }
  
    @Override
	public synchronized Object[] getParamArray() { return paramArray; }
    
    public synchronized void setParamArray(Object[] params) {
    	this.paramArray = params;
    }
    
    public String getReturnClass() {
    	return returnClass;
    }
    
    public void setReturnClass(String returnClass) {
    	this.returnClass = returnClass;
    }
    
    /**
     * Get the parameters based on the paramTypes array using class.forName, if an exception is 
     * thrown in that process, use getClass of the actual instance
     */
    @Override
	public synchronized Class<?>[] getParams() {
      	if(params == null) {
    		params = new Class<?>[paramArray.length];
    		for(int i = 0; i < paramArray.length; i++) {
    			try {
    				params[i] = Class.forName(paramTypes[i]); // set deserialization to rights
    			} catch (ClassNotFoundException e) {
    				params[i] = paramArray[i].getClass(); // default, may not be relevant
    			}
    		}
    	}
    	return params;
    }
    
    @Override
    public synchronized String toString() { 
    	StringBuilder sb = new StringBuilder(String.format("<<<<<%s%n" ,this.getClass().getName()));
    	sb.append(String.format("for Session:%s%nMethod:%s",session,methodName));
    	if(paramArray == null || paramArray.length == 0) {
    			sb.append("()");
    	} else {
    		sb.append("(");
    		for(Object param: paramArray) {
    			if(param == null) {
    				sb.append(" null,");
    			} else {
    				sb.append(" ");
    				sb.append(param.getClass());
    				sb.append(" ");
    				sb.append(param.toString());
    				sb.append(",");
    			}
    		}
    		sb.append(")");
    	}
  		sb.append("\r\nReturn Object: ");
  		if(objectReturn != null) {
  			sb.append("Class ");
  			sb.append(objectReturn.getClass().getName());
  			sb.append(", ");
  		}
		sb.append(objectReturn);
		sb.append(" >>>>>\r\n");
    	return sb.toString();
    }
    
	@Override
	public synchronized CountDownLatch getCountDownLatch() {
		return latch;
	}
   
	@Override
	public synchronized void setCountDownLatch(CountDownLatch cdl) {
		latch = cdl;	
	}
		
	@Override
	public synchronized void setObjectReturn(Object o) {
		objectReturn = o;		
	}

	@Override
	public synchronized Object getObjectReturn() {
		return objectReturn;
	}
	/**
	 * Call methods of the main RelatrixKV class, which will return an instance or an object that is not Serializable.<p/>
	 * RealtrixKV invokes to original retrieval or storage method, possibly returning an iterator or stream.<p/>
	 * In the case if non-Serializable return type of Iterator ro Stream, we save it server side and link it to the session for later retrieval.<br/>
	 * We create an intermediary that proxies the functionality back to the server and client, and is Serializable and contains 
	 * the necessary infrastructure to encapsulate the iterator or stream.<p/>
	 * Note that here we are returning RockSack iterators and streams rather than Relatrix Factory iterators and streams.<br/>
	 * We can use the native iterators and streams here
	 * because the functionality is available in whole, and we dont have to add the morphism processing aspect.
	 */
	@Override
	public synchronized void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		Object result = RelatrixKVServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if( result != null && !((result instanceof Serializable) && !(result instanceof Externalizable))) {					
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			if( result instanceof Stream) {
				result = new IteratorWrapper(((SackStream)result).iterator());
			} else {
				if( result instanceof Iterator ) {
					result = new IteratorWrapper((Iterator<?>) result);
				}
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			if( result.getClass() == com.neocoretechs.rocksack.KeyValue.class) {
				setObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
				getCountDownLatch().countDown();
				return;
			}
			RelatrixKVServer.sessionToObject.put(getSession(), result);
			RemoteIteratorKVClient ric = null;
			if(result.getClass() == IteratorWrapper.class) {	
				ric = new RemoteIteratorKVClient(RelatrixKVServer.address.getHostName(), 
							RelatrixKVServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.IteratorWrapper"));
			} else {
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			}
			// Link the object instance to session for later method invocation
			RelatrixKVServer.sessionToObject.put(ric.getSession(), result);
			setObjectReturn(ric);
		} else {
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();		
	}
}
