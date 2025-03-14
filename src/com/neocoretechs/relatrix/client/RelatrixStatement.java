package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteEntrySetIterator;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteHeadSetIterator;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteKeySetIterator;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteSetIterator;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteSubSetIterator;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteTailSetIterator;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;
import com.neocoretechs.rocksack.Alias;

/**
 * The following class allows the transport of Relatrix method calls to the server, and on the server
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, and in the case of an Iterator, sets
 * up the proper instance of {@link RemoteIterator} to install a persistent Iterator to receive calls to deliver iterated objects.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RelatrixStatement implements Serializable, RelatrixStatementInterface {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    protected String session = null;
    protected Alias alias = null;
    protected String methodName;
    protected Object[] paramArray;
    private Object retObj;
    private long retLong;
    private transient CountDownLatch latch;
    private transient CyclicBarrier barrier;
    
    public RelatrixStatement() {
   		session = UUID.randomUUID().toString();
   		this.paramArray = new Object[0];
    }
    
    public RelatrixStatement(String session) {
    	this.session = session;
    	this.paramArray = new Object[0];
    }
    /**
     * Prep RelatrixStatement to send remote method call
     */
    public RelatrixStatement(String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = UUID.randomUUID().toString();
    	packParamArray();
    }
   
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getSession()
	 */
    @Override
	public synchronized String getSession() {
    	return session; 
    }
    
    protected synchronized void setSession(String session) { this.session = session; }
    
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getMethodName()
	 */
    @Override
	public synchronized String getMethodName() { return methodName; }
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getParamArray()
	 */
    @Override
	public synchronized Object[] getParamArray() { return paramArray; }

    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getParams()
	 */
    @Override
	public synchronized Class<?>[] getParams() {
    	//System.out.println("params:"+paramArray.length);
    	//for(int i = 0; i < paramArray.length; i++)
    		//System.out.println("paramArray "+i+"="+paramArray[i]);
        Class<?>[] c = new Class[paramArray.length];
        for(int i = 0; i < paramArray.length; i++)
                c[i] = paramArray[i].getClass();
        return c;
    }
    @Override
    public synchronized String toString() { return String.format("%s for Session:%s Method:%s Arg:%s%n",
             this.getClass().getName(),session,methodName,
             (paramArray == null || paramArray.length == 0 ? "nil" : Arrays.toString(paramArray))); }
    
	@Override
	public synchronized CountDownLatch getCountDownLatch() {
		return latch;
	}
	@Override
	public synchronized void setCountDownLatch(CountDownLatch cdl) {
		latch = cdl;	
	}
	@Override
	public synchronized CyclicBarrier getCyclicBarrier() {
		return barrier;
	}
	@Override
	public synchronized void setCyclicBarrier(CyclicBarrier cb) {
		barrier = cb;
	}
	@Override
	public synchronized void setLongReturn(long val) {
		retLong = val;
	}
	@Override
	public synchronized void setObjectReturn(Object o) {
		if(o instanceof AbstractRelation) {
			retObj = TransportMorphism.createTransport((AbstractRelation) o);
		} else {
			if(o instanceof Result)
				((Result)o).rigForTransport();
			retObj = o;
		}
		if(DEBUG)
			System.out.printf("%s.setObjectReturn %s%n", this.getClass().getName(), retObj);
	}

	@Override
	public synchronized long getLongReturn() {
		return retLong;
	}

	@Override
	public synchronized Object getObjectReturn() {
		if(retObj instanceof Result)
			((Result)retObj).unpackFromTransport();
		else
			if(retObj != null && retObj.getClass() == TransportMorphism.class)
				retObj = TransportMorphism.createMorphism((TransportMorphism)retObj);
		if(DEBUG)
			System.out.printf("%s.getObjectReturn returning %s%n", this.getClass().getName(), retObj);
		return retObj;
	}
	
	protected void packParamArray() {
    	for(int i = 0; i < paramArray.length; i++) {
    		if(paramArray[i] instanceof AbstractRelation) {
    			paramArray[i] = TransportMorphism.createTransport((AbstractRelation) paramArray[i]);
    		}
    	}
	}
	
	protected void unpackParamArray() {
		for(int i = 0; i < paramArray.length; i++)
			if(paramArray[i].getClass() == TransportMorphism.class)
				paramArray[i] = TransportMorphism.createMorphism((TransportMorphism)paramArray[i]);
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
			// put it in the array and send our intermediary back
			RelatrixServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixIterator.class) {
				setObjectReturn( new ServerSideRemoteSetIterator(getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator.class ) {
					setObjectReturn( new ServerSideRemoteSubSetIterator(getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator.class ) {
						setObjectReturn( new ServerSideRemoteHeadSetIterator(getSession()) );
					} else {
						if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixTailsetIterator.class ) {
							setObjectReturn( new ServerSideRemoteTailSetIterator(getSession()) );
						} else {
							if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator.class) {
								setObjectReturn( new ServerSideRemoteEntrySetIterator(getSession()) );
							} else {
								if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKeysetIterator.class) {
									setObjectReturn( new ServerSideRemoteKeySetIterator(getSession()) );
								} else {
									throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
								}
							}
						}
					}
				}
			}
		} else {
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();
	}

}
