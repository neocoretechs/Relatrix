package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.stream.SackStream;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.client.iterator.RemoteIteratorClientTransaction;
import com.neocoretechs.relatrix.iterator.IteratorWrapper;
import com.neocoretechs.relatrix.server.RelatrixKVTransactionServer;

/**
 * The following class allows the transport of method calls to the server {@link RelatrixKVTransactionServer} using a transaction context.
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) NeoCoreTechs 2022
 *
 */
public class RelatrixKVTransactionStatement extends RelatrixKVStatement implements RelatrixKVTransactionStatementInterface, Serializable {
	private static final long serialVersionUID = 1452088222610286234L;
	private static boolean DEBUG = false;
    protected TransactionId xid;
    
    public RelatrixKVTransactionStatement() {
    }
    
	public RelatrixKVTransactionStatement(TransactionId xid, UUID session) {
		super(session);
		this.xid = xid;
	}
	
	public RelatrixKVTransactionStatement(UUID session, String tmeth, Object ... o1) {
		super(session, tmeth, o1);
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
	
	public void setTransactionId(TransactionId transactionId) {
		this.xid = transactionId;
	}
	
    @Override
    public synchronized String toString() { 
    	String s = super.toString();
    	return "Xid:"+xid+" "+s;
    }
	/**
	 * Call methods of the main RelatrixKVTransaction class, which will return an instance or an object that is not Serializable.<p>
	 * RealtrixKV invokes to original retrieval or storage method, possibly returning an iterator or stream.<p>
	 * In the case if non-Serializable return type of Iterator or Stream, we save it server side and link it to the session for later retrieval.<br>
	 * We create an intermediary that proxies the functionality back to the server and client, and is Serializable and contains 
	 * the necessary infrastructure to encapsulate the iterator or stream.<p>
	 * Note that here we are returning RockSack iterators and streams rather than Relatrix Factory iterators and streams.<br>
	 * We can use the native iterators and streams here
	 * because the functionality is available in whole, and we dont have to add the morphism processing aspect.
	 */
	@Override
	public synchronized void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		setCompletionObject();
		Object result = RelatrixKVTransactionServer.relatrixMethods.invokeMethod(this);
		if( result != null && !(result instanceof Serializable) && !(result instanceof Externalizable)) {
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			RemoteIteratorClientTransaction ric = null;
			switch(result) {
			case Stream _-> {
				result = new IteratorWrapper(((SackStream)result).iterator());
				if( DEBUG )
					System.out.printf("%s wrapping nonserializable object Stream reference using Transport:%s result:%s%n",this.getClass().getName(),this,result);
				ric = new RemoteIteratorClientTransaction(xid, session, ((InetSocketAddress)RelatrixKVTransactionServer.address).getAddress().getHostName(), 
								RelatrixKVTransactionServer.iteratorPorts[0], RelatrixKVTransactionServer.port);
			}
			case Iterator _-> {
				result = new IteratorWrapper((Iterator<?>) result);
				if( DEBUG )
					System.out.printf("%s wrapping nonserializable object Iterator reference using Transport:%s result:%s%n",this.getClass().getName(),this,result);
				ric = new RemoteIteratorClientTransaction(xid, session, ((InetSocketAddress)RelatrixKVTransactionServer.address).getAddress().getHostName(), 
						RelatrixKVTransactionServer.iteratorPorts[0], RelatrixKVTransactionServer.port);
			}
			case com.neocoretechs.rocksack.KeyValue _ -> {
				if( DEBUG ) {
					System.out.printf("%s setting kev/value object return for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
				}
				setServerObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
				signalCompletion(getObjectReturn());
				return;
			}
			default -> throw new IllegalArgumentException("Processing chain not set up to handle intermediary for non serializable object " + result);
			}
			// Link the object instance to session for later method invocation
			ric.setIteratorId(UUID.randomUUID());
			RelatrixKVTransactionServer.IteratorServerProcesses.setIterator(ric.getSession(), ric.getIteratorId(), (Iterator<?>) result);
			result = ric;
		} else {
			// put it in the array and send our intermediary back
			if(result != null) {
				switch(result) {
				case AbstractRelation _ -> {
					Relation.resolve((Relation) result);
				}
				case Result _ -> {
					if(((Result)result).get() instanceof AbstractRelation) {
						Relation rel = (Relation) ((Result)result).get();
						Relation.resolve(rel);
						((Result)result).set(rel);
					}
				}
				default -> {
					break;
				}
				}
			}
		}
		setServerObjectReturn(result);
		signalCompletion(result);
	}	

}
