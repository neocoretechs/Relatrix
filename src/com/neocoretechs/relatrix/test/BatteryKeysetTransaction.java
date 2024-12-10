package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.iterator.Entry;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.PrimaryKeySet;

/**
 * The set of tests verifies the lower level {@link KeySet} functions in the {@link  RelatrixTransaction}
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017
 */
public class BatteryKeysetTransaction {
	public static boolean DEBUG = false;
	static KeySet keyset;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;
	static int numDelete = 100; // for delete test
	static ArrayList<PrimaryKeySet> keys = new ArrayList<PrimaryKeySet>();
	static ArrayList<PrimaryKeySet> findkeys = new ArrayList<PrimaryKeySet>();
	static TransactionId xid;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryKeysetTransaction <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixKVTransaction.setTablespace(argv[0]);
		xid = RelatrixTransaction.getTransactionId();
		battery1AR17(argv);
		battery1(argv);
		battery1AR4(argv);
		battery1AR44(argv);
		battery1AR5(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR12(argv);
		battery1AR14(argv);
		RelatrixKVTransaction.commit(xid);
		RelatrixKVTransaction.endTransaction(xid);
		//battery1AR17(argv);
		 System.out.println("BatteryKeysetTransaction TEST BATTERY COMPLETE.");
		
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
		long timx = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String d = null;
		String m = null;
		String r = null;

		for(int i = min; i < max; i++) {
			d = String.format(uniqKeyFmt, i);
			m = String.format(uniqKeyFmt, i+1);
			r = String.format(uniqKeyFmt, i+2);
			PrimaryKeySet identity = new PrimaryKeySet();
			identity.setDomainKey(DBKey.newKey(xid,IndexResolver.getIndexInstanceTable(), d));
			identity.setMapKey(DBKey.newKey(xid,IndexResolver.getIndexInstanceTable(), m));
			identity.setTransactionId(xid);
			// mirrors partial Relatrix store
			// PrimaryKeySet is annotated to DomainMapRange
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			DBKey dbkey = identity.store();
			//identity.setRangeKey(DBKey.newKey(xid,IndexResolver.getIndexInstanceTable(),r)); // form it as template for duplicate key search
			// re-create it, now that we know its valid, in a form that stores the components with DBKeys
			// and maintains the classes stores in IndexInstanceTable for future commit.
			identity.setDBKey(dbkey);
			// this will store the key to its class table with identity as value, and the identity to its class
			// table with key as value
			IndexResolver.getIndexInstanceTable().put(xid, dbkey, identity);
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("DBKey stored "+recs+" "+identity);
				timx = System.currentTimeMillis();
			}
			if( DEBUG  )
				System.out.println("Relatrix.store stored :"+identity);
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
		int cnt = 0;
		long tims = System.currentTimeMillis();
		PrimaryKeySet prev = (PrimaryKeySet) RelatrixKVTransaction.firstKey(xid,PrimaryKeySet.class);
		System.out.println("firstKey="+prev);
		Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,(Comparable) prev);
		System.out.println("Battery1AR4");
		PrimaryKeySet first = null;
		if(its.hasNext())
			first =  ((Map.Entry<PrimaryKeySet,DBKey>)its.next()).getKey();
		else
			System.out.println("No next, expected second key");
		findkeys.add(first); // skip first key we just got
		keys.add(first);
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<PrimaryKeySet, DBKey> nexe = (Map.Entry<PrimaryKeySet,DBKey>)nex;
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
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. obtained "+findkeys.size());
	}
	/**
	 * Get a random element from the array we built in  findkeys from PrimaryKeySet
	 * remove it from array, make sure we can locate it. Do this until the array is empty
	 * and we verified we can locate them all.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR44(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR44");
		while(!findkeys.isEmpty()) {
			int rnd = new Random().nextInt(findkeys.size());
			PrimaryKeySet ident = findkeys.get(rnd);
			findkeys.remove(rnd);

			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			if(RelatrixKVTransaction.nearest(xid,ident) == null) {
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
	 * Testing of Iterator<?> its = RelatrixKV.entrySet on PrimaryKeySet
	 * we then get by index from IndexInstanceTable, giving us an instance, then compare it to iterated element.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String[] argv) throws Exception {
		int cnt = 0;
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKVTransaction.entrySet(xid,PrimaryKeySet.class);
		System.out.println("Battery1AR5");
		if(its != null) {
		while(its.hasNext()) {
			Entry nex = (Entry) its.next();
			i = IndexResolver.getIndexInstanceTable().getByIndex(xid,(DBKey) nex.getValue()); 
			if( i == null )
				throw new Exception("IndexResolver for "+nex+" returned null");
			if(((Comparable)i).compareTo(nex.getKey()) != 0) {
				System.out.println("RANGE KEY MISMATCH: "+nex+" for "+i);
				throw new Exception("RANGE KEY MISMATCH: "+nex+" for "+i);
			}
			if(DEBUG)
				System.out.println("1AR5 "+(cnt++)+"="+nex);
		}
		} else {
			throw new Exception("Iterator returned null");
		}
		
		 System.out.println("BATTERY1AR5 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * 
	 * Testing of firstKey on PrimaryKeySet, make sure its the first in the array we built of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKVTransaction.firstKey(xid,PrimaryKeySet.class); // first key
		((PrimaryKeySet)k).getDomainKey();
		((PrimaryKeySet)k).getMapKey();
		System.out.println("Battery1AR9 firstKey");
		if(!keys.contains(k)) {
			System.out.println("BATTERY1A9 cant find contains key "+i);
			throw new Exception("BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		if(keys.get(0).compareTo(k) != 0) {
			System.out.println("BATTERY1A9 presumed first key not at element 0 "+k+" "+keys.get(0));
			throw new Exception("BATTERY1A9 presumed first key not at element 0 "+k+" "+keys.get(0));
		}
		System.out.println(k);
		System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test lastKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKVTransaction.lastKey(xid,PrimaryKeySet.class); // key
		((PrimaryKeySet)k).getDomainKey();
		((PrimaryKeySet)k).getMapKey();
		System.out.println("Battery1AR10 lastKey");
		if(!keys.contains(k)) {
			System.out.println("BATTERY1AR10 cant find last key "+i);
			throw new Exception("BATTERY1AR10 unexpected cant find last of key "+i);
		}
		if(keys.get(keys.size()-1).compareTo(k) != 0) {
			System.out.println("BATTERY1A9 presumed last key not at element "+(keys.size()-1)+" for "+k+" "+keys.get(keys.size()-1));
			throw new Exception("BATTERY1A9 presumed last key not at element "+(keys.size()-1)+" for "+k+" "+keys.get(keys.size()-1));
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
		long bits = RelatrixKVTransaction.size(xid,PrimaryKeySet.class);
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
		Comparable c = (Comparable) RelatrixKVTransaction.firstKey(xid,PrimaryKeySet.class);
		if( c != null ) {
			Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,c);
			System.out.println("Battery1AR12");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<PrimaryKeySet, DBKey> nexe = (Map.Entry<PrimaryKeySet,DBKey>)nex;
				DBKey db = IndexResolver.getIndexInstanceTable().getByInstance(xid,nexe.getKey()); // get the DBKey for this instance integer
				PrimaryKeySet keyset = (PrimaryKeySet) IndexResolver.getIndexInstanceTable().getByIndex(xid,nexe.getValue());
				if(nexe.getKey().compareTo(keyset) != 0 || nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}
				if(DEBUG)
					System.out.println("1AR12 "+(cnt++)+"="+nexe);
			}
		} else {
			System.out.println("firstKey on PrimaryKeySet came back null");
			throw new Exception("firstKey on PrimaryKeySet came back null");
		}
		System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	
	/**
	 * findHeadMapKV for PrimaryKeySet instances, perform getByInstance on key of each iterated entry
	 * and compare the DBKey of iterated entry to resolved key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		Comparable c = (Comparable) RelatrixKVTransaction.lastKey(xid,PrimaryKeySet.class);
		if(c != null) {
			Iterator<?> its = RelatrixKVTransaction.findHeadMapKV(xid,c);
			System.out.println("Battery1AR14");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<PrimaryKeySet,DBKey> nexe = (Map.Entry<PrimaryKeySet,DBKey>)nex;
				DBKey db = IndexResolver.getIndexInstanceTable().getByInstance(xid,nexe.getKey()); // get the DBKey for this instance 
				if(nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RESOLVED KEY MISMATCH:"+nex+" with resolved key "+db);
					throw new Exception("RESOLVED KEY MISMATCH:"+nex+" with resolved key "+db);
				}
				if(DEBUG)
					System.out.println("1AR14 "+(cnt++)+"="+nexe);
			}
		} else {
			System.out.println("lastKey on PrimaryKeySet came back null");
			throw new Exception("lastKey on PrimaryKeySet came back null");
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
		long s = RelatrixKVTransaction.size(xid,DBKey.class);
		System.out.println("Cleaning DB of "+s+" elements.");
		Iterator<?> it = RelatrixKVTransaction.keySet(xid,DBKey.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKVTransaction.remove(xid,(Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("DBKey remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		s = RelatrixKVTransaction.size(xid,String.class);
		it = RelatrixKVTransaction.keySet(xid,String.class);
		timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKVTransaction.remove(xid,(Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("String remove "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = RelatrixKVTransaction.size(xid,DBKey.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKVTransaction.keySet(xid,PrimaryKeySet.class);
			while(its.hasNext()) {
				Object fkey = it.next();
				Object o = RelatrixKVTransaction.remove(xid,(Comparable) fkey);
				if((System.currentTimeMillis()-timx) > 5000) {
					System.out.println("DomainMapRange remove "+o);
					timx = System.currentTimeMillis();
				}
			}
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
