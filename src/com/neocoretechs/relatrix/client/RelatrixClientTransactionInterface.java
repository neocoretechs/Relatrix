package com.neocoretechs.relatrix.client;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.neocoretechs.relatrix.key.DBKey;

public interface RelatrixClientTransactionInterface extends RelatrixKVClientTransactionInterface {
	/**
	 * Get the last good DBKey from the DBKey table, which is the highest numbered last key delivered.
	 * @return The last good key
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	UUID getNewKey() throws ClassNotFoundException, IllegalAccessException, IOException;
	
	Object getByIndex(String alias, String transactionId, DBKey index) throws IllegalAccessException, IOException, NoSuchElementException;
	
	UUID getByAlias(String alias)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException, NoSuchElementException;

	UUID getByPath(String tableSpace, boolean b)
			throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException;
}
