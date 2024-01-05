package com.neocoretechs.relatrix.test.kv;

import java.util.Map;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.RemoteKeySetIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of client side KV server stream transaction ops.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This test the client side Java 8 streams obtained from the server
 * NOTES:
 * start server RelatrixKVTransactionServer.
 * A database unique to this test module should be used.
 * program argument is local server, remote server, remote port
 * @author Jonathan Groff (C) NeoCoreTechs 2022,2023
 *
 */
public class BatteryRelatrixKVClientTransactionStream {
	public static boolean DEBUG = false;
	public static RelatrixKVClientTransaction rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	private static int dupes;
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 3) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClient <DB local client NODE> <DB remote server node> <DB PORT>");
			System.exit(1);
		}
		System.out.println("local="+argv[0]+" remote="+argv[0]+" port="+argv[1]);
		rkvc = new RelatrixKVClientTransaction(argv[0], argv[1], Integer.parseInt(argv[2]));
		String xid = rkvc.getTransactionId();
		battery1(xid);	// build and store
		battery11(xid);  // build and store
		battery1AR6(xid);
		battery1AR7(xid);
		battery1AR8(xid); // search by value, slow operation no key
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
		System.out.println("BatteryRelatrixKVClientTransactionStream TEST BATTERY COMPLETE.");
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
			battery1AR17(xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.storekv(xid, fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.commit(xid);
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
				rkvc.storekv(xid2, fkey, new Long(fkey));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			rkvc.rollback(xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
		rkvc.endTransaction(xid2);
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
		RemoteStream stream = rkvc.entrySetStream(xid,String.class);
		System.out.println("KV Battery1AR6");
		stream.of().forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
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
		RemoteStream stream = rkvc.keySetStream(xid, String.class);
		System.out.println("KV Battery1AR7");
		stream.of().forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
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
			boolean bits = rkvc.contains(xid, fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(xid, fkey);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = rkvc.containsValue(xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue  ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(xid, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
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
		Object k = rkvc.firstKey(xid, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt((String)k) != i ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
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
		Object k = rkvc.lastKey(xid, String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong((String) k) != (long)i ) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
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
		long bits = rkvc.size(xid, String.class);
		System.out.println("KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
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
		RemoteStream stream = rkvc.findTailMapStream(xid, fkey);
		System.out.println("KV Battery1AR11");
		stream.of().forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
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
		RemoteStream stream = rkvc.findTailMapKVStream(xid, fkey);
		System.out.println("KV Battery1AR12");
		stream.of().forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
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
		RemoteStream stream = rkvc.findHeadMapStream(xid, fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		stream.of().forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
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
		RemoteStream stream = rkvc.findHeadMapKVStream(xid, fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		stream.of().forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
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
		RemoteStream stream = rkvc.findSubMapStream(xid, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		stream.of().forEach(e ->{
			if(Integer.parseInt((String) e) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
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
		RemoteStream stream = rkvc.findSubMapKVStream(xid, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		stream.of().forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries, this is done in a new transaction
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String xid) throws Exception {
		long tims = System.currentTimeMillis();
		String xid2 = rkvc.getTransactionId();
		System.out.println("KV Battery1AR17");
		long timx = System.currentTimeMillis();
		RemoteKeySetIteratorTransaction its = rkvc.keySet(xid2,String.class);
		while(rkvc.hasNext(xid2, its)) {
			String fkey = (String) rkvc.next(xid2, its);
			rkvc.remove(xid2, fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		its.close();
		rkvc.commit(xid2);
		long siz = rkvc.size(xid2, String.class);
		i = 0;
		if(siz > 0) {
			RemoteStream stream = rkvc.entrySetStream(xid,String.class);
			stream.of().forEach(e ->{
				if(((Map.Entry<String,Long>)e).getValue() != i) {
					System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
				}
				System.out.println(i+"="+e);
				++i;
			});
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed. Total="+i);
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		rkvc.endTransaction(xid2);
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	
}
