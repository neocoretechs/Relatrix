package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.neocoretechs.relatrix.key.DBKey;
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
public abstract class Morphism implements Comparable, Serializable, Cloneable {
		private static boolean DEBUG = false;
		public static boolean STRICT_SCHEMA = false; // if true, enforce type-based comparison on first element inserted, else can mix types with string basis for incompatible class types
		public static boolean ENFORCE_TYPE_CHECK = true; // if true, enforces type compatibility in relationships, if false, user must supply compareTo that spans all types used. STRICT_SCHEMA ignored
        static final long serialVersionUID = -9129948317265641091L;
        
		protected transient Comparable  domain;       // domain object
        protected transient Comparable  map;          // map object
        protected transient Comparable  range;        // range
        
        protected KeySet keys;
        
        public Morphism() {}
        
        /**
         * Construct and establish key position for the elements of a morphism.
         * @param d
         * @param m
         * @param r
         */
        public Morphism(Comparable d, Comparable m, Comparable r) {
        	this.keys = new KeySet();
        	setDomain(d);
            setMap(m);
            setRange(r);
        }
        
        /**
         * Constructor for the event when we have a keyset from a previous morphism.
         * We assume keyset is valid, and so no need to resolve elements.
         * @param d
         * @param m
         * @param r
         * @param keys The {@link KeySet} of a previous relationship that has the same keys, but perhaps in a different order.
         */
        public Morphism(Comparable d, Comparable m, Comparable r, KeySet keys) {
        	this.keys = keys;
          	this.domain = d;
            this.map = m;
            this.range = r;
        }
        
        /**
         * Construct and establish key position for the elements of a morphism. Do not utilize DBKeys
         * and provide a default empty KeySet. A template is used for retrieval and checking for
         * existence of relationships without creating a permanent entry in the database.
         * @param d
         * @param m
         * @param r
         */
        public Morphism(Comparable d, Comparable m, Comparable r, boolean template) {
        	this.keys = new KeySet();
        	setDomainTemplate(d);
            setMapTemplate(m);
            setRangeTemplate(r);
        }
        
        public KeySet getKeys() { return keys; }
        public void setKeys(KeySet keys) { this.keys = keys; }
        /**
         * If true, enforces type checking for components of relationships. If classes are incompatible,
         * an attempt is made to use a string representation as a default method of providing ordering. If false, STRICT_SCHEMA ignored.
         * If false, User is responsible for providing compareTo in every class used in relationships that is compatible
         * with every other class used in relationships for a given database. Default is true.
         * @param bypass
         */
        public static void enforceTypeCheck(boolean enforce) {
        	ENFORCE_TYPE_CHECK = enforce;
        }
        /**
         * If true, schema if restricted to first relationship type inserted, which means sort
         * ordering and comparison is based on the domain, map, and range elements remaining of consistent types
         * which also restricts using relationships as components of other relationships. Default is false.
         * @param strict
         */
        public static void enforceStrictSchema(boolean strict) {
        	STRICT_SCHEMA = strict;
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
					domain = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getDomainKey());
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
						this.domain = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getDomainKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(domain)) == null)
							keys.setDomainKey(DBKey.newKey(IndexResolver.getIndexInstanceTable(),domain));
						else
							keys.setDomainKey(dbKey);
					}
				}
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void setDomainTemplate(Comparable<?> domain) {
			this.domain = domain;
			keys.setDomainKey(new DBKey());
		}
		
		public Comparable getMap() {
			try {
				if(map != null) 
					return map;
				if(keys.getMapKey().isValid()) {
					map = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getMapKey());
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
						this.map = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getMapKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(map)) == null)
							keys.setMapKey(DBKey.newKey(IndexResolver.getIndexInstanceTable(),map));
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
					range = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getRangeKey());
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
						this.range = (Comparable) IndexResolver.getIndexInstanceTable().getByIndex(keys.getRangeKey());
					} else {
						DBKey dbKey = null;
						if((dbKey = (DBKey)IndexResolver.getIndexInstanceTable().getByInstance(range)) == null)
							keys.setRangeKey(DBKey.newKey(IndexResolver.getIndexInstanceTable(),range));
						else
							keys.setRangeKey(dbKey);						
					}
				}
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void setRangeTemplate(Comparable<?> range) {
			this.range = range;
			keys.setRangeKey(new DBKey());
		}
		       
        /**
         * Failsafe compareTo.
         * If classes are not the same and the target is not assignable from the source, that is, not a subclass, toss an error
         * If none of the above conditions apply, the default is to perform a straight up 'compareTo' as we all implement Comparable.
         * 
         * @param from DBKey wrapping instance If null, this element 'less than' to element, for template use
         * @param to DBKey wrapping instance If null, throw an error, as should never be null
         * @return result of compareTo unless classes differ, then result of string-based compareTo as we need some form of default comparison
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public static int fullCompareTo(Comparable from, Comparable to) {
        	if( to == null )
        		throw new RuntimeException("Morphism.fullCompareTo 'to' element is null, from is "+from);
        	if(from == null)
        		return -1;
         	// now see if the classes are compatible for comparison, if not, convert them to strings and compare
        	// the string representations as a failsafe.
        	if(ENFORCE_TYPE_CHECK) {
        		Class toClass = to.getClass();
        		if( !from.getClass().equals(toClass) && !toClass.isAssignableFrom(from.getClass())) {
        			if(STRICT_SCHEMA)
        				throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
        						" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
        			else
        				//compare a universal string representation as a unifying datatype for typed class templates
        				return from.toString().compareTo(to.toString());
        		}
        	}
        	// Otherwise, use the standard compareTo for all objects which invokes our indicies
        	// DBKey comapreTo handles resolution of key to instance
        	return from.compareTo(to);
        }
        /**
         * This mechanism needs refinement to allow incompatible classes in relationships to work under
         * comparison. ultimately the user may need schema-specific implementations.
         * This implementation merely compares the string representation.
         * @param from
         * @param to
         * @return
         */
        public static int partialCompareTo(Comparable from, Comparable to) {
    		if( to == null )
    			throw new RuntimeException("Morphism.partialCompareTo 'to' element is null, from is "+from);
    		if(from == null)
    			return -1;
    		// the string representations as a failsafe
    		//compare a universal string representation as a unifying datatype for typed class templates
    		return from.toString().compareTo(to.toString());
        }
        /**
         * we check whether the enclosed class equals the target class.
         * If classes are not the same and the target is not assignable from the source, throw runtime exception
         * If none of the above conditions apply, perform a straight up 'equals'
         * @param from
         * @param to
         * @return
         */
        public static boolean fullEquals(Comparable<?> from, Comparable<?> to) {
        	if( DEBUG )
        		System.out.println("fullEquals equals:"+from+" "+to.getClass()+":"+to);
           	Class<?> toClass = to.getClass();
        	// If classes are not the same try a comparison of the string representations as a unifying type
        	if( !from.getClass().equals(toClass) && !toClass.isAssignableFrom(from.getClass()) ) {
      			if(STRICT_SCHEMA)
    				throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
    						" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
    			else
    				//compare a universal string representation as a unifying datatype for typed class templates
    				return from.toString().equals(to.toString());
        	}
        	return from.equals(to);
        }
        
        public static boolean partialEquals(Comparable from, Comparable to) {
     		if( to == null )
     			throw new RuntimeException("Morphism.partialEquals 'to' element is null, from is "+from);
     		if(from == null)
     			return false;
     		// the string representations as a failsafe
     		//compare a universal string representation as a unifying datatype for typed class templates
     		return from.toString().equals(to.toString());
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

}
