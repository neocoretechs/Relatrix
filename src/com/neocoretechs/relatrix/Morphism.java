package com.neocoretechs.relatrix;

import java.io.Externalizable;
import java.io.IOException;

import java.util.List;
import java.util.NoSuchElementException;

import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.NotifyDBCompareTo;
import com.neocoretechs.rocksack.TransactionId;

/**
* Morphism - domain, map, range structure
* ref's for relation datatype
*
* The permutations for our tuple are as follows
* keyop:<p/><dd>
* 
*              0       d,m,r                         <dd>
*              1       d,r,m                         <dd>
*              2       m,d,r                         <dd>
*              3       m,r,d                         <dd>
*              4       r,d,m                         <dd>
*              5       r,m,d                         <dd>
* <p/>
* we use this key for retrieval depending
* on the desired traversal scenario, that is,
* in what order do we want the values returned...    <dd>
* The is the base class for the different morphism permutations that allow us to form different
* sets from categories. The template class can be used to retrieve sets based on their class type.
* @author Jonathan Groff (C) NeoCoreTechs 1997,2014,2015,2024
*/
public abstract class Morphism extends KeySet implements Comparable, Externalizable, Cloneable {
		private static boolean DEBUG = false;

        static final long serialVersionUID = -9129948317265641091L;
        public static enum displayLevels {VERBOSE, BRIEF, MINIMAL};
        public static displayLevels displayLevel = displayLevels.BRIEF;
        
		protected transient Comparable  domain;       // domain object
        protected transient Comparable  map;          // map object
        protected transient Comparable  range;        // range
        
        protected transient boolean templateFlag = false;
        
        public Morphism() {}
        
        /**
         * Resolving constructor 1 <p/>
         * Construct and establish key position for the elements of a morphism.
         * @param d domain instance object
         * @param m map instance object
         * @param r range instance object
         */
        public Morphism(Comparable d, Comparable m, Comparable r) {
        	setDomain(d);
            setMap(m);
            setRange(r);
        }
        
        /**
         * Resolving constructor 2 <p/>
         * Construct and establish key position for the elements of a morphism.
         * @param alias
         * @param d
         * @param m
         * @param r
         */
        public Morphism(Alias alias, Comparable d, Comparable m, Comparable r) {
        	this.alias = alias;
        	setDomain(alias, d);
            setMap(alias, m);
            setRange(alias, r);
        }
        /**
         * Resolving constructor 3
         * @param transactionId
         * @param d
         * @param m
         * @param r
         */
        public Morphism(TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
      		this.transactionId = transactionId;
        	setDomain(d);
            setMap(m);
            setRange(r);
        }
    	/**
    	 * Resolving constructor 4 <p/>
    	 * We need transaction id first, so we cant call superclass constructor. When we dont yet have keys, we must resolve.
    	 * @param alias
    	 * @param transactionId
    	 * @param d
    	 * @param m
    	 * @param r
    	 */
    	public Morphism(Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
    		this.transactionId = transactionId;
    		this.alias = alias;
    		if(alias != null) {
    			setDomain(alias, d);
    			setMap(alias, m);
    			setRange(alias, r);
    		} else {
    			setDomain(d);
    			setMap(m);
    			setRange(r);
    		}
    	}
    	
        /**
         * Template constructor 1 <p/>
         * Construct and establish key position for the elements of a morphism template. 
         * In a template, we dont create instances, merely resolve them leaving effective
         * null key for those without instances
         * @param d
         * @param m
         * @param r
         */
        Morphism(boolean flag, Comparable d, Comparable m, Comparable r) {
        	this.templateFlag = flag;
        	setDomainTemplate(d);
            setMapTemplate(m);
            setRangeTemplate(r);
        }
        
        /**
         * Template constructor 2 <p/>
         * Construct and establish key position for the elements of a morphism.
         * In a template, we dont create instances, merely resolve them leaving effective
         * null key for those without instances
         * @param d
         * @param m
         * @param r
         */
        Morphism(boolean flag, Alias alias, Comparable d, Comparable m, Comparable r) {
        	this.templateFlag = flag;
        	this.alias = alias;
        	setDomainTemplate(alias, d);
            setMapTemplate(alias, m);
            setRangeTemplate(alias, r);
        } 
    	/**
    	 * Template constructor 3 <p/>
    	 * Construct and establish key position for the elements of a morphism template.
    	 * In a template, we dont create keys for instances that dont resolve, we use effective null key
    	 * @param d
    	 * @param m
    	 * @param r
    	 */
    	Morphism(boolean flag, Alias alias, TransactionId transactionId, Comparable d, Comparable m, Comparable r) {
    		this.templateFlag = flag;
    		this.transactionId = transactionId;
    		this.alias = alias;
    		if(alias != null) {
    			setDomainTemplate(alias, d);
    			setMapTemplate(alias, m);
    			setRangeTemplate(alias, r);
    		} else {
    			setDomainTemplate(d);
    			setMapTemplate(m);
    			setRangeTemplate(r);
    		}
    	}
        
