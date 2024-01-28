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

	/**
	 * Set a template for operations requiring a default (blank) term for purposes such as retrieval/comparison.
	 * The domain is set to the instance value and the key is set to a new, default (blank) {@link DBKey}.
	 * @param domain
	 */
	public void setDomainTemplate(Comparable<?> domain) {
		this.domain = domain;
		setDomainKey(new DBKey());
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

	public void setMapTemplate(Comparable<?> map) {
		this.map = map;
		setMapKey(new DBKey());
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

	public void setRangeTemplate(Comparable<?> range) {
		this.range = range;
		setRangeKey(new DBKey());
	}
	public String toString() { 
		return String.format("Class:%s %n[%s->%s->%s]%n[%s->%s->%s]%n",this.getClass().getName(),
				(getDomain() == null ? "NULL" :getDomain().getClass().getName()),
				(getMap() == null ? "NULL" : getMap().getClass().getName()), 
				(getRange() == null ? "NULL" : getRange().getClass().getName()),
				(getDomain() == null ? "NULL" : getDomain()),
				(getMap() == null ? "NULL" : getMap()), 
				(getRange() == null ? "NULL" : getRange()));
	}

	/**
	 * key combinations for Relatrix follow
	 */
	public abstract int compareTo(Object dmrpk);
	public abstract boolean equals(Object dmrpk);
	public abstract Object clone() throws CloneNotSupportedException;
	/**
	 * for relate cmpr, we return a value in the range 0-63
	 * in which the values for domain,map range : >,<,=,dont care = 0-3
	 * are encoded as three 0-3 values in the first six bit positions.
	 * a dont care is coded when a dmr value is zero.
	 * @param cmpdmr the Morphism to compare to
	 * @return the 0-63 compare value
	 */
	private short cmpr(Morphism cmpdmr) {
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
	public static short form_template_keyop(Comparable<?>[] tdmr, short[] dret) {
		short dmr_prec[] = {2,1,0};
		if( tdmr[0] != null ) // domain not null
			dmr_prec[0] += 6; // RGuid
		else
			if( dret[1] == 1 ) dmr_prec[0] += 3; // '?'
		// if '*' leave alone, this gets all
		if( tdmr[1] != null ) // map not null
			dmr_prec[1] += 6; // RGuid
		else
			if( dret[2] == 1 ) dmr_prec[1] += 3; // '?'
		//
		if( tdmr[2] != null ) // range not null
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
	 * Assume instance is instanceof Morphism from a previous test. Resolve dbkeys into instances to use downstream
	 * @param instance
	 * @param func
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void resolve(Comparable target, List<Comparable> res) {
		if(!(target instanceof Morphism)) {
			res.add(target);
			return;
		}
		Comparable tdomain, tmap, trange;
		tdomain = (Comparable) ((Morphism)target).getDomain();
		//((DBKey)map).getInstance();
		tmap = (Comparable) ((Morphism)target).getMap();
		//((DBKey)range).getInstance();
		trange = (Comparable) ((Morphism)target).getRange();
		resolve(tdomain, res);
		resolve(tmap, res);
		resolve(trange, res);
		if(DEBUG)
			System.out.printf("Morphism.resolve %s %s %s%n", tdomain, tmap, trange);

	}

	public DBKey store() throws IllegalAccessException, ClassNotFoundException, DuplicateKeyException, IOException {
		if(alias == null)
			return super.store(transactionId);
		else
			return storeAlias(alias,transactionId);
	}
}
