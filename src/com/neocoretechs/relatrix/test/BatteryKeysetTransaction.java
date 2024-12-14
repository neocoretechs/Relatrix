package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.rocksack.iterator.Entry;

import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.DomainMapRange;

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
	static int max = 5;
	static int numDelete = 100; // for delete test
	static ArrayList<DomainMapRange> keys = new ArrayList<DomainMapRange>();

	static TransactionId xid;
	static ConcurrentHashMap<DBKey, Comparable> dbtable = new ConcurrentHashMap<DBKey, Comparable>();
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
		//battery1AR4(argv);
		battery1AR4A(argv);
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
			r = String.format(uniqKeyFmt, i+1);
			
			DomainMapRange identity = new DomainMapRange();
			identity.setTransactionId(xid);
			// mirrors partial Relatrix store
			// DomainMapRange is annotated to DomainMapRange
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			DBKey dbkey = identity.store(d,m,r);
			if(!DBKey.isValid(dbkey)) {
				System.out.println("Identity store element key "+dbkey+" not valid due to:"+DBKey.whyInvalid(dbkey));
				throw new Exception("Identity store element key "+dbkey+" not valid due to:"+DBKey.whyInvalid(dbkey));
			}
			//identity.setRangeKey(DBKey.newKey(xid,IndexResolver.getIndexInstanceTable(),r)); // form it as template for duplicate key search
			// re-create it, now that we know its valid, in a form that stores the components with DBKeys
			// and maintains the classes stores in IndexInstanceTable for future commit.
			dbtable.put(dbkey, identity);
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("DBKey stored "+recs+" "+identity);
				timx = System.currentTimeMillis();
			}
			if( DEBUG  )
				System.out.println("Relatrix.store stored :"+identity);
			++recs;
		}
		System.out.println("---DBtable---");
		dbtable.forEach((k,v)->{System.out.println(k+" "+v);});
		RelatrixTransaction.commit(xid);
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
		DomainMapRange prev = (DomainMapRange) RelatrixKVTransaction.firstKey(xid,DomainMapRange.class);
		System.out.println("firstKey="+prev);
		Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,(Comparable) prev);
		System.out.println("Battery1AR4");
		while(its.hasNext()) {
			Map.Entry<DomainMapRange, DBKey> nexe = (Map.Entry<DomainMapRange,DBKey>)its.next();
			if(cnt > 0 && nexe.getKey().compareTo(prev) <= 0) { // should always be >
				System.out.println("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
				throw new Exception("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
			}
			prev = nexe.getKey();
			prev.setDBKey(nexe.getValue());
			if(!DBKey.isValid(nexe.getValue())) {
				System.out.println("Keys table element from tailMap iterator "+nexe.getValue()+" not valid due to:"+DBKey.whyInvalid(nexe.getValue()));
				throw new Exception("Keys table element from tailMap iterator "+nexe.getValue()+" not valid due to:"+DBKey.whyInvalid(nexe.getValue()));
			}
			keys.add(prev);
			if(DEBUG)
				System.out.println("1AR4 "+(cnt)+"="+nexe);
			++cnt;
		}
		if(keys.size() != max) {
			System.out.println("Size  MISMATCH: "+keys.size()+" max:"+max);
			throw new Exception("Size  MISMATCH: "+keys.size()+" max:"+max);
		}
		System.out.println("---Instance keys---");
		keys.forEach(j->{System.out.println(j);});
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. obtained "+keys.size());
	}
	/**
	 * Alternate test to load keys table from DBKey
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR4A(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		DBKey prev = (DBKey) RelatrixKVTransaction.firstKey(xid,DBKey.class);
		DomainMapRange pk = null;
		System.out.println("firstKey="+prev);
		Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,(Comparable) prev);
		System.out.println("Battery1AR4A");
		while(its.hasNext()) {
			Map.Entry<DBKey, Comparable> nexe = (Map.Entry<DBKey, Comparable>)its.next();
			if(cnt > 0 && nexe.getKey().compareTo(prev) <= 0) { // should always be >
				System.out.println("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
				throw new Exception("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
			}
			prev = nexe.getKey();
			Object o = nexe.getValue();	
			if(!DBKey.isValid(prev)) {
				System.out.println("Keys table element from tailMap iterator "+prev+" not valid due to:"+DBKey.whyInvalid(prev));
				throw new Exception("Keys table element from tailMap iterator "+prev+" not valid due to:"+DBKey.whyInvalid(prev));
			}
			if(o instanceof DomainMapRange) {
				pk = (DomainMapRange) o;
				pk.setDBKey(prev);
				keys.add(pk);
			}
			if(DEBUG)
				System.out.println("1AR4A "+(cnt)+"="+nexe);
			++cnt;
		}
		if(keys.size() != max) {
			System.out.println("Size  MISMATCH: "+keys.size()+" max:"+max);
			throw new Exception("Size  MISMATCH: "+keys.size()+" max:"+max);
		}
		System.out.println("---DBKey keys---");
		keys.forEach(j->{System.out.println(j);});
		 System.out.println("BATTERY1AR4A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. obtained "+keys.size());
	}
	/**
	 * Make sure we can resolve the stored keys IndexResolver. Iterates the keys table we built earlier,
	 * uses the resolver to get the DomainMapRange pointed to by iterated DBKey, make
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR44(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR44");
		DomainMapRange pk;
		// first make sure our tables coincide
		Iterator<?> its = keys.iterator();
		if(its != null) {
			while(its.hasNext()) {
				DomainMapRange nex = (DomainMapRange) its.next();
				if(nex.getDBKey() == null) {
					System.out.println("KEY ERROR, DBKey null in mirror: "+nex+" at "+cnt);
					throw new Exception("KEY ERROR DBKey null in mirror: "+nex+" at "+cnt);
				}
				if(!DBKey.isValid(nex.getDBKey())) {
					System.out.println("Keys table element "+nex.getDBKey()+" not valid due to:"+DBKey.whyInvalid(nex.getDBKey()));
					throw new Exception("Keys table element "+nex.getDBKey()+" not valid due to:"+DBKey.whyInvalid(nex.getDBKey()));
				}
				if(dbtable.get(nex.getDBKey()) == null) {
					System.out.println("Did NOT find element "+cnt+":"+nex.getDBKey()+" in dbtable of "+dbtable.size()+" tables dont match.");
					throw new Exception("Did NOT find element "+cnt+":"+nex.getDBKey()+" in dbtable of "+dbtable.size()+" tables dont match.");
				}
				++cnt;
			}
			// proceed to work with verified tables
			cnt = 0;
			its = keys.iterator();
			while(its.hasNext()) {
				DomainMapRange nex = (DomainMapRange) its.next();
				pk = (DomainMapRange) IndexResolver.getIndexInstanceTable().getByIndex(xid,nex.getDBKey()); 
				// if we didnt resolve it, see if its in the table we built that mirrors what should be in db
				if( pk == null ) {
					if(dbtable.get(nex.getDBKey()) != null)
						System.out.println("Found element "+nex.getDBKey()+" in dbtable of "+dbtable.size());
					else
						System.out.println("Did NOT find element "+nex.getDBKey()+" in dbtable of "+dbtable.size());
					throw new Exception("IndexResolver for "+nex+" returned null at "+cnt);
				}
				if(pk.getDBKey() == null) {
					System.out.println("KEY ERROR, DBKey null from resolved: "+nex+" for "+pk+" at "+cnt);
					throw new Exception("KEY ERROR DBKey null from resolved: "+nex+" for "+pk+" at "+cnt);
				}
				// get it from our mirrored table by DbKey, and make sure it matches
				// this should verify everything
				Object pk2 = dbtable.get(nex.getDBKey());
				if(pk2 == null) {
					System.out.println("Failed to locate DBKey in mirror table: "+nex+" at "+cnt+" but resolved "+pk);
					throw new Exception("Failed to locate DBKey in mirror table: "+nex+" at "+cnt+" but resolved "+pk);
				}
				if(((Comparable)pk2).compareTo(pk) != 0) {
					System.out.println("KEY MISMATCH: "+nex+" for "+pk+" at "+cnt);
					throw new Exception("KEY MISMATCH: "+nex+" for "+pk+" at "+cnt);
				}
				++cnt;
				if(DEBUG)
					System.out.println("1AR44 "+(cnt)+"="+nex);
			}
		} else {
			throw new Exception("Iterator returned null");
		}
		
		 System.out.println("BATTERY1AR44 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.entrySet on DomainMapRange
	 * we then get by index from IndexInstanceTable, giving us an instance, then compare it to iterated element.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String[] argv) throws Exception {
		int cnt = 0;
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixKVTransaction.entrySet(xid,DomainMapRange.class);
		System.out.println("Battery1AR5");
		if(its != null) {
			while(its.hasNext()) {
				Entry nex = (Entry) its.next();
				i = IndexResolver.getIndexInstanceTable().getByIndex(xid,(DBKey) nex.getValue()); 
				if( i == null ) {
					if(dbtable.get(nex.getValue()) != null)
						System.out.println("Found element in dbtable");
					else
						System.out.println("Did NOT find element in dbtable)");
					throw new Exception("IndexResolver for "+nex+" returned null at "+cnt);
				}
				if(((Comparable)i).compareTo(nex.getKey()) != 0) {
					System.out.println("RANGE KEY MISMATCH: "+nex+" for "+i+" at "+cnt);
					throw new Exception("RANGE KEY MISMATCH: "+nex+" for "+i+" at "+cnt);
				}
				++cnt;
				if(DEBUG)
					System.out.println("1AR5 "+(cnt)+"="+nex);
			}
		} else {
			throw new Exception("Iterator returned null");
		}
		
		 System.out.println("BATTERY1AR5 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * 
	 * Testing of firstKey on DomainMapRange, make sure its the first in the array we built of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Comparable k = (Comparable) RelatrixKVTransaction.firstKey(xid,DomainMapRange.class); // first key
		((DomainMapRange)k).getDomainKey();
		((DomainMapRange)k).getMapKey();
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
		Comparable k = (Comparable) RelatrixKVTransaction.lastKey(xid,DomainMapRange.class); // key
		((DomainMapRange)k).getDomainKey();
		((DomainMapRange)k).getMapKey();
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
		long bits = RelatrixKVTransaction.size(xid,DomainMapRange.class);
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
		Comparable c = (Comparable) RelatrixKVTransaction.firstKey(xid,DomainMapRange.class);
		if( c != null ) {
			Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,c);
			System.out.println("Battery1AR12");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<DomainMapRange, DBKey> nexe = (Map.Entry<DomainMapRange,DBKey>)nex;
				DBKey db = IndexResolver.getIndexInstanceTable().getByInstance(xid,nexe.getKey()); // get the DBKey for this instance integer
				DomainMapRange keyset = (DomainMapRange) IndexResolver.getIndexInstanceTable().getByIndex(xid,nexe.getValue());
				if(nexe.getKey().compareTo(keyset) != 0 || nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RANGE KEY MISMATCH:"+nex);
					throw new Exception("RANGE KEY MISMATCH:"+nex);
				}
				if(DEBUG)
					System.out.println("1AR12 "+(cnt++)+"="+nexe);
			}
		} else {
			System.out.println("firstKey on DomainMapRange came back null");
			throw new Exception("firstKey on DomainMapRange came back null");
		}
		System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	
	/**
	 * findHeadMapKV for DomainMapRange instances, perform getByInstance on key of each iterated entry
	 * and compare the DBKey of iterated entry to resolved key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		Comparable c = (Comparable) RelatrixKVTransaction.lastKey(xid,DomainMapRange.class);
		if(c != null) {
			Iterator<?> its = RelatrixKVTransaction.findHeadMapKV(xid,c);
			System.out.println("Battery1AR14");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<DomainMapRange,DBKey> nexe = (Map.Entry<DomainMapRange,DBKey>)nex;
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
			System.out.println("lastKey on DomainMapRange came back null");
			throw new Exception("lastKey on DomainMapRange came back null");
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
			Iterator<?> its = RelatrixKVTransaction.keySet(xid,DomainMapRange.class);
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
