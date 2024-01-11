package com.neocoretechs.relatrix.key;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.rocksack.session.DatabaseManager;

/**
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2023
 *
 */
public class IndexManager {
	private static boolean DEBUG = false;
	private static final String databaseCatalogProperty = "Relatrix.Catalog";
	private static String databaseCatalog = "/etc/db/";
	private static ConcurrentHashMap<String, UUID> pathToIndex = new ConcurrentHashMap<String,UUID>();
	private static ConcurrentHashMap<UUID, String> indexToPath = new ConcurrentHashMap<UUID,String>();
	
	static {
		databaseCatalog = System.getProperty(databaseCatalogProperty);
	}
	
	public IndexManager() throws IOException, IllegalAccessException, NoSuchElementException {
		RelatrixKV.setAlias(databaseCatalogProperty, databaseCatalog);
		read();
	}
	
	void read() throws IllegalAccessException, NoSuchElementException, IOException {
		Iterator<?> it = RelatrixKV.entrySet(databaseCatalogProperty, UUID.class);
		while(it.hasNext()) {
			Entry e = (Entry) it.next();
			indexToPath.put((UUID)e.getKey(), (String)e.getValue());
			pathToIndex.put((String)e.getValue(), (UUID)e.getKey());
		}
		RelatrixKV.close(databaseCatalogProperty, UUID.class);
	}
	
	void write() throws IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		Iterator<Map.Entry<UUID, String>> it = indexToPath.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<UUID, String> entry = it.next();
			RelatrixKV.store(databaseCatalogProperty, entry.getKey(), entry.getValue());
		}
		RelatrixKV.close(databaseCatalogProperty, UUID.class);
	}
	/**
	 * Get the UUID for the given tablespace path. If the index does not exist, it will be created based on param
	 * @param path
	 * @param create
	 * @return the UUID of path
	 */
	static UUID get(String path, boolean create) {
		if(DEBUG)
			System.out.println("IndexManager.get attempt for path:"+path);
		UUID v = pathToIndex.get(path);
		if(v == null && create) {
			if(DEBUG)
				System.out.println("IndexManager.get creating new index for path:"+path);
			v = UUID.randomUUID();
			pathToIndex.put(path, v);
			indexToPath.put(v, path);
			try {
				RelatrixKV.store(databaseCatalogProperty, v, path);
				RelatrixKV.close(databaseCatalogProperty, UUID.class);
			} catch (IllegalAccessException | NoSuchElementException | IOException | DuplicateKeyException e) {
				e.printStackTrace();
			}
		}
		return v;
	}
	/**
	 * Get the path for the given index.IF the path does not exist, it will NOT be created.
	 * @param index
	 * @return path from indexToPath
	 */
	static String get(UUID index) {
		if(DEBUG)
			System.out.println("IndexManager.get attempt for UUID:"+index);
		String v = indexToPath.get(index);
		if(v == null) {
			if(DEBUG)
				System.out.println("IndexManager.get did not find index:"+index);
		}
		return v;
	}
	/**
	 * Get the tablespace path for the given alias
	 * @param alias
	 * @return The path for this alias or null if none
	 */
	static String getAliasToPath(String alias) {
		if(DEBUG)
			System.out.println("IndexManager.getAliasToPath attempt for alias:"+alias+" will return:"+DatabaseManager.getAliasToPath(alias));
		return DatabaseManager.getAliasToPath(alias);
	}
	/**
	 * Get the index for the given alias. If the index does not exist, it will be created
	 * @param alias
	 * @return The UUID index for the alias
	 * @throws NoSuchElementException If the alias was not found
	 */
	static UUID getByAlias(String alias) throws NoSuchElementException {
		String path = getAliasToPath(alias);
		if(path == null)
			throw new NoSuchElementException("The alias "+alias+" was not found.");
		if(DEBUG)
			System.out.println("IndexManager.getByAlias attempt for alias:"+alias+" got path:"+path);
		return get(path, true);
	}
	
	/**
	 * Remove the given tablespace path for index.
	 * @param index
	 * @return previous String path of removed UUID index
	 */
	static String remove(UUID index) {
		if(DEBUG)
			System.out.println("VolumeManager.remove for index:"+index);
		String ret = indexToPath.remove(index);
		if(ret != null)
			pathToIndex.remove(ret);
		return ret;
	}
	/**
	 * Remove the index for the given tablespace path.
	 * @param path
	 * @return UUID index of removed path
	 */
	static UUID remove(String path) {
		UUID ret = pathToIndex.remove(path);
		if(DEBUG)
			System.out.println("IndexManager.remove for path:"+path+" will return previous index:"+ret);		
		if(ret != null)
			indexToPath.remove(ret);
		return ret;
	}
	
}
