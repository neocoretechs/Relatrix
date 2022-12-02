package com.neocoretechs.relatrix.test.kv;

import java.util.Map;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.client.RelatrixClientInterface;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.RemoteEntrySetIterator;
import com.neocoretechs.relatrix.client.RemoteEntrySetIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteHeadMapIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteKeySetIterator;
import com.neocoretechs.relatrix.client.RemoteKeySetIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteSubMapIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteSubMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteTailMapIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteTailMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapKVIteratorTransaction;

/**
 * Transaction KV client test battery. Test of client side transaction KV server.
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
 * program argument is node server is running on, which was started on port designated by command line arg 2, with database of your choice
 * @author jg (C) 2022
 *
 */
public class BatteryRelatrixKVClientTransaction {
	public static boolean DEBUG = false;
	public static RelatrixKVClientTransaction rkvc;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	private static int dupes;
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 2) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientTransaction <DB NODE> <DB PORT>");
			System.exit(1);
		}
		rkvc = new RelatrixKVClientTransaction(argv[0], argv[0], Integer.parseInt(argv[1]));
		String xid = rkvc.getTransactionId();
		battery1(xid);	
		battery11(xid);
		battery1AR6(xid);
		battery1AR7(xid);
		battery1AR8(xid);
		battery1AR9(xid);
		battery1AR10(xid);
		battery1AR101(xid);
		battery1AR11(xid);
		battery1AR12(xid);
		battery1AR13(xid);
		battery1AR14(xid);
		battery1AR15(xid);
		battery1AR16(xid);
		battery1AR17(xid);
		battery18(xid);
		System.out.println("BatteryRelatrixKVClientTransaction TEST BATTERY COMPLETE.");
		rkvc.endTransaction(xid);
		rkvc.close();
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String xid) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) rkvc.size(xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			batteryCleanDB(xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(xid, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.transactionCommit(xid);
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Store another transaction then roll it back.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String xid) throws Exception {
		System.out.println("KV Battery11 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		String xid2 = rkvc.getTransactionId();
		for(int i = max; i < max*2; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(xid2, fkey, new Long(fkey));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			rkvc.transactionRollback(xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
		rkvc.endTransaction(xid2);
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
	public static void battery1AR6(String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteEntrySetIteratorTransaction its = rkvc.entrySet(xid, String.class);
		System.out.println("KV Battery1AR6 "+its);
		while(rkvc.hasNext(xid, its)) {
			Object nex =  rkvc.next(xid, its);
			Entry enex = (Entry)nex;
			//System.out.println(i+"="+nex);
			if(((Long)enex.getValue()).intValue() != i)
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+nex);
			else
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
	public static void battery1AR7(String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteKeySetIteratorTransaction its = rkvc.keySet(xid, String.class);
		System.out.println("KV Battery1AR7");
		while(rkvc.hasNext(xid, its)) {
			String nex = (String) rkvc.next(xid, its);
			// Map.Entry
			if(Integer.parseInt(nex) != i)
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			else
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
	public static void battery1AR8(String xid) throws Exception {
		int i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(xid, fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(xid, fkey);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms." );
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = rkvc.containsValue(xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max; j > max-numLookupByValue ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(xid, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of first(), and firstValue
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(xid, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt((String)k) != i ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			//throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(xid, String.class);
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
	public static void battery1AR10(String xid) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(xid, String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong((String) k) != (long)i ) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			//throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(xid, String.class);
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
	public static void battery1AR101(String xid) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = rkvc.size(xid, String.class);
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
	public static void battery1AR11(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailMapIteratorTransaction its = rkvc.findTailMap(xid, fkey);
		System.out.println("KV Battery1AR11");
		while(rkvc.hasNext(xid, its)) {
			String nex = (String) rkvc.next(xid, its);
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
	public static void battery1AR12(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailMapKVIteratorTransaction its = rkvc.findTailMapKV(xid, fkey);
		System.out.println("KV Battery1AR12");
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
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
	public static void battery1AR13(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapIteratorTransaction its = rkvc.findHeadMap(xid, fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		while(rkvc.hasNext(xid, its)) {
			String nex = (String) rkvc.next(xid, its);
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
	public static void battery1AR14(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapKVIteratorTransaction its = rkvc.findHeadMapKV(xid, fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
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
	public static void battery1AR15(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubMapIteratorTransaction its = rkvc.findSubMap(xid, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		while(rkvc.hasNext(xid, its)) {
			String nex = (String) rkvc.next(xid, its);
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
	public static void battery1AR16(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubMapKVIteratorTransaction its = rkvc.findSubMapKV(xid, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
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
	public static void battery1AR17(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		//int i = min;
		//int j = max;
		String xid2 = rkvc.getTransactionId();
		// with j at max, should get them all since we stored to max -1
		//String tkey = String.format(uniqKeyFmt, j);
		System.out.println("KV Battery1AR17");
		// with i at max, should catch them all
		for(int i = min; i < max; i++) {
			String fkey = String.format(uniqKeyFmt, i);
			System.out.println("Removing"+fkey);
			rkvc.remove(xid2, fkey);
			// Map.Entry
			if(rkvc.contains(xid2, fkey)) { 
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+i);
				//throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+i);
			}
		}
		rkvc.transactionCommit(xid2, String.class);
		long siz = rkvc.size(xid2, String.class);
		if(siz > 0) {
			RemoteEntrySetIteratorTransaction its = rkvc.entrySet(xid2, String.class);
			while(rkvc.hasNext(xid2, its)) {
				Object nex = rkvc.next(xid2, its);
				//System.out.println(i+"="+nex);
				System.out.println(nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			//throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		rkvc.endTransaction(xid2);
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");

	}
	
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery18(String xid) throws Exception {
		System.out.println("KV Battery18 ");
		String xid2 = rkvc.getTransactionId();
		int max1 = max - 50000;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(xid2, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("Checkpointing..");
		rkvc.transactionCheckpoint(xid2);
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.transactionalStore(xid2, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.transactionCommit(xid2, String.class);
		rkvc.endTransaction(xid2);
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	private static void batteryCleanDB(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		//int i = min;
		//int j = max;
		// with j at max, should get them all since we stored to max -1
		//String tkey = String.format(uniqKeyFmt, j);
		System.out.println("CleanDB");
		// with i at max, should catch them all
		for(int i = min; i < max; i++) {
			String fkey = String.format(uniqKeyFmt, i);
			RelatrixKVTransaction.remove(xid, fkey);
		}
		 System.out.println("CleanDB SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
