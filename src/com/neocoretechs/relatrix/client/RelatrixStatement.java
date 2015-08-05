package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * The following class allows the transport of Relatrix method calls to the server
 * @author jg
 *
 */
public class RelatrixStatement implements Serializable, RemoteRequestInterface, RemoteResponseInterface {
    static final long serialVersionUID = 8649844374668828845L;
    private static boolean DEBUG = false;
    private String session = null;
    private String className = "com.neocoretechs.relatrix.Relatrix";
    private String methodName;
    private Object[] paramArray;
    private Object retObj;
    private long retLong;
    private transient CountDownLatch latch;
    private transient CyclicBarrier barrier;
    
    public RelatrixStatement() {}
    /**
    * Prep RelatrixStatement to send remote method call
    */
 
    public RelatrixStatement(String tmeth, Object ... o1) {
             //session = tsession;
             //className = tclass;
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
             Class<?>[] c = new Class[paramArray.length];
             for(int i = 0; i < paramArray.length; i++)
                     c[i] = paramArray[i].getClass();
             return c;
    }
  
    public String toString() { return "RelatrixStatement for Session:"+
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

}
