package com.neocoretechs.relatrix.client;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Arrays;

import com.neocoretechs.relatrix.iterator.RelatrixEntrysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixHeadsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixKeysetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixSubsetIteratorTransaction;
import com.neocoretechs.relatrix.iterator.RelatrixTailsetIteratorTransaction;
import com.neocoretechs.relatrix.server.RelatrixTransactionServer;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteEntrySetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteHeadSetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteKeySetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteSetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteSubSetIteratorTransaction;
import com.neocoretechs.relatrix.server.remoteiterator.ServerSideRemoteTailSetIteratorTransaction;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

/**
 * The following class extends {@link RelatrixStatement} and allows the transport of transaction method calls to the server {@link RelatrixTransactionServer} and
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, and in the case of an Iterator, sets
 * up the proper instance of {@link RemoteIteratorTransaction} to install a persistent Iterator to receive calls to deliver iterated objects.
 * @author Jonathan Groff (C) NeoCoreTechs 2021,2022
 *
 */
public class RelatrixTransactionStatement extends RelatrixStatement implements Serializable {
	private static final long serialVersionUID = -503217108835099285L;
	private static boolean DEBUG = false;
    String alias = null;
    
    public RelatrixTransactionStatement() {
    	super();
    }
    
    public RelatrixTransactionStatement(String tmeth, Object ... o1) {
    	super(tmeth, o1);
    }
    
	public RelatrixTransactionStatement(String session) {
		super(session);
	}
    
    @Override
    public synchronized String toString() { return String.format("%s for Session:%s Method:%s Arg:%s%n",
             this.getClass().getName(), getSession(), methodName,
             (paramArray == null || paramArray.length == 0 ? "nil" : Arrays.toString(paramArray))); }
    
    @Override
 	public synchronized Class<?>[] getParams() {
    	if( paramArray == null )
    		paramArray = new Object[0];
    	if(DEBUG)
    		System.out.println("params:"+paramArray.length+" "+Arrays.toString(paramArray));
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
			RelatrixTransactionServer.sessionToObject.put(getSession(), result);
			if( result.getClass() == RelatrixIteratorTransaction.class) {
				setObjectReturn( new ServerSideRemoteSetIteratorTransaction(getSession()) );
			} else {
				if(result.getClass() == RelatrixSubsetIteratorTransaction.class ) {
					setObjectReturn( new ServerSideRemoteSubSetIteratorTransaction(getSession()) );
				} else {
					if(result.getClass() == RelatrixHeadsetIteratorTransaction.class ) {
						setObjectReturn( new ServerSideRemoteHeadSetIteratorTransaction(getSession()) );
					} else {
						if( result.getClass() == RelatrixEntrysetIteratorTransaction.class) {
							setObjectReturn( new ServerSideRemoteEntrySetIteratorTransaction(getSession()) );
						} else {
							if( result.getClass() == RelatrixKeysetIteratorTransaction.class) {
								setObjectReturn( new ServerSideRemoteKeySetIteratorTransaction(getSession()) );
							} else {
								if(result.getClass() == RelatrixTailsetIteratorTransaction.class ) {
									setObjectReturn( new ServerSideRemoteTailSetIteratorTransaction(getSession()) );
								} else {
									throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
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
