package com.neocoretechs.relatrix.test.kv;

import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteKVIterator;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteKeySetIterator;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of Client side KV server stream retrieval ops.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This test the client side Java 8 streams obtained from the server
 * NOTES:
 * start server RelatrixKVServer.
 * A database unique to this test module should be used.
 * program argument is local server, remote server, remote port
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2022,2024
 *
 */
public class BatteryRelatrixKVClientStream {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;

	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 3) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientStream <DB local client NODE> <DB remote server node> <DB PORT>");
			System.exit(1);
		}
		//rkvc = new RelatrixKVClient("volvatron", "volvatron", 9500);
		System.out.println("local="+argv[0]+" remote="+argv[1]+" port="+argv[2]);
		rkvc = new RelatrixKVClient(argv[0], argv[1], Integer.parseInt(argv[2]));
		battery1(argv);	// build and store
		battery11(argv);  // build and store
		battery1AR6(argv);
		battery1AR7(argv);
		battery1AR8(argv); // search by value, slow operation no key
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(argv);
		battery1AR12(argv);
		battery1AR13(argv);
		battery1AR14(argv);
		battery1AR15(argv);
		battery1AR16(argv);
		battery1AR17(argv);
		battery18(argv);
		System.out.println("BatteryRelatrixKVClientStream TEST BATTERY COMPLETE.");
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
		long j = rkvc.size(String.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			battery1AR17(argv);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
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
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			Object o = rkvc.get(fkey);
			if(i != ((Long)o).intValue()) {
				System.out.println("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
				throw new Exception("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
			}
		}
		System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		RemoteStream stream = (RemoteStream) rkvc.entrySetStream(String.class);
		System.out.println("KV Battery1AR6");
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		RemoteStream stream = (RemoteStream) rkvc.keySetStream(String.class);
		System.out.println("KV Battery1AR7");
		stream.forEach(e ->{
			if(Integer.parseInt((String)e) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
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
	 * Testing of Stream<?> its = Relatrix.findSet("?", "?", "*");
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv) throws Exception {
		i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(fkey);
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
			boolean bits = rkvc.containsValue(String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue +" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(String.class, (long)j);
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
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
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
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
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
			throw new RuntimeException("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
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
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findTailMapStream(fkey);
		System.out.println("KV Battery1AR11");
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findTailMapKVStream(fkey);
		System.out.println("KV Battery1AR12");
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR13(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = (RemoteStream) rkvc.findHeadMapStream(fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = (RemoteStream) rkvc.findHeadMapKVStream(fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR15(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = (RemoteStream) rkvc.findSubMapStream(fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		stream.forEach(e ->{
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR16(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream =  rkvc.findSubMapKVStream(fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		stream.forEach(e ->{
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
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17");
		RemoteKVIterator its = (RemoteKVIterator) rkvc.keySet(String.class);
		System.out.println("KV Battery1AR7");
		long timx = System.currentTimeMillis();
		while(its.hasNext()) {
			String fkey = (String) its.next();
			rkvc.remove(fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		its.close();
		long siz = rkvc.size(String.class);
		if(siz > 0) {
				Stream stream = rkvc.entrySetStream(String.class);
				stream.forEach(e ->{
					//System.out.println(i+"="+key);
					System.out.println(e);
				});
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery18(String[] argv) throws Exception {
		System.out.println("KV Battery18 ");
		int max1 = max - 50000;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		long s = rkvc.size(String.class);
		if(s != max1)
			System.out.println("Size at halway point of restore incorrect:"+s+" should be "+max1);
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(fkey, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
}
