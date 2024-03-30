package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.server.RelatrixTransactionServer;

/**
 * The following class allows the transport of transaction Relatrix method calls to the server.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class RelatrixTransactionStatement extends RelatrixStatement implements Serializable {
	private static final long serialVersionUID = -503217108835099285L;
	private static boolean DEBUG = false;
    String xid;
    String alias = null;
    
    public RelatrixTransactionStatement() {}
    
    /**
    * Prep RelatrixStatement to send remote method call
    */
    public RelatrixTransactionStatement(String tmeth, String xid, Object ... o1) {
    	super(tmeth, o1);
    	this.xid = xid;
    	if(DEBUG)
    		System.out.println(this.getClass().getName()+" Id:"+xid+" meth:"+tmeth+" o1:"+Arrays.toString(o1));
    }
    /**
     * Prep RelatrixStatement to send remote method call
     */
     public RelatrixTransactionStatement(String tmeth, String alias, String xid, Object ... o1) {
     	super(alias, tmeth, o1);
     	this.xid = xid;
     	if(DEBUG)
     		System.out.println(this.getClass().getName()+" Id:"+xid+" meth:"+tmeth+" o1:"+Arrays.toString(o1));
     }
     
    public RelatrixTransactionStatement(String xid, String session) {
		super(session);
		this.xid = xid;
	}

	public String getTransactionId() {
    	return xid;
    }
    
    @Override
    public synchronized String toString() { return String.format("%s for Session:%s XId:%s Method:%s Arg:%s%n",
             this.getClass().getName(), getSession(), this.xid, methodName,
             (paramArray == null || paramArray.length == 0 ? "nil" : Arrays.toString(paramArray))); }
    
    @Override
 	public synchronized Class<?>[] getParams() {
     	//System.out.println("params:"+paramArray.length);
     	//for(int i = 0; i < paramArray.length; i++)
     		//System.out.println("paramArray "+i+"="+paramArray[i]);
    	if( paramArray == null )
    		paramArray = new Object[0];
         Class<?>[] c = new Class[paramArray.length];
         for(int i = 0; i < paramArray.length; i++)
                 c[i] = paramArray[i].getClass();
         return c;
     }
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval.
	 */
	@Override
	public synchronized void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		Object result = RelatrixTransactionServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
			// Stream..?
			if( result instanceof Stream) {
				setObjectReturn( new RemoteStreamTransaction(xid, (Stream) result) );
				getCountDownLatch().countDown();
				return;
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			RelatrixTransactionServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction.class) {
				setObjectReturn( new RemoteTailSetIteratorTransaction(xid, getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction.class ) {
					setObjectReturn( new RemoteSubSetIteratorTransaction(xid, getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction.class ) {
						setObjectReturn( new RemoteHeadSetIteratorTransaction(xid, getSession()) );
					} else {

						if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction.class) {
							setObjectReturn( new RemoteEntrySetIteratorTransaction(xid, getSession()) );
						} else {
							if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction.class ) {
								setObjectReturn( new RemoteSubMapKVIteratorTransaction(xid, getSession()) );
							} else {
								throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
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
