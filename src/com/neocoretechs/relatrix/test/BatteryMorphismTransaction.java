package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


import com.neocoretechs.rocksack.iterator.Entry;

import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.rocksack.TransactionId;
import com.neocoretechs.relatrix.key.DBKey;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.relatrix.key.KeySet;
import com.neocoretechs.relatrix.key.PrimaryKeySet;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixKVTransaction;

/**
 * The set of tests verifies the lower level {@link KeySet} {@link PrimaryKeySet} functions in the {@link  RelatrixTransaction} <p/>
 * Verifies the {@link IndexResolver} and storage of the main {@link DBKey} identity and {@link Relation} tables,
 * which also partially tests the abstract {@link AbstractRelation} class.<p/>
 * We are going to load up a table of Relation instances and a map of [DBKey,Relation] that mirrors the
 * DBKey identity class table that we prepare as we store the initial dataset, and use these to compare the stored data
 * throughout the balance of testing.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017
 */
public class BatteryMorphismTransaction {
	public static boolean DEBUG = false;
	static KeySet keyset;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static ArrayList<Relation> keys = new ArrayList<Relation>();

	static TransactionId xid;
	static ConcurrentHashMap<DBKey, Comparable> dbtable = new ConcurrentHashMap<DBKey, Comparable>();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.BatteryMorphismTransaction <directory_tablespace_path>");
			System.exit(1);
		}
		RelatrixTransaction.setTablespace(argv[0]);
		xid = RelatrixTransaction.getTransactionId();
		battery1AR17(argv);
		battery1(argv);
		// load keys table from Relation class instance, which is the concrete subclass of PrimaryKeySet
		battery1AR4(argv);
		battery1AR44(argv);
		battery1AR5(argv);
		battery1AR55(argv);
		battery1AR101(argv);
		// now do alternate keys table loadout retrieving from DBKey class and repeat tests comparing tables with stored data
		keys.clear();
		battery1AR4A(argv);
		battery1AR44(argv);
		// 5 and 55 dont involve keys table, only dbtable
		battery1AR101(argv);
		// and perform balance of testing
		battery1AR12(argv);
		battery1AR14(argv);
		RelatrixTransaction.commit(xid);
		RelatrixTransaction.endTransaction(xid);
		//battery1AR17(argv);
		 System.out.println("BatteryMorphismTransaction TEST BATTERY COMPLETE.");
		 System.exit(0);	
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
			// Relatrix store
			// Relation is annotated to Relation
			// check for domain/map match
			// Enforce categorical structure; domain->map function uniquely determines range.
			// If the search winds up at the key or the key is empty or the domain->map exists, the key
			// cannot be inserted
			Relation mo = RelatrixTransaction.store(xid,d,m,r);
			DBKey dbkey = mo.getIdentity();
			if(!DBKey.isValid(dbkey)) {
				System.out.println("Identity store element key "+dbkey+" not valid due to:"+DBKey.whyInvalid(dbkey));
				throw new Exception("Identity store element key "+dbkey+" not valid due to:"+DBKey.whyInvalid(dbkey));
			}
			// store in mirror table
			dbtable.put(dbkey, mo);
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("DBKey stored "+recs+" "+mo);
				timx = System.currentTimeMillis();
			}
			if( DEBUG  )
				System.out.println("Relatrix.store stored :"+mo);
			++recs;
		}
		if(DEBUG) {
			System.out.println("---DBtable---");
			dbtable.forEach((k,v)->{System.out.println(k+" "+v);});
		}
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
		Relation prev = (Relation) RelatrixTransaction.firstKey(xid,Relation.class);
		System.out.println("firstKey="+prev);
		Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,(Comparable) prev);
		System.out.println("Battery1AR4");
		while(its.hasNext()) {
			Map.Entry<Relation, DBKey> nexe = (Map.Entry<Relation,DBKey>)its.next();
			if(cnt > 0 && nexe.getKey().compareTo(prev) <= 0) { // should always be >
				System.out.println("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
				throw new Exception("RANGE KEY MISMATCH: "+nexe+" prev:"+prev);
			}
			prev = nexe.getKey();
			prev.setIdentity(nexe.getValue());
			prev.setTransactionId(xid);
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
		if(DEBUG) {
			System.out.println("---Instance keys---");
			keys.forEach(j->{System.out.println(j);});
		}
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
		DBKey prev = (DBKey) RelatrixTransaction.firstKey(xid,DBKey.class);
		Relation pk = null;
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
			if(o instanceof Relation) {
				pk = (Relation) o;
				pk.setIdentity(prev);
				pk.setTransactionId(xid);
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
		if(DEBUG) {
			System.out.println("---DBKey keys---");
			keys.forEach(j->{System.out.println(j);});
		}
		 System.out.println("BATTERY1AR4A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. obtained "+keys.size());
	}
	/**
	 * Iterate the stored tables twice, the first time verifying that the data shows up in the mirror map
	 * and the identity DBKey is valid, the next iteration uses the resolver from the identity key
	 * and compares the instance data to the mirror table.
	 * Make sure we can resolve the stored keys via IndexResolver. Iterates the keys table we built earlier,
	 * uses the resolver to get the Relation pointed to by iterated DBKey.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR44(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR44");
		Relation pk;
		// first make sure our tables coincide
		Iterator<?> its = keys.iterator();
		if(its != null) {
			while(its.hasNext()) {
				Relation nex = (Relation) its.next();
				if(nex.getIdentity() == null) {
					System.out.println("KEY ERROR, DBKey null in mirror: "+nex+" at "+cnt);
					throw new Exception("KEY ERROR DBKey null in mirror: "+nex+" at "+cnt);
				}
				if(!DBKey.isValid(nex.getIdentity())) {
					System.out.println("Keys table element "+nex.getIdentity()+" not valid due to:"+DBKey.whyInvalid(nex.getIdentity()));
					throw new Exception("Keys table element "+nex.getIdentity()+" not valid due to:"+DBKey.whyInvalid(nex.getIdentity()));
				}
				if(dbtable.get(nex.getIdentity()) == null) {
					System.out.println("Did NOT find element "+cnt+":"+nex.getIdentity()+" in dbtable of "+dbtable.size()+" tables dont match.");
					throw new Exception("Did NOT find element "+cnt+":"+nex.getIdentity()+" in dbtable of "+dbtable.size()+" tables dont match.");
				}
				++cnt;
			}
			// proceed to work with verified tables
			System.out.println("...Continuing test with IndexResolver at "+(System.currentTimeMillis()-tims)+" ms.");
			cnt = 0;
			its = keys.iterator();
			while(its.hasNext()) {
				Relation nex = (Relation) its.next();
				pk = (Relation) IndexResolver.getIndexInstanceTable().get(xid,nex.getIdentity()); 
				// if we didnt resolve it, see if its in the table we built that mirrors what should be in db
				if( pk == null ) {
					if(dbtable.get(nex.getIdentity()) != null)
						System.out.println("Found element "+nex.getIdentity()+" in dbtable of "+dbtable.size());
					else
						System.out.println("Did NOT find element "+nex.getIdentity()+" in dbtable of "+dbtable.size());
					throw new Exception("IndexResolver for "+nex+" returned null at "+cnt);
				}
				if(pk.getIdentity() == null) {
					System.out.println("KEY ERROR, DBKey null from resolved: "+nex+" for "+pk+" at "+cnt);
					throw new Exception("KEY ERROR DBKey null from resolved: "+nex+" for "+pk+" at "+cnt);
				}
				// get it from our mirrored table by DbKey, and make sure it matches
				// this should verify everything
				Object pk2 = dbtable.get(nex.getIdentity());
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
	 * Testing of Iterator<?> its = RelatrixKV.entrySet on Relation
	 * we then get by index from IndexInstanceTable, giving us an instance, then compare it to iterated element.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR5(String[] argv) throws Exception {
		int cnt = 0;
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixTransaction.entrySet(xid,Relation.class);
		System.out.println("Battery1AR5");
		if(its != null) {
			while(its.hasNext()) {
				Entry nex = (Entry) its.next();
				i = IndexResolver.getIndexInstanceTable().get(xid,(DBKey) nex.getValue()); 
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
	 * Testing of Iterator<?> its = RelatrixTransaction.entrySet on Relation
	 * Should produce a set of morphisms with resolved identities etc.
	 * we then get by index from IndexInstanceTable, giving us an instance, then compare it to iterated element.
	 * Compare resolved identity to tables to verify those as well
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR55(String[] argv) throws Exception {
		int cnt = 0;
		Object i;
		long tims = System.currentTimeMillis();
		Iterator<?> its = RelatrixTransaction.entrySet(xid, Relation.class);
		System.out.println("Battery1AR55");
		if(its != null) {
			while(its.hasNext()) {
				Entry nex = (Entry) its.next();
				i = IndexResolver.getIndexInstanceTable().get(xid,(DBKey) nex.getValue()); 
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
				// make sure identity is valid
				if(((Relation)nex.getKey()).getIdentity() == null) {
					System.out.println("Relation identity is null for "+nex.getKey()+" at "+cnt);
					throw new Exception("Relation identity is null for "+nex.getKey()+" at "+cnt);
				}
				Relation dmr = ((Relation)nex.getKey());
				if(dbtable.get(dmr.getIdentity()).compareTo(dmr) != 0) {
					System.out.println("Table instance does not match retrieved instance:"+dmr+" -- "+dbtable.get(dmr.getIdentity())+" at "+cnt);
					throw new Exception("Table instance does not match retrieved instance:"+dmr+" -- "+dbtable.get(dmr.getIdentity())+" at "+cnt);
				}
				++cnt;
				if(DEBUG)
					System.out.println("1AR55 "+(cnt)+"="+nex);
			}
		} else {
			throw new Exception("Iterator returned null");
		}
		
		 System.out.println("BATTERY1AR55 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	* test size
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = RelatrixTransaction.size(xid,Relation.class);
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
		Comparable c = (Comparable) RelatrixTransaction.firstKey(xid,Relation.class);
		if( c != null ) {
			Iterator<?> its = RelatrixKVTransaction.findTailMapKV(xid,c);
			System.out.println("Battery1AR12");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<Relation, DBKey> nexe = (Map.Entry<Relation,DBKey>)nex;
				DBKey db = IndexResolver.getIndexInstanceTable().getKey(xid,nexe.getKey()); // get the DBKey for this instance integer
				Relation keyset = (Relation) IndexResolver.getIndexInstanceTable().get(xid,nexe.getValue());
				if(nexe.getKey().compareTo(keyset) != 0 || nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("COMPARISON KEY MISMATCH:"+nex+" ["+db+","+keyset+"]");
					throw new Exception("COMPARISON KEY MISMATCH:"+nex+" ["+db+","+keyset+"]");
				}
				if(DEBUG)
					System.out.println("1AR12 "+(cnt++)+"="+nexe);
			}
		} else {
			System.out.println("firstKey on Relation came back null");
			throw new Exception("firstKey on Relation came back null");
		}
		System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV for Relation instances, perform getByInstance on key of each iterated entry
	 * and compare the DBKey of iterated entry to resolved key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		int cnt = 0;
		long tims = System.currentTimeMillis();
		Comparable c = (Comparable) RelatrixTransaction.lastKey(xid,Relation.class);
		if(c != null) {
			Iterator<?> its = RelatrixKVTransaction.findHeadMapKV(xid,c);
			System.out.println("Battery1AR14");
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				Map.Entry<Relation,DBKey> nexe = (Map.Entry<Relation,DBKey>)nex;
				DBKey db = IndexResolver.getIndexInstanceTable().getKey(xid,nexe.getKey()); // get the DBKey for this instance 
				if(nexe.getValue().compareTo(db) != 0) {
					// Map.Entry
					System.out.println("RESOLVED KEY MISMATCH:"+nex+" with resolved key "+db);
					throw new Exception("RESOLVED KEY MISMATCH:"+nex+" with resolved key "+db);
				}
				if(DEBUG)
					System.out.println("1AR14 "+(cnt++)+"="+nexe);
			}
		} else {
			System.out.println("lastKey on Relation came back null");
			throw new Exception("lastKey on Relation came back null");
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
		int i = 0;
		long s = RelatrixTransaction.size(xid);
		System.out.println(" Cleaning DB of "+s+" elements. for xid:"+xid);
		long timx = System.currentTimeMillis();
		Iterator<?> it = RelatrixTransaction.findSet(xid, '*', '*', '*');
		while(it.hasNext()){
			Result fkey = (Result) it.next();
			RelatrixTransaction.remove(xid,(Comparable) fkey.get(0));
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println("remove "+i+" "+fkey.get(0));
				timx = System.currentTimeMillis();
			}
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
