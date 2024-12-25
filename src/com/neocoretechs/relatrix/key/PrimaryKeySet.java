package com.neocoretechs.relatrix.key;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.neocoretechs.rocksack.Alias;
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
//@DatabaseClass(tablespace="com.neocoretechs.relatrix.DomainMapRange")
public class PrimaryKeySet implements Externalizable, Comparable {
	private static final long serialVersionUID = -2614468413972955193L;
	private static boolean DEBUG = false;
	protected DBKey domainKey;
    protected DBKey mapKey;
    protected transient DBKey identity;
	protected transient TransactionId transactionId = null;
	protected transient Alias alias = null;
	private transient boolean isIdentityImmutable = false; // once identity or alias is set, instance is immutable
	private transient boolean isAliasImmutable = false; // once identity or alias is set, instance is immutable

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
		this.isAliasImmutable = true;
	}
	PrimaryKeySet(DBKey domainKey, DBKey mapKey, Alias alias, TransactionId transactionId) {
		this.domainKey = domainKey;
		this.mapKey = mapKey;
		this.transactionId = transactionId;
		this.alias = alias;
		this.isAliasImmutable = true;
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
		if(this.isIdentityImmutable)
			throw new RuntimeException("Identity is immutable for Morphism instance "+this);
		this.identity = identity;
		this.isIdentityImmutable = true;
	}
   	public Alias getAlias() {
		return alias;
	}
	public void setAlias(Alias alias) {
		if(this.isAliasImmutable)
			throw new RuntimeException("Alias is immutable for Morphism instance "+this);
		this.alias = alias;
		this.isAliasImmutable = true;
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
	 * If the decision is made to toss out the entry, may have spurious instances of domain and map.
	 * @param skeyd domain instance
	 * @param skeym instance for map
	 * @return The PrimaryKeySet instance with either null or resolved identity of the DomainMapRange instance
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static PrimaryKeySet locate(Comparable skeyd, Comparable skeym) throws IllegalAccessException, ClassNotFoundException, IOException {
		PrimaryKeySet pk = new PrimaryKeySet();
		// check for domain/map match
		// Enforce categorical structure; domain->map function uniquely determines range.
		// If the search winds up at the key or the key is empty or the domain->map exists, the key
		// cannot be inserted
		Object d = IndexResolver.getIndexInstanceTable().getKey(skeyd);
		if( d == null ) {
			pk.setDomainKey(DBKey.newKey(IndexResolver.getIndexInstanceTable(), skeyd)); // puts to index and instance
		} else {
			pk.setDomainKey((DBKey) d);
		}
		Object m = IndexResolver.getIndexInstanceTable().getKey(skeym);
		if( m == null ) {
			pk.setMapKey(DBKey.newKey(IndexResolver.getIndexInstanceTable(), skeym)); // puts to index and instance
		} else {
			pk.setMapKey((DBKey) m);
		}
		// do we have a relation already?
		Object dKey = IndexResolver.getIndexInstanceTable().getKey(pk);
		// is it found, hence not unique?
		if(dKey != null) {
			pk.identity = (DBKey) dKey;
			pk.isIdentityImmutable = true;
		}
		return pk;	
	}
	/**
	 * Locate the primary key in prep for storage.  For the passed key instances 
	 * create the instance/DBKey and DBKey/instance tablespace entries for domain and map.
	 * If the decision is made to toss out the entry, may have spurious instances of domain and map.
	 * @param alias
	 * @param skeyd
	 * @param skeym
	 * @return The PrimaryKeySet instance with either null or resolved identity of the DomainMapRange instance
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static PrimaryKeySet locate(Alias alias, Comparable skeyd, Comparable skeym) throws IllegalAccessException, ClassNotFoundException, IOException {
		PrimaryKeySet pk = new PrimaryKeySet();
		//
		pk.setAlias(alias);
		Object d = IndexResolver.getIndexInstanceTable().getKey(alias,skeyd);
		if( d == null ) {
			pk.setDomainKey(DBKey.newKey(alias, IndexResolver.getIndexInstanceTable(), skeyd)); // puts to index and instance
		} else {
			pk.setDomainKey((DBKey) d);
		}
		Object m = IndexResolver.getIndexInstanceTable().getKey(alias,skeym);
		if( m == null ) {
			pk.setMapKey(DBKey.newKey(alias, IndexResolver.getIndexInstanceTable(), skeym)); // puts to index and instance
		} else {
			pk.setMapKey((DBKey) m);
		}
		Object dKey = IndexResolver.getIndexInstanceTable().getKey(alias, pk);
		if(dKey != null) {
			pk.identity = (DBKey) dKey;
			pk.isIdentityImmutable = true;
		}
		return pk; // now call DBKey.newKey(alias, indexTable, this);		
	}
	/**
	 * Locate the primary key in prep for storage.  For the passed key instances 
	 * create the instance/DBKey and DBKey/instance tablespace entries for domain and map.
	 * If the decision is made to toss out the entry, may have spurious instances of domain and map.
	 * @param transactionId
	 * @param skeyd
	 * @param skeym
	 * @return The PrimaryKeySet instance with either null or resolved identity of the DomainMapRange instance
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static PrimaryKeySet locate(TransactionId transactionId, Comparable skeyd, Comparable skeym) throws IllegalAccessException, ClassNotFoundException, IOException {
		PrimaryKeySet pk = new PrimaryKeySet();
		pk.transactionId = transactionId;
		Object d = IndexResolver.getIndexInstanceTable().getKey(transactionId, skeyd);
		if( d == null ) {
			pk.setDomainKey(DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(), skeyd)); // puts to index and instance
		} else {
			pk.setDomainKey((DBKey) d);
		}
		Object m = IndexResolver.getIndexInstanceTable().getKey(transactionId,skeym);
		if( m == null ) {
			pk.setMapKey(DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(), skeym)); // puts to index and instance
		} else {
			pk.setMapKey((DBKey) m);
		}
		Object dKey = IndexResolver.getIndexInstanceTable().getKey(transactionId, pk);
		if(dKey != null) {
			pk.identity = (DBKey) dKey;
			pk.isIdentityImmutable = true;
		}
		return pk; // now DBKey.newKey(transactionId, indexTable, this);		
	}
	/**
	 * Locate the primary key in prep for storage.  For the passed key instances 
	 * create the instance/DBKey and DBKey/instance tablespace entries for domain and map.
	 * If the decision is made to toss out the entry, may have spurious instances of domain and map.
	 * @param alias
	 * @param transactionId
	 * @param skeyd
	 * @param skeym
	 * @return The PrimaryKeySet instance with either null or resolved identity of the DomainMapRange instance
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static PrimaryKeySet locate(Alias alias, TransactionId transactionId, Comparable skeyd, Comparable skeym) throws IllegalAccessException, ClassNotFoundException, IOException {
		PrimaryKeySet pk = new PrimaryKeySet();
		pk.setAlias(alias);
		pk.transactionId = transactionId;
		// transaction id and alias not null
		Object d = IndexResolver.getIndexInstanceTable().getKey(alias, transactionId, skeyd);
		if( d == null ) {
			pk.setDomainKey(DBKey.newKey(alias, transactionId, IndexResolver.getIndexInstanceTable(), skeyd)); // puts to index and instance
		} else {
			pk.setDomainKey((DBKey)d);
		}
		Object m = IndexResolver.getIndexInstanceTable().getKey(alias, transactionId, skeym);
		if(m == null) {
			pk.setMapKey(DBKey.newKey(alias, transactionId, IndexResolver.getIndexInstanceTable(), skeym)); // puts to index and instance
		} else {
			pk.setMapKey((DBKey)m);
		}
		Object dKey = IndexResolver.getIndexInstanceTable().getKey(alias, transactionId, pk);
		if(dKey != null) {
			pk.identity = (DBKey) dKey;
			pk.isIdentityImmutable = true;
		}
		return pk;	
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException { 
		domainKey = new DBKey(in.readLong(), in.readLong());
		mapKey = new DBKey(in.readLong(), in.readLong());	
	} 
	
	@Override  
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(domainKey.getMsb());
		out.writeLong(domainKey.getLsb());
		out.writeLong(mapKey.getMsb());
		out.writeLong(mapKey.getLsb());
	}
	
	@Override
	public int compareTo(Object o) {
		if(DEBUG)
			System.out.println("PrimaryKeyset CompareTo "+this+", "+o+" domain this:"+this.getDomainKey()+" domain o:"+((PrimaryKeySet)o).getDomainKey()+" map this:"+getMapKey()+", map o:"+((PrimaryKeySet)o).getMapKey());
		int i = getDomainKey().compareTo(((PrimaryKeySet)o).getDomainKey());
		if(i != 0) {
			if(DEBUG)
				System.out.println("PrimaryKeyset CompareTo returning "+i+" at DomainKey");
			return i;
		}
		if(DEBUG)
			System.out.println("PrimaryKeyset CompareTo returning "+getMapKey().compareTo(((PrimaryKeySet)o).getMapKey())+" at MapKey");
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
		if(alias != null)
			if(transactionId != null)
				return String.format("Identity %s Alias %s Xid %s domainKey:%s mapKey:%s%n", identity, alias, transactionId, domainKey, mapKey);
			else
				return String.format("Identity %s Alias %s domainKey:%s mapKey:%s%n", identity, alias, domainKey, mapKey);
		if(transactionId != null)
			return String.format("Identity %s Xid %s domainKey:%s mapKey:%s%n", identity, transactionId, domainKey, mapKey);
		return String.format("Identity %s domainKey:%s mapKey:%s%n", identity, domainKey, mapKey);
	}
	
}
