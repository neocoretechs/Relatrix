package com.neocoretechs.relatrix.client.json;

import java.io.Externalizable;
import java.io.Serializable;

import org.json.JSONObject;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatement;
import com.neocoretechs.relatrix.client.RelatrixTransactionStatementInterface;
import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction;

import com.neocoretechs.relatrix.server.json.RelatrixJsonTransactionServer;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

/**
 * The following class extends {@link RelatrixTransactionStatement} and allows the transport of transaction method calls to the server {@link RelatrixJsonTransactionServer} and
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, and in the case of an Iterator,
 *  to install a persistent Iterator to receive calls to deliver iterated objects.
 * @author Jonathan Groff (C) NeoCoreTechs 2025
 *
 */
public class RelatrixJsonTransactionStatement extends RelatrixTransactionStatement implements RelatrixTransactionStatementInterface, Serializable {
	private static final long serialVersionUID = -6487669704181333578L;
	private static boolean DEBUG = false;
    
    public RelatrixJsonTransactionStatement() {
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
    public RelatrixJsonTransactionStatement(String tmeth, Object ... o1) {
    	super(tmeth, o1);
    }
    
	public RelatrixJsonTransactionStatement(TransactionId transactionId, String session) {
		super(transactionId, session);
	}
    
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval.
	 */
	@Override
	public synchronized void process() throws Exception {
		if(DEBUG)
			System.out.println(this);
		unpackParamArray();
		Object result = RelatrixJsonTransactionServer.relatrixMethods.invokeMethod(this);
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
			RemoteIteratorJsonClientTransaction ric = null;
			if( result.getClass() == RelatrixIteratorTransaction.class) {
				ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
						RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction"));
			} else {
				if(result.getClass() == RelatrixSubsetIteratorTransaction.class ) {
					ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
							RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction"));
				} else {
					if(result.getClass() == RelatrixHeadsetIteratorTransaction.class ) {
						ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
								RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction"));
					} else {
						if( result.getClass() == RelatrixEntrysetIteratorTransaction.class) {
							ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
									RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction"));
						} else {
							if( result.getClass() == RelatrixKeysetIteratorTransaction.class) {
								ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
										RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction"));
							} else {
								if(result.getClass() == RelatrixTailsetIteratorTransaction.class ) {
									ric = new RemoteIteratorJsonClientTransaction(getTransactionId(), RelatrixJsonTransactionServer.address.getHostName(), 
											RelatrixJsonTransactionServer.findIteratorServerPort("com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction"));
								} else {
									throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
								}
							}
						}
					}
				}
			}
			RelatrixJsonTransactionServer.sessionToObject.put(ric.getSession(), result);
			JSONObject jric = new JSONObject(ric);
			setReturnClass(RemoteIteratorJsonClientTransaction.class.getName());
			setObjectReturn(jric);
			signalCompletion(jric);
		} else {
			result = new JSONObject(result);
			setObjectReturn(result);
			signalCompletion(result);
		}
	}

}
