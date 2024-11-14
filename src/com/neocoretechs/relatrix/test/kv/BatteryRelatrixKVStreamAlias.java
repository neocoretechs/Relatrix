package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.rocksack.Alias;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of embedded KV server stream retrieval ops.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This tests the Java 8 streams obtained from the server
 * NOTES:
 * The database aliases define db names and program argument defines tablespace, alias is prepended for fully qualified tablespace names
 * C:/users/you/Relatrix should be valid path as program arg. C:/users/you/Relatrix/ALIAS1java.lang.String through ALIAS3... will be created.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2023
 *
 */
public class BatteryRelatrixKVStreamAlias {
	public static boolean DEBUG = false;

	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int numLookupByValue = 10; // lookup by value quite slow
	static int i;
	static int j;
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVStreamAlias <directory_tablespace_path>");
			System.exit(1);
		}
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixKV.setAlias(alias1,tablespace+alias1);
		RelatrixKV.setAlias(alias2,tablespace+alias2);
		RelatrixKV.setAlias(alias3,tablespace+alias3);
		battery1(argv);	// build and store
		battery1AR6(argv);
		battery1AR7(argv);
		battery1AR11(argv);
		battery1AR12(argv);
		battery1AR13(argv);
		battery1AR14(argv);
		battery1AR15(argv);
		battery1AR16(argv);
		battery1AR17(alias1);
		battery1AR17(alias2);
		battery1AR17(alias3);
		battery18(argv);
		System.out.println("BatteryRelatrixKVStreamAlias TEST BATTERY COMPLETE.");
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * Ensure that we start with known baseline number of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) RelatrixKV.size(alias1, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias1+" "+RelatrixKV.getAlias(alias1)+" of "+j+" elements.");
			battery1AR17(alias1);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKV.store(alias1, fkey+alias1, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		//
		j = (int) RelatrixKV.size(alias2, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias2+" "+RelatrixKV.getAlias(alias2)+" of "+j+" elements.");
			battery1AR17(alias2);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKV.store(alias2, fkey+alias2, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		//
		j = (int) RelatrixKV.size(alias3, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias3+" "+RelatrixKV.getAlias(alias3)+" of "+j+" elements.");
			battery1AR17(alias3);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKV.store(alias3, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
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
		Stream stream1 = RelatrixKV.entrySetStream(alias1, String.class);
		Stream stream2 = RelatrixKV.entrySetStream(alias2, String.class);
		Stream stream3 = RelatrixKV.entrySetStream(alias3, String.class);
		System.out.println("KV Battery1AR6");
		stream1.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias1.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias2.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias3.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
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
		Stream stream1 = RelatrixKV.keySetStream(alias1, String.class);
		Stream stream2 = RelatrixKV.keySetStream(alias2, String.class);
		Stream stream3 = RelatrixKV.keySetStream(alias3, String.class);
		System.out.println("KV Battery1AR7");
		stream1.forEach(e ->{
			if(!((String)e).endsWith(alias1.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(!((String)e).endsWith(alias2.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(!((String)e).endsWith(alias3.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
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
	 * findMap test, basically tailmap returning keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream1 = RelatrixKV.findTailMapStream(alias1, fkey);
		Stream stream2 = RelatrixKV.findTailMapStream(alias2, fkey);
		Stream stream3 = RelatrixKV.findTailMapStream(alias3, fkey);
		System.out.println("KV Battery1AR11");
		stream1.forEach(e ->{
			if(!((String)e).endsWith(alias1.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR11 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR11 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(!((String)e).endsWith(alias2.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR11 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR11 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(!((String)e).endsWith(alias3.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR11 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR11 unexpected number of keys "+i);
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
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream1 = RelatrixKV.findTailMapKVStream(alias1, fkey);
		Stream stream2 = RelatrixKV.findTailMapKVStream(alias2, fkey);
		Stream stream3 = RelatrixKV.findTailMapKVStream(alias3, fkey);
		System.out.println("KV Battery1AR12");
		stream1.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias1.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR12 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR12 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias2.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR12 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR12 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias3.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR12 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR12 unexpected number of keys "+i);
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
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream1 = RelatrixKV.findHeadMapStream(alias1,fkey);
		Stream stream2 = RelatrixKV.findHeadMapStream(alias2,fkey);
		Stream stream3 = RelatrixKV.findHeadMapStream(alias3,fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		stream1.forEach(e ->{
			if(!((String)e).endsWith(alias1.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR13 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR13 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(!((String)e).endsWith(alias2.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR13 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR13 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(!((String)e).endsWith(alias3.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR13 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR13 unexpected number of keys "+i);
		}
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
		Stream stream1 = RelatrixKV.findHeadMapKVStream(alias1, fkey);
		Stream stream2 = RelatrixKV.findHeadMapKVStream(alias2, fkey);
		Stream stream3 = RelatrixKV.findHeadMapKVStream(alias3, fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		stream1.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias1.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR14 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR14 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias2.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR14 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR14 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias3.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR14 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR14 unexpected number of keys "+i);
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
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream1 = RelatrixKV.findSubMapStream(alias1, fkey, tkey);
		Stream stream2 = RelatrixKV.findSubMapStream(alias2, fkey, tkey);
		Stream stream3 = RelatrixKV.findSubMapStream(alias3, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		stream1.forEach(e ->{
			if(!((String)e).endsWith(alias1.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR15 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR15 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(!((String)e).endsWith(alias2.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR15 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR15 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(!((String)e).endsWith(alias3.getAlias()) || Integer.parseInt(((String)e).substring(0,100)) != i	) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR15 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR15 unexpected number of keys "+i);
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
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream1 = RelatrixKV.findSubMapKVStream(alias1, fkey, tkey);
		Stream stream2 = RelatrixKV.findSubMapKVStream(alias2, fkey, tkey);
		Stream stream3 = RelatrixKV.findSubMapKVStream(alias3, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		stream1.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias1.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR16 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR16 unexpected number of keys "+i);
		}
		i = min;
		stream2.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias2.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR16 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR16 unexpected number of keys "+i);
		}
		i = min;
		stream3.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias3.getAlias()) ||
					Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i	) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR16 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR16 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR17(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();

		// with j at max, should get them all since we stored to max -1
		System.out.println("KV Battery1AR17 for alias:"+alias12);
		System.out.println("CleanDB");
		long s = RelatrixKV.size(alias12, String.class);
		Iterator it = RelatrixKV.keySet(alias12, String.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove(alias12, (Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		// verify
		long siz = RelatrixKV.size(alias12, String.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKV.entrySet(alias12, String.class);
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				//System.out.println(i+"="+nex);
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
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
				RelatrixKV.store(alias1, fkey+alias1, new Long(i));
				RelatrixKV.store(alias2, fkey+alias2, new Long(i));
				RelatrixKV.store(alias3, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		long s = RelatrixKV.size(alias1, String.class);
		if(s != max1)
			System.out.println("Size at halway point of restore incorrect:"+s+" should be "+max1);
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKV.store(alias1, fkey+alias1, new Long(i));
				RelatrixKV.store(alias2, fkey+alias2, new Long(i));
				RelatrixKV.store(alias3, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records in 3 alias, rejected "+dupes+" dupes.");
	}

}
