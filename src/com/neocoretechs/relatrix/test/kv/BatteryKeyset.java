package com.neocoretechs.relatrix.test.kv;

import java.io.IOException;
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
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.PrimaryKeySet;

/**
 * The set of tests verifies the lower level {@link KeySet} functions in the {@link  Relatrix}
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017
 *
 */
public class BatteryKeyset {
	public static boolean DEBUG = false;
	static KeySet keyset;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 10000;
	static int numDelete = 100; // for delete test
	static ArrayList<KeySet> keys = new ArrayList<KeySet>();
	static ArrayList<KeySet> findkeys = new ArrayList<KeySet>();
	static IndexInstanceTableInterface indexTable = new IndexInstanceTable();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryKeyset <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixKV.setTablespace(argv[0]);
		//battery1AR17(argv);
		battery1(argv);
		battery2(argv);
		battery1AR4(argv);
		battery1AR44(argv);
		battery1AR5(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR12(argv);
		battery1AR14(argv);
		//battery1AR17(argv);
		 System.out.println("BatteryKeyset TEST BATTERY COMPLETE.");
		
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
		String d = null;
		String m = null;
		String r = null;

		for(int i = min; i < max; i++) {
			d = String.format(uniqKeyFmt, i);
			m = String.format(uniqKeyFmt, i+1);
			r = String.format(uniqKeyFmt, i+2);
	
			PrimaryKeySet pks = new PrimaryKeySet();
			pks.setDomainKey(IndexResolver.getIndexInstanceTable().getKey(d));
			pks.setMapKey(IndexResolver.getIndexInstanceTable().getKey(m));
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			//if(Relatrix.isPrimaryKey(RelatrixKV.nearest(identity), identity)) {
			if(DBKey.isValid(pks.getDomainKey()) && DBKey.isValid(pks.getMapKey()) && RelatrixKV.get(pks) != null) {
				//throw new DuplicateKeyException("Duplicate key for relationship:"+identity);
				System.out.println("Duplicate key for relationship:"+pks);
				++dupes;
				continue;
			}
			KeySet identity = new KeySet();
			identity.setDomainKey(DBKey.newKey(indexTable, d));
			identity.setMapKey(DBKey.newKey(indexTable, m));
			//identity.setRangeKey(DBKey.nullDBKey);
			identity.setRangeKey(DBKey.newKey(indexTable,r)); // form it as template for duplicate key search
			// re-create it, now that we know its valid, in a form that stores the components with DBKeys
			// and maintains the classes stores in IndexInstanceTable for future commit.
			IndexResolver.getIndexInstanceTable().put(identity);
			if( DEBUG  )
				System.out.println("Relatrix.store stored :"+identity);
			++recs;
		}	
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	private static void battery2(String[] argv) throws IllegalAccessException, ClassNotFoundException, IOException {
		for(int i = min; i < max; i++) {
			String d = String.format(uniqKeyFmt, i);
			String m = String.format(uniqKeyFmt, i+1);
			KeySet identity = new KeySet();
			identity.setDomainKey(indexTable.getKey(d));
			identity.setMapKey(indexTable.getKey(m));
			identity.setRangeKey(new DBKey(DBKey.nullKey));
			//PrimaryKeySet pks = new PrimaryKeySet(identity);
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			//Object o = RelatrixKV.nearest(identity);
			//if(!Relatrix.isPrimaryKey(o, identity))
				//System.out.println("FAILED to find:"+identity+" found key="+o);
			Iterator<?> it = RelatrixKV.findTailMapKV(identity);
			int cnt = 0;
			while(it.hasNext()) {
				Object o = it.next();
				Map.Entry e = (Map.Entry)o;
				KeySet k = ((KeySet)e.getKey());
				if(k.domainKeyEquals(identity) && k.mapKeyEquals(identity)) {
					if(DEBUG)
						System.out.println("Found at "+cnt);
					break;
				}
				cnt++;
			}
		}
	}
	/**
	 * check order of DBKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR4(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		KeySet prev = (KeySet) RelatrixKV.firstKey(KeySet.class);
		Iterator<?> its = RelatrixKV.findTailMapKV((Comparable) prev);
		System.out.println("Battery1AR4");
		KeySet first = ((Map.Entry<KeySet,DBKey>)its.next()).getKey();
		findkeys.add(first); // skip first key we just got
		keys.add(first);
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<KeySet, DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
			if(nexe.getKey().compareTo(prev) <= 0) { // should always be >
			// Map.Entry
				System.out.println("RANGE KEY MISMATCH: "+nex);
				throw new Exception("RANGE KEY MISMATCH: "+nex);
			}
			prev = nexe.getKey();
			findkeys.add(nexe.getKey());
			keys.add(nexe.getKey());
			if(DEBUG)
				System.out.println("1AR4 "+(cnt++)+"="+nex);
		}
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR44(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR44");
		while(!findkeys.isEmpty()) {
			int rnd = new Random().nextInt(findkeys.size());
			KeySet ident = findkeys.get(rnd);
			findkeys.remove(rnd);

			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			if(RelatrixKV.nearest(ident) == null) {
				if(DEBUG)
					System.out.println("Didnt find "+ident);
				else
					throw new Exception("Didnt find "+ident);
			} else {
				if(DEBUG)
					System.out.println("FOUND "+ident);
			}
			
		}
		 System.out.println("BATTERY1AR44 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String[] argv) throws Exception {
		int cnt = 0;
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKV.entrySet(KeySet.class);
		System.out.println("Battery1AR5");
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			i =  indexTable.get((DBKey) nex.getValue()); 
			if(((Comparable)i).compareTo(nex.getKey()) != 0) {
				System.out.println("RANGE KEY MISMATCH: "+nex);
				throw new Exception("RANGE KEY MISMATCH: "+nex);
			}
			if(DEBUG)
				System.out.println("1AR5 "+(cnt++)+"="+nex);
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
		((KeySet)k).getDomainKey();
		((KeySet)k).getMapKey();
		((KeySet)k).getRangeKey();
		System.out.println("Battery1AR9 firstKey");
		if(!keys.contains(k)) {
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
		((KeySet)k).getDomainKey();
		((KeySet)k).getMapKey();
		((KeySet)k).getRangeKey();
		System.out.println("Battery1AR10 lastKey");
		if(!keys.contains(k)) {
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
		System.out.println("Battery1AR101 Size="+bits);
		if( bits != keys.size() ) {
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
		int cnt = 0;
		long tims = System.currentTimeMillis();
		Comparable c = (Comparable) RelatrixKV.firstKey(KeySet.class);
		if( c != null ) {
			Iterator<?> its = RelatrixKV.findTailMapKV(c);
			System.out.println("Battery1AR12");
			int i = 0;
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<KeySet, DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
				DBKey db = indexTable.getKey(nexe.getKey()); // get the DBKey for this instance integer
				KeySet keyset = (KeySet) indexTable.get(nexe.getValue());
				if(nexe.getKey().compareTo(keyset) != 0 || nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}
				if(DEBUG)
					System.out.println("1AR12 "+(cnt++)+"="+nexe);
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
		int cnt = 0;
		long tims = System.currentTimeMillis();
		Comparable c = (Comparable) RelatrixKV.lastKey(KeySet.class);
		if(c != null) {
			Iterator<?> its = RelatrixKV.findHeadMapKV(c);
			System.out.println("Battery1AR14");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<KeySet,DBKey> nexe = (Map.Entry<KeySet,DBKey>)nex;
				DBKey db = indexTable.getKey(nexe.getKey()); // get the DBKey for this instance 
				if(nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}
				if(DEBUG)
					System.out.println("1AR14 "+(cnt++)+"="+nexe);
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
		System.out.println("Cleaning DB of "+s+" elements.");
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
