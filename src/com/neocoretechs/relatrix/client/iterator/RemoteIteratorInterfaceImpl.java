package com.neocoretechs.relatrix.client.iterator;

import java.util.UUID;
/**
 * Conform to the client/server pattern of the Relatrix clients and the Iterator contract. There are some slight implementation differences
 * in that we need a process method that can be called on return from the Relatrix servers 
 * to connect the iterator client back to the iterator servers.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public abstract class RemoteIteratorInterfaceImpl implements RemoteIteratorInterface {
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG = true;
	public abstract Object sendCommand(String command) throws Exception;
	/**
	 * iterator interface contract
	 * @return The next iterated object or null
	 */
	@Override
	public Object next() {
		try {
			if(DEBUG)
				System.out.printf("%s next %s%n",this.getClass().getName(), this.toString());
			return sendCommand("next");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * iterator interface contract
	 * @return The boolean result of hasNext on server
	 */
	@Override
	public boolean hasNext() {
		try {
			if(DEBUG)
				System.out.printf("%s hasNext %s%n",this.getClass().getName(), this.toString());
			return (boolean) sendCommand("hasNext");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public abstract void close();

	@Override
	public abstract void shutdown();

	@Override
	public abstract String getRemoteNode();
	
	@Override
	public abstract int getRemotePort();

	@Override
	public abstract UUID getSession();

}
