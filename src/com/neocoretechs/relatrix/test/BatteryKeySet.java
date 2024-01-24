package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexInstanceTable;
import com.neocoretechs.relatrix.key.IndexInstanceTableInterface;
import com.neocoretechs.relatrix.key.KeySet;

/**
 * @author jg (C) 2024
 *
 */
public class BatteryKeySet {
	public static boolean DEBUG = false;
	static KeySet keyset;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static IndexInstanceTableInterface indexTable = new IndexInstanceTable();
	static ArrayList<DBKey> keys = new ArrayList<DBKey>();
	static ArrayList<KeySet> values = new ArrayList<KeySet>();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryKeySet <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixKV.setTablespace(argv[0]);
		battery1(argv);
		battery1AR4(argv);
		battery1AR5(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR12(argv);
		battery1AR14(argv);
		battery1AR17(argv);
		 System.out.println("BatteryKeySet TEST BATTERY COMPLETE.");
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * Ensure that we start with known baseline number of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		KeySet fkey = null;
		String skeyd = null;
		String skeym = null;
		String skeyr = null;

		//Integer payload = 0;
		int j = min;
		j = (int) RelatrixKV.size(KeySet.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			battery1AR17(argv);		
		}
		for(int i = min; i < max; i++) {
			DBKey dbkeyd = null;
			DBKey dbkeym = null;
			DBKey dbkeyr = null;
			fkey = new KeySet();
			skeyd = String.format(uniqKeyFmt, i);
			skeym = String.format(uniqKeyFmt, i+1);
			skeyr = String.format(uniqKeyFmt, i+2);
			try {
				dbkeyd = DBKey.newKey(indexTable, skeyd); // puts to index and instance
			} catch(DuplicateKeyException dke) { continue;}
			try {
				dbkeym = DBKey.newKey(indexTable, skeym); // puts to index and instance
			} catch(DuplicateKeyException dke) { continue;}
			try {
				dbkeyr = DBKey.newKey(indexTable, skeyr); // puts to index and instance
			} catch(DuplicateKeyException dke) { continue;}
				fkey.setDomainKey(dbkeyd);
				fkey.setMapKey(dbkeym);
				fkey.setRangeKey(dbkeyr);
				fkey.setPrimaryKeyCheck(true);
				if(RelatrixKV.get(fkey) != null)
					throw new DuplicateKeyException(fkey);
				fkey.setPrimaryKeyCheck(false);
				try {
					keys.add(DBKey.newKey(indexTable, fkey));
					values.add(fkey);
				} catch(DuplicateKeyException dke) { ++dupes; }
				++recs;
	
		}
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	/**
	 * check order of DBKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR4(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		KeySet prev = (KeySet) RelatrixKV.firstKey(KeySet.class);
		Iterator<?> its = RelatrixKV.findTailMapKV((Comparable) prev);
		System.out.println("Battery1AR4");
		its.next(); // skip first key we just got
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<KeySet, DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
			if(nexe.getKey().compareTo(prev) != 1) { // should always be >
			// Map.Entry
				System.out.println("RANGE KEY MISMATCH: "+nex);
				throw new Exception("RANGE KEY MISMATCH: "+nex);
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
	public static void battery1AR5(String[] argv) throws Exception {
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(KeySet.class);
		System.out.println("Battery1AR5");
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			i =  indexTable.getByIndex((DBKey) nex.getValue()); 
			if(((Comparable)i).compareTo(nex.getKey()) != 0) {
				System.out.println("RANGE KEY MISMATCH: "+nex);
				throw new Exception("RANGE KEY MISMATCH: "+nex);
			}
			//System.out.println(i+"="+nex);
		}
		 System.out.println("BATTERY1AR5 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
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
		Comparable k = (Comparable) RelatrixKV.firstKey(KeySet.class); // first key
		System.out.println("Battery1AR9");
		if(!values.contains(k)) {
			System.out.println("BATTERY1A9 cant find contains key "+i);
			throw new Exception("BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		System.out.println(k);
		System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKV.lastKey(KeySet.class); // key
		System.out.println("Battery1AR10");
		if(!values.contains(k)) {
			System.out.println("BATTERY1AR10 cant find last key "+i);
			throw new Exception("BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println(k);
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixKV.size(KeySet.class);
		System.out.println("Battery1AR101");
		if( bits != values.size() ) {
			System.out.println("BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("BATTERY1AR101 size mismatch "+bits+" should be "+i);
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
		Comparable c = (Comparable) RelatrixKV.firstKey(KeySet.class);
		if( c != null ) {
			Iterator<?> its = RelatrixKV.findTailMapKV(c);
			System.out.println("Battery1AR12");
			int i = 0;
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<KeySet, DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
				DBKey db = indexTable.getByInstance(nexe.getValue()); // get the DBKey for this instance integer
				KeySet keyset = (KeySet) indexTable.getByIndex(nexe.getValue());
				if(nexe.getKey().compareTo(keyset) != 0 || nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}

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
		Comparable c = (Comparable) RelatrixKV.lastKey(KeySet.class);
		if(c != null) {
			Iterator<?> its = RelatrixKV.findHeadMapKV(c);
			System.out.println("Battery1AR14");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<KeySet,DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
				DBKey db = indexTable.getByInstance(nexe.getValue()); // get the DBKey for this instance 
				if(nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}
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
		System.out.println("CleanDB");
		long s = RelatrixKV.size(DBKey.class);
		Iterator it = RelatrixKV.keySet(DBKey.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("DBKey remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		// remove payload reverse index
		s = RelatrixKV.size(KeySet.class);
		it = RelatrixKV.keySet(KeySet.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("KeySet remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		s = RelatrixKV.size(String.class);
		it = RelatrixKV.keySet(String.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKV.remove((Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("String remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = RelatrixKV.size(DBKey.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKV.entrySet(DBKey.class);
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				//System.out.println(i+"="+nex);
				System.out.println("RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
			System.out.println("RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
