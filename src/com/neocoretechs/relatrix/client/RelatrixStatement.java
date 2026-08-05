package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.Alias;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.TransportMorphismInterface;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClient;
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
public class RelatrixStatement implements Serializable, RelatrixStatementInterface {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    protected UUID session = null;
    protected UUID iteratorId;
    protected Alias alias = null;
    public String methodName;
    public Object[] paramArray;
    protected String[] paramTypes;
    protected Object objectReturn;
    protected String returnClass;
    protected transient Class[] params = null;
    private transient CompletableFuture<Object> completionObject = new CompletableFuture<Object>();
    private transient CountDownLatch completionLatch = new CountDownLatch(1);

    public RelatrixStatement() {
    }
    
    public RelatrixStatement(UUID session) {
    	this.session = session;
    	this.paramArray = new Object[0];
 		this.paramTypes = new String[0];
 		this.params = new Class[0];
    }
    /**
     * Prep the statement for a remote call. Set our types to the actual class types for now..
     * @param session TODO
     * @param tmeth
     * @param o1
     */
    public RelatrixStatement(UUID session, String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = session;
 		this.paramTypes = new String[o1.length];
 		this.params = new Class<?>[o1.length];
 		for(int i = 0; i < o1.length; i++) {
 			paramTypes[i] = o1[i].getClass().getName();
 			params[i] = o1[i].getClass();
 		}
    	packParamArray();
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
    
    public String getReturnClass() {
    	return returnClass;
    }
    
    public void setReturnClass(String returnClass) {
    	this.returnClass = returnClass;
    }
    
    @Override
	public Object[] getParamArray() { return paramArray; }
    
    public void setParamArray(Object[] params) {
    	this.paramArray = params;
    }

    /**
     * Get the parameters based on the paramTypes array using class.forName, if an exception is 
     * thrown in that process, use getClass of the actual instance. We have to override any
     * deserialization process that may have convoluted the original types in the parameter array
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
    public String toString() { return String.format("%s for Session:%s Method:%s params:%s return class:%s%n",
             this.getClass().getName(),session,methodName,
             (paramArray == null ? "nil" : Arrays.toString(paramArray)), returnClass); }
    
	@Override
	public CountDownLatch getCompletionObject() {
		return completionLatch;
	}
    @Override 
    public CompletableFuture<Object> getCompletionFuture() {
    	return completionObject;
    }
	@Override
	public void setCompletionObject() {
		completionLatch = new CountDownLatch(1);
		completionObject = new CompletableFuture<Object>();
	}

	/*@Override
	public synchronized void signalCompletion(Object o) {
		if(o != null)
	    	if (o instanceof Throwable) 
	    		((CompletableFuture)completionObject).completeExceptionally((Throwable)o);
	    	else
	    		((CompletableFuture)completionObject).complete(o);
		if(DEBUG)
			System.out.printf("%s.signalCompletion%n", this.getClass().getName());
	    completionLatch.countDown();
	}*/
	@Override
	public void signalCompletion(Object o) {
	    // Capture state under lock, but do not complete the CF while holding the lock
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
	public Object getObjectReturn() {
		if(objectReturn instanceof TransportMorphismInterface)
			((TransportMorphismInterface)objectReturn).unpackFromTransport();
		else
			if(objectReturn != null && objectReturn.getClass() == TransportMorphism.class)
				objectReturn = TransportMorphism.createMorphism((TransportMorphism)objectReturn);
		if(DEBUG)
			System.out.printf("%s.getObjectReturn returning class %s%n", this.getClass().getName(), objectReturn.getClass().getName());
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
    	if(DEBUG)
			System.out.printf("%s.packParamArray %s%n", this.getClass().getName(),Arrays.toString(paramArray));
	}
	
	protected void unpackParamArray() {
		for(int i = 0; i < paramArray.length; i++)
			if(paramArray[i] != null && paramArray[i].getClass() == TransportMorphism.class) {
				paramArray[i] = TransportMorphism.createMorphism((TransportMorphism)paramArray[i]);
			} else {
				if(paramArray[i] instanceof TransportMorphismInterface)
					((TransportMorphismInterface)paramArray[i]).unpackFromTransport();
			}
	 	if(DEBUG)
			System.out.printf("%s.unpackParamArray%n", this.getClass().getName());
	}
	
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval
	 */
	@Override
	public void process() throws Exception {
		unpackParamArray();
		setCompletionObject();
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
			RemoteIteratorClient ric = null;
			for(int ic = 0; ic < RelatrixServer.iteratorServerClasses.length; ic++) {
				if(result.getClass() == RelatrixServer.iteratorServerClasses[ic]) {	
					ric = new RemoteIteratorClient(session, ((InetSocketAddress)RelatrixServer.address).getAddress().getHostName(), RelatrixServer.iteratorPorts[ic], RelatrixServer.port);
					break;
				}
			}
			if(ric == null)
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			// Link the object instance to session for later method invocation
			ric.setIteratorId(UUID.randomUUID());
			RelatrixServer.IteratorServerProcesses.setIterator(ric.getSession(), ric.getIteratorId(), (Iterator<?>) result);
			setServerObjectReturn(ric);
			signalCompletion(ric);
		} else {
			if(result instanceof AbstractRelation) {
				resolve((Relation) result);
			} else {
				if(result instanceof Result && ((Result)result).get() instanceof AbstractRelation) {
					Relation rel = (Relation) ((Result)result).get();
					resolve(rel);
					((Result)result).set(rel);
				}
			}
			setServerObjectReturn(result);
			signalCompletion(result);
		}
	}
    public static void resolve(Relation target) {
     	Comparable tdomain, tmap, trange;
      	tdomain = (Comparable) ((AbstractRelation)target).getDomain();
    	tmap = (Comparable) ((AbstractRelation)target).getMap();
    	trange = (Comparable) ((AbstractRelation)target).getRange();
    	if(tdomain instanceof AbstractRelation)
    		resolve((Relation) tdomain);
    	if(tmap instanceof AbstractRelation)
    		resolve((Relation) tmap);
    	if(trange instanceof AbstractRelation)
    		resolve((Relation) trange);
    	if(DEBUG)
    		System.out.printf("AbstractRelation.resolve %s %s %s%n", tdomain, tmap, trange);
    }

	@Override
	public void setServerObjectReturn(Object o) {
		objectReturn = o;
		this.paramArray = new Object[0];
 		this.paramTypes = new String[0];
 		this.params = new Class[0];
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
