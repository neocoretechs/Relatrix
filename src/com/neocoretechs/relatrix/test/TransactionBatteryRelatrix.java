package com.neocoretechs.relatrix.test;


import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DuplicateKeyException;

import com.neocoretechs.relatrix.Relatrix;

/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the battery1 testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This module tests the transactional Relatrix methods. Storage and retrieval in a transaction context.
 * NOTES:
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * VM argument is props file i.e. -DBigSack.properties="c:/users/you/Relatrix/BigSack.properties"
 * A database unique to this test module should be used.
 * @author jg C 2017
 *
 */
public class TransactionBatteryRelatrix {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 2000;

	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		 //System.out.println("Analysis of all");
		BigSackAdapter.setTableSpaceDir(argv[0]);
		battery1(argv);
		battery2(argv);
		System.out.println("TEST BATTERY COMPLETE.");	
	}
	/**
	 * Loads up on keys, we have same domain with multiple maps to test unique keys in multiple domains with same map
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = key + String.format(uniqKeyFmt, 99999);
		for(int i = min; i < max; i++) {
			fmap = "Has unit " + i;
			try {
				Relatrix.transactionalStore(fkey, fmap, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		Relatrix.transactionCommit();
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	public static void battery2(String[] argv) throws Exception {
		System.out.println("Battery2 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = key + String.format(uniqKeyFmt, 99999);
		for(int i = min; i < max; i++) {
			fmap = "Has unit " + i;
			try {
				Relatrix.transactionalStore(fkey, fmap, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		Relatrix.transactionCommit();
		// These should all have failed with primary key rejection
		if( recs > 0)
			System.out.println("BATTERY2 FAIL in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
		else
			System.out.println("BATTERY2 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
}