        /**
         * Copy constructor 1, default, if we have keys, assume references are resolved.
         * @param d
         * @param dkey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
        	super(domainKey, mapKey, rangeKey);
        	this.templateFlag = false;
        	domain = d;
            map = m;
            range = r;
        }
        
        /**
         * Copy constructor 2, alias, if we have keys, assume instances are resolved.
         * @param alias
         * @param d
         * @param domainkey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(Alias alias, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey, alias);
        	this.templateFlag = false;
        	domain = d;
            map = m;
            range = r;
        }
        
        /**
         * Copy constructor 3, transaction id <p/>
         * @param transactionId
         * @param d
         * @param domainKey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
      	Morphism(TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey, transactionId);
         	domain = d;
            map = m;
            range = r;
		}
        /**
         * Copy constructor 4, alias, transaction id
         * @param alias
         * @param transactionId
         * @param d
         * @param domainkey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey, alias, transactionId);
        	this.templateFlag = false;
        	domain = d;
            map = m;
            range = r;
        }
        
        /**
         * Copy constructor 5 template default, if we have keys, assume we have resolution.
         * @param flag
         * @param d
         * @param domainkey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(boolean flag, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey);
        	this.templateFlag = flag;
        	domain = d;
            map = m;
            range = r;
        }
        
        /**
         * Copy constructor 6 template, alias
         * @param flag
         * @param alias
         * @param d
         * @param domainkey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(boolean flag, Alias alias, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey, alias);
        	this.templateFlag = flag;
         	domain = d;
            map = m;
            range = r;
        }
        
        /**
         * Copy constructor 7 template, transactionId
         * @param flag
         * @param transactionId
         * @param d
         * @param domainKey
         * @param m
         * @param mapKey
         * @param r
         * @param rangeKey
         */
        Morphism(boolean flag, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
          	super(domainKey, mapKey, rangeKey, transactionId);
        	this.templateFlag = flag;
         	domain = d;
            map = m;
            range = r;
		}
        /**
         * Copy constructor 8 template, alias, transactionId
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
        Morphism(boolean flag, Alias alias, TransactionId transactionId, Comparable d, DBKey domainKey, Comparable m, DBKey mapKey, Comparable r, DBKey rangeKey) {
           	super(domainKey, mapKey, rangeKey, alias, transactionId);
        	this.templateFlag = flag;
         	domain = d;
            map = m;
            range = r;
        }

		@Override
        public abstract Object clone() throws CloneNotSupportedException;
        
        /*
         * These are for NotifyDbCompareTo interface, which triggers in pore-and post compare from SerializedComparator
    	@Override
    	public void preCompare() {
    		keyCompare = true;
    	}
    	@Override
    	public void postCompare() {
    		keyCompare = false;
    	}
        */
         	
        /**
         * Method invoked from custom deserializer to indicate that RockSack is retrieving keys and
         * comparison should be on keys rather then resolved instances.
         * @param keyCompare true to indicate database is deserializing and {@link KeySet} {@link DBKey} should be comparred    
        public void setKeyCompare(boolean keyCompare) {
        	this.keyCompare = keyCompare;
        }
      	*/
    	
