package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction;

import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

/**
 * The following class extends {@link RelatrixStatement} and allows the transport of transaction method calls to the server {@link RelatrixTransactionServer} and
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, and in the case of an Iterator,
 *  to install a persistent Iterator to receive calls to deliver iterated objects.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class RelatrixTransactionStatement extends RelatrixStatement implements RelatrixTransactionStatementInterface, Serializable {
	private static final long serialVersionUID = -503217108835099285L;
	private static boolean DEBUG = false;
	private TransactionId transactionId;
    
    public RelatrixTransactionStatement() {
    	super();
    }
    
    /*
    public RelatrixTransactionStatement(RelatrixTransactionStatement rts) {
    	super(rts.getMethodName(), rts.getParamArray());
    	this.transactionId = rts.transactionId;
    	this.session = rts.session;
    	this.alias = rts.alias;
    	this.returnClass = rts.returnClass;
    	this.setObjectReturn(rts.getObjectReturn());
    }
    */
    public RelatrixTransactionStatement(String tmeth, Object ... o1) {
    	super(tmeth, o1);
    	if(o1.length > 1) {
			if(o1[0].getClass().equals(TransactionId.class)) {
				this.transactionId = (TransactionId) o1[0];
			} else {
				if(o1[1].getClass().equals(TransactionId.class))
					this.transactionId = (TransactionId) o1[1];
			}
		} else {
			if(o1.length > 0) {
				if(o1[0].getClass().equals(TransactionId.class))
					this.transactionId = (TransactionId) o1[0];
			}
		}
    }
    
	public RelatrixTransactionStatement(TransactionId transactionId, String session) {
		super(session);
		this.transactionId = transactionId;
	}
    
    @Override
    public synchronized String toString() { return String.format("%s for Session:%s xid:%s Method:%s params:%s return Class:%s%n",
             this.getClass().getName(), getSession(), transactionId, methodName,
             (paramArray == null ? "nil" : Arrays.toString(paramArray)), returnClass); }
    
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval.
	 */
	@Override
	public synchronized void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		unpackParamArray();
		Object result = RelatrixTransactionServer.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
			// Stream..? If so, we basically forego the local stream and
			// preserve the underlying iterator, sending back the corresponding remote iterator.
			// The client, being engaged in a steam operation, will create the local RemoteStream with returned
			// remote iterator
			if( result instanceof BaseIteratorAccessInterface) {
				result = ((BaseIteratorAccessInterface)result).getBaseIterator();
			}
			if( DEBUG ) {
				System.out.printf("%s Storing nonserializable object reference for session:%s, Method:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			RemoteIteratorClientTransaction ric = null;
			if( result.getClass() == RelatrixIteratorTransaction.class) {
				ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
						RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction"));
			} else {
				if(result.getClass() == RelatrixSubsetIteratorTransaction.class ) {
					ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
							RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction"));
				} else {
					if(result.getClass() == RelatrixHeadsetIteratorTransaction.class ) {
						ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
								RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction"));
					} else {
						if( result.getClass() == RelatrixEntrysetIteratorTransaction.class) {
							ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
									RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction"));
						} else {
							if( result.getClass() == RelatrixKeysetIteratorTransaction.class) {
								ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
										RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction"));
							} else {
								if(result.getClass() == RelatrixTailsetIteratorTransaction.class ) {
									ric = new RemoteIteratorClientTransaction(transactionId, RelatrixTransactionServer.address.getHostName(), 
											RelatrixTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction"));
								} else {
									throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
								}
							}
						}
					}
				}
			}
			RelatrixTransactionServer.sessionToObject.put(ric.getSession(), result);
			setObjectReturn(ric);
			signalCompletion(ric);
		} else {
			setObjectReturn(result);
			signalCompletion(result);
		}
	}

	@Override
	public TransactionId getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(TransactionId transactionId) {
		this.transactionId = transactionId;
	}

}
