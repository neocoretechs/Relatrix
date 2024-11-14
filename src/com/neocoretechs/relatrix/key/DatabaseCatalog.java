package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;
/**
 * Encapsulates a database catalog entry for the Relatrix system. The catalog is indexed by this class
 * instance, which contains a {@link RelatrixIndex}, which ultimately encapsulates 2 longs originally
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
public class DatabaseCatalog implements Comparable, Externalizable {
	private RelatrixIndex relatrixIndex;
	
	public DatabaseCatalog() {}
	
	public DatabaseCatalog(UUID randomUUID) {
		relatrixIndex = new RelatrixIndex(randomUUID.getMostSignificantBits(), randomUUID.getLeastSignificantBits());
	}
	
	public DatabaseCatalog(RelatrixIndex relatrixIndex) {
		this.relatrixIndex = relatrixIndex;
	}
	/**
	 * @return the relatrixIndex
	 */
	public RelatrixIndex getRelatrixIndex() {
		return relatrixIndex;
	}

	/**
	 * @param relatrixIndex the relatrixIndex to set
	 */
	public void setRelatrixIndex(RelatrixIndex relatrixIndex) {
		this.relatrixIndex = relatrixIndex;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		relatrixIndex.writeExternal(out);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		relatrixIndex = new RelatrixIndex();
		relatrixIndex.readExternal(in);
	}

	@Override
	public int compareTo(Object o) {
		return relatrixIndex.compareTo(o);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relatrixIndex == null) ? 0 : relatrixIndex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatabaseCatalog other = (DatabaseCatalog) obj;
		if (relatrixIndex == null) {
			if (other.relatrixIndex != null)
				return false;
		} else if (!relatrixIndex.equals(other.relatrixIndex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Database Catalog Id: %s",relatrixIndex.toString());
	}

}
