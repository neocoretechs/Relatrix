package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;
/**
 * Encapsulates a database catalog entry for the Relatrix system. The catalog is indexed by this class
 * instance, which contains a {@link RelatrixIndex}, which ultimately encapsulates 2 longs originally
 * generated from a random Java UUID.
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
