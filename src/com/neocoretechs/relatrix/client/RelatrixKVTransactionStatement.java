package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.UUID;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteEntrySetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteHeadMapIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteHeadMapKVIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteKeySetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteSubMapIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteSubMapKVIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteTailMapIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteTailMapKVIteratorTransaction;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

/**
 * The following class allows the transport of RelatrixKV method calls to the server using a ransaction context.
 * @author Jonathan Groff (C) neoCoreTechs 2022
 *
 */
public class RelatrixKVTransactionStatement extends RelatrixKVStatement implements Serializable {
	private static final long serialVersionUID = 1452088222610286234L;
	private static boolean DEBUG = false;
    protected TransactionId xid;
    
    public RelatrixKVTransactionStatement() { if(DEBUG)System.out.println("Default Constructor:"+this);}
    
	public RelatrixKVTransactionStatement(TransactionId xid, String session) {
		this.xid = xid;
		this.session = session;
	}
	
	public RelatrixKVTransactionStatement(String tmeth, Object ... o1) {
		super(tmeth, o1);
		if(o1.length > 1) {
			if(o1[0].getClass().equals(TransactionId.class)) {
				this.xid = (TransactionId) o1[0];
			} else {
				if(o1[1].getClass().equals(TransactionId.class))
					this.xid = (TransactionId) o1[1];
			}
		} else {
			if(o1.length > 0) {
				if(o1[0].getClass().equals(TransactionId.class))
					this.xid = (TransactionId) o1[0];
			}
		}
	}
	/*
	public RelatrixKVTransactionStatement(String tmeth, TransactionId xid, Object ... o1) {
		super(tmeth, new Object[]{xid, o1});
	}
	
	public RelatrixKVTransactionStatement(String tmeth, Alias alias, TransactionId xid, Object ... o1) {
		super(tmeth, new Object[]{alias, xid, o1});
	}
	*/
	public TransactionId getTransactionId() {
    	return xid;
    }

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

    @Override
    public synchronized String toString() { 
    	String s = super.toString();
    	return "Xid:"+xid+" "+s;
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
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			if( result instanceof BaseIteratorAccessInterface) {
				result = ((BaseIteratorAccessInterface)result).getBaseIterator();
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference using Transport:%s result:%s%n",this.getClass().getName(),this,result);
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
