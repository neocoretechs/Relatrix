package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
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
        static final long serialVersionUID = -9129948317265641091L;
        
		private transient Comparable  domain;       // domain object
        private transient Comparable  map;          // map object
        private transient Comparable  range;        // range
        
        private KeySet keys;
        
        public KeySet getKeys() { return keys; }
        public void setKeys(KeySet keys) { this.keys = keys; }
        /**
         * Transparently process DBKey, returning actual instance
         * @return The real Comparable instance, pointed to by DBKey
         */
        public Comparable getDomain() {
			try {
				Comparable tdomain = null;
				if(domain != null)
					return domain;
				if(keys.getDomainKey().isValid()) {
					domain = (Comparable) IndexInstanceTable.getByIndex(keys.getDomainKey());
				}
				return domain;
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		public void setDomain(Comparable<?> domain) {
			try {
				this.domain = domain;
				if(domain == null)
					keys.setDomainKey( new DBKey());
				else
					keys.setDomainKey(DBKey.newKey(domain));
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		public Comparable getMap() {
			try {
				Comparable tmap = null;
				if(map != null) 
					return map;
				if(keys.getMapKey().isValid()) {
					map = (Comparable) IndexInstanceTable.getByIndex(keys.getMapKey());
				}
				return map;
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		public void setMap(Comparable<?> map) {
			try {
				this.map = map;
				if(map == null)
					keys.setMapKey(new DBKey());
				else
					keys.setMapKey(DBKey.newKey(map));
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		public Comparable getRange() {
			try {
				if(range != null)
					return range;
				if(keys.getRangeKey().isValid()) {
					range = (Comparable) IndexInstanceTable.getByIndex(keys.getRangeKey());
				}
				return range;
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		public void setRange(Comparable<?> range) {
			try {
				this.range = range;
				if(range == null) 
					keys.setRangeKey(new DBKey());
				else
					keys.setRangeKey(DBKey.newKey(range));
			} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}

        public Morphism() {}
        
        /**
         * Construct and establish key position for the elements of a morphism.
         * @param d
         * @param m
         * @param r
         */
        public Morphism(Comparable d, Comparable m, Comparable r) {
        	keys = new KeySet();
        	setDomain(d);
            setMap(m);
            setRange(r);
        }
        
        /**
         * Constructor for the event when we have a keyset from a previous morphism.
         * @param d
         * @param m
         * @param r
         * @param keys
         */
        public Morphism(Comparable d, Comparable m, Comparable r, KeySet keys) {
        	this.domain = d;
            this.map = m;
            this.range = r;
            this.keys = keys;
        }
        /**
         * Failsafe compareTo.
         * If classes are not the same and the target is not assignable from the source, that is , not a subclass, toss an error
         * If none of the above conditions apply, the default is to perform a straight up 'compareTo' as we all implement Comparable
         * @param from DBKey wrapping instance
         * @param to DBKey wrapping instance
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public static int fullCompareTo(Comparable from, Comparable to) {
        	if( DEBUG ) {
        		if( from == null )
        			throw new RuntimeException("Morphism.fullCompareTo 'from' element is null, to is "+to);
        		if( to == null )
        			throw new RuntimeException("Morphism.fullCompareTo 'to' element is null, from is "+from);
        	}
         	// now see if the classes are compatible for comparison, if not, convert them to strings and compare
        	// the string representations as a failsafe.	
        	Class toClass = to.getClass();
        	if( !from.getClass().equals(toClass) && !toClass.isAssignableFrom(from.getClass())) {
        		//compare a universal string representation as a unifying datatype for typed class templates
        		//return from.toString().compareTo(to.toString());
        		throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
        				" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
        	}
        	// Otherwise, use the standard compareTo for all objects which invokes our indicies
        	// DBKey comapreTo handles resolution of key to instance
        	return from.compareTo(to);
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
        		//return from.toString().equals(to.toString());
          		throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
        				" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
        	}
        	return from.equals(to);
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
        
        
        private void resolve(Comparable instance, DBKey func) throws IllegalAccessException, ClassNotFoundException, IOException {
        	if(!instance.getClass().isAssignableFrom(Morphism.class))
        		return;
         	Comparable tdomain, tmap, trange;
          	tdomain = ((Morphism)instance).getDomain();
        	//((DBKey)map).getInstance();
        	tmap = ((Morphism)instance).getMap();
        	//((DBKey)range).getInstance();
        	trange = ((Morphism)instance).getRange();
        	if(DEBUG)
        		System.out.printf("%s.resolve %s %s %s%n", this.getClass().getName(), domain, map, range);
  
        		// resolve domain-level relationships derived from original relationship in this morphism we just deserialized
				while(tdomain != null && tdomain.getClass().isAssignableFrom(Morphism.class)) {
					tdomain = (Comparable) ((DBKey)((Morphism)tdomain).domain);
					if( ((Morphism)tdomain).map != null && ((DBKey)((Morphism)tdomain).map).getClass().isAssignableFrom(Morphism.class)) {
						Comparable txmap = (Comparable) ((DBKey)(((Morphism)tdomain).map));
						if( ((Morphism)txmap).range != null && ((DBKey)((Morphism)txmap).range).getClass().isAssignableFrom(Morphism.class)) {
							Comparable txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							while(txrange != null && ((DBKey)txrange).getClass().isAssignableFrom(Morphism.class)) {
								txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							}
						}
					}
				}
				// resolve map-level relationships descended from original relationship
				while(tmap != null && ((DBKey)tmap).getClass().isAssignableFrom(Morphism.class)) {
					tdomain = (Comparable) ((DBKey)((Morphism)tmap).domain);
					if( ((Morphism)tmap).map != null && ((DBKey)((Morphism)tmap).map).getClass().isAssignableFrom(Morphism.class)) {
						Comparable txmap = (Comparable) ((DBKey)(((Morphism)tmap).map));
						if( ((Morphism)txmap).range != null && ((DBKey)((Morphism)txmap).range).getClass().isAssignableFrom(Morphism.class)) {
							Comparable txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							while(txrange != null && ((DBKey)txrange).getClass().isAssignableFrom(Morphism.class)) {
								txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							}
						}
					}
				}
				// resolve range-level relationships descended from original relationship
				while(trange != null && ((DBKey)trange).getClass().isAssignableFrom(Morphism.class)) {
					tdomain = (Comparable) ((DBKey)((Morphism)trange).domain);
					if( ((Morphism)trange).map != null && ((DBKey)((Morphism)trange).map).getClass().isAssignableFrom(Morphism.class)) {
						Comparable txmap = (Comparable) ((DBKey)(((Morphism)trange).map));
						if( ((Morphism)txmap).range != null && ((DBKey)((Morphism)txmap).range).getClass().isAssignableFrom(Morphism.class)) {
							Comparable txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							while(txrange != null && ((DBKey)txrange).getClass().isAssignableFrom(Morphism.class)) {
								txrange = (Comparable) ((DBKey)(((Morphism)txmap).range));
							}
						}
					}
				}
        }

}
