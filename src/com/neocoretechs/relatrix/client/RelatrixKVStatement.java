package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.neocoretechs.relatrix.server.RelatrixKVServer;
import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * The following class allows the transport of RelatrixKV method calls to the server
 * @author jg
 *
 */
public class RelatrixKVStatement implements Serializable, RemoteRequestInterface, RemoteResponseInterface {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    private String session = null;
    private String className = "com.neocoretechs.relatrix.RelatrixKV";
    protected String methodName;
    protected Object[] paramArray;
    private Object retObj;
    private long retLong;
    private transient CountDownLatch latch;
    private transient CyclicBarrier barrier;
    
    public RelatrixKVStatement() {}
    
    /**
    * Prep RelatrixStatement to send remote method call
    */
    public RelatrixKVStatement(String tmeth, Object ... o1) {
             methodName = tmeth;
             paramArray = o1;
    }
  
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getClassName()
	 */
    @Override
	public String getClassName() { return className; }
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getSession()
	 */
    @Override
	public String getSession() {
    	if( session == null ) {
    		session = UUID.randomUUID().toString();
    		if( DEBUG ) System.out.println("Generated ID for RelatrixStatement:"+session);
    	}
    	return session; 
    }
    
    protected void setSession(String session) { this.session = session; }
    
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getMethodName()
	 */
    @Override
	public String getMethodName() { return methodName; }
    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getParamArray()
	 */
    @Override
	public Object[] getParamArray() { return paramArray; }

    /* (non-Javadoc)
	 * @see com.neocoretechs.relatrix.client.RemoteRequestInterface#getParams()
	 */
    @Override
	public Class<?>[] getParams() {
    	//System.out.println("params:"+paramArray.length);
    	//for(int i = 0; i < paramArray.length; i++)
    		//System.out.println("paramArray "+i+"="+paramArray[i]);
        Class<?>[] c = new Class[paramArray.length];
        for(int i = 0; i < paramArray.length; i++)
                c[i] = paramArray[i].getClass();
        return c;
    }
  
    public String toString() { return "RelatrixKVStatement for Session:"+
             session+" Class:"+className+" Method:"+methodName+" Arg:"+
             (paramArray == null || paramArray.length == 0 ? "nil" : (paramArray[0] == null ? "NULL PARAM!" : paramArray[0])); }
    
	@Override
	public CountDownLatch getCountDownLatch() {
		return latch;
	}
	@Override
	public void setCountDownLatch(CountDownLatch cdl) {
		latch = cdl;	
	}
	@Override
	public CyclicBarrier getCyclicBarrier() {
		return barrier;
	}
	@Override
	public void setCyclicBarrier(CyclicBarrier cb) {
		barrier = cb;
	}
	@Override
	public void setLongReturn(long val) {
		retLong = val;
	}
	@Override
	public void setObjectReturn(Object o) {
		retObj = o;		
	}

	@Override
	public long getLongReturn() {
		return retLong;
	}

	@Override
	public Object getObjectReturn() {
		return retObj;
	}
	/**
	 * Call methods of the main RelatrixKV class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval. We create an intermediary
	 * that proxyies the functionality back to the server and client, and is serializable and contains the necessary infrastructure
	 * to encapsulate the iterator.<p/>
	 * Note that here we are returning BigSack iterators rather than Relatrix Factory iterators. We can use the native iterators here
	 * because the functionality is available in whole, and we dont have to add the morphism processing aspect.
	 */
	@Override
	public void process() throws Exception {
		Object result = RelatrixKVServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if( result != null && !((result instanceof Serializable) && !(result instanceof Externalizable))) {
			if( DEBUG ) {
				System.out.println("RelatrixKVStatement Storing local object reference for "+getSession()+", data:"+result);
			}
			// put it in the array and send our intermediary back
			RelatrixKVServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.bigsack.iterator.TailSetKVIterator.class) {
				setObjectReturn( new RemoteTailmapKVIterator(getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.bigsack.iterator.SubSetKVIterator.class ) {
					setObjectReturn( new RemoteSubmapKVIterator(getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.bigsack.iterator.HeadSetKVIterator.class ) {
						setObjectReturn( new RemoteHeadmapKVIterator(getSession()) );
					} else {
						if( result.getClass() == com.neocoretechs.bigsack.iterator.TailSetIterator.class) {
							setObjectReturn( new RemoteTailmapIterator(getSession()) );
						} else {
							if( result.getClass() == com.neocoretechs.bigsack.iterator.SubSetIterator.class) {
								setObjectReturn( new RemoteSubmapIterator(getSession()) );
							} else {
								if( result.getClass() == com.neocoretechs.bigsack.iterator.HeadSetIterator.class) {
									setObjectReturn( new RemoteHeadmapIterator(getSession()) );
								} else {
									if( result.getClass() == com.neocoretechs.bigsack.iterator.EntrySetIterator.class) {
										setObjectReturn( new RemoteEntrysetIterator(getSession()) );
									} else {
										if( result.getClass() == com.neocoretechs.bigsack.iterator.KeySetIterator.class) {
											setObjectReturn( new RemoteKeysetIterator(getSession()) );
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
		} else {
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();
	}
	
	

}
