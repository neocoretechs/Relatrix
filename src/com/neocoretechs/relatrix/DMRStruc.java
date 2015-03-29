package com.neocoretechs.relatrix;

import java.io.IOException;
import java.io.Serializable;

import com.neocoretechs.relatrix.forgetfulfunctor.*;

/**
* DMRStruc - domain, map, range structure
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
* sets from categories. Support for the typed lamdba calculus is provided by the template
* class which can be used to retrieve sets based on their class type.
* @author Groff (C) NeoCoreTechs 1997,2014
*/
public abstract class DMRStruc implements Comparable, Serializable {
		private static boolean DEBUG = false;
        static final long serialVersionUID = -9129948317265641091L;
        
		public Comparable  domain;       // domain object
        public Comparable  map;          // map object
        public Comparable  range;        // range
        
        public Comparable getDomain() {
			return domain;
		}
		public void setDomain(Comparable domain) {
			this.domain = domain;
		}
		public Comparable getMap() {
			return map;
		}
		public void setMap(Comparable map) {
			this.map = map;
		}
		public Comparable getRange() {
			return range;
		}
		public void setRange(Comparable range) {
			this.range = range;
		}

        public DMRStruc() {}
        
        public DMRStruc(Comparable d, Comparable m, Comparable r) {
        	domain = d;
            map = m;
            range = r;
        }
        /**
         * Failsafe compareTo since at times different key classes need to reconcile in support of the typed lambda calculus.
         * Also supports retrieval via 'forgetful functor class template' by determining if template is instanceof TemplateClass.  If it is
         * we check whether the enclosed class equals the target class.
         * If classes are not the same and the target is not assignable from the source, try a comparison
         * of the universal string representation of the two classes.
         * If none of the above conditions apply, perform a straight up 'compareTo'
         * @param from
         * @param to
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public static int fullCompareTo(Comparable from, Comparable to) {
        	Class toClass;
          	//System.out.println("fullCompareTo to:"+to.getClass()+":"+to+" from:"+from.getClass()+":"+from);
        	// check forgetful functor, if template is java.lang.Class just see if its assignable
        	if ( to instanceof com.neocoretechs.relatrix.forgetfulfunctor.TemplateClass ) {
        		if( DEBUG )
        			System.out.println("fullCompareTo forgetful functor template "+from.getClass()+":"+from+" to "+to.getClass()+":"+to);
        		if( ((TemplateClass)to).getComparableClass().equals(from.getClass()) ) {
        			if( DEBUG )
        				System.out.println("fullCompareTo template return using "+((TemplateClass)to).getComparableClass());
        			return 0;
        		} else {
        			// now try to compare to our minimum value
        			Comparable minTo = (Comparable)((TemplateClass)to).getMinimumValue();
        			toClass = minTo.getClass();
        			if( DEBUG )
        				System.out.println("fullCompareTo min val return using "+from.getClass()+":"+from+" "+toClass+":"+minTo);
        	     	if( !from.getClass().equals(toClass)  ) {
                		return from.toString().compareTo(minTo.toString());
                	}
                	return from.compareTo(minTo);
        		}
        	}
        	// now see if the classes are compatible for comparison, if not, convert them to strings and compare
        	// in a digital hail mary
        	toClass = to.getClass();
          	boolean toIsSubclass = toClass.isAssignableFrom(from.getClass());
        	if( !from.getClass().equals(toClass) && !toIsSubclass ) {
        		// compare a universal string representation
        		return from.toString().compareTo(to.toString());
        	}
        	return from.compareTo(to);
        }
        /**
         * Failsafe equals since at times different key classes need to reconcile in support of the typed lambda calculus.
         * Also supports retrieval via 'forgetful functor class template' by determining if template is instanceof TemplateClass.  If it is
         * we check whether the enclosed class equals the target class.
         * If classes are not the same and the target is not assignable from the source, try a comparison
         * of the universal string representation of the two classes.
         * If none of the above conditions apply, perform a straight up 'equals'
         * This is an attempt to provide support for the typed lambda calculus
         * @param from
         * @param to
         * @return
         */
        public static boolean fullEquals(Comparable from, Comparable to) {
        	if( DEBUG )
        		System.out.println("fullEquals equals:"+from+" "+to.getClass()+":"+to);
    		// check forgetful functor, if template is instance of class just see if its assignable
    		if ( to instanceof com.neocoretechs.relatrix.forgetfulfunctor.TemplateClass)  {
        		if( ((TemplateClass)to).getComparableClass().equals(from.getClass()) ) {
        			if( DEBUG )
        				System.out.println("fullEquals forgetful functor template returning "+from+" "+to);
        			return true;
        		}
        		return false;
    		}
           	Class toClass = to.getClass();
        	boolean toIsSubclass = toClass.isAssignableFrom(from.getClass());
        	// maybe try a string representation
        	if( !from.getClass().equals(toClass) && !toIsSubclass ) {
        		return from.toString().equals(to.toString());
        	}
        	return from.equals(to);
        }
        
