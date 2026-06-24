package com.neocoretechs.relatrix.client.json;

import java.io.Externalizable;
import java.io.Serializable;
import java.net.InetSocketAddress;

import com.neocoretechs.relatrix.client.RemoteIteratorClient;

import com.neocoretechs.relatrix.server.json.RelatrixServerJson;
import com.neocoretechs.relatrix.stream.BaseIteratorAccessInterface;

/**
 * The following class allows the transport of Relatrix method calls to the server, and on the server
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, 
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RelatrixStatementJson extends RelatrixKVStatementJson implements Serializable {
	private static boolean DEBUG = false;
    static final long serialVersionUID = 8649844374668828845L;
  
    public RelatrixStatementJson() {
    }
    
    public RelatrixStatementJson(String session) {
    	super(session);
    }
    /**
     * Prep the statement for a remote call. Set our types to the actual class types for now..
     * @param tmeth
     * @param o1
     */
    public RelatrixStatementJson(String tmeth, Object ... o1) {
    	super(tmeth, o1);
    }
   
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval
	 */
	@Override
	public synchronized void process() throws Exception {
		unpackParamArray();
		Object result = RelatrixServerJson.relatrixMethods.invokeMethod(this);
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
			RemoteIteratorClient ric = null;
			for(int ic = 0; ic < RelatrixServerJson.iteratorServerClasses.length; ic++) {
				if(result.getClass() == RelatrixServerJson.iteratorServerClasses[ic]) {	
					ric = new RemoteIteratorClient(((InetSocketAddress)RelatrixServerJson.address).getAddress().getHostName(), RelatrixServerJson.iteratorPorts[ic]);
				}
			}
			if(ric == null)
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			// Link the object instance to session for later method invocation
			RelatrixServerJson.sessionToObject.put(ric.getSession(), result);
			setReturnClass(RemoteIteratorClient.class.getName());
			setObjectReturn(ric);
			signalCompletion(ric);
		} else {
			setObjectReturn(result);
			signalCompletion(result);
		}
	}

}
