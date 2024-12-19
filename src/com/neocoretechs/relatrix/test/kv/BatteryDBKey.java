package com.neocoretechs.relatrix.test.kv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexInstanceTableInterface;

import com.neocoretechs.relatrix.key.RelatrixIndex;

/**
 * The set of tests verifies the lower level {@link DBKey} functions in the {@link  Relatrix}
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017
 *
 */
public class BatteryDBKey {
	public static boolean DEBUG = false;
	static DBKey dbkey;
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static IndexInstanceTableInterface indexTable = new IndexInstanceTable();
	static ArrayList<DBKey> findkeys = new ArrayList<DBKey>();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryDBKey <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixKV.setTablespace(argv[0]);
		battery1AR17(argv);
		battery1(argv);
		battery1AR4(argv);
		battery1AR44(argv);
		battery1AR5(argv);
		battery1AR6(argv);
		battery1AR7(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR12(argv);
		battery1AR14(argv);
		battery1AR17(argv);
		 System.out.println("BatteryDBKey TEST BATTERY COMPLETE.");
		
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
		DBKey fkey = null;
		//Integer payload = 0;

		for(int i = min; i < max; i++) {
			//try {
				fkey = DBKey.newKey(indexTable, i); // puts to index and instance
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
	public static void battery1AR4(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		DBKey prev = null;
		Iterator<?> its = RelatrixKV.findTailMapKV((Comparable) RelatrixKV.firstKey(DBKey.class));
		System.out.println("KV Battery1AR4");
		// set up previous key as first key, insert to key map
		prev = (DBKey) RelatrixKV.firstKey(DBKey.class);
		Comparable nex = (Comparable) its.next();
		Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
		findkeys.add(nexe.getKey());
		while(its.hasNext()) {
			nex = (Comparable) its.next();
			nexe = (Map.Entry<DBKey,Integer>)nex;
			if(nexe.getKey().compareTo(prev) <= 0) { // should always be >
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH: prev:"+prev+" nex:"+nexe.getKey()+" cmpr:"+nexe.getKey().compareTo(prev));
				throw new Exception("KV RANGE KEY MISMATCH: prev:"+prev+" nex:"+nexe.getKey()+" cmpr:"+nexe.getKey().compareTo(prev));
			}
			prev = nexe.getKey();
			findkeys.add(nexe.getKey());
		}
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR44(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR44");
		while(!findkeys.isEmpty()) {
			int rnd = new Random().nextInt(findkeys.size());
			DBKey ident = findkeys.get(rnd);
			findkeys.remove(rnd);
			//PrimaryKeySet pks = new PrimaryKeySet(ident);
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			RelatrixIndex ri = ident.getInstanceIndex();
			ri.setLsb(0); // set artificial partial instance key
			DBKey pks = new DBKey(ri); // new dbkey with partial instance
			if(!RelatrixKV.contains(pks)) { // shouldnt find this
				System.out.println("Expected Didnt find "+pks+" using "+ident);
			} else {
				System.out.println("UNEXPECTED FOUND "+pks+" using "+ident);
			}
			if(!RelatrixKV.contains(ident)) { // should find this
				System.out.println("Expected contains fail "+ident+" using itself");
			} else {
				System.out.println("UNEXPECTED contains "+ident+" using itself");
			}
			Iterator it = RelatrixKV.findTailMapKV(pks); // probably should find this
			if(it.hasNext())
				System.out.println("ITERATOR FOUND Tailmap :"+it.next());
			else
				System.out.println("Tailmap also failed..");
			
		}
		 System.out.println("BATTERY1AR44 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String[] argv) throws Exception {
		int i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(Integer.class);
		System.out.println("KV Battery1AR5");
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			i = (int) indexTable.get((DBKey) nex.getValue()); // get value, which is dbkey, then look up dbkey key
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
	public static void battery1AR6(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(Integer.class);
		System.out.println("KV Battery1AR6");
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
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.keySet(Integer.class);
		System.out.println("KV Battery1AR7");
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
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKV.firstKey(Integer.class); // first key
		System.out.println("KV Battery1AR9");
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
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = RelatrixKV.lastKey(Integer.class); // key
		System.out.println("KV Battery1AR10");
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
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixKV.size(DBKey.class);
		System.out.println("KV Battery1AR101");
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
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.findTailMapKV((Comparable) RelatrixKV.firstKey(DBKey.class));
		System.out.println("KV Battery1AR12");
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
			DBKey db = indexTable.getKey(nexe.getValue()); // get the DBKey for this instance integer
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
	public static void battery1AR14(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.findHeadMapKV((Comparable) RelatrixKV.lastKey(DBKey.class));
		System.out.println("KV Battery1AR14");
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<DBKey, Integer> nexe = (Map.Entry<DBKey,Integer>)nex;
			DBKey db = indexTable.getKey(nexe.getValue()); // get the DBKey for this instance integer
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
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int j = min;
		long s = RelatrixKV.size(DBKey.class);
		System.out.println("Cleaning DB of "+s+" elements.");
		Iterator it = RelatrixKV.keySet(DBKey.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("DBKey "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		// remove payload reverse index
		s = RelatrixKV.size(Integer.class);
		it = RelatrixKV.keySet(Integer.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("Integer "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = RelatrixKV.size(DBKey.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKV.entrySet(DBKey.class);
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
