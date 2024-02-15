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
	 * We need transaction id first, so we cant call superclass constructor
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
	/**
	 * We need transaction id first, so we cant call superclass constructor
	 * @param alias
	 * @param transactionId
	 * @param d
	 * @param m
	 * @param r
	 */
	public MorphismTransaction(String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
		this.transactionId = transactionId;
		this.alias = alias;
		setDomain(alias, d);
		setMap(alias, m);
		setRange(alias, r);
	}
	
	/**
	 * Construct and establish key position for the elements of a morphism template.
	 * In a template, we dont create keys for instances that dont resolve, we use effective null key
	 * @param d
	 * @param m
	 * @param r
	 */
	public MorphismTransaction(boolean flag, String transactionId, Comparable d, Comparable m, Comparable r) {
		this.templateFlag = flag;
		this.transactionId = transactionId;
		setDomainTemplate(d);
		setMapTemplate(m);
		setRangeTemplate(r);
	}
	/**
	 * Construct and establish key position for the elements of a morphism template.
	 * In a template, we dont create keys for instances that dont resolve, we use effective null key
	 * @param d
	 * @param m
	 * @param r
	 */
	public MorphismTransaction(boolean flag, String alias, String transactionId, Comparable d, Comparable m, Comparable r) {
		this.templateFlag = flag;
		this.transactionId = transactionId;
		this.alias = alias;
		setDomainTemplate(alias, d);
		setMapTemplate(alias, m);
		setRangeTemplate(alias, r);
	}
    /**
     * Copy constructor 1, default
     * @param transactionId
     * @param d
     * @param dkey
     * @param m
     * @param mapKey
     * @param r
     * @param rangeKey
     */
    public MorphismTransaction(String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
    	this.templateFlag = false;
		this.transactionId = transactionId;
    	domain = d;
        map = m;
        range = r;
        setDomainKey(domainkey);
        setMapKey(mapKey);
        setRangeKey(rangeKey);
    }
    
    /**
     * Copy constructor 2, alias
     * @param alias
     * @param transactionId
     * @param d
     * @param domainkey
     * @param m
     * @param mapKey
     * @param r
     * @param rangeKey
     */
    public MorphismTransaction(String alias, String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
    	this.alias = alias;
    	this.templateFlag = false;
    	this.transactionId = transactionId;
    	domain = d;
        map = m;
        range = r;
        setDomainKey(domainkey);
        setMapKey(mapKey);
        setRangeKey(rangeKey);
    }
    
    /**
     * Copy constructor 3 template default
     * @param flag
     * @param transactionId
     * @param d
     * @param domainkey
     * @param m
     * @param mapKey
     * @param r
     * @param rangeKey
     */
    public MorphismTransaction(boolean flag, String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
    	this.templateFlag = flag;
    	this.transactionId = transactionId;
    	domain = d;
        map = m;
        range = r;
        setDomainKey(domainkey);
        setMapKey(mapKey);
        setRangeKey(rangeKey);
    }
    
    /**
     * Copy constructor 4 template alias
     * @param flag
     * @param alias
     * @param transactionId
     * @param d
     * @param domainkey
     * @param m
     * @param mapKey
     * @param r
     * @param rangeKey
     */
    public MorphismTransaction(boolean flag, String alias, String transactionId, Comparable d, DBKey domainkey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
    	this.templateFlag = flag;
       	this.transactionId = transactionId;
    	this.alias = alias;
     	domain = d;
        map = m;
        range = r;
        setDomainKey(domainkey);
        setMapKey(mapKey);
        setRangeKey(rangeKey);
    } 
    
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String xid) {
		this.transactionId = xid;
	}
	
	@Override
	protected DBKey newKey(Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(), instance);
	}
	@Override	
	protected DBKey newKey(String alias, Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return DBKey.newKeyAlias(alias, transactionId, IndexResolver.getIndexInstanceTable(), instance);
	}
	@Override	
	protected Comparable resolveKey(DBKey key) throws IllegalAccessException, ClassNotFoundException, IOException {
		return (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(transactionId, key);
	}
	@Override	
	protected DBKey resolveInstance(Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
		return (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(transactionId, instance);
	}
	@Override	
	protected DBKey resolveInstance(String alias, Comparable instance) throws IllegalAccessException, ClassNotFoundException, NoSuchElementException, IOException {
		return (DBKey)IndexResolver.getIndexInstanceTable().getByInstanceAlias(alias, transactionId, instance);
	}

}
