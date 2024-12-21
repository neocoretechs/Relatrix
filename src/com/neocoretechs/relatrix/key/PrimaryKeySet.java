package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.DatabaseClass;
import com.neocoretechs.rocksack.TransactionId;
/**
 * Class to contain serialzable set of keys to maintain order of domain/map/range relationships in Relatrix.<p/>
 * Since we are dealing with morphisms, basically an algebraic function mapping for f:x->y, or m:d->r, then
 * the primary key is composed of the domain and map components of the morphism. Since a function which takes a domain
 * object and maps it to a given range through a mapping object can result in only 1 mapping of a domain to range
 * through a particular mapping function. Consider as an extremely simplified example the domain integer object 1 
 * using the mapping function addOne results in a range object of 2, and only 2, and naturally composes with the
 * morphism domain 2 map addOne with a range of 3, producing functors 1 addOne 2 addOne 3 etc.<p/>
 * As is stated by the annotation, this class functions as DomainMapRange in the database, and that class
 * is a subclass of this. 
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2022,2023,2024
 *
 */
@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class PrimaryKeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
	protected DBKey domainKey;
    protected DBKey mapKey;
    protected transient DBKey identity;
	protected transient TransactionId transactionId = null;
	protected transient Alias alias = null;

    public PrimaryKeySet() {}
    
	PrimaryKeySet(DBKey domainKey, DBKey mapKey) {
		this.domainKey = domainKey;
		this.mapKey = mapKey;
	}
	PrimaryKeySet(DBKey domainKey, DBKey mapKey, TransactionId transactionId) {
		this.domainKey = domainKey;
		this.mapKey = mapKey;
		this.transactionId = transactionId;
	}
	PrimaryKeySet(DBKey domainKey, DBKey mapKey, Alias alias) {
		this.domainKey = domainKey;
		this.mapKey = mapKey;
		this.alias = alias;
	}
	PrimaryKeySet(DBKey domainKey, DBKey mapKey, Alias alias, TransactionId transactionId) {
		this.domainKey = domainKey;
		this.mapKey = mapKey;
		this.transactionId = transactionId;
		this.alias = alias;
	}
	public DBKey getDomainKey() {
		return domainKey;
	}
	public void setDomainKey(DBKey domainKey) {
		this.domainKey = domainKey;
	}
	public DBKey getMapKey() {
		return mapKey;
	}
	public void setMapKey(DBKey mapKey) {
		this.mapKey = mapKey;
	}
	/**
	 * Get the identity key, that is, the key that is used as key in index table, and value in instance class table.
	 * @return the identity DBKey
	 */
	public DBKey getIdentity() {
		return identity;
	}
	/**
	 * Set the identity key, that is, the key that is used as key in index table, and value in instance class table.
	 * @param identity
	 */
	public void setIdentity(DBKey identity) {
		this.identity = identity;
	}
   	public Alias getAlias() {
		return alias;
	}
	public void setAlias(Alias alias) {
		this.alias = alias;
	}
	boolean isValid() {
		return DBKey.isValid(domainKey) && DBKey.isValid(mapKey);
	}
	public boolean isDomainKeyValid() {
		return DBKey.isValid(domainKey);
	}
	public boolean isMapKeyValid() {
		return DBKey.isValid(mapKey);
	}
	public boolean domainKeyEquals(PrimaryKeySet o) {
		return domainKey.equals(o.domainKey);
	}
	public boolean mapKeyEquals(PrimaryKeySet o) {
		return mapKey.equals(o.mapKey);
	}
	public TransactionId getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(TransactionId xid) {
		this.transactionId = xid;
	}
	/**
	 * Locate the primary key in prep for storage.  For the passed key instances 
	 * create the instance/DBKey and DBKey/instance tablespace entries for domain and map.
	 * If the decision is made to toss out the entry, may have spurious instances of domain and map. New key must be
	 * created for relationship itself during storage via setDBKey(DBKey.newKey) once range is populated, and
	 * setDomainResolved with domain object, setMapResolved with map object and setRange with range object.
	 * Other values will be set from locate for main instance. This method will create a new temporary PrimaryKeySet
	 * instance to locate using only the domain and map keys, then the key values will be used to populate 'this' instance.
	 * @param skeyd domain instance
	 * @param skeym instance for map
	 * @return true if key of stored KeySet, which represents domain, map identity unique, else false
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected boolean locate(Comparable skeyd, Comparable skeym) throws IllegalAccessException, ClassNotFoundException, IOException {
		IndexInstanceTableInterface indexTable = IndexResolver.getIndexInstanceTable();
		PrimaryKeySet pk = new PrimaryKeySet();
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		if(transactionId == null) {
			if(alias == null) {
				Object d = RelatrixKV.get(skeyd);
				if( d == null ) {
					pk.setDomainKey(DBKey.newKey(indexTable, skeyd)); // puts to index and instance
				} else {
					pk.setDomainKey((DBKey) d);
				}
				Object m = RelatrixKV.get(skeym);
				if( m == null ) {
					pk.setMapKey(DBKey.newKey(indexTable, skeym)); // puts to index and instance
				} else {
					pk.setMapKey((DBKey) m);
				}
				// set the keys, use set..Template to set values
				this.domainKey = pk.domainKey;
				this.mapKey = pk.mapKey;
				// do we have a relation already?
				Object dKey = RelatrixKV.get(pk);
				if(dKey != null) {
					this.identity = (DBKey) dKey;
					return false;
				}
				return true; // must now call DBKey.newKey(indexTable, this);
			}
			// alias not null, transaction id null
			Object d = RelatrixKV.get(alias,skeyd);
			if( d == null ) {
				pk.setDomainKey(DBKey.newKey(alias, indexTable, skeyd)); // puts to index and instance
			} else {
				pk.setDomainKey((DBKey) d);
			}
			Object m = RelatrixKV.get(alias,skeym);
			if( m == null ) {
				pk.setMapKey(DBKey.newKey(alias, indexTable, skeym)); // puts to index and instance
			} else {
				pk.setMapKey((DBKey) m);
			}
			this.domainKey = pk.domainKey;
			this.mapKey = pk.mapKey;
			pk.alias = alias;
			Object dKey = RelatrixKV.get(alias, pk);
			if(dKey != null) {
				this.identity = (DBKey) dKey;
				return false;
			}
			return true; // now call DBKey.newKey(alias, indexTable, this);
		} else {
			// Transaction Id not null
			if(alias == null) {
				Object d = RelatrixKVTransaction.get(transactionId, skeyd);
				if( d == null ) {
					pk.setDomainKey(DBKey.newKey(transactionId, indexTable, skeyd)); // puts to index and instance
				} else {
					pk.setDomainKey((DBKey) d);
				}
				Object m = RelatrixKVTransaction.get(transactionId,skeym);
				if( m == null ) {
					pk.setMapKey(DBKey.newKey(transactionId, indexTable, skeym)); // puts to index and instance
				} else {
					pk.setMapKey((DBKey) m);
				}
				this.domainKey = pk.domainKey;
				this.mapKey = pk.mapKey;
				pk.transactionId = transactionId;
				Object dKey = RelatrixKVTransaction.get(transactionId, pk);
				if(dKey != null) {
					this.identity = (DBKey) dKey;
					return false;
				}
				return true; // now DBKey.newKey(transactionId, indexTable, this);
			}
			// transaction id and alias not null
			Object d = RelatrixKVTransaction.get(alias, transactionId, skeyd);
			if( d == null ) {
				pk.setDomainKey(DBKey.newKey(alias, transactionId, indexTable, skeyd)); // puts to index and instance
			} else {
				pk.setDomainKey((DBKey)d);
			}
			Object m = RelatrixKVTransaction.get(alias, transactionId, skeym);
			if(m == null) {
				pk.setMapKey(DBKey.newKey(alias, transactionId, indexTable, skeym)); // puts to index and instance
			} else {
				pk.setMapKey((DBKey)m);
			}
			this.domainKey = pk.domainKey;
			this.mapKey = pk.mapKey;
			pk.alias = alias;
			pk.transactionId = transactionId;
			Object dKey = RelatrixKVTransaction.get(alias, transactionId, pk);
			if(dKey != null) {
				this.identity = (DBKey) dKey;
				return false;
			}
			return true; // now do DBKey.newKey(alias, transactionId, indexTable, this);
		}
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		RelatrixIndex d1 = new RelatrixIndex(in.readLong(), in.readLong());
		RelatrixIndex m1 = new RelatrixIndex(in.readLong(), in.readLong());
		domainKey = new DBKey(d1);
		mapKey = new DBKey(m1);	
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(domainKey.getInstanceIndex().getMsb());
		out.writeLong(domainKey.getInstanceIndex().getLsb());
		out.writeLong(mapKey.getInstanceIndex().getMsb());
		out.writeLong(mapKey.getInstanceIndex().getLsb());
	}
	
	@Override
	public int compareTo(Object o) {
		//if(DEBUG)
			//System.out.println("Keyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((KeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((KeySet)o).getMapKey());
		int i = getDomainKey().compareTo(((PrimaryKeySet)o).getDomainKey());
		if(i != 0) {
			//if(DEBUG)
				//System.out.println("Keyset CompareTo returning "+i+" at DomainKey");
			return i;
		}
		return getMapKey().compareTo(((PrimaryKeySet)o).getMapKey());
	}
	
	@Override
	public boolean equals(Object o) {
		return getDomainKey().equals(((PrimaryKeySet)o).getDomainKey()) &&
				getMapKey().equals(((PrimaryKeySet)o).getMapKey());// &&
	}
	
	@Override
	public int hashCode() {
		return getDomainKey().hashCode() + getMapKey().hashCode();
	}
	
	public String toString() {
		return String.format("Identity %s domainKey:%s mapKey:%s%n", identity, domainKey, mapKey);
	}
	
}
