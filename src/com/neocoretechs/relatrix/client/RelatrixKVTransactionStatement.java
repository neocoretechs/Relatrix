package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.stream.SackStream;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.relatrix.TransactionId;
import com.neocoretechs.relatrix.iterator.IteratorWrapper;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteKVIteratorTransaction;

/**
 * The following class allows the transport of method calls to the server {@link RelatrixKVTransactionServer} using a transaction context.
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) neoCoreTechs 2022
 *
 */
public class RelatrixKVTransactionStatement extends RelatrixKVStatement implements Serializable {
	private static final long serialVersionUID = 1452088222610286234L;
	private static boolean DEBUG = false;
    protected TransactionId xid;
    
    public RelatrixKVTransactionStatement() {
    	super();
    	if(DEBUG)
    		System.out.println("Default Constructor:"+this);
    }
    
	public RelatrixKVTransactionStatement(TransactionId xid, String session) {
		super(session);
		this.xid = xid;
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

	public TransactionId getTransactionId() {
    	return xid;
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
			if( result instanceof Stream) {
				result = new IteratorWrapper(((SackStream)result).iterator());
			} else {
				if( result instanceof Iterator ) {
					result = new IteratorWrapper((Iterator<?>) result);
				}
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference using Transport:%s result:%s%n",this.getClass().getName(),this,result);
			}
			// put it in the array and send our intermediary back
			if( result.getClass() == com.neocoretechs.rocksack.KeyValue.class) {
				setObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
				getCountDownLatch().countDown();
				return;
			}
			RelatrixKVTransactionServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == IteratorWrapper.class) {
				setObjectReturn(new ServerSideRemoteKVIteratorTransaction(xid, getSession()));
			} else {
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			}
		} else {
			setObjectReturn(result);
		}
		getCountDownLatch().countDown();
	}	

}
