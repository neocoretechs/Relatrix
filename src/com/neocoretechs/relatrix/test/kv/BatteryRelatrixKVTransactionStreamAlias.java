package com.neocoretechs.relatrix.test.kv;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of KV transaction server stream ops.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This test the client side Java 8 streams obtained from the server
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is local server, remote server, remote port
 * @author jg (C) 2022
 *
 */
public class BatteryRelatrixKVTransactionStreamAlias {
	public static boolean DEBUG = false;
	static String alias1 = "ALIAS1";
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	static long timx = System.currentTimeMillis();
	private static int dupes;
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVTransactionStreamAlias <directory_tablespace_path>");
			System.exit(1);
		}
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixKV.setAlias(alias1,tablespace+alias1);
		String xid = RelatrixKVTransaction.getTransactionId();
		battery1(xid);	// build and store
		battery11(xid);  // build and store
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
		battery17(xid);
		System.out.println("BatteryRelatrixKVTransactionStreamAlias TEST BATTERY COMPLETE.");
		RelatrixKVTransaction.endTransaction(xid);
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
		j = (int) RelatrixKVTransaction.size(alias1, xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			batteryCleanDB(xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		RelatrixKVTransaction.commit(alias1, xid);
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
		String xid2 = RelatrixKVTransaction.getTransactionId();
		for(int i = max; i < max*2; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid2, fkey, new Long(fkey));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			RelatrixKVTransaction.rollback(alias1, xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
		RelatrixKVTransaction.endTransaction(xid2);
	}
	
	/**
	 * Test the higher level functions in the RelatrixKV.
	 * public Set<Map.Entry<K,V>> entrySet()
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's stream returns the entries in ascending key order. 
	 * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
	 *  If the map is modified while an iteration over the set is in progress (except through the stream's 
	 *  own remove operation, or through the setValue operation on a map entry returned by the stream) the results
	 *   of the streaming are undefined. The set supports element removal, which removes the corresponding mapping from the map, 
	 *   via the stream. Remove, Set.remove, removeAll, retainAll and clear operations. 
	 *   It does not support the add or addAll operations.
	 *   from battery1 we should have 0 to max, say 1000 keys of length 100
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR6(String xid) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = RelatrixKVTransaction.entrySetStream(alias1, xid, String.class);
		System.out.println("KV Battery1AR6");
		stream.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			//throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Stream<?> its = RelatrixKV.keySet;
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR7(String xid) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = RelatrixKVTransaction.keySetStream(alias1, xid, String.class);
		System.out.println("KV Battery1AR7");
		stream.forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			//throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("KV BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String xid) throws Exception {
		i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = RelatrixKVTransaction.contains(alias1, xid, fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = RelatrixKVTransaction.contains(alias1, xid, fkey);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					//throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = RelatrixKVTransaction.containsValue(alias1, xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue  ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = RelatrixKVTransaction.containsValue(alias1, xid, String.class, (long)j);
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
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR9(String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKVTransaction.firstKey(alias1, xid, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt((String)k) != i ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			//throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) RelatrixKVTransaction.firstValue(alias1, xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			//throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR10(String xid) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKVTransaction.lastKey(alias1, xid, String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong((String) k) != (long)i ) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			//throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)RelatrixKVTransaction.lastValue(alias1, xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			//throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR101(String xid) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixKVTransaction.size(alias1, xid, String.class);
		System.out.println("KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			//throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR11(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = RelatrixKVTransaction.findTailMapStream(alias1, xid, fkey);
		System.out.println("KV Battery1AR11");
		stream.forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR12(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = RelatrixKVTransaction.findTailMapKVStream(alias1, xid, fkey);
		System.out.println("KV Battery1AR12");
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR13(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = RelatrixKVTransaction.findHeadMapStream(alias1, xid, fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR14(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = RelatrixKVTransaction.findHeadMapKVStream(alias1, xid, fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR15(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = RelatrixKVTransaction.findSubMapStream(alias1, xid, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		stream.forEach(e ->{
			if(Integer.parseInt((String) e) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR16(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = RelatrixKVTransaction.findSubMapKVStream(alias1, xid, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
				//throw new Exception("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Write beyond current key range, then roll back in different transaction
	 * @param argv
	 * @throws Exception
	 */
	public static void battery17(String xid) throws Exception {
		System.out.println("KV Battery17 ");
		String xid2 = RelatrixKVTransaction.getTransactionId();
		int max1 = max + 50000;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = max; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid2, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("Rollback..");
		RelatrixKVTransaction.rollback(alias1, xid2);
		RelatrixKVTransaction.endTransaction(xid2);
		// check status using original transaction
		timx = System.currentTimeMillis();
		System.out.println("KV Battery17");
		long siz = RelatrixKVTransaction.size(alias1, xid, String.class);
		i = 0;
		Stream stream = RelatrixKVTransaction.entrySetStream(alias1, xid, String.class);
		stream.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			}
			if((System.currentTimeMillis()-timx) > 5000) {
					System.out.println(i+" "+e);
					timx = System.currentTimeMillis();
			}
			++i;
		});
		siz = RelatrixKVTransaction.size(alias1, xid, String.class);
		if(siz != max || siz != i || i != max)
			System.out.println("KV RANGE BATTERY17 SIZE MISMATCH: size="+siz+" max="+max+" count="+i);
		System.out.println("KV BATTERY17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. ");
	}
	
	/**
	 * remove entries, we do this in the current transaction
	 * @param argv
	 * @throws Exception
	 */
	private static void batteryCleanDB(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB");
		long s = RelatrixKVTransaction.size(alias1, xid, String.class);
		Iterator it = RelatrixKVTransaction.keySet(alias1,  xid, String.class);
		// with i at max, should catch them all
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKVTransaction.remove(alias1, xid, (Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		 System.out.println("CleanDB SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
