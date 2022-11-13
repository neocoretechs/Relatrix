package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * The following class allows the transport of Relatrix method calls to the server.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RelatrixStatement implements Serializable, RelatrixStatementInterface {
	private static boolean DEBUG = true;
    static final long serialVersionUID = 8649844374668828845L;
    private String session = null;
    protected String methodName;
    protected Object[] paramArray;
    private Object retObj;
    private long retLong;
    private transient CountDownLatch latch;
    private transient CyclicBarrier barrier;
    
    public RelatrixStatement() {}
    
    /**
    * Prep RelatrixStatement to send remote method call
    */
    public RelatrixStatement(String tmeth, Object ... o1) {
             methodName = tmeth;
             paramArray = o1;
    }
  

    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getSession()
	 */
    @Override
	public synchronized String getSession() {
    	if( session == null ) {
    		session = UUID.randomUUID().toString();
    		if( DEBUG ) 
      			System.out.printf("%s Generated ID for %s%n",this.getClass().getName(),session);
    	}
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
             (paramArray == null || paramArray.length == 0 ? "nil" : (paramArray[0] == null ? "NULL PARAM!" : paramArray[0]))); }
    
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
		retObj = o;		
	}

	@Override
	public synchronized long getLongReturn() {
		return retLong;
	}

	@Override
	public synchronized Object getObjectReturn() {
		return retObj;
	}
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval
	 */
	@Override
	public synchronized void process() throws Exception {
		Object result = RelatrixServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
			// Stream..?
			if( result instanceof Stream) {
					setObjectReturn( new RemoteStream(result) );
					getCountDownLatch().countDown();
					return;
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			RelatrixServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixIterator.class) {
				setObjectReturn( new RemoteTailSetIterator(getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubsetIterator.class ) {
					setObjectReturn( new RemoteSubSetIterator(getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator.class ) {
						setObjectReturn( new RemoteHeadSetIterator(getSession()) );
					} else {
						if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKeyIterator.class) {
							setObjectReturn( new RemoteKeySetIterator(getSession()) );
						} else {
							if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubmapIterator.class ) {
								setObjectReturn( new RemoteSubMapIterator(getSession()) );
							} else {
								if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadmapIterator.class ) {
									setObjectReturn( new RemoteHeadMapIterator(getSession()) );
								} else {
									if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixEntrysetIterator.class) {
										setObjectReturn( new RemoteEntrySetIterator(getSession()) );
									} else {
										if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKVIterator.class ) {
											setObjectReturn( new RemoteTailMapKVIterator(getSession()) );
										} else {
											if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadmapKVIterator.class ) {
												setObjectReturn( new RemoteHeadMapKVIterator(getSession()) );
											} else {
												if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubmapKVIterator.class ) {
													setObjectReturn( new RemoteSubMapKVIterator(getSession()) );
												} else {
													throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
												}
											}
										}
									}
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
