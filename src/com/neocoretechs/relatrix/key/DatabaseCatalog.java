package com.neocoretechs.relatrix.key;

import java.io.Externalizable;

import java.util.UUID;
/**
 * Encapsulates a database catalog entry for the Relatrix system. The catalog is indexed by this class
 * instance, which is derived from {@link RelatrixIndex}, which ultimately encapsulates 2 longs originally
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
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public final class DatabaseCatalog extends RelatrixIndex implements Comparable, Externalizable {
	
	public DatabaseCatalog() {}
	
	public DatabaseCatalog(UUID randomUUID) {
		super(randomUUID.getMostSignificantBits(), randomUUID.getLeastSignificantBits());
	}
	
	public DatabaseCatalog(RelatrixIndex relatrixIndex) {
		super(relatrixIndex);
	}

	@Override
	public String toString() {
		return String.format("Database Catalog Id: %s",super.toString());
	}

}
