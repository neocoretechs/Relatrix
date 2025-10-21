package com.neocoretechs.relatrix.test.kv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexInstanceTableInterface;
import com.neocoretechs.relatrix.key.IndexResolver;

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
		IndexResolver.setLocal();
		battery1AR17(argv);
		battery1(argv);
		battery1AR4(argv);
		battery1AR7(argv);
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
			Relation r = new Relation(i,i,i);
			fkey = DBKey.newKey(IndexResolver.getIndexInstanceTable(), r); // puts to index and instance
			RelatrixKV.store(fkey, new MapRangeDomain(r));
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
		Map.Entry<DBKey, Relation> nexe = (Map.Entry<DBKey,Relation>)nex;
		findkeys.add(nexe.getKey());
		while(its.hasNext()) {
			nex = (Comparable) its.next();
			nexe = (Map.Entry<DBKey,Relation>)nex;
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

	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.keySet(Relation.class);
		System.out.println("KV Battery1AR7");
		while(its.hasNext()) {
			Relation nex = (Relation) its.next();
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
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int j = min;
		long s = RelatrixKV.size(DBKey.class);
		System.out.println("Cleaning DB of "+s+" elements.");
		Iterator<?> it = RelatrixKV.keySet(DBKey.class);
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
		s = RelatrixKV.size(Relation.class);
		it = RelatrixKV.keySet(Relation.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey.getClass().getName()+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		s = RelatrixKV.size(MapRangeDomain.class);
		it = RelatrixKV.keySet(MapRangeDomain.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey.getClass().getName()+i+" "+fkey);
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
