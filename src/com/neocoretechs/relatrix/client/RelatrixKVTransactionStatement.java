package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;

import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;

/**
 * The following class allows the transport of RelatrixKV method calls to the server using a ransaction context.
 * @author Jonathan Groff (C) neoCoreTechs 2022
 *
 */
public class RelatrixKVTransactionStatement extends RelatrixKVStatement {
	private static boolean DEBUG = true;
    static final long serialVersionUID = 8649844374668828845L;
    String xid;
    
    public RelatrixKVTransactionStatement() {}
    
    public RelatrixKVTransactionStatement(String xid, String tmeth, Object ... o1) {
    	super(tmeth, o1);
    	this.xid = xid;
    }
    
    public String getTransactionId() {
    	return xid;
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
		Object result = RelatrixKVTransactionServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if( result != null && !((result instanceof Serializable) && !(result instanceof Externalizable))) {					
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
			RelatrixKVTransactionServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == com.neocoretechs.rocksack.iterator.TailSetKVIterator.class) {
				setObjectReturn( new RemoteTailMapKVIteratorTransaction(xid, getSession()) );
			} else {
				if(result.getClass() == com.neocoretechs.rocksack.iterator.SubSetKVIterator.class ) {
					setObjectReturn( new RemoteSubMapKVIteratorTransaction(xid, getSession()) );
				} else {
					if(result.getClass() == com.neocoretechs.rocksack.iterator.HeadSetKVIterator.class ) {
						setObjectReturn( new RemoteHeadMapKVIteratorTransaction(xid, getSession()) );
					} else {
						if( result.getClass() == com.neocoretechs.rocksack.iterator.TailSetIterator.class) {
							setObjectReturn( new RemoteTailMapIteratorTransaction(xid, getSession()) );
						} else {
							if( result.getClass() == com.neocoretechs.rocksack.iterator.SubSetIterator.class) {
								setObjectReturn( new RemoteSubMapIteratorTransaction(xid, getSession()) );
							} else {
								if( result.getClass() == com.neocoretechs.rocksack.iterator.HeadSetIterator.class) {
									setObjectReturn( new RemoteHeadMapIteratorTransaction(xid, getSession()) );
								} else {
									if( result.getClass() == com.neocoretechs.rocksack.iterator.EntrySetIterator.class) {
										setObjectReturn( new RemoteEntrySetIteratorTransaction(xid, getSession()) );
									} else {
										if( result.getClass() == com.neocoretechs.rocksack.iterator.KeySetIterator.class) {
											setObjectReturn( new RemoteKeySetIteratorTransaction(xid, getSession()) );
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