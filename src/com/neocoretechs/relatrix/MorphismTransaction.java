package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
public abstract class MorphismTransaction extends Morphism {
		private static boolean DEBUG = false;
        static final long serialVersionUID = -9129948317265641092L;
        
        public MorphismTransaction() {}
        
        /**
         * Construct and establish key position for the elements of a morphism.
         * @param d
         * @param m
         * @param r
         */
        public MorphismTransaction(Comparable d, Comparable m, Comparable r) {
        	super(d,m,r);
        }
        
        /**
         * Constructor for the event when we have a keyset from a previous morphism.
         * We assume keyset is valid, and so no need to resolve elements.
         * @param d
         * @param m
         * @param r
         * @param keys The {@link KeySet} of a previous relationship that has the same keys, but perhaps in a different order.
         */
        public MorphismTransaction(Comparable d, Comparable m, Comparable r, KeySet keys) {
        	super(d,m,r,keys);
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
        	super(d,m,r,template);
        }
        
        /**
         * Transparently process DBKey, returning actual instance
         * @return The real Comparable instance, pointed to by DBKey
        */ 
        public Comparable getDomain() {
			try {
				if(domain != null)
					return domain;
				if(keys.getDomainKey().isValid()) {
					domain = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getDomainKey());
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
					keys.setDomainKey(new DBKey());
				} else {
					if(keys.getDomainKey().isValid()) {
						this.domain = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getDomainKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getCurrentIndexInstanceTable().getByInstance(domain)) == null)
							keys.setDomainKey(DBKey.newKey(IndexResolver.getCurrentIndexInstanceTable(),domain));
						else
							keys.setDomainKey(dbKey);
					}
				}
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		public Comparable getMap() {
			try {
				if(map != null) 
					return map;
				if(keys.getMapKey().isValid()) {
					map = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getMapKey());
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
					keys.setMapKey(new DBKey());
				} else {
					if(keys.getMapKey().isValid()) {
						this.map = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getMapKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getCurrentIndexInstanceTable().getByInstance(map)) == null)
							keys.setMapKey(DBKey.newKey(IndexResolver.getCurrentIndexInstanceTable(),map));
						else
							keys.setMapKey(dbKey);
					}
				}
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void setMapTemplate(Comparable<?> map) {
			this.map = map;
			keys.setMapKey(new DBKey());
		}

		public Comparable getRange() {
			try {
				if(range != null)
					return range;
				if(keys.getRangeKey().isValid()) {
					range = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getRangeKey());
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
					keys.setRangeKey(new DBKey());
				} else {
					if(keys.getRangeKey().isValid()) {
						this.range = (Comparable) IndexResolver.getCurrentIndexInstanceTable().getByIndex(keys.getRangeKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getCurrentIndexInstanceTable().getByInstance(range)) == null)
							keys.setRangeKey(DBKey.newKey(IndexResolver.getCurrentIndexInstanceTable(),range));
						else
							keys.setRangeKey(dbKey);						
					}
				}
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
}
