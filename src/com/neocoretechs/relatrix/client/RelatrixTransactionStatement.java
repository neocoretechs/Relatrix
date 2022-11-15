package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.server.RelatrixServer;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;

/**
 * The following class allows the transport of transaction Relatrix method calls to the server.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class RelatrixTransactionStatement extends RelatrixStatement {
	private static boolean DEBUG = true;
    static final long serialVersionUID = 8649844374668828846L;
    String xid;
    
    public RelatrixTransactionStatement() {}
    
    /**
    * Prep RelatrixStatement to send remote method call
    */
    public RelatrixTransactionStatement(String xid, String tmeth, Object ... o1) {
    	super(tmeth, o1);
    	this.xid = xid;
    }
    
    public String getTransactionId() {
    	return xid;
    }
    
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval.
	 */
	@Override
	public synchronized void process() throws Exception {
		Object result = RelatrixTransactionServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
			// Stream..?
			if( result instanceof Stream) {
					setObjectReturn( new RemoteStreamTransaction(xid, result) );
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
						if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKeyIteratorTransaction.class) {
							setObjectReturn( new RemoteKeySetIteratorTransaction(xid, getSession()) );
						} else {
							if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubmapIteratorTransaction.class ) {
								setObjectReturn( new RemoteSubMapIteratorTransaction(xid, getSession()) );
							} else {
								if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadmapIteratorTransaction.class ) {
									setObjectReturn( new RemoteHeadMapIteratorTransaction(xid, getSession()) );
								} else {
									if( result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction.class) {
										setObjectReturn( new RemoteEntrySetIteratorTransaction(xid, getSession()) );
									} else {
										if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixKVIteratorTransaction.class ) {
											setObjectReturn( new RemoteTailMapKVIteratorTransaction(xid, getSession()) );
										} else {
											if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixHeadmapKVIteratorTransaction.class ) {
												setObjectReturn( new RemoteHeadMapKVIteratorTransaction(xid, getSession()) );
											} else {
												if(result.getClass() == com.neocoretechs.relatrix.iterator.RelatrixSubmapKVIteratorTransaction.class ) {
													setObjectReturn( new RemoteSubMapKVIteratorTransaction(xid, getSession()) );
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
