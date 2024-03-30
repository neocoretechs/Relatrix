package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.RemoteEntrySetIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteHeadMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteKeySetIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteSubMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteSubMapKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteTailMapIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteTailMapKVIteratorTransaction;

/**
 * Transaction KV client test battery. Test of client side transaction KV server.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values, hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The set of tests verifies the higher level client side transactional store and 'findSet' functions in the KV Relatrix, 
 * which can be used as examples as well.
 * NOTES:
 * start server RelatrixKVTransactionServer.
 * A database unique to this test module should be used.
 * program argument is node of local client, node server is running on, port of server started with database of your choice.
 * @author Jonathan Groff (C) NeoCoreTechs 2022,2023
 *
 */
public class BatteryRelatrixKVClientTransactionAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClientTransaction rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	private static int dupes;
	private static int numLookupByValue = 10;
	static String alias1 = "ALIAS1";
	static String alias2 = "ALIAS2";
	static String alias3 = "ALIAS3";
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 4) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientTransactionAlias <DB local client NODE> <DB remote server node> <DB PORT> <remote tablespace prefix>");
			System.exit(1);
		}
		rkvc = new RelatrixKVClientTransaction(argv[0], argv[1], Integer.parseInt(argv[2]));
		String tablespace = argv[3];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		if(rkvc.getAlias(alias1) == null)
			rkvc.setAlias(alias1,tablespace+alias1);
		if(rkvc.getAlias(alias2) == null)
			rkvc.setAlias(alias2,tablespace+alias2);
		if(rkvc.getAlias(alias3) == null)
			rkvc.setAlias(alias3,tablespace+alias3);
		String xid = rkvc.getTransactionId();
		battery1(alias1,xid);
		battery1(alias2,xid);
		battery1(alias3,xid);	
		battery11(xid);
		battery1AR6(alias1,xid);
		battery1AR6(alias2,xid);
		battery1AR6(alias3,xid);
		battery1AR7(alias1,xid);
		battery1AR7(alias2,xid);
		battery1AR7(alias3,xid);
		battery1AR8(alias1,xid);
		battery1AR8(alias2,xid);
		battery1AR8(alias3,xid);
		battery1AR9(alias1,xid);
		battery1AR9(alias2,xid);
		battery1AR9(alias3,xid);
		battery1AR10(alias1,xid);
		battery1AR10(alias2,xid);
		battery1AR10(alias3,xid);
		battery1AR101(alias1,xid);
		battery1AR101(alias2,xid);
		battery1AR101(alias3,xid);
		battery1AR11(alias1,xid);
		battery1AR11(alias2,xid);
		battery1AR11(alias3,xid);
		battery1AR12(alias1,xid);
		battery1AR12(alias2,xid);
		battery1AR12(alias3,xid);
		battery1AR13(alias1,xid);
		battery1AR13(alias2,xid);
		battery1AR13(alias3,xid);
		battery1AR14(alias1,xid);
		battery1AR14(alias2,xid);
		battery1AR14(alias3,xid);
		battery1AR15(alias1,xid);
		battery1AR15(alias2,xid);
		battery1AR15(alias3,xid);
		battery1AR16(alias1,xid);
		battery1AR16(alias2,xid);
		battery1AR16(alias3,xid);
		battery1AR17(alias1,xid);
		battery1AR17(alias2,xid);
		battery1AR17(alias3,xid);
		battery18(alias1,xid);
		battery18(alias2,xid);
		battery18(alias3,xid);
		rkvc.removeAlias(alias1);
		rkvc.removeAlias(alias2);
		rkvc.removeAlias(alias3);
		System.out.println("BatteryRelatrixKVClientTransaction TEST BATTERY COMPLETE.");
		rkvc.endTransaction(xid);
		rkvc.close();
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String alias, String xid) throws Exception {
		System.out.println(alias+" KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) rkvc.size(alias, xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			battery1AR17(alias, xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias, xid, fkey+alias, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.commit(alias, xid);
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Store another transaction then roll it back.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String xid) throws Exception {
		System.out.println("KV Battery11 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		String xid2 = rkvc.getTransactionId();
		for(int i = max; i < max*2; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias1, xid2, fkey+alias1, new Long(fkey));
				rkvc.store(alias2, xid2, fkey+alias2, new Long(fkey));
				rkvc.store(alias3, xid2, fkey+alias3, new Long(fkey));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			rkvc.rollback(alias1, xid2);
			rkvc.rollback(alias2, xid2);
			rkvc.rollback(alias3, xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
		rkvc.endTransaction(xid2);
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
	public static void battery1AR6(String alias, String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.entrySet(alias, xid, String.class);
		System.out.println(alias+" KV Battery1AR6 ");
		while(its.hasNext()) {
			Object nex =  its.next();
			Entry enex = (Entry)nex;
			//System.out.println(i+"="+nex);
			if(((Long)enex.getValue()).intValue() != i)
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
	public static void battery1AR7(String alias, String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteKVIteratorTransaction its = (RemoteKVIteratorTransaction) rkvc.keySet(alias, xid, String.class);
		System.out.println(alias+" KV Battery1AR7");
		while(its.hasNext()) {
			String nex = (String) its.next();
			// Map.Entry
			if(Integer.parseInt(nex.substring(0,100)) != i || !nex.endsWith(alias))
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			else
				++i;
		}
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("KV BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String alias, String xid) throws Exception {
		int i = min;
		System.out.println(alias+" KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(alias, xid, fkey+alias);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(alias, xid, fkey+alias);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms." );
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = rkvc.containsValue(alias, xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(alias, xid, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Testing of first(), and firstValue
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String alias, String xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(alias, xid, String.class); // first key
		System.out.println(alias+" KV Battery1AR9");
		String ks = (String)k;
		if( Integer.parseInt(ks.substring(0,100)) != i || !ks.endsWith(alias)) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long kv = (long) rkvc.firstValue(alias, xid, String.class);
		if( kv != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @throws Exception
	 */
	public static void battery1AR10(String alias, String xid) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(alias, xid, String.class); // key
		String ks = (String)k;
		System.out.println(alias+" KV Battery1AR10");
		if( Long.parseLong(ks.substring(0,100)) != (long)i || !ks.endsWith(alias)) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long kv = (long)rkvc.lastValue(alias, xid, String.class);
		if( kv != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* test size
	* @throws Exception
	*/
	public static void battery1AR101(String alias, String xid) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = rkvc.size(alias, xid, String.class);
		System.out.println(alias+" KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @throws Exception
	 */
	public static void battery1AR11(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteKVIteratorTransaction its = (RemoteKVIteratorTransaction) rkvc.findTailMap(alias, xid, fkey+alias);
		System.out.println(alias+" KV Battery1AR11");
		while(its.hasNext()) {
			String nex = (String) its.next();
			// Map.Entry
			if(Integer.parseInt(nex.substring(0,100)) != i || !nex.endsWith(alias)) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailMapKVIteratorTransaction its = (RemoteTailMapKVIteratorTransaction) rkvc.findTailMapKV(alias, xid, fkey);
		System.out.println(alias+" KV Battery1AR12");
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR13(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapIteratorTransaction its = (RemoteHeadMapIteratorTransaction) rkvc.findHeadMap(alias, xid, fkey);
		System.out.println(alias+" KV Battery1AR13");
		i = min;
		while(rkvc.hasNext(xid, its)) {
			String nex = (String) rkvc.next(xid, its);
			if(Integer.parseInt(nex.substring(0,100)) != i || !nex.endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV Returns a view of the portion of this map whose key/value entries key part are strictly less than toKey.
	 * @throws Exception
	 */
	public static void battery1AR14(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapKVIteratorTransaction its = (RemoteHeadMapKVIteratorTransaction) rkvc.findHeadMapKV(alias, xid, fkey);
		System.out.println(alias+" KV Battery1AR14");
		i = min;
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @throws Exception
	 */
	public static void battery1AR15(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteKVIteratorTransaction its = (RemoteKVIteratorTransaction) rkvc.findSubMap(alias, xid, fkey, tkey);
		System.out.println(alias+" KV Battery1AR15");
		// with i at max, should catch them all
		while(its.hasNext()) {
			String nex = (String) its.next();
			if(Integer.parseInt(nex.substring(0,100)) != i || !nex.endsWith(alias)) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 */
	public static void battery1AR16(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubMapKVIteratorTransaction its = (RemoteSubMapKVIteratorTransaction) rkvc.findSubMapKV(alias, xid, fkey, tkey);
		System.out.println(alias+" KV Battery1AR16");
		while(rkvc.hasNext(xid, its)) {
			Comparable nex = (Comparable) rkvc.next(xid, its);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias)) {
			
				// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
				throw new Exception("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @throws Exception
	 */
	public static void battery1AR17(String alias, String xid) throws Exception {
		long tims = System.currentTimeMillis();
		String xid2 = rkvc.getTransactionId();
		System.out.println(alias+" KV Battery1AR17 for trans:"+xid2);
		RemoteKVIteratorTransaction its = (RemoteKVIteratorTransaction) rkvc.keySet(alias,xid2,String.class);
		long timx = System.currentTimeMillis();
		while(its.hasNext()) {
			String fkey = (String)its.next();
			rkvc.remove(alias,xid2,fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		its.close();
		long siz = rkvc.size(alias, xid2, String.class);
		if(siz > 0) {
			Iterator itt = rkvc.entrySet(alias, xid2, String.class);
			while(itt.hasNext()) {
				Object nex = itt.next();
				System.out.println(nex);
			}
			((RemoteIteratorTransaction)itt).close();
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		rkvc.commit(alias, xid2);
		rkvc.endTransaction(xid2);
		System.out.println("end trans:"+xid2+" now using:"+xid);
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @throws Exception
	 */
	public static void battery18(String alias, String xid) throws Exception {
		System.out.println(alias+" KV Battery18 ");
		String xid2 = rkvc.getTransactionId();
		int max1 = max - 50000;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias, xid2, fkey+alias, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println(alias+" Checkpointing..");
		rkvc.checkpoint(xid2);
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias, xid2, fkey+alias, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		rkvc.rollbackToCheckpoint(alias, xid2);
		String lkey = (String) rkvc.lastKey(alias, xid2, String.class);
		if(Integer.parseInt(lkey.substring(0,100)) != max1-1 || rkvc.size(alias, xid2, String.class) != max1) {
			System.out.println("KV Battery18 consistency mismatch: last record doesnt match predicted ");
			throw new Exception("KV Battery18 consistency mismatch: last record doesnt match predicted ");
		}
		rkvc.rollback(alias, xid2);
		rkvc.endTransaction(xid2);
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}

	
}
