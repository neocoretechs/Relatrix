package com.neocoretechs.relatrix.test.kv;

import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteKeySetIterator;
import com.neocoretechs.relatrix.client.RemoteStream;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of Client side KV server stream ops.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals with variation to differentiate databases.
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This tests the complete set of client side Java 8 stream calls with database alias directed to the server.
 * NOTES:
 * start server RelatrixKVServer.
 * A database unique to this test module should be used.
 * program argument is local server, remote server, remote port, remote tablespace prefix.<br/>
 * Remote tablespace prefix will be appended with alias1, alias2, alias3 variables for tests.
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2023
 *
 */
public class BatteryRelatrixKVClientStreamAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;

	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	// database aliases
	static String alias1 = "ALIAS1";
	static String alias2 = "ALIAS2";
	static String alias3 = "ALIAS3";
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 4) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientStreamAlias <DB local client NODE> <DB remote server node> <DB PORT> <server_directory_path_to_tablespace_alias");
			System.exit(1);
		}
		//rkvc = new RelatrixKVClient("volvatron", "volvatron", 9500);
		System.out.println("local="+argv[0]+" remote="+argv[1]+" port="+argv[2]);
		rkvc = new RelatrixKVClient(argv[0], argv[1], Integer.parseInt(argv[2]));
		String tablespace = argv[3];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		if(rkvc.getAlias(alias1) == null)
			rkvc.setAlias(alias1,tablespace+alias1);
		if(rkvc.getAlias(alias2) == null)
			rkvc.setAlias(alias2,tablespace+alias2);
		if(rkvc.getAlias(alias3) == null)
			rkvc.setAlias(alias3,tablespace+alias3);
		battery1(alias1);	// build and store
		battery1(alias2);	// build and store
		battery1(alias3);	// build and store
		battery11(alias1);
		battery11(alias2);
		battery11(alias3);
		battery1AR6(alias1);
		battery1AR6(alias2);
		battery1AR6(alias3);
		battery1AR7(alias1);
		battery1AR7(alias2);
		battery1AR7(alias3);
		battery1AR8(argv); // search by value, slow operation no key
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(alias1);
		battery1AR11(alias2);
		battery1AR11(alias3);
		battery1AR12(alias1);
		battery1AR12(alias2);
		battery1AR12(alias3);
		battery1AR13(alias1);
		battery1AR13(alias2);
		battery1AR13(alias3);
		battery1AR14(alias1);
		battery1AR14(alias2);
		battery1AR14(alias3);
		battery1AR15(alias1);
		battery1AR15(alias2);
		battery1AR15(alias3);
		battery1AR16(alias1);
		battery1AR16(alias2);
		battery1AR16(alias3);
		battery1AR17(alias1);
		battery1AR17(alias2);
		battery1AR17(alias3);
		System.out.println("BatteryRelatrixKVClientStream TEST BATTERY COMPLETE.");
		rkvc.close();
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String alias) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		long j = rkvc.size(alias, String.class);
		if(j > 0) {
			System.out.println(alias+" Cleaning DB of "+j+" elements.");
			battery1AR17(alias);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias, fkey+alias, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println(alias+" KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String alias) throws Exception {
		System.out.println(alias+" KV Battery11 ");
		long tims = System.currentTimeMillis();
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			Object o = rkvc.get(alias, fkey+alias);
			if(i != ((Long)o).intValue()) {
				System.out.println("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
				throw new Exception("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
			}
		}
		System.out.println(alias+" KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the higher level functions in the RelatrixKV.
	 * public Set<Map.Entry<K,V>> entrySet()
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's stream returns the entries in ascending key order. 
	 * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
	 * If the map is modified while an iteration over the set is in progress (except through the stream's 
	 * own remove operation, or through the setValue operation on a map entry returned by the stream) the results
	 * of the streaming are undefined. The set supports element removal, which removes the corresponding mapping from the map, 
	 * via the stream. Remove, Set.remove, removeAll, retainAll and clear operations. 
	 * It does not support the add or addAll operations.
	 * from battery1 we should have 0 to max, say 1000 keys of length 100
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String alias) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = rkvc.entrySetStream(alias, String.class);
		System.out.println(alias+" KV Battery1AR6");
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
		 System.out.println(alias+" BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Stream<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String alias) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = rkvc.keySetStream(alias, String.class);
		System.out.println(alias+" KV Battery1AR7");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias)) {
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
			boolean bits1 = rkvc.contains(alias1, fkey+alias1);
			boolean bits2 = rkvc.contains(alias2, fkey+alias2);
			boolean bits3 = rkvc.contains(alias3, fkey+alias3);
			if( !bits1 || !bits2 || !bits3 ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits1 = rkvc.contains(alias1, fkey+alias1);
				boolean bits2 = rkvc.contains(alias2, fkey+alias2);
				boolean bits3 = rkvc.contains(alias3, fkey+alias3);
				if( !bits1 || !bits2 || !bits3 ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits1 = rkvc.containsValue(alias1, String.class, (long)j);
			boolean bits2 = rkvc.containsValue(alias2, String.class, (long)j);
			boolean bits3 = rkvc.containsValue(alias3, String.class, (long)j);
			if( !bits1 || !bits2 || !bits3 ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue +" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits1 = rkvc.containsValue(alias1, String.class, (long)j);
				boolean bits2 = rkvc.containsValue(alias2, String.class, (long)j);
				boolean bits3 = rkvc.containsValue(alias3, String.class, (long)j);
				if( !bits1 || !bits2 || !bits3 ) {
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
		Object k = rkvc.firstKey(alias1, String.class); // first key
		Object j = rkvc.firstKey(alias2, String.class); // first key
		Object l = rkvc.firstKey(alias3, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt(((String)k).substring(0,100)) != i  || !((String)k).endsWith(alias1) ||
				Integer.parseInt(((String)j).substring(0,100)) != i  || !((String)j).endsWith(alias2) ||
				Integer.parseInt(((String)l).substring(0,100)) != i  || !((String)l).endsWith(alias3)) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i+" found "+k+", "+j+", "+l);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(alias1, String.class);
		long kt = (long) rkvc.firstValue(alias2, String.class);
		long ku = (long) rkvc.firstValue(alias3, String.class);
		if( ks != i || kt != i || ku != i) {
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
		Object k = rkvc.lastKey(alias1, String.class); // first key
		Object j = rkvc.lastKey(alias2, String.class); // first key
		Object l = rkvc.lastKey(alias3, String.class); // first key
		System.out.println("KV Battery1AR10");
		if( Integer.parseInt(((String)k).substring(0,100)) != i  || !((String)k).endsWith(alias1) ||
				Integer.parseInt(((String)j).substring(0,100)) != i  || !((String)j).endsWith(alias2) ||
				Integer.parseInt(((String)l).substring(0,100)) != i  || !((String)l).endsWith(alias3)) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(alias1, String.class);
		long kt = (long)rkvc.lastValue(alias2, String.class);
		long ku = (long)rkvc.lastValue(alias3, String.class);
		if( ks != (long)i || kt != (long)i || ku != (long)i) {
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
		long bits1 = rkvc.size(alias1, String.class);
		long bits2 = rkvc.size(alias2, String.class);
		long bits3 = rkvc.size(alias3, String.class);
		System.out.println("KV Battery1AR101");
		if( bits1 != i || bits2 != i || bits3 != i) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits1+" or "+bits2+" or "+bits3+" should be:"+i);
			throw new RuntimeException("KV BATTERY1AR101 size mismatch "+bits1+" or "+bits2+" or "+bits3+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findTailMapStream(alias, fkey); // intentionally leave off suffix for no exact match
		System.out.println(alias+" KV Battery1AR11");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias)) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findTailMapKVStream(alias, fkey);
		System.out.println(alias+" KV Battery1AR12");
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR13(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findHeadMapStream(alias, fkey); // no exact match
		System.out.println(alias+" KV Battery1AR13");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findHeadMapKVStream(alias, fkey); // no exact match
		System.out.println(alias+" KV Battery1AR14");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt((((Map.Entry<String,Long>)e).getKey()).substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR15(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = rkvc.findSubMapStream(alias, fkey, tkey); // no exact match
		System.out.println(alias+" KV Battery1AR15");
		stream.forEach(e ->{
			if(Integer.parseInt(((String) e).substring(0,100)) != i || !((String)e).endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR16(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = rkvc.findSubMapKVStream(alias, fkey, tkey); // no exact match
		System.out.println(alias+" KV Battery1AR16");
		stream.forEach(e ->{
			if(Integer.parseInt((((Map.Entry<String,Long>)e).getKey()).substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias+" BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param alias
	 * @throws Exception
	 */
	public static void battery1AR17(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17 for alias "+alias);
		RemoteKeySetIterator its = (RemoteKeySetIterator) rkvc.keySet(alias, String.class);
		long timx = System.currentTimeMillis();
		while(rkvc.hasNext(its)) {
			String fkey = (String) rkvc.next(its);
			rkvc.remove(alias, fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		its.close();
		long siz = rkvc.size(alias, String.class);
		if(siz > 0) {
				Stream stream =  rkvc.entrySetStream(alias, String.class);
				stream.forEach(e ->{
					//System.out.println(i+"="+key);
					System.out.println(e);
				});
				System.out.println(alias+" KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception(alias+" KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println(alias+" BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	
}
