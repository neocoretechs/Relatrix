package com.neocoretechs.relatrix.test;

import java.util.Iterator;

import com.neocoretechs.bigsack.BigSackAdapter;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.DomainMapRange;

import com.neocoretechs.relatrix.Relatrix;


/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the battery1 testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The first test battery verifies the lower level functions of the BigSack and the BigSackAdapter that
 * connects the Relatrix to the BigSack K/V store.
 * The next set of tests verifies the higher level 'findSet' functors in the Relatrix, which can be used
 * as examples of Relatrix processing.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * VM argument is props file i.e. -DBigSack.properties="c:/users/you/Relatrix/BigSack.properties"
 * @author jg (C) 2016,2017
 *
 */
public class BatteryRelatrix {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;
	static int numDelete = 100; // for delete test
	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		 //System.out.println("Analysis of all");
		BigSackAdapter.setTableSpaceDir(argv[0]);
		battery1(argv);
		battery11(argv);
		battery1AR6(argv);
		battery1AR7(argv);
		battery1AR8(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(argv);
		battery1AR12(argv);
	
		 System.out.println("TEST BATTERY COMPLETE.");
		
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.transactionalStore(fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		Relatrix.transactionCommit();
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String[] argv) throws Exception {
		System.out.println("Battery11 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.transactionalStore(fkey, "Has unit", new Long(99999));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			System.out.println("BATTERY11 FAIL, stored "+recs+" when zero should have been stored");
			Relatrix.transactionRollback();
		} else {
			System.out.println("BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
			Relatrix.transactionCommit();
		}
	}
	

	/**
	 * Test the higher level functions in the Relatrix. Use the 'findSet' permutations to
	 * verify the previously inserted data
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "?", "?");
		System.out.println("Battery1AR6");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// 3 question marks = dimension 3 in return array
				if( DEBUG ) System.out.println("1AR6:"+i+" "+nex[0]+","+nex[1]+","+nex[2]);
				String skey = key + String.format(uniqKeyFmt, i);
				if(!skey.equals(nex[0]) )
					System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
				if(!nex[1].equals("Has unit"))
					System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex[1]);
				Long unit = new Long(i);
				if(!nex[2].equals(unit))
					System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[2]);
				++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR9 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR9 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "*", "*");
		System.out.println("Battery1AR7");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1AR7:"+i+" "+nex[0]);
			String skey = key + String.format(uniqKeyFmt, i);
			if(!skey.equals(nex[0]) )
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	public static void battery1AR8(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "?", "*");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// two '?' in findset gives use 2 element array, the domain and map
			if( DEBUG ) System.out.println("1AR8:"+i+" "+nex[0]+" "+nex[1]);
			String skey = key + String.format(uniqKeyFmt, i);
			if(!skey.equals(nex[0]) )
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			if(!nex[1].equals("Has unit"))
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex[1]);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR8 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR8 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("*", "*", "*");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// the returned array has 1 element, the identity Morphism DomainMapRange
			if( DEBUG ) System.out.println("1AR9:"+i+" "+nex[0]);
			String skey = key + String.format(uniqKeyFmt, i);
			if(!skey.equals( ((DomainMapRange)nex[0]).domain ) )
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			if(!((DomainMapRange)nex[0]).map.equals("Has unit"))
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex[0]);
			Long unit = new Long(i);
			if(!((DomainMapRange)nex[0]).range.equals(unit))
				System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR9 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR9 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}


	public static void battery1AR10(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, min);
		Iterator<?> its = Relatrix.findSet(fkey, "Has unit", "*");
		// return all identities with the given key for all ranges, should be 1
		while(its.hasNext()) {
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Comparable[] nex = (Comparable[]) its.next();
			if( nex.length != 1)
				throw new Exception("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length);
			if(DEBUG) System.out.println("1AR10:"+i+" "+nex[0]);
			String skey = key + String.format(uniqKeyFmt, i);
			if(!skey.equals( ((DomainMapRange)nex[0]).domain ) )
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			if(!((DomainMapRange)nex[0]).map.equals("Has unit"))
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex[0]);
			Long unit = new Long(i);
			if(!((DomainMapRange)nex[0]).range.equals(unit))
				System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != 1 ) {
			System.out.println("BATTERY1AR10 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR10 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	public static void battery1AR101(String[] argv) throws Exception {
		int i = 0;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, max);
		// key is max, so zero keys should be retrieved since we insert 0 to max-1
		Iterator<?> its = Relatrix.findSet(fkey, "Has unit", new Long(max));
		while(its.hasNext()) {
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Comparable[] nex = (Comparable[]) its.next();
			if( nex.length != 1)
				throw new Exception("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length);
			if(DEBUG) System.out.println("1AR101:"+i+" "+nex[0]);
			String skey = key + String.format(uniqKeyFmt, i);
			if(!skey.equals( ((DomainMapRange)nex[0]).domain ) )
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			if(!((DomainMapRange)nex[0]).map.equals("Has unit"))
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex[0]);
			Long unit = new Long(i);
			if(!((DomainMapRange)nex[0]).range.equals(unit))
				System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != 0 ) {
			System.out.println("BATTERY1AR101 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR101 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * negative assertion of above
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		// forgetful functor test
		Iterator<?> its = Relatrix.findSet(fkey, "Has time", "*");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			if( DEBUG ) System.out.println("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex[0]);
			throw new Exception("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex[0]);
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of remove
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.remove(fkey);
		try {
			Iterator<?> its = Relatrix.findSet(fkey, "*", "*");
			if(its.hasNext()) {
				throw new Exception("BATTERY1AR12-1 failed to delete key "+fkey);
			}
		} catch(RuntimeException re) {} // if types differ in domain from fkey
		try {
			Iterator<?> its = Relatrix.findSet("*", fkey, "*");
			if(its.hasNext()) {
				throw new Exception("BATTERY1AR12-2 failed to delete key "+fkey);
			}
		} catch(RuntimeException re) {} // if types differ in domain from fkey
		try {
			Iterator<?> its = Relatrix.findSet("*", "*", fkey);
			if(its.hasNext()) {
				throw new Exception("BATTERY1AR12-3 failed to delete key "+fkey);
			}
		} catch(RuntimeException re) {} // if types differ in domain from fkey
	
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
