package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.relatrix.server.RelatrixKVServer;

/**
 * The following class allows the transport of RelatrixKV method calls to the server
 * @author Jonathan Groff (C) neoCoreTechs 2021
 *
 */
public class RelatrixKVStatement implements Serializable, RelatrixStatementInterface {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
    private String session = null;
    protected String alias = null;
    protected String methodName;
    protected Object[] paramArray;
    private Object retObj;
    private long retLong;
    private transient CountDownLatch latch;
    private transient CyclicBarrier barrier;
    
    public RelatrixKVStatement() {
   		session = UUID.randomUUID().toString();
    	if(DEBUG)System.out.println("Default Constructor:"+this);
    }
    
    /**
     * Prep RelatrixStatement to send remote method call
     */
    public RelatrixKVStatement(String tmeth, Object ... o1) {
    	this.methodName = tmeth;
    	this.paramArray = o1;
    	this.session = UUID.randomUUID().toString();
    	if(DEBUG)
    		System.out.println("Constructor:"+this);
    }
    /**
     * Prep RelatrixStatement to send remote method call
     */
    public RelatrixKVStatement(String tmeth, String alias, Object ... o1) {
    	this.methodName = tmeth;
    	this.alias = alias;
    	this.paramArray = o1;
    	this.session = UUID.randomUUID().toString();
    	if(DEBUG)
    		System.out.println("Constructor:"+this);
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
  		if(retObj != null) {
  			sb.append("Class ");
  			sb.append(retObj.getClass().getName());
  			sb.append(", ");
  		}
		sb.append(retObj);
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
			// Stream..?
			if( result instanceof Stream) {
					setObjectReturn( new RemoteStream((Stream) result) );
					getCountDownLatch().countDown();
					return;
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			RelatrixKVServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.rocksack.iterator.TailSetKVIterator.class) {
				setObjectReturn( new RemoteTailMapKVIterator(getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.rocksack.iterator.SubSetKVIterator.class ) {
					setObjectReturn( new RemoteSubMapKVIterator(getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.rocksack.iterator.HeadSetKVIterator.class ) {
						setObjectReturn( new RemoteHeadMapKVIterator(getSession()) );
					} else {
						if( result.getClass() == com.neocoretechs.rocksack.iterator.TailSetIterator.class) {
							setObjectReturn( new RemoteTailMapIterator(getSession()) );
						} else {
							if( result.getClass() == com.neocoretechs.rocksack.iterator.SubSetIterator.class) {
								setObjectReturn( new RemoteSubMapIterator(getSession()) );
							} else {
								if( result.getClass() == com.neocoretechs.rocksack.iterator.HeadSetIterator.class) {
									setObjectReturn( new RemoteHeadMapIterator(getSession()) );
								} else {
									if( result.getClass() == com.neocoretechs.rocksack.iterator.EntrySetIterator.class) {
										setObjectReturn( new RemoteEntrySetIterator(getSession()) );
									} else {
										if( result.getClass() == com.neocoretechs.rocksack.iterator.KeySetIterator.class) {
											setObjectReturn( new RemoteKeySetIterator(getSession()) );
										} else {
											if( result.getClass() == com.neocoretechs.rocksack.KeyValue.class) {
												setObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
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
		} else {
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();
	}	

}
