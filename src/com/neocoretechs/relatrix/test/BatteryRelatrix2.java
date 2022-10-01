package com.neocoretechs.relatrix.test;

import java.util.Iterator;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.rocksack.session.RockSackAdapter;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Relatrix;


/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the battery testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The set of tests verifies the higher level 'findSubSet' and 'findHeadSet' functors in the Relatrix, which can be used
 * as examples of Relatrix processing.
 * NOTES:
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author jg (C) 2016,2017
 *
 */
public class BatteryRelatrix2 {
	public static boolean DEBUG = true;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;

	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		RockSackAdapter.setTableSpaceDir(argv[0]);
		battery1(argv);
		battery1A(argv);
		battery1B(argv);
		battery1C(argv);
		battery1D(argv);
		battery1E(argv);
		battery1F(argv);
	
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
	 * Test the higher level functions in the Relatrix. Use the 'findSet' permutations to
	 * verify the previously inserted data. This case tests that a check is performed for a concrete object for each
	 * retrieval type; findTailSet, findHeadSet, findSubSet
	 * These retrievals must be anchored with at least one concrete object.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1A(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		try {
			Iterator<?> its = Relatrix.findHeadSet("?", "?", "?");
			System.out.println("Battery1A FAIL on findHeadSet, should throw IllegalArgumentException");
			throw new Exception("Battery1A FAIL on findHeadSet, should throw IllegalArgumentException");
		} catch(Exception e) {
			if( !(e instanceof IllegalArgumentException)) throw(e);
		}
		try {
			Iterator<?> its = Relatrix.findTailSet("?", "?", "?");
			System.out.println("Battery1A FAIL on findTailSet, should throw IllegalArgumentException");
			throw new Exception("Battery1A FAIL on findTailSet, should throw IllegalArgumentException");
		} catch(Exception e) {
			if( !(e instanceof IllegalArgumentException)) throw(e);
		}
		try {
			String skey = key + String.format(uniqKeyFmt, 0);
			Iterator<?> its = Relatrix.findSubSet("?", "?", "?", skey);
			System.out.println("Battery1A FAIL on findSubSet, should throw IllegalArgumentException");
			throw new Exception("Battery1A FAIL on findsubSet, should throw IllegalArgumentException");
		} catch(Exception e) {
			if( !(e instanceof IllegalArgumentException)) throw(e);
		}
		 System.out.println("BATTERY1A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findHeadSet
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1B(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findHeadSet(skey, "*", "*");
		System.out.println("Battery1B");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1B:"+i+" "+nex[0]);
			if(skey.compareTo((String)(((Morphism)nex[0]).getDomain())) < 0 ) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			}
			++j;
		}
		if( j != i)
		 throw new Exception("BATTERY1B number of keys mismatched:"+j+" should be "+(i));
		 System.out.println("BATTERY1B SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findHeadSet
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * return range element for verification
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1C(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findHeadSet(skey, "*", "?");
		System.out.println("Battery1C");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1C:"+i+" "+j+" "+nex[0]);
			if(j != ((Long)nex[0]) ) {
				System.out.println("RANGE KEY MISMATCH:"+(i+j)+" "+skey+" - "+nex[0]);
				throw new Exception("RANGE KEY MISMATCH:"+(i+j)+" "+skey+" - "+nex[0]);
			}
			++j;
		}
		if( j != i)
		 throw new Exception("BATTERY1C number of keys mismatched:"+j+" should be "+(i));
		 System.out.println("BATTERY1C SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * Check for empty set with same keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1D(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findSubSet(skey, "*", "*", skey);
		System.out.println("Battery1D");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1D:"+i+" "+nex[0]);
			System.out.println("SUBSET MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			//throw new Exception("SUBSET MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			++j;
		}
		if( j != 0)
		 throw new Exception("BATTERY1D number of keys mismatched:"+j+" should be "+(i-1));
		System.out.println("BATTERY1D SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1E(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		String skey2 = key + String.format(uniqKeyFmt, i+10); // if 500 and 510 we should get 500 to 509
		Iterator<?> its = Relatrix.findSubSet(skey, "*", "?", skey2);
		System.out.println("Battery1E");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1E:"+i+" "+nex[0]);
			if(i+j != ((Long)nex[0]) ) {
				System.out.println("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex[0]);
				throw new Exception("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex[0]);
			}
			++j;
		}
		if( j != 10)
		 throw new Exception("BATTERY1E number of keys mismatched:"+j+" should be 10");
		 System.out.println("BATTERY1E SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1F(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		String skey2 = key + String.format(uniqKeyFmt, i+10); // if 500 and 510 we should get 500 to 509
		Iterator<?> its = Relatrix.findSubSet(skey, "Has unit", "?", skey2, "Has unit");
		System.out.println("Battery1F");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1F:"+i+" "+nex[0]);
			if(i+j != ((Long)nex[0]) ) {
				System.out.println("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex[0]);
				throw new Exception("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex[0]);
			}
			++j;
		}
		if( j != 10)
		 throw new Exception("BATTERY1F number of keys mismatched:"+j+" should be 10");
		 System.out.println("BATTERY1F SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
}