        public String toString() { 
        	return "Class "+this.getClass().getName()+
        			" ID["+(domain == null ? "<DomainTemplate>" : domain.getClass().getName()+":"+domain.toString())
        			+"->"+
        			(map == null ? "<MapTemplate>" : map.getClass().getName()+":"+map.toString()) 
        			+ "->"+
        			(range == null ? "<RangeTemplate>" : range.getClass().getName()+":"+range.toString())+"]"; 
        	}
        /**
         * key combinations for Relatrix follow
         */
        public abstract int compareTo(Object dmrpk);
        public abstract boolean equals(Object dmrpk);
        
        /**
        * for relate cmpr, we return a value in the range 0-63
        * in which the values for domain,map range : >,<,=,dont care = 0-3
        * are encoded as three 0-3 values in the first six bit positions.
        * a dont care is coded when a dmr value is zero.
        * @param cmpdmr the DMRStruc to compare to
        * @return the 0-63 compare value
        */
        private short cmpr(DMRStruc cmpdmr)
        {
        	short cmpres = 0;
            if(domain == null)
        		cmpres = 48;
        	else
        		if( domain.compareTo(cmpdmr.domain) < 0)
        			cmpres = 16;
        		else
        			if(domain.equals(cmpdmr.domain) )
        				cmpres = 32;
                //
            if(map == null)
        		cmpres ^= 12;
        	else
        		if(map.compareTo(cmpdmr.map)  < 0)
        			cmpres ^= 4;
        		else
        			if( map.equals(cmpdmr.map) )
        				cmpres ^= 8;
                //
           if(range == null)
        		cmpres ^= 3;
        	else
        		if( range.compareTo( cmpdmr.range ) < 0)
        			cmpres ^= 1;
        		else
        			if( range.equals(cmpdmr.range) )
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
        public Comparable iterate_dmr(short[] dmr_return) throws IllegalAccessException, IOException
        {
                if(dmr_return[0] >= 3) 
                	return null;
                // no return vals? send back relation location
                if( dmr_return[0] == (-1)  || (dmr_return[1] == 0 && dmr_return[2] == 0 && dmr_return[3] == 0) ) 
                	return this;
                do {
                  dmr_return[0]++;
                  // If the element of the tuple needs returning based on formation of our dmr_return, do so
                  if( dmr_return[dmr_return[0]] != 0)
                	  return returnTupleOrder(dmr_return[0]);
                } while( dmr_return[0] < 3 );
                return null;
        }
        /**
        * form_template_keyop - Passed Comparable arrray is functioning as template for search
        * depending on the values in domain,map,range (0 for ? or *, !0 or object)
        * and the ones we care about returning (boolean true in dret)
        * construct the proper index to key array (keyop) and return it
        * (see form_dmrkey for keyop descr)
        * method: construct a little weighting value for each one based
        * on a base val of position domain=2,map=1,range=0
        * and modified by args to findset value=6,?=3,*=0
        * this establishes a precedent for our return values
        * @param dret the return value flag array with iterator at 0
        * @return the keyop
        */
        public static short form_template_keyop(Comparable[] tdmr, short[] dret)
        {
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
        public Comparable returnTupleOrder(int n) {
        	// default dmr
        	switch(n) {
        		case 1:
        			return domain;
        		case 2:
        			return map;
        		case 3:
        			return range;
        		default:
        			break;
        	}
        	throw new RuntimeException("returnTupleOrder invalid tuple "+n);
        }

}
