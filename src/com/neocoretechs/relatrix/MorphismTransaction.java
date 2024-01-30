package com.neocoretechs.relatrix;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;

/**
* Morphism - domain, map, range structure
* ref's for relation datatype
*
* The permutations for our tuple are as follows
* keyop:       0       d,m,r                         <dd>
*              1       d,r,m                         <dd>
*              2       m,d,r                         <dd>
*              3       m,r,d                         <dd>
*              4       r,d,m                         <dd>
*              5       r,m,d                         <dd>
* we use this key for bin tree retrieval depending   <dd>
* on the desired traversal scenario, that is,        <dd>
* in what order do we want the values returned...    <dd>
* The is the base class for the different morphism permutations that allow us to form different
* sets from categories. The template class can be used to retrieve sets based on their class type.
* @author Jonathan Groff (C) NeoCoreTechs 1997,2014,2015
*/
public abstract class MorphismTransaction extends Morphism implements Comparable, Externalizable, Cloneable {
	private static boolean DEBUG = false;
	static final long serialVersionUID = -9129948317265641092L;

	protected String transactionId;

	public MorphismTransaction() {}

	/**
	 * Construct and establish key position for the elements of a morphism.
	 * @param d
	 * @param m
	 * @param r
	 */
	public MorphismTransaction(String transactionId, Comparable d, Comparable m, Comparable r) {
		this.transactionId = transactionId;
		setDomain(d);
		setMap(m);
		setRange(r);
	}

	public MorphismTransaction(String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
		this.transactionId = transactionId;
		this.alias = alias;
		setDomain(alias, d);
		setMap(alias, m);
		setRange(alias, r);
	}
	/**
	 * Construct and establish key position for the elements of a morphism. Do not utilize DBKeys
	 * and provide a default empty KeySet. A template is used for retrieval and checking for
	 * existence of relationships without creating a permanent entry in the database.
	 * @param d
	 * @param m
	 * @param r
	 */
	public MorphismTransaction(Comparable d, Comparable m, Comparable r, boolean template) {
		setDomainTemplate(d);
		setMapTemplate(m);
		setRangeTemplate(r);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String xid) {
		this.transactionId = xid;
	}
	/**
	 * Transparently process DBKey, returning actual instance
	 * @return The real Comparable instance, pointed to by DBKey
	 */ 
	public Comparable getDomain() {
		try {
			if(domain != null)
				return domain;
			if(getDomainKey().isValid()) {
				domain = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getDomainKey());
			}
			return domain;
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setDomain(Comparable<?> domain) {
		try {
			this.domain = domain;
			if(domain == null) {
				setDomainKey(new DBKey());
			} else {
				if(getDomainKey().isValid()) {
					this.domain = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getDomainKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(transactionId, domain)) == null)
						setDomainKey(DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(),domain));
					else
						setDomainKey(dbKey);
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * If domain is null, create a new {@link DBKey} in {@link KeySet}. If domain not null, get the domain
	 * key from KeySet and check if its valid. If it is valid, the domain will be set to the {@link IndexResolver}
	 * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the domain key of the KeySet.
	 * If the domain key is not valid, a getByInstance of the domain on the database indicated by the alias tablespace is
	 * performed to try and obtain a domain DBKey. If this method call comes back null, then a new key is formed
	 * using the domain instance value stored to the database alias index table using the IndexResolver.
	 * If the method call to getByInstance for the domain instance comes back not null, then we simply set the domain key
	 * in the KeySet to the value retrieved from the IndexResolver.<p/>
	 * Recall that our tables are stored using an instance key and DBKey value for each database/class, and a DBKey key and instance value 
	 * master table for each database. The master catalog is stored using a UUID class key, and values being the database path.
	 * In the DBKey, the UUID of the database in the master catalog and the UUID of the instance form the index. The DBKey
	 * points to the primary database and the alias here is used if we create an entirely new instance.
	 * @param alias the database alias if we end up creating an index to a new instance
	 * @param domain
	 */
	public void setDomain(String alias, Comparable<?> domain) {
		try {
			this.domain = domain;
			if(domain == null) {
				setDomainKey(new DBKey());
			} else {
				if(getDomainKey().isValid()) {
					this.domain = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getDomainKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstanceAlias(alias, transactionId, domain)) == null)
						setDomainKey(DBKey.newKeyAlias(alias, transactionId, IndexResolver.getIndexInstanceTable(),domain));
					else
						setDomainKey(dbKey);
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
			throw new RuntimeException(e);
		}
	}

	public Comparable getMap() {
		try {
			if(map != null) 
				return map;
			if(getMapKey().isValid()) {
				map = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getMapKey());
			}
			return map;
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setMap(Comparable<?> map) {
		try {
			this.map = map;
			if(map == null) {
				setMapKey(new DBKey());
			} else {
				if(getMapKey().isValid()) {
					this.map = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getMapKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(transactionId, map)) == null)
						setMapKey(DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(),map));
					else
						setMapKey(dbKey);
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	public void setMap(String alias, Comparable<?> map) {
		try {
			this.map = map;
			if(map == null) {
				setMapKey(new DBKey());
			} else {
				if(getMapKey().isValid()) {
					this.map = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getMapKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstanceAlias(alias, transactionId, map)) == null)
						setMapKey(DBKey.newKeyAlias(alias, transactionId, IndexResolver.getIndexInstanceTable(), map));
					else
						setMapKey(dbKey);
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
			throw new RuntimeException(e);
		}
	}

	public Comparable getRange() {
		try {
			if(range != null)
				return range;
			if(getRangeKey().isValid()) {
				range = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getRangeKey());
			}
			return range;
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setRange(Comparable<?> range) {
		try {
			this.range = range;
			if(range == null) {
				setRangeKey(new DBKey());
			} else {
				if(getRangeKey().isValid()) {
					this.range = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getRangeKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(transactionId, range)) == null)
						setRangeKey(DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(),range));
					else
						setRangeKey(dbKey);						
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setRange(String alias, Comparable<?> range) {
		try {
			this.range = range;
			if(range == null) {
				setRangeKey(new DBKey());
			} else {
				if(getRangeKey().isValid()) {
					this.range = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, getRangeKey());
				} else {
					DBKey dbKey = null;
					if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstanceAlias(alias, transactionId, range)) == null)
						setRangeKey(DBKey.newKeyAlias(alias, transactionId, IndexResolver.getIndexInstanceTable(), range));
					else
						setRangeKey(dbKey);						
				}
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * key combinations for Relatrix follow
	 */
	public abstract int compareTo(Object dmrpk);
	public abstract boolean equals(Object dmrpk);
	public abstract Object clone() throws CloneNotSupportedException;


	public DBKey store() throws IllegalAccessException, ClassNotFoundException, DuplicateKeyException, IOException {
		if(alias == null)
			return super.store(transactionId);
		else
			return storeAlias(alias,transactionId);
	}
}
