package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexInstanceTableInterface;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.PrimaryKeySet;

/**
 * The set of tests verifies the lower level {@link com.neocoretechs.relatrix.Morphism} functions in the {@link  Relatrix}
 * and {@link PrimaryKeySet} functionality.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017
 *
 */
public class BatteryMorphism {
	public static boolean DEBUG = true;
	static KeySet keyset;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static IndexInstanceTableInterface indexTable = new IndexInstanceTable();
	static ArrayList<DomainMapRange> keys = new ArrayList<DomainMapRange>();
	//static ArrayList<KeySet> values = new ArrayList<KeySet>();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.BatteryMorphism <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixKV.setTablespace(argv[0]);
		battery1AR17(argv);		
		battery1(argv);
		battery1AR7(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR17(argv);
		 System.out.println("BatteryMorphism TEST BATTERY COMPLETE.");
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * Ensure that we start with known baseline number of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		long timx = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;

		String d = null;
		String m = null;
		String r = null;

		//Integer payload = 0;
		for(int i = min; i < max; i++) {
			d = String.format(uniqKeyFmt, i);
			m = String.format(uniqKeyFmt, i+1);
			r = String.format(uniqKeyFmt, i+2);
			DomainMapRange identity = new DomainMapRange();
			identity.setDomainKey(DBKey.newKey(indexTable, d));
			identity.setMapKey(DBKey.newKey(indexTable, m));
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			try {
				DBKey dbkey = identity.store();
				identity.setRangeKey(DBKey.newKey(indexTable,r)); // form it as template for duplicate key search
				// re-create it, now that we know its valid, in a form that stores the components with DBKeys
				// and maintains the classes stores in IndexInstanceTable for future commit.
				identity.setDBKey(dbkey);
				IndexResolver.getIndexInstanceTable().put(dbkey, identity);
				if( DEBUG  ) {
					if((System.currentTimeMillis()-timx) >= 1000) {
						System.out.println("Relatrix.store stored :"+identity);
						timx = System.currentTimeMillis();
					}	
				}
				++recs;
			} catch(DuplicateKeyException dkey) {

			}
		}
	
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}

	public static void battery1AR7(String[] argv) throws Exception {
		long timx = System.currentTimeMillis();
		System.out.println("BATTERY1AR7 locate stored elements");
		for(int i = min; i < max; i++) {
			String d = String.format(uniqKeyFmt, i);
			String m = String.format(uniqKeyFmt, i+1);
			String r = String.format(uniqKeyFmt, i+2);
			DomainMapRange identity = new DomainMapRange(d,m,r); // form it as template for duplicate key search

			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			if(Relatrix.get(identity) == null) {
				throw new Exception("Failed to find existing key "+identity);
			}
			keys.add(identity);
			if( DEBUG  ) {
				if((System.currentTimeMillis()-timx) >= 1000) {
					System.out.println("locating :"+identity+" record count:"+i);
					timx = System.currentTimeMillis();
				}	
			}
		}
		System.out.println("BATTERY1AR7 SUCCESS");
	}
	/**
	 * 
	 * Testing of first(), and firstValue
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKV.firstKey(DomainMapRange.class); // first key
		System.out.println("Battery1AR9 firstKey");
		if(!keys.contains(k)) {
			System.out.println("BATTERY1A9 cant find contains key "+i);
			throw new Exception("BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		System.out.println(k);
		System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKV.lastKey(DomainMapRange.class); // key
		System.out.println("Battery1AR10 lastKey");
		if(!keys.contains(k)) {
			System.out.println("BATTERY1AR10 cant find last key "+i);
			throw new Exception("BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println(k);
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixKV.size(DomainMapRange.class);
		System.out.println("Battery1AR101 Size="+bits);
		if( bits != keys.size() ) {
			System.out.println("BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		long s = Relatrix.size();
		System.out.println("Cleaning DB of "+s+" possible elements.");
		
		Iterator<?> it = RelatrixKV.keySet(String.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			Relatrix.remove((Comparable) fkey); // recursive remove
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("String remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = RelatrixKV.size(DBKey.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKV.entrySet(DBKey.class);
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				//System.out.println(i+"="+nex);
				System.out.println("RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
			System.out.println("RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
