package com.neocoretechs.relatrix.client.json;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.TransportMorphismInterface;
import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteIteratorClient;
import com.neocoretechs.relatrix.server.RelatrixServer;

import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;


/**
 * The following class allows the transport of Relatrix method calls to the server, and on the server
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, 
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RelatrixJsonStatement extends RelatrixStatement implements RelatrixStatementInterface, Serializable {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    protected String session = null;
    protected Alias alias = null;
    public String methodName;
    public Object[] paramArray;
    protected String[] paramTypes;
    private Object objectReturn;
    protected String returnClass;
    protected transient Class<?>[] params = null;
    private transient CountDownLatch latch;

    public RelatrixJsonStatement() {
    }
    
    public RelatrixJsonStatement(String session) {
    	this.session = session;
    	this.paramArray = new Object[0];
 		this.paramTypes = new String[0];
 		this.params = new Class[0];
    }
    /**
     * Prep the statement for a remote call. Set our types to the actual class types for now..
     * @param tmeth
     * @param o1
     */
    public RelatrixJsonStatement(String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = UUID.randomUUID().toString();
 		this.paramTypes = new String[o1.length];
 		this.params = new Class<?>[o1.length];
 		for(int i = 0; i < o1.length; i++) {
 			paramTypes[i] = o1[i].getClass().getName();
 			params[i] = o1[i].getClass();
 		}
    	packParamArray();
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
    
    public String getReturnClass() {
    	return returnClass;
    }
    
    public void setReturnClass(String returnClass) {
    	this.returnClass = returnClass;
    }
    
    @Override
	public synchronized Object[] getParamArray() { return paramArray; }
    
    public synchronized void setParamArray(Object[] params) {
    	this.paramArray = params;
    }

    /**
     * Get the parameters based on the paramTypes array using class.forName, if an exception is 
     * thrown in that process, use getClass of the actual instance. We have to override any
     * deserialization process that may have convoluted the original types in the parameter array
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
    public synchronized String toString() { return String.format("%s for Session:%s Method:%s params:%s return class:%s%n",
             this.getClass().getName(),session,methodName,
             (paramArray == null ? "nil" : Arrays.toString(paramArray)), returnClass); }
    
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
		if(o instanceof AbstractRelation) {
			objectReturn = TransportMorphism.createTransport((Relation) o);
		} else {
			if(o instanceof TransportMorphismInterface)
				((TransportMorphismInterface)o).packForTransport();
			objectReturn = o;
		}
		if(DEBUG)
			System.out.printf("%s.setObjectReturn %s%n", this.getClass().getName(), objectReturn);
	}

	@Override
	public synchronized Object getObjectReturn() {
		if(objectReturn instanceof TransportMorphismInterface)
			((TransportMorphismInterface)objectReturn).unpackFromTransport();
		else
			if(objectReturn != null && objectReturn.getClass() == TransportMorphism.class)
				objectReturn = TransportMorphism.createMorphism((TransportMorphism)objectReturn);
		if(DEBUG)
			System.out.printf("%s.getObjectReturn returning class %s: %s%n", this.getClass().getName(), objectReturn.getClass().getName(), objectReturn);
		return objectReturn;
	}
	
	protected void packParamArray() {
    	for(int i = 0; i < paramArray.length; i++) {
    		if(paramArray[i] instanceof AbstractRelation) {
    			paramArray[i] = TransportMorphism.createTransport((Relation) paramArray[i]);
    		} else {
    			if(paramArray[i] instanceof TransportMorphismInterface)
        			((TransportMorphismInterface)paramArray[i]).packForTransport();;
    		}
    	}
	}
	
	protected void unpackParamArray() {
		for(int i = 0; i < paramArray.length; i++)
			if(paramArray[i] != null && paramArray[i].getClass() == TransportMorphism.class) {
				paramArray[i] = TransportMorphism.createMorphism((TransportMorphism)paramArray[i]);
			} else {
				if(paramArray[i] instanceof TransportMorphismInterface)
					((TransportMorphismInterface)paramArray[i]).unpackFromTransport();
			}		
	}
	
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval
	 */
	@Override
	public synchronized void process() throws Exception {
		unpackParamArray();
		Object result = RelatrixServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			if( result instanceof BaseIteratorAccessInterface) {
				result = ((BaseIteratorAccessInterface)result).getBaseIterator();
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			RemoteIteratorJsonClient ric = null;
			if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixIterator.class) {	
				ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
							RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixIterator"));
			} else {
				if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator.class ) {
					ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
							RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator"));
				} else {
					if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator.class ) {
						ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
								RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator"));
					} else {
						if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixTailsetIterator.class ) {
							ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
									RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixTailsetIterator"));
						} else {
							if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator.class) {
								ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
										RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator"));
							} else {
								if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator.class) {
									ric = new RemoteIteratorJsonClient(RelatrixServer.address.getHostName(), 
											RelatrixServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator"));
								} else {
									throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
								}
							}
						}
					}
				}
			}
			// Link the object instance to session for later method invocation
			RelatrixServer.sessionToObject.put(ric.getSession(), result);
			JSONObject jric = new JSONObject(ric);
			setReturnClass(RemoteIteratorJsonClient.class.getName());
			setObjectReturn(jric);
		} else {
			result = new JSONObject(result);
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();
	}

}
