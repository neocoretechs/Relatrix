package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;

import java.net.InetSocketAddress;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.KeyValue;

import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;
import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

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
    protected UUID session = null;
    protected UUID iteratorId;
    protected String methodName;
    protected Object[] paramArray;
    protected String[] paramTypes;
    protected transient Class<?>[] params = null;
    private Object objectReturn;
    private String returnClass;
    private transient CompletableFuture<Object> completionObject = new CompletableFuture<Object>();
    private transient CountDownLatch completionLatch = new CountDownLatch(1);
    
    public RelatrixKVStatement() {
    }
    
    public RelatrixKVStatement(UUID session) {
    	this.session = session;
 		this.paramArray = new Object[0];
 		this.paramTypes = new String[0];
		this.params = new Class[0];
    }
    /**
     * Prep RelatrixStatement to send remote method call
     * @param session TODO
     */
    public RelatrixKVStatement(UUID session, String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = session;
		this.paramTypes = new String[o1.length];
 		this.params = new Class<?>[o1.length];
 		for(int i = 0; i < o1.length; i++) {
 			paramTypes[i] = o1[i].getClass().getName();
 			params[i] = o1[i].getClass();
 		}
    }
   
    @Override
	public UUID getSession() {
    	return session; 
    }
    
    public void setSession(UUID session) { this.session = session; }
    
    @Override
	public String getMethodName() { return methodName; }
    
    public void setMethodName(String methodName) {
    	this.methodName = methodName;
    }
  
    @Override
	public Object[] getParamArray() { return paramArray; }
    
    public void setParamArray(Object[] params) {
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
	public Class<?>[] getParams() {
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
    public String toString() { 
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
	public CountDownLatch getCompletionObject() {
		return completionLatch;
	}
    
	@Override
	public void setCompletionObject() {
		completionObject = new CompletableFuture<Object>();
		completionLatch = new CountDownLatch(1);
	}

	@Override
	public void signalCompletion(Object o) {
	    // Capture state under lock, but do not complete the CF while holding the lock
		if(completionLatch == null)
			System.err.printf("%s.signalCompletion: COMPLETION LATCH IS NULL for rs=%x:%n",this.getClass().getName(), System.identityHashCode(this));
		else
			completionLatch.countDown();
	    // Log before attempting to complete
	    if (DEBUG)
	        System.out.printf("%s.SIGNAL before complete rs=%x cf=%x oClass=%s%n",this.getClass().getName(),System.identityHashCode(this), completionObject == null ? 0 : System.identityHashCode(completionObject), o == null ? "null" : o.getClass().getName());
	    // Complete the CompletableFuture outside the synchronized block
	    if (completionObject != null) {
	        try {
	            if (o instanceof Throwable) 
	            	completionObject.completeExceptionally((Throwable) o);
	            else 
	            	completionObject.complete(o);
	        } catch (Throwable t) {
	            // Ensure caller doesn't hang if complete throws
	            try { 
	            	completionObject.completeExceptionally(t); 
	            } catch (Throwable ignore) {}
	            System.err.printf("%s.signalCompletion: cf.complete threw for rs=%x: %s%n",this.getClass().getName(), System.identityHashCode(this), t);
	        }
	    } else {
	        if (DEBUG) 
	            System.out.printf("%s.SIGNAL no CompletableFuture present for rs=%x%n",this.getClass().getName(), System.identityHashCode(this));
	    }
	    if(DEBUG)
	        System.out.printf("%s.SIGNAL after complete rs=%x cf=%x done=%b%n",this.getClass().getName(), System.identityHashCode(this),(completionObject != null ? System.identityHashCode(completionObject) : 0), (completionObject != null ? completionObject.isDone() : false));
	}
	
	@Override
	public void setObjectReturn(Object o) {
		objectReturn = o;		
	}

	@Override
	public Object getObjectReturn() {
		return objectReturn;
	}
	@Override
	public CompletableFuture<Object> getCompletionFuture() {
		return completionObject;
	}

	/**
	 * Call methods of the main RelatrixKV class, which will return an instance or an object that is not Serializable.<p>
	 * RealtrixKV invokes to original retrieval or storage method, possibly returning an iterator or stream.<p>
	 * In the case if non-Serializable return type of Iterator or Stream, we save it server side and link it to the session for later retrieval.<br>
	 * We create an intermediary that proxies the functionality back to the server and client, and is Serializable and contains 
	 * the necessary infrastructure to encapsulate the iterator or stream.<p>
	 * Note that here we are returning RockSack iterators and streams rather than Relatrix Factory iterators and streams.<br>
	 * We can use the native iterators and streams here
	 * because the functionality is available in whole, and we dont have to add the morphism processing aspect.
	 */
	@Override
	public void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		setCompletionObject();
		Object result = RelatrixKVServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if( result != null && !(result instanceof Serializable) && !(result instanceof Externalizable)) {					
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			if( result instanceof BaseIteratorAccessInterface) {
				result = ((BaseIteratorAccessInterface)result).getBaseIterator();
				if( DEBUG ) {
					System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
				}
				RemoteIteratorClient ric = null;
				if(result.getClass() == RelatrixKVServer.iteratorServerClass) {
					if( DEBUG ) {
						System.out.printf("%s setting RemoteIteratorClient for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
					}
					ric = new RemoteIteratorClient(session, ((InetSocketAddress)RelatrixKVServer.address).getAddress().getHostName(), RelatrixKVServer.iteratorPorts[0], RelatrixKVServer.port);
				}
				if(ric == null)
					throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
				// Link the object instance to session for later method invocation
				RelatrixKVServer.sessionToObject.put(ric.getSession(), result);
				setServerObjectReturn(ric);
				signalCompletion(ric);
			} else
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			return;
		}
		// put it in the array and send our intermediary back
		if( result.getClass() == com.neocoretechs.rocksack.KeyValue.class) {
			if( DEBUG ) {
				System.out.printf("%s setting kev/value object return for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			setServerObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
			signalCompletion(result);
			return;
		}
		setServerObjectReturn(result);
		signalCompletion(result);
	}

	@Override
	public void setServerObjectReturn(Object o) {
		objectReturn = 0;
	}

	@Override
	public UUID getIteratorId() {
		return iteratorId;
	}

	@Override
	public void setIteratorId(UUID itid) {
		this.iteratorId = itid;	
	}

	@Override
	public void setClient(RemoteIteratorClient iteratorClient) {
	}
}