        /**
         * Transparently process DBKey, returning actual instance. If the domain is already deserialized as an instance
         * it will be returned without further processing. If the domain is null and the key in the {@link KeySet} is
         * valid, the {@link IndexResolver} uses its {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface}
         * to perform a getByIndex call on the domain key of the KeySet, thus retrieving a domain from the database indicated
         * @return The real Comparable instance, pointed to by DBKey
         */
        public Comparable getDomain() {
        	try {
        		if(domain != null)
        			return domain;
        		if(DBKey.isValid(getDomainKey())) {
        			domain = resolveKey(getDomainKey());
        		}
        		return domain;
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * If domain is null, create a new {@link DBKey} in {@link KeySet}. If domain not null, get the domain
         * key from KeySet and check if its valid. If it is valid, the domain will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the domain key of the KeySet.
         * If the domain key is not valid, a getByInstance of the domain on current database default tablespace is
         * performed to try and obtain a a domain DBKey. If this method call comes back null, then a new key is formed
         * using the domain instance value stored to the current index table using the IndexResolver.
         * If the method call to getByInstance for the domain instance comes back not null, then we simply set the domain key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class. 
         * {@link com.neocoretechs.relatrix.key.RelatrixIndex} .
         * @param domain
         */
        public void setDomain(Comparable<?> domain) {
        	if(domain == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
        		checkKeyComaptibility(domain);
        		this.domain = domain;
        		DBKey dbKey = null;
    			if((dbKey = resolveInstance(domain)) == null)
    				setDomainKey(newKey(domain)); // stores instance
    			else
        			setDomainKey(dbKey);
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
         * master table for each database. {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param alias2 the database alias
         * @param domain
         */
        public void setDomain(Alias alias2, Comparable<?> domain) {
        	if(domain == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
         		checkKeyComaptibility(alias2, domain);
        		this.domain = domain;
        		DBKey dbKey = null;
        		if((dbKey = resolveInstance(alias2, domain)) == null)
        			setDomainKey(newKey(alias2, domain)); // stores instance
        		else
        			setDomainKey(dbKey);
        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }		

		/**
         * If domain is null, create a new {@link DBKey} in {@link KeySet}. If domain not null, get the domain
         * key from KeySet and check if its valid. If it is valid, the domain will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the domain key of the KeySet.
         * If the domain key is not valid, a getByInstance of the domain on current database default tablespace is
         * performed to try and obtain a a domain DBKey. If this method call comes back null, then a new key is formed
         * using the effective null key value.
         * If the method call to getByInstance for the domain instance comes back not null, then we simply set the domain key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class, and a DBKey key and instance value 
         * master table for each database. {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param domain the domain instance
         */
        public void setDomainTemplate(Comparable<?> domain) {
        	try {
         		checkKeyComaptibility(domain);
        		this.domain = domain;
        		if(domain != null) { 
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(domain)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();
        				setDomainKey(dbKey);
        			} else
        				setDomainKey(dbKey);
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();
        			setDomainKey(dbKey);
        		}
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        
        /**
         * Set the domain instance without additional resolution
         * @param domain
         */
        public void setDomainResolved(Comparable<?> domain) {
        	this.domain = domain;
        }
        
        /**
         * If domain is null, create a new {@link DBKey} in {@link KeySet}. If domain not null, get the domain
         * key from KeySet and check if its valid. If it is valid, the domain will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the domain key of the KeySet.
         * If the domain key is not valid, a getByInstance of the domain on the database indicated by the alias tablespace is
         * performed to try and obtain a domain DBKey. If this method call comes back null, then a new key is formed
         * using the effective null key value.
         * If the method call to getByInstance for the domain instance comes back not null, then we simply set the domain key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class, and a DBKey key and instance value 
         * master table for each database. {@link com.neocoretechs.relatrix.key.RelatrixIndex}  
         * @param alias2 the database alias 
         * @param domain
         */
        private void setDomainTemplate(Alias alias2, Comparable<?> domain) {
        	try {
        		this.domain = domain;
        		if(domain != null) { 
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(alias2,domain)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();
        				setDomainKey(dbKey);
        			} else
        				setDomainKey(dbKey);
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();
        			setDomainKey(dbKey);
        		}

        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }		
        /**
         * Transparently process DBKey, returning actual instance. If the map is already deserialized as an instance
         * it will be returned without further processing. If the map is null and the key in the {@link KeySet} is
         * valid, the {@link IndexResolver} uses its {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface}
         * to perform a getByIndex call on the map key of the KeySet, thus retrieving a map from the database indicated
         * in the first half of the DBKey, and the key to the instance in the last half of the DBKey.
         * {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @return The real Comparable instance, pointed to by DBKey
         */
        public Comparable getMap() {
        	try {
        		if(map != null) 
        			return map;
        		if(DBKey.isValid(getMapKey())) {
        			map = resolveKey(getMapKey());
        		}
        		return map;
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * If map is null, create a new {@link DBKey} in {@link KeySet}. If map not null, get the map
         * key from KeySet and check if its valid. If it is valid, the map will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the map key of the KeySet.
         * If the map key is not valid, a getByInstance of the map on current database default tablespace is
         * performed to try and obtain a map DBKey. If this method call comes back null, then a new key is formed
         * using the map instance value stored to the current index table using the IndexResolver.
         * If the method call to getByInstance for the map instance comes back not null, then we simply set the map key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class, and a DBKey key and instance value 
         * master table for each database. {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param map the map instance
         */
        public void setMap(Comparable<?> map) {
        	if(map == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
         		checkKeyComaptibility(map);
        		this.map = map;
        		DBKey dbKey = null;
        		if((dbKey = resolveInstance(map)) == null)
        			setMapKey(newKey(map)); // stores instance
        		else
        			setMapKey(dbKey);
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * Set the map instance, resolving the key from the alias database
         * @param alias2
         * @param map
         */
        public void setMap(Alias alias2, Comparable<?> map) {
        	if(map == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
         		checkKeyComaptibility(alias2, map);
        		this.map = map;
        		DBKey dbKey = null;
        		if((dbKey = resolveInstance(alias2, map)) == null)
        			setMapKey(newKey(alias2, map)); // store instance
        		else
        			setMapKey(dbKey);
        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * If map is null, create a new {@link DBKey} in {@link KeySet}. If map not null, get the map
         * key from KeySet and check if its valid. If it is valid, the map will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the map key of the KeySet.
         * If the map key is not valid, a getByInstance of the map on current database default tablespace is
         * performed to try and obtain a map DBKey. If this method call comes back null, then a new key is formed
         * using the effective null key value.
         * If the method call to getByInstance for the map instance comes back not null, then we simply set the map key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class, and a DBKey key and instance value 
         * master table for each database. {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param map the map instance
         */
        public void setMapTemplate(Comparable<?> map) {
        	try {
         		checkKeyComaptibility(map);
        		this.map = map;
        		if(map != null) {
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(map)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();
        				setMapKey(dbKey);
        			} else {
        				setMapKey(dbKey);
        			}
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();
        			setMapKey(dbKey);
        		}
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * Set a map template from another database
         * @param alias2
         * @param map
         */
        private void setMapTemplate(Alias alias2, Comparable<?> map) {
        	try {
        		this.map = map;
        		if(map != null) {
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(alias2, map)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();
        				setMapKey(dbKey);
        			} else {
        				setMapKey(dbKey);
        			} 
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();
        			setMapKey(dbKey);
        		}
        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * Set the map instance without further resolution
         * @param map
         */
        public void setMapResolved(Comparable<?> map) {
        	this.map = map;
        }
        /**
         * Transparently process DBKey, returning actual instance. If the range is already deserialized as an instance
         * it will be returned without further processing. If the range is null and the key in the {@link KeySet} is
         * valid, the {@link IndexResolver} uses its {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface}
         * to perform a getByIndex call on the range key of the KeySet, thus retrieving a range from the DBKey. 
		 * {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @return The real Comparable instance, pointed to by DBKey
         */
        public Comparable getRange() {
        	try {
        		if(range != null)
        			return range;
        		if(DBKey.isValid(getRangeKey())) {
        			range = resolveKey(getRangeKey());
        		}
        		return range;
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * If range is null, create a new {@link DBKey} in {@link KeySet}. If range not null, get the range
         * key from KeySet and check if its valid. If it is valid, the range will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the range key of the KeySet.
         * If the range key is not valid, a getByInstance of the range on current database default tablespace is
         * performed to try and obtain a range DBKey. If this method call comes back null, then a new key is formed
         * using the range instance value stored to the current index table using the IndexResolver.
         * If the method call to getByInstance for the range instance comes back not null, then we simply set the range key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class.
         * {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param range the range instance
         */
        public void setRange(Comparable<?> range) {
        	if(range == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
         		checkKeyComaptibility(range);
        		this.range = range;
        		DBKey dbKey = null;
        		if((dbKey = resolveInstance(range)) == null)
        			setRangeKey(newKey(range));
        		else
        			setRangeKey(dbKey);						
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * 
         * @param alias2
         * @param range
         */
        public void setRange(Alias alias2, Comparable<?> range) {
        	if(range == null)
        		throw new RuntimeException("Cannot set relationship component null.");
        	try {
         		checkKeyComaptibility(alias2, range);
        		this.range = range;
        		DBKey dbKey = null;
        		if((dbKey = resolveInstance(alias2, range)) == null)
        			setRangeKey(newKey(alias2, range));
        		else
        			setRangeKey(dbKey);						
        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }

        /**
         * If range is null, create a new {@link DBKey} in {@link KeySet}. If range not null, get the range
         * key from KeySet and check if its valid. If it is valid, the range will be set to the {@link IndexResolver}
         * {@link com.neocoretechs.relatrix.key.IndexInstanceTableInterface} getByIndex for the range key of the KeySet.
         * If the range key is not valid, a getByInstance of the range on current database default tablespace is
         * performed to try and obtain a range DBKey. If this method call comes back null, then a new key is formed
         * using the effective null key value.
         * If the method call to getByInstance for the range instance comes back not null, then we simply set the range key
         * in the KeySet to the value retrieved from the IndexResolver.<p/>
         * Recall that our tables are stored using an instance key and DBKey value for each database/class.
         * {@link com.neocoretechs.relatrix.key.RelatrixIndex} 
         * @param range
         */
        public void setRangeTemplate(Comparable<?> range) {
        	try {
         		checkKeyComaptibility(range);
        		this.range = range;
        		if(range != null) {
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(range)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();						
        				setRangeKey(dbKey);
        			} else {
        				setRangeKey(dbKey);	
        			}
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();						
        			setRangeKey(dbKey);        			
        		}
        	} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * 
         * @param alias2
         * @param range
         */
        private void setRangeTemplate(Alias alias2, Comparable<?> range) {
        	try {
        		this.range = range;
        		if(range != null) {
        			DBKey dbKey = null;
        			if((dbKey = resolveInstance(alias2, range)) == null) {
        				dbKey = new DBKey();
        				dbKey.setNullKey();
        				setRangeKey(dbKey);
        			} else {
        				setRangeKey(dbKey);
        			}
        		} else {
        			DBKey dbKey = new DBKey();
        			dbKey.setNullKey();						
        			setRangeKey(dbKey);        
        		}
        	} catch (IllegalAccessException | ClassNotFoundException | IOException | NoSuchElementException e) {
        		throw new RuntimeException(e);
        	}
        }
        /**
         * 
         * @param range
         */
        public void setRangeResolved(Comparable<?> range) {
        	this.range = range;
        }
        
        /**
         * Using the passed instance and this Morphism, check the alias and transactionId for non null
         * and then call proper {@link DBKey}.newKey with the indexResolver {@link com.neocoretechs.relatrix.key.IndexInstanceTable}
         * and instance. The process will store the instance in the proper class table as key, and the DBKey table as value,
         * and return the DBKey that links both together.
         * @param instance The instance to be stored
         * @return The DBKey newly created from storage and resolution process
         * @throws IllegalAccessException
         * @throws ClassNotFoundException
         * @throws IOException
         */
		protected DBKey newKey(Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
			if(alias == null) {
				if(transactionId == null)
					return DBKey.newKey(IndexResolver.getIndexInstanceTable(), instance);
				return DBKey.newKey(transactionId, IndexResolver.getIndexInstanceTable(), instance);
			}
			return newKey(alias, instance);
		}
		/**
		 * To relate a key in another database
		 * @param aliasOther
		 * @param instance
		 * @return
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		DBKey newKey(Alias aliasOther, Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
			if(transactionId == null) {
				return DBKey.newKey(aliasOther, IndexResolver.getIndexInstanceTable(), instance);
			} 
			return DBKey.newKey(aliasOther, transactionId, IndexResolver.getIndexInstanceTable(), instance);
		}
		/**
		 * Resolve an instance from the passed DBKey
		 * @param key
		 * @return
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		protected Comparable resolveKey(DBKey key) throws IllegalAccessException, ClassNotFoundException, IOException {
			if(DEBUG) {
				System.out.printf("%s.resolveKey for id=%s xid=%s%n",this.getClass().getName(),this.getIdentity(),transactionId);
				if(alias != null)
					return resolveKey(alias, key);
				if(transactionId == null) {
					Comparable c = (Comparable) IndexResolver.getIndexInstanceTable().get(key);
					System.out.printf("%s.resolveKey for key:%s resulted in:%s%n",this.getClass().getName(),key,c);
					return c;
				} else {
					Comparable c = (Comparable) IndexResolver.getIndexInstanceTable().get(transactionId,key);
					System.out.printf("%s.resolveKey for xid:%s key:%s resulted in:%s%n",this.getClass().getName(),transactionId,key,c);
					return c;
				}
			}
			if(alias != null)
				return resolveKey(alias, key);
			if(transactionId == null)
				return (Comparable) IndexResolver.getIndexInstanceTable().get(key);
			return (Comparable) IndexResolver.getIndexInstanceTable().get(transactionId,key);
		}
		
		/**
		 * Resolve an instance from the passed DBKey from the aliased database
		 * @param key
		 * @return
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		private Comparable resolveKey(Alias alias2, DBKey key) throws IllegalAccessException, ClassNotFoundException, IOException {
			if(DEBUG) {
				if(transactionId == null) {
					Comparable c = (Comparable) IndexResolver.getIndexInstanceTable().get(alias2,key);
					System.out.printf("%s.resolveKey for key:%s resulted in:%s%n",this.getClass().getName(),key,c);
					return c;
				} else {
					Comparable c = (Comparable) IndexResolver.getIndexInstanceTable().get(alias2,transactionId,key);
					System.out.printf("%s.resolveKey for xid:%s key:%s resulted in:%s%n",this.getClass().getName(),transactionId,key,c);
					return c;
				}
			}
			if(transactionId == null)
				return (Comparable) IndexResolver.getIndexInstanceTable().get(alias2,key);
			return (Comparable) IndexResolver.getIndexInstanceTable().get(alias2,transactionId,key);
		}
		
		/**
		 * 
		 * @param instance
		 * @return
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		protected DBKey resolveInstance(Comparable instance) throws IllegalAccessException, ClassNotFoundException, IOException {
			if(DEBUG) {
				if(alias != null)
					return resolveInstance(alias, instance);
				if(transactionId == null) {
					DBKey c = (DBKey) IndexResolver.getIndexInstanceTable().getKey(instance);
					System.out.printf("%s.resolveInstance for instance:%s resulted in:%s%n",this.getClass().getName(),instance,c);
					return c;
				} else {
					DBKey c = (DBKey) IndexResolver.getIndexInstanceTable().getKey(transactionId,instance);
					System.out.printf("%s.resolveInstance for xid:%s instance:%s resulted in:%s%n",this.getClass().getName(),transactionId,instance,c);
					return c;
				}
			}
			if(alias != null)
				return resolveInstance(alias, instance);
			if(transactionId == null)
				return (DBKey)IndexResolver.getIndexInstanceTable().getKey(instance);
			return (DBKey)IndexResolver.getIndexInstanceTable().getKey(transactionId, instance);
		}
		
		/**
		 * 
		 * @param alias2
		 * @param instance
		 * @return
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws NoSuchElementException
		 * @throws IOException
		 */
		private DBKey resolveInstance(Alias alias2, Comparable instance) throws IllegalAccessException, ClassNotFoundException, NoSuchElementException, IOException {
			if(DEBUG) {
				if(transactionId == null) {
					DBKey c = (DBKey) IndexResolver.getIndexInstanceTable().getKey(alias2, instance);
					System.out.printf("%s.resolveInstance for alias:%s instance:%s resulted in:%s%n",this.getClass().getName(),alias2,instance,c);
					return c;
				} else {
					DBKey c = (DBKey) IndexResolver.getIndexInstanceTable().getKey(alias2, transactionId, instance);
					System.out.printf("%s.resolveInstance for xid:%s alias:%s instance:%s resulted in:%s%n",this.getClass().getName(),transactionId,alias2,instance,c);
					return c;	
				}
			}
			if(transactionId == null)
				return (DBKey)IndexResolver.getIndexInstanceTable().getKey(alias2, instance);
			return (DBKey)IndexResolver.getIndexInstanceTable().getKey(alias2, transactionId, instance);
		}
		
		/**
		 * Check that the potential Morphism that is being assigned as part of a relationship is of the same alias
		 * as the instance it is being assigned to such that the keys are of the same database.
		 * @param alias The alias from which the element will be retrieved
		 * @param m The possible Morphism we will check or compatibility
		 * @throws IllegalAccessException If the param is a Morphism and the alias is not the alias of this instance
		 */
        private void checkKeyComaptibility(Alias alias, Comparable<?> m) throws IllegalAccessException {
			if(m instanceof Morphism) {
				if(this.alias == null || (this.alias != null && !alias.equals(this.alias)))
					throw new IllegalAccessException("Alias "+alias+" is not the same database and therefore cannot assign Morphism "+this);		
			}	
		}
        
        /**
         * Check that the potential Morphism that is being assigned as part of a relationship is of the same alias
		 * as the instance it is being assigned to such that the keys are of the same database.
         * @param m The possible Morphism we will check for compatibility if it has an alias and its a Morphism
         * @throws IllegalAccessException
         */
        private void checkKeyComaptibility(Comparable<?> m) throws IllegalAccessException {
    		if(m instanceof Morphism) {
				if(((Morphism)m).alias != null) {
					if(this.alias == null || (this.alias != null && !((Morphism)m).alias.equals(this.alias)))
						throw new IllegalAccessException("Alias of Morphism "+((Morphism)m)+" is not the same database and therefore cannot assign Morphism:"+m);
				} else {
					// Morphism alias is null
					if(this.alias != null)
						throw new IllegalAccessException("Alias of this Morphism "+this+" is not the default database and therefore cannot assign Morphism:"+m);
				}
    		}
 		}
        
    	/**
    	 * Check the given target for a valid {@link Morphism} and that it has a valid {@link DBKey} identity
    	 * @param target the potential Morphism
    	 * @return the identity DBKey or null if not Morphism or identity isnt established or valid
    	 */
    	public static DBKey checkMorphism(Comparable<?> target) {
    		if(target instanceof Morphism) {
    			if(DBKey.isValid(((Morphism)target).identity)) {
    				if(DEBUG)
    					System.out.println("checkMorphism valid:"+((Morphism)target).identity);
    				return ((Morphism)target).identity;
    			}
    		}
    		return null;
    	}
    	
        /**
        * for relate cmpr, we return a value in the range 0-63
        * in which the values for domain,map range : >,<,=,dont care = 0-3
        * are encoded as three 0-3 values in the first six bit positions.
        * a dont care is coded when a dmr value is zero.
        * @param cmpdmr the Morphism to compare to
        * @return the 0-63 compare value
        */
        protected short cmpr(Morphism cmpdmr) {
        	short cmpres = 0;
            if(getDomain() == null)
        		cmpres = 48;
        	else
        		if( getDomain().compareTo(cmpdmr.getDomain()) < 0)
        			cmpres = 16;
        		else
        			if(getDomain().equals(cmpdmr.getDomain()) )
        				cmpres = 32;
                //
            if(getMap() == null)
        		cmpres ^= 12;
        	else
        		if(getMap().compareTo(cmpdmr.getMap())  < 0)
        			cmpres ^= 4;
        		else
        			if( getMap().equals(cmpdmr.getMap()) )
        				cmpres ^= 8;
                //
           if(getRange() == null)
        		cmpres ^= 3;
        	else
        		if( getRange().compareTo( cmpdmr.getRange() ) < 0)
        			cmpres ^= 1;
        		else
        			if( getRange().equals(cmpdmr.getRange()) )
        				cmpres ^= 2;
        	return cmpres;
        }
        
        /**
        * iterate_dmr - return proper domain, map, or range
        * based on dmr_return values.  In dmr_return, value 0
        * is iterator for ?,*.  1-3 BOOLean for d,m,r return yes/no
        * @return the next location to retrieve
        * @throws IOException 
        * @throws IllegalAccessException 
        */
        public Comparable<?> iterate_dmr(short[] dmr_return) throws IllegalAccessException, IOException {
                if(dmr_return[0] >= 3) 
                	return null;
                // no return vals? send back relation location
                if( dmr_return[0] == (-1)  || (dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0) ) 
                	return this;
                do {
                  dmr_return[0]++;
                  // If the element of the tuple needs returning based on formation of our dmr_return, do so
                  if( dmr_return[dmr_return[0]] == 1)
                	  return returnTupleOrder(dmr_return[0]);
                } while( dmr_return[0] < 3 );
                return null;
        }
        
        /**
        * form_template_keyop - Passed Comparable array is functioning as template for search
        * depending on the values in domain,map,range (object=0, ?=1 or *=2, !0 or object)
        * and the ones we care about returning (boolean true in dret)
        * construct the proper index to key array (keyop) and return it
        * (see form_dmrkey for keyop descr)
        * method: construct a little weighting value for each one based
        * on a base val of position domain=2,map=1,range=0
        * and modified by args to findset object=6,?=3,*=0
        * this establishes a precedent for our return values
        * @param dret the return value flag array with iterator at 0
        * @return the keyop
        */
        public static short form_template_keyop(Result3 result3, short[] dret) {
            short dmr_prec[] = {2,1,0};
            Comparable[] result = result3.toArray();
            if( result[0] != null ) // domain not null
                        dmr_prec[0] += 6; // RGuid
        	else
                        if( dret[1] == 1 ) dmr_prec[0] += 3; // '?'
                // if '*' leave alone, this gets all
            if( result[1] != null ) // map not null
                        dmr_prec[1] += 6; // RGuid
        	else
                        if( dret[2] == 1 ) dmr_prec[1] += 3; // '?'
                //
            if( result[2] != null ) // range not null
                        dmr_prec[2] += 6;
        	else    
                        if( dret[3] == 1 ) dmr_prec[2] += 3;
                //
                // we have precedents, now find order
        	if( dmr_prec[0] > dmr_prec[1] && dmr_prec[0] > dmr_prec[2] ) {
        		// domain > map,range
        		if( dmr_prec[1] > dmr_prec[2] )
        		// domain > (map > range)
                                return (short)0; // dmr
        		else
        		// domain > (map < range)
                                return (short)1; // drm
        	}
        	if( dmr_prec[1] > dmr_prec[0] && dmr_prec[1] > dmr_prec[2] ) {
        		// map > domain,range
        		if( dmr_prec[0] > dmr_prec[2] )
        		// map > (domain > range)
                                return (short)2; // mdr
        		else
        		// map > (domain < range)
                                return (short)3; // mrd
        	}
        	if( dmr_prec[2] > dmr_prec[0] && dmr_prec[2] > dmr_prec[1] ) {
        		// range > domain,map
        		if( dmr_prec[0] > dmr_prec[1] )
        		// range > (domain > map)
                                return (short)4; // rdm
        		else
        		// range > (domain < map)
                                return (short)5; // rmd
        	}
            // this method is internal and this should not happen
            throw new RuntimeException("Invalid keyop in form_keyop ");
        }
        
        /**
         * When participating in a retrieval we want to return the proper part of the tuple
         * depending on the operation so 'n' equates to the position in the findset semantics (?,*,<object>)
         * above, ? is in position 1, so n would be 1. In a subclass the order is different depending on the sort index
         * @param n
         * @return
         */
        public Comparable<?> returnTupleOrder(int n) {
        	// default dmr
        	switch(n) {
        		case 1:
        			return getDomain();
        		case 2:
        			return getMap();
        		case 3:
        			return getRange();
        		default:
        			break;
        	}
        	throw new RuntimeException("returnTupleOrder invalid tuple "+n);
        }
        
        /**
  		 * Beginning at target, recursively resolve all relationships related to target.
  		 * If target is not Morphism, add it to res result List and return, if it is Morphism, recursively call
  		 * this method on the domain, map, and range.
         * @param target Comparable to begin resolution
         * @param res list to populate with resolved relationships
         */
        public static void resolve(Comparable target, List<Comparable> res) {
        	if(!(target instanceof Morphism)) {
        		res.add(target);
        		return;
        	}
         	Comparable tdomain, tmap, trange;
          	tdomain = (Comparable) ((Morphism)target).getDomain();
        	tmap = (Comparable) ((Morphism)target).getMap();
        	trange = (Comparable) ((Morphism)target).getRange();
        	resolve(tdomain, res);
        	resolve(tmap, res);
        	resolve(trange, res);
        	if(DEBUG)
        		System.out.printf("Morphism.resolve %s %s %s%n", tdomain, tmap, trange);
        }
        
        /**
         * Beginning from target, recursively resolve the Morphisms directly contained in target.
         * If target is Morphism, add the key to morphisms result List and recursively call
  		 * this method on the domain, map, and range, else return.
         * @param target the initial instance of any Comparable
         * @param morphisms List to be populated with resolved Morphisms
         */
        public static void resolveMorphisms(Comparable target, List<DBKey> morphisms) {
        	if(!(target instanceof Morphism)) {
        		return;
        	}
        	morphisms.add(((Morphism)target).getIdentity());
         	Comparable tdomain, tmap, trange;
          	tdomain = (Comparable) ((Morphism)target).getDomain();
        	tmap = (Comparable) ((Morphism)target).getMap();
        	trange = (Comparable) ((Morphism)target).getRange();
        	resolveMorphisms(tdomain, morphisms);
        	resolveMorphisms(tmap, morphisms);
        	resolveMorphisms(trange, morphisms);
        	if(DEBUG)
        		System.out.printf("Morphism.resolve %s %s %s%n", tdomain, tmap, trange);
        }
             
        @Override
        public String toString() {
     		String s = String.format("Class:%s Key:%s%n",this.getClass().getName(),this.getIdentity());
    		StringBuffer sb = new StringBuffer(s);
    		if(alias != null) {
    			sb.append("Alias:");
    			sb.append(alias);
    		}
    		if(transactionId != null) {
    			sb.append(" Xid:");
    			sb.append(transactionId);
    		}
    		if(templateFlag)
    			sb.append(" <template>");
       		if(alias != null || transactionId != null || templateFlag)
    			sb.append("\n");
       		switch(displayLevel) {
       		case VERBOSE:
       			sb.append("Class:[");
       			sb.append(getDomain() == null ? "NULL" :getDomain().getClass().getName());
       			sb.append("->");
       			sb.append(getMap() == null ? "NULL" : getMap().getClass().getName());
       			sb.append("->");
       			sb.append(getRange() == null ? "NULL" : getRange().getClass().getName());
       			sb.append("]\n");
       			sb.append("Keys:[");
       			sb.append(getDomainKey() == null ? "NULL" : getDomainKey().toString());
       			sb.append("->");
       			sb.append(getMapKey() == null ? "NULL" : getMapKey().toString());
       			sb.append("->");
       			sb.append(getRangeKey() == null ? "NULL" : getRangeKey().toString());
       			sb.append("]\n");
       			sb.append("Vals:[");
       			sb.append(getDomain() == null ? "NULL" : getDomain().toString());
       			sb.append("->");
       			sb.append(getMap() == null ? "NULL" : getMap().toString());
       			sb.append("->");
       			sb.append(getRange() == null ? "NULL" : getRange().toString());
       			sb.append("]\n");
       			return sb.toString();
       		case BRIEF:
       			sb.append("Class:[");
       			sb.append(getDomain() == null ? "NULL" :getDomain().getClass().getName());
       			sb.append("->");
       			sb.append(getMap() == null ? "NULL" : getMap().getClass().getName());
       			sb.append("->");
       			sb.append(getRange() == null ? "NULL" : getRange().getClass().getName());
       			sb.append("]\n");
       			sb.append("Vals:[");
       			sb.append(getDomain() == null ? "NULL" : getDomain().toString());
       			sb.append("->");
       			sb.append(getMap() == null ? "NULL" : getMap().toString());
       			sb.append("->");
       			sb.append(getRange() == null ? "NULL" : getRange().toString());
       			sb.append("]\n");
       			return sb.toString();
       		case MINIMAL:
       		default:
       			sb.append("Vals:[");
       			sb.append(getDomain() == null ? "NULL" : getDomain().toString());
       			sb.append("->");
       			sb.append(getMap() == null ? "NULL" : getMap().toString());
       			sb.append("->");
       			sb.append(getRange() == null ? "NULL" : getRange().toString());
       			sb.append("]\n");
       			return sb.toString();
       		}
        }

}
