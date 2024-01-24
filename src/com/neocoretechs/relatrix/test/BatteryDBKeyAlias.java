package com.neocoretechs.relatrix.test;

import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexInstanceTableInterface;

/**
 * @author jg (C) 2024
 *
 */
public class BatteryDBKeyAlias {
	public static boolean DEBUG = false;
	static DBKey dbkey;
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static IndexInstanceTableInterface indexTable = new IndexInstanceTable();
	static String alias1 = "ALIAS1";
	static String alias2 = "ALIAS2";
	static String alias3 = "ALIAS3";
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryDBKey <directory_tablespace_path>");
			System.exit(1);
		}
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixKV.setAlias(alias1,tablespace+alias1);
		RelatrixKV.setAlias(alias2,tablespace+alias2);
		RelatrixKV.setAlias(alias3,tablespace+alias3);	
		battery1(alias1);
		battery1(alias2);
		battery1(alias3);
		battery1AR4(alias1);
		battery1AR4(alias2);
		battery1AR4(alias3);
		battery1AR5(alias1);
		battery1AR5(alias2);
		battery1AR5(alias3);
		battery1AR6(alias1);
		battery1AR6(alias2);
		battery1AR6(alias3);
		battery1AR7(alias1);
		battery1AR7(alias2);
		battery1AR7(alias3);
		battery1AR9(alias1);
		battery1AR9(alias2);
		battery1AR9(alias3);
		battery1AR10(alias1);
		battery1AR10(alias2);
		battery1AR10(alias3);
		battery1AR101(alias1);
		battery1AR101(alias2);
		battery1AR101(alias3);
		battery1AR12(alias1);
		battery1AR12(alias2);
		battery1AR12(alias3);
		battery1AR14(alias1);
		battery1AR14(alias2);
		battery1AR14(alias3);
		battery1AR17(alias1);
		battery1AR17(alias2);
		battery1AR17(alias3);
		 System.out.println("BatteryDBKey TEST BATTERY COMPLETE.");
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * Ensure that we start with known baseline number of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String alias) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		DBKey fkey = null;
		//Integer payload = 0;
		int j = min;
		j = (int) RelatrixKV.size(alias, DBKey.class);
		if(j > 0) {
			System.out.println("Cleaning DB "+alias+" of "+j+" elements.");
			battery1AR17(alias);		
		}
		for(int i = min; i < max; i++) {
			//try {
				fkey = DBKey.newKeyAlias(alias, indexTable, i); // puts to index and instance
				//RelatrixKV.store(fkey, new Long(i));
				++recs;
			//} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * check order of DBKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR4(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		DBKey prev = null;
		Iterator<?> its = RelatrixKV.findTailMapKV(alias, (Comparable) RelatrixKV.firstKey(alias, DBKey.class));
		System.out.println(alias+" KV Battery1AR4");
		prev = (DBKey) RelatrixKV.firstKey(alias, DBKey.class);
		its.next(); // skip first key we just got
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
			if(nexe.getKey().compareTo(prev) != 1) { // should always be >
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH: "+nex);
				throw new Exception("KV RANGE KEY MISMATCH: "+nex);
			}
			prev = nexe.getKey();
		}
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String alias) throws Exception {
		int i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(alias, Integer.class);
		System.out.println(alias+" KV Battery1AR5");
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			i = (int) indexTable.getByIndex((DBKey) nex.getValue()); // get value, which is dbkey, then look up dbkey key
			//System.out.println(i+"="+nex);
			if((Integer)nex.getKey() != i)
				System.out.println("KEY MISMATCH:"+i+" - "+nex);
		}
		 System.out.println("BATTERY1AR5 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
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
	public static void battery1AR6(String alias) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(alias, Integer.class);
		System.out.println(alias+" KV Battery1AR6");
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			//System.out.println(i+"="+nex);
			if((Integer)nex.getKey() != i)
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+nex);
			else
				++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String alias) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.keySet(alias, Integer.class);
		System.out.println(alias+" KV Battery1AR7");
		while(its.hasNext()) {
			Integer nex = (Integer) its.next();
			// Map.Entry
			//if(Integer.parseInt(nex) != i)
				//System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			//else
				++i;
		}
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("KV BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * 
	 * Testing of first(), and firstValue
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String alias) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKV.firstKey(alias, Integer.class); // first key
		System.out.println(alias+" KV Battery1AR9");
		int ks = ((int) k);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String alias) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKV.lastKey(alias, Integer.class); // key
		System.out.println(alias+" KV Battery1AR10");
		int ks = (int)( k);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String alias) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixKV.size(alias, DBKey.class);
		System.out.println(alias+" KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * findMapKV tailmapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.findTailMapKV(alias, (Comparable) RelatrixKV.firstKey(alias, DBKey.class));
		System.out.println(alias+" KV Battery1AR12");
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
			DBKey db = indexTable.getByInstanceAlias(alias, nexe.getValue()); // get the DBKey for this instance integer
			if(nexe.getKey().compareTo(db) != 0) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+nex);
			}

		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	
	/**
	 * findHeadMapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.findHeadMapKV(alias, (Comparable) RelatrixKV.lastKey(alias, DBKey.class));
		System.out.println(alias+" KV Battery1AR14");
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
			DBKey db = indexTable.getByInstanceAlias(alias, nexe.getValue()); // get the DBKey for this instance integer
			if(nexe.getKey().compareTo(db) != 0) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+nex);
			}
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String alias) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB "+alias);
		long s = RelatrixKV.size(alias, DBKey.class);
		Iterator it = RelatrixKV.keySet(alias, DBKey.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove(alias, (Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(alias+" DBKey "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		// remove payload reverse index
		s = RelatrixKV.size(alias, Integer.class);
		it = RelatrixKV.keySet(alias, Integer.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove(alias, (Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("Integer "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = RelatrixKV.size(alias, DBKey.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKV.entrySet(alias, DBKey.class);
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

	
}
