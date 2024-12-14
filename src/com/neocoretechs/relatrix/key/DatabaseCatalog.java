package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.iterator.Entry;
/**
 * Encapsulates a database catalog entry for the Relatrix system. The catalog is used by
 * {@link RelatrixIndex} to resolve database references. The RelatrixIndex encapsulates 2 longs originally
 * generated from a random Java UUID.<p/>
 * The morphism components are indexed by a {@link com.neocoretechs.relatrix.key.DBKey} that contains a reference to the database and instance
 * within that database as 2 unique Id's (UUIDs). The first Id points to the entry in the database catalog. The catalog
 * sets up an alias called Relatrix.Catalog to a path set with the system property also called Relatrix.Catalog, or lacking that property, 
 * the path the alias refers to defaults to the static variable databaseCatalog.<p/>
 * When a Relatrix instance starts, the Database Catalog is read as an entrySet using RelatrixKV. The entrySet is
 * obtained from the databaseCatalogAlias pointing to the DatabaseCatalog class instances.
 * The indexToPath and pathToIndex hashMaps in Relatrix are populated with the actual paths to the individual
 * database references in the entrySet indexed by the DatabaseCatalog instances in the entrySet.
 * If during normal operations a getByPath or getByAlias occurs and the entry is not found, the hashMaps are updated as well.
 * The references map to a table of aliases which are the UUIDs, and the user defined alias is kept in a table that maps to the UUID
 * in the DatabaseCatalog.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public final class DatabaseCatalog  {
	private static boolean DEBUG = false;
	public static volatile DatabaseCatalog databaseCatalog = null;
	static final String databaseCatalogProperty = "Relatrix.Catalog";
	static final String defaultDatabaseProperty = "Default.database";
	static String databaseCatalogPath = "/etc/db/";
	public static final Alias databaseCatalogAlias = new Alias(databaseCatalogProperty);
	public static final Alias defaultDatabaseAlias = new Alias(defaultDatabaseProperty);
	private static ConcurrentHashMap<String, RelatrixIndex> pathToIndex = new ConcurrentHashMap<String,RelatrixIndex>();
	private static ConcurrentHashMap<RelatrixIndex, String> indexToPath = new ConcurrentHashMap<RelatrixIndex,String>();
	private static ConcurrentHashMap<Alias, RelatrixIndex> aliasToIndex = new ConcurrentHashMap<Alias,RelatrixIndex>();
	
	static {
		if(System.getProperty(databaseCatalogProperty) != null)
			databaseCatalogPath = System.getProperty(databaseCatalogProperty);
		try {
			if(RelatrixKV.getAlias(databaseCatalogAlias) == null) // account for Relatrix static initializer
				RelatrixKV.setAlias(databaseCatalogAlias, databaseCatalogPath);
			readDatabaseCatalog();
		} catch (IOException | IllegalAccessException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	private DatabaseCatalog() { }

	public static DatabaseCatalog getInstance() {
		if( databaseCatalog == null )
			synchronized(DatabaseCatalog.class) {
				if(databaseCatalog == null) {
					databaseCatalog = new DatabaseCatalog();
				}
			}
		return databaseCatalog;
	}	
	/**
	 * Set the default tablespace such that defaultDatabaseAlias points to it
	 * @param path
	 * @throws IOException 
	 */
	public static void setTablespace(String path) throws IOException {
		getByAlias(defaultDatabaseAlias, path);
	}
	
	public static void setAlias(Alias alias, String path) throws IOException {
		getByAlias(alias, path);
	}
	
	static void readDatabaseCatalog() throws IllegalAccessException, NoSuchElementException, IOException {
		if(DEBUG)
			System.out.println("Relatrix.readDatabaseCatalog");
		Iterator<?> it = RelatrixKV.entrySet(databaseCatalogAlias, RelatrixIndex.class);
		while(it.hasNext()) {
			Entry e = (Entry) it.next();
			indexToPath.put((RelatrixIndex)e.getKey(), (String)e.getValue());
			pathToIndex.put((String)e.getValue(), (RelatrixIndex)e.getKey());
			RelatrixKV.setAlias(new Alias(((RelatrixIndex)e.getKey()).getAsUUID().toString()), (String)e.getValue());
			if(DEBUG)
				System.out.println("Relatrix.readDatabaseCatalog indexToPath:"+e.getKey()+" pathToIndex:"+e.getValue());
		}
		if(DEBUG)
			System.out.println("Closing "+databaseCatalogProperty);
		//RelatrixKV.close(databaseCatalogProperty, UUID.class);
		if(DEBUG)
			System.out.println("Closed "+databaseCatalogProperty);
	}

	static void writeDatabaseCatalog() throws IllegalAccessException, NoSuchElementException, IOException, DuplicateKeyException {
		Iterator<java.util.Map.Entry<RelatrixIndex, String>> it = indexToPath.entrySet().iterator();
		while(it.hasNext()) {
			java.util.Map.Entry<RelatrixIndex, String> entry = it.next();
			RelatrixKV.store(databaseCatalogAlias, entry.getKey(), entry.getValue());
		}
	}
	/**
	 * Get the RelatrixIndex for the given tablespace path. If the index does not exist, it will be created based on param
	 * @param path
	 * @param create
	 * @return the RelatrixIndex of path
	 */
	public static RelatrixIndex getByPath(String path, boolean create) {
		if(DEBUG)
			System.out.println("Relatrix.getByPath attempt for path:"+path+" create:"+create);
		RelatrixIndex v = pathToIndex.get(path);
		if(v == null && create) {
			UUID uuid = UUID.randomUUID();
			v = new RelatrixIndex(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
			if(DEBUG)
				System.out.println("Relatrix.getByPath creating new index for path:"+path+" with index:"+v);
			pathToIndex.put(path, v);
			indexToPath.put(v, path);
			try {
				RelatrixKV.store(databaseCatalogAlias, v, path);
			} catch (IllegalAccessException | NoSuchElementException | IOException | DuplicateKeyException e) {
				e.printStackTrace();
			}
		}
		if(DEBUG)
			System.out.println("Relatrix.getByPath returning:"+v+" for path:"+path+" create:"+create);
		return v;
	}
	/**
	 * Get the index for the given user defined alias.<p/>
	 * The table of aliasToIndex specifies the user alias to RelatrixIndex, apart from the table 
	 * where we define the alias as UUID linked to RelatrixIndex, this is an additional layer
	 * to translate the user readable alias to the UUID system alias
	 * @param alias
	 * @param path length > 0 with supplied path to create if not found
	 * @return The {@link RelatrixIndex} index for the alias
	 * @throws IOException 
	 */
	public static RelatrixIndex getByAlias(Alias alias, String... path) throws IOException  {
		RelatrixIndex ri = aliasToIndex.get(alias);
		if(ri == null && path.length > 0) {
			UUID uuid = UUID.randomUUID();
			ri = new RelatrixIndex(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
			if(DEBUG)
				System.out.println("Relatrix.getByPath creating new index for path:"+path[0]+" with index:"+ri);
			pathToIndex.put(path[0], ri);
			indexToPath.put(ri, path[0]);
			aliasToIndex.put(alias, ri);
			RelatrixKV.setAlias(new Alias(ri.getAsUUID().toString()), path[0]);
			try {
				RelatrixKV.store(databaseCatalogAlias, ri, path[0]);
			} catch (IllegalAccessException | NoSuchElementException | IOException | DuplicateKeyException e) {
				e.printStackTrace();
			}
			if(DEBUG)
				System.out.println("DatabaseCatalog.getByAlias attempt for alias:"+alias+" got index:"+ri);
		}
		return ri;
	}

	/**
	 * Remove the given tablespace path for index.
	 * @param index
	 * @return previous String path of removed {@link DatabaseCatalog} index
	 */
	static String removeDatabaseCatalog(RelatrixIndex index) {
		if(DEBUG)
			System.out.println("Relatrix.removeDatabaseCatalog for index:"+index);
		String ret = indexToPath.remove(index);
		if(ret != null)
			pathToIndex.remove(ret);
		try {
			RelatrixKV.remove(databaseCatalogAlias,index);
			aliasToIndex.forEach((k,v)->{
				if(v.equals(index)) {
					aliasToIndex.remove(k);
				}
			});				
			//RelatrixKV.close(databaseCatalogProperty, UUID.class);
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * Remove the index for the given tablespace path.
	 * @param path
	 * @return {@link DatabaseCatalog} index of removed path
	 */
	static RelatrixIndex removeDatabaseCatalog(String path) {
		RelatrixIndex ret = pathToIndex.remove(path);
		if(DEBUG)
			System.out.println("Relatrix.removeDatabaseCatalog for path:"+path+" will return previous index:"+ret);		
		if(ret != null) {
			indexToPath.remove(ret);
			try {
				RelatrixKV.remove(databaseCatalogAlias,ret);
				aliasToIndex.forEach((k,v)->{
					if(v.equals(ret)) {
						aliasToIndex.remove(k);
					}
				});
			} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	@Override
	public String toString() {
		return String.format("Database Catalog Id: %s",super.toString());
	}

}

