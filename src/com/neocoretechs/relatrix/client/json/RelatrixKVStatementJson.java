package com.neocoretechs.relatrix.client.json;

import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.json.cbor.CborBuilder;
import org.json.cbor.CborException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.KeyValue;
import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.session.BufferedMap;
import com.neocoretechs.rocksack.stream.SackStream;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.TransportMorphismInterface;

import com.neocoretechs.relatrix.client.RelatrixStatement;
import com.neocoretechs.relatrix.client.RelatrixStatementInterface;
import com.neocoretechs.relatrix.client.RemoteIteratorClient;
import com.neocoretechs.relatrix.client.json.util.JsonRecordClassGenerator;
import com.neocoretechs.relatrix.client.json.util.RelatrixTypeSynthesizer;
import com.neocoretechs.relatrix.iterator.IteratorWrapper;
import com.neocoretechs.relatrix.server.BytecodeNotFoundInRepositoryException;
import com.neocoretechs.relatrix.server.HandlerClassLoader;
import com.neocoretechs.relatrix.server.json.RelatrixKVServerJson;


/**
 * The following class allows the transport of Relatrix method calls to the server, and on the server
 * contains the main process method to invoke the reflected methods marked with the {@link com.neocoretechs.relatrix.server.ServerMethod} annotation.
 * The process method calls setObjectReturn with the result of the invoked method, 
 * At the creation of each new statement, a session UUID is generated, this id is used to track the statement
 * and link to instance of created objects for remote method invocation.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RelatrixKVStatementJson extends RelatrixStatement implements RelatrixStatementInterface, Serializable {
	private static boolean DEBUG = true;
    static final long serialVersionUID = 8649844374668828845L;

    public RelatrixKVStatementJson() {
    }
    
    public RelatrixKVStatementJson(String session) {
    	super(session);
    }
    /**
     * Prep the statement for a remote call. Set our types to the actual class types for now..
     * @param tmeth
     * @param o1
     */
    public RelatrixKVStatementJson(String tmeth, Object ... o1) {
    	super(tmeth,o1);
    }
     
    @Override
    public synchronized void setParamArray(Object[] o1) {
    	this.paramArray = o1;
    	this.paramTypes = new String[o1.length];
    	this.params = new Class<?>[o1.length];
    	for(int i = 0; i < o1.length; i++) {
    			paramTypes[i] = o1[i].getClass().getName();//ws.item.getClass().getName();
    			params[i] = o1[i].getClass();//ws.item.getClass();
    			if(DEBUG)
    				System.out.printf("%s.setParamArray setting param %d item:%s type:%s class:%s%n", this.getClass().getName(), i, paramArray[i], paramTypes[i], params[i]);
    	}
    }
	
	@Override
	public synchronized void setObjectReturn(Object o) {
		if(o == null) {
			objectReturn = null;
			return;
		}
		if(o instanceof AbstractRelation) {
			objectReturn = TransportMorphism.createTransport((Relation) o);
		} else {
			if(o instanceof TransportMorphismInterface)
				((TransportMorphismInterface)o).packForTransport();
			objectReturn = o;
		}
		setParamArray(new Object[] {objectReturn});
		if(DEBUG)
			System.out.printf("%s.setObjectReturn %s%n", this.getClass().getName(), objectReturn);
	}
			
	public void setJsonParams() {
	   	for(int i = 0; i < params.length; i++) {
    			paramArray[i] = new JSONObject(params[i]);
    			paramTypes[i] = JSONObject.class.getName();
    			params[i] = JSONObject.class;
				if(DEBUG)
					System.out.printf("%s.setJsonParams setting param %d item:%s type:%s class:%s%n", this.getClass().getName(), i, paramArray[i], paramTypes[i], params[i]);
	   	}
	}
	
	@Override
	public synchronized Object getObjectReturn() {
		if(objectReturn != null) {
			if(objectReturn instanceof TransportMorphismInterface)
				((TransportMorphismInterface)objectReturn).unpackFromTransport();
			else
				if(objectReturn.getClass() == TransportMorphism.class)
					objectReturn = TransportMorphism.createMorphism((TransportMorphism)objectReturn);
			if(DEBUG)
				System.out.printf("%s.getObjectReturn returning class %s: %s%n", this.getClass().getName(), objectReturn.getClass().getName(), objectReturn);
		}
		if(DEBUG)
			if(objectReturn == null)
				System.out.printf("%s.getObjectReturn returning null%n", this.getClass().getName());
		return objectReturn;
	}
	
	/**
	 * Call methods of the main Relatrix class, which will return an instance or an object that is not Serializable
	 * in which case we save it server side and link it to the session for later retrieval
	 */
	@Override
	public synchronized void process() throws Exception {
		unpackParamArray();
		setJsonParams();
		Object result = RelatrixKVServerJson.relatrixMethods.invokeMethod(this);
		// See if we are dealing with an object that must be remotely maintained, e.g. iterator
		// which does not serialize so we front it
		//if( !result.getClass().isAssignableFrom(Serializable.class) ) {
		if(result != null && !(result instanceof Serializable) && !(result instanceof Externalizable) ) {
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
				System.out.printf("%s Storing nonserializable object reference for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
			}
			// put it in the array and send our intermediary back
			if( result.getClass() == com.neocoretechs.rocksack.KeyValue.class) {
				if( DEBUG ) {
					System.out.printf("%s setting kev/value object return for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
				}
				setObjectReturn(new Entry(((KeyValue)result).getmKey(),((KeyValue)result).getmValue()));
				signalCompletion(getObjectReturn());
				return;
			}
			RelatrixKVServerJson.sessionToObject.put(getSession(), result);
			RemoteIteratorClient ric = null;
			if(result.getClass() == IteratorWrapper.class) {
				if( DEBUG ) {
					System.out.printf("%s setting RemoteIteratorClient for session:%s, this Statement:%s result:%s%n",this.getClass().getName(),getSession(),this,result);
				}
				for(int ic = 0; ic < RelatrixKVServerJson.iteratorServerClasses.length; ic++) {
					if(result.getClass() == RelatrixKVServerJson.iteratorServerClasses[ic]) {	
						ric = new RemoteIteratorClient(((InetSocketAddress)RelatrixKVServerJson.address).getAddress().getHostName(), RelatrixKVServerJson.iteratorPorts[ic]);
					}
				}
			} else
				throw new Exception("Processing chain not set up to handle intermediary for non serializable object "+result);
			// Link the object instance to session for later method invocation
			RelatrixKVServerJson.sessionToObject.put(ric.getSession(), result);
			setObjectReturn(ric);
			signalCompletion(ric);
		} else {
			setObjectReturn(result);
			signalCompletion(result);
		}
	}

}
