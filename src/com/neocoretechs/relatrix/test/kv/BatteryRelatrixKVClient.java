package com.neocoretechs.relatrix.test.kv;

import java.util.Map;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteEntrysetIterator;
import com.neocoretechs.relatrix.client.RemoteHeadmapIterator;
import com.neocoretechs.relatrix.client.RemoteHeadmapKVIterator;
import com.neocoretechs.relatrix.client.RemoteKeysetIterator;
import com.neocoretechs.relatrix.client.RemoteSubmapIterator;
import com.neocoretechs.relatrix.client.RemoteSubmapKVIterator;
import com.neocoretechs.relatrix.client.RemoteTailmapIterator;
import com.neocoretechs.relatrix.client.RemoteTailmapKVIterator;

/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The set of tests verifies the higher level 'transactionalStore' and 'findSet' functors in the Relatrix, which can be used
 * as examples of Relatrix processing.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * VM argument is props file i.e. -DBigSack.properties="c:/users/you/Relatrix/BigSack.properties"
 * @author jg (C) 2020
 *
 */
public class BatteryRelatrixKVClient {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		rkvc = new RelatrixKVClient("volvatron", "volvatron", 9500);
		/*battery1(argv);	
		battery11(argv);
		battery1AR6(argv);
		battery1AR7(argv);*/
		battery1AR8(argv);
		/*battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(argv);
		battery1AR12(argv);
		battery1AR13(argv);
		battery1AR14(argv);
		battery1AR15(argv);
		battery1AR16(argv);
		battery1AR17(argv);*/
		System.out.println("TEST BATTERY COMPLETE.");
		rkvc.close();
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.transactionCommit(String.class);
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String[] argv) throws Exception {
		System.out.println("KV Battery11 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(fkey, new Long(fkey));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			System.out.println("KV BATTERY11 FAIL, stored "+recs+" when zero should have been stored");
			rkvc.transactionRollback(String.class);
		} else {
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
			rkvc.transactionCommit(String.class);
		}
	}
	
	/**
	 * Test the higher level functions in the RelatrixKV.
	 * public Set<Map.Entry<K,V>> entrySet()
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's iterator returns the entries in ascending key order. 
	 * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
	 *  If the map is modified while an iteration over the set is in progress (except through the iterator's 
	 *  own remove operation, or through the setValue operation on a map entry returned by the iterator) the results
	 *   of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, 
	 *   via the Iterator.remove, Set.remove, removeAll, retainAll and clear operations. 
	 *   It does not support the add or addAll operations.
	 *   from battery1 we should have 0 to max, say 1000 keys of length 100
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteEntrysetIterator its = rkvc.entrySet(String.class);
		System.out.println("KV Battery1AR6");
		while(rkvc.hasNext(its)) {
			Comparable nex = (Comparable) rkvc.next(its);
			//System.out.println(i+"="+nex);
			if(((Long)nex).intValue() != i)
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+nex);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			//throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteKeysetIterator its = rkvc.keySet(String.class);
		System.out.println("KV Battery1AR7");
		while(rkvc.hasNext(its)) {
			String nex = (String) rkvc.next(its);
			// Map.Entry
			if(Integer.parseInt(nex) != i)
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			++i;
		}
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			//throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("KV BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = Relatrix.findSet("?", "?", "*");
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv) throws Exception {
		int i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(fkey);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = rkvc.containsValue(String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 unexpected cant find contains value "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max; j > min; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 unexpected cant find contains value "+j);
					//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
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
		Object k = rkvc.firstKey(String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt((String)k) != i ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			//throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			//throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong((String) k) != (long)i ) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			//throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			//throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = rkvc.size(String.class);
		System.out.println("KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			//throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailmapIterator its = rkvc.findTailMap(fkey);
		System.out.println("KV Battery1AR11");
		while(rkvc.hasNext(its)) {
			String nex = (String) rkvc.next(its);
			// Map.Entry
			if(Integer.parseInt(nex) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailmapKVIterator its = rkvc.findTailMapKV(fkey);
		System.out.println("KV Battery1AR12");
		while(rkvc.hasNext(its)) {
			Comparable nex = (Comparable) rkvc.next(its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR13(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadmapIterator its = rkvc.findHeadMap(fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		while(rkvc.hasNext(its)) {
			String nex = (String) rkvc.next(its);
			if(Integer.parseInt(nex) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
				//throw new Exception("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 *  findHeadMapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadmapKVIterator its = rkvc.findHeadMapKV(fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		while(rkvc.hasNext(its)) {
			Comparable nex = (Comparable) rkvc.next(its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR15(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubmapIterator its = rkvc.findSubMap(fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		while(rkvc.hasNext(its)) {
			String nex = (String) rkvc.next(its);
			if(Integer.parseInt(nex) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
				//throw new Exception("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR16(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubmapKVIterator its = rkvc.findSubMapKV(fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		while(rkvc.hasNext(its)) {
			Comparable nex = (Comparable) rkvc.next(its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
				//throw new Exception("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		//int i = min;
		//int j = max;

		// with j at max, should get them all since we stored to max -1
		//String tkey = String.format(uniqKeyFmt, j);
		System.out.println("KV Battery1AR17");
		// with i at max, should catch them all
		for(int i = min; i < max; i++) {
			String fkey = String.format(uniqKeyFmt, i);
			rkvc.remove(fkey);
			// Map.Entry
			if(rkvc.contains(fkey)) { 
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+i);
				//throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+i);
			}
		}
		rkvc.transactionCommit(String.class);
		long siz = rkvc.size(String.class);
		if(siz > 0) {
			RemoteEntrysetIterator its = rkvc.entrySet(String.class);
			while(rkvc.hasNext(its)) {
				Comparable nex = (Comparable) rkvc.next(its);
				//System.out.println(i+"="+nex);
				System.out.println(nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			//throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");

	}
	
}
