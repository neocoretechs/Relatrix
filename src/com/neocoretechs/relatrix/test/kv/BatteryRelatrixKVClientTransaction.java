package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;


import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.TransactionId;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;

/**
 * Client side test of transaction KV server using {@link RelatrixKVClientTransaction}. Yes, this should be a nice JUnit fixture someday.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals.<p/>
 * The distinction between methods like findTailMap and findTailMapKV is just one of
 * returning the keys, or the keys and values. Some performance gains can be realized by just retrieving
 * needed data.
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.<p/>
 * NOTES:
 * start server: java com.neocoretechs.relatrix.server.RelatrixKVTransactionServer D:/etc/Relatrix/db/test DBMACHINE 9010 <p/>
 * would start the server on the node called DBMACHINE using port 9010 and the tablespace path D:/etc/Relatrix/db
 * for a series of databases such as D:/etc/Relatrix/db/testjava.lang.String etc.<p/>
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2022,2024
 */
public class BatteryRelatrixKVClientTransaction {
	public static boolean DEBUG = false;
	public static RelatrixKVClientTransaction rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	private static int dupes;
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 3) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientTransaction <DB local client NODE> <DB remote server node> <DB PORT>");
			System.exit(1);
		}
		rkvc = new RelatrixKVClientTransaction(argv[0], argv[1], Integer.parseInt(argv[2]));
		TransactionId xid = rkvc.getTransactionId();
		battery1(xid);	
		battery11(xid);
		battery1AR6(xid);
		battery1AR7(xid);
		battery1AR8(xid);
		battery1AR9(xid);
		battery1AR10(xid);
		battery1AR101(xid);
		battery1AR11(xid);
		battery1AR12(xid);
		battery1AR13(xid);
		battery1AR14(xid);
		battery1AR15(xid);
		battery1AR16(xid);
		battery1AR17(xid);
		battery18(xid);
		System.out.println("BatteryRelatrixKVClientTransaction TEST BATTERY COMPLETE.");
		rkvc.endTransaction(xid);
		rkvc.close();
		
	}
	/**
	 * Check the size of test database, if non zero, proceed to delete existing data.
	 * Loads up db for String.class on keys from min to max-1
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1(TransactionId xid) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) rkvc.size(xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning DB of "+j+" elements.");
			battery1AR17(xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(xid, fkey, new Long(i));
			++recs;
		}
		rkvc.commit(xid);
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Store another transaction then roll it back.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery11(TransactionId xid) throws Exception {
		System.out.println("KV Battery11 id "+xid);
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		TransactionId xid2 = rkvc.getTransactionId();
		for(int i = max; i < max*2; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(xid2, fkey, new Long(fkey));
			++recs;
		}
		if( recs > 0) {
			rkvc.rollback(xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. rolled back "+xid2);
		}
		rkvc.endTransaction(xid2);
	}
	
	/**
	 * Test the higher level functions in the RelatrixKV.
	 * public Set<Map.Entry<K,V>> entrySet()
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's iterator returns the entries in ascending key order. 
	 * from battery1 we should have min to max-1 keys
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR6(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.entrySet(xid, String.class);
		System.out.println("KV Battery1AR6 transaction "+xid+" entrySet "+(System.currentTimeMillis()-tims)+" ms.");
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
	 * Testing of Iterator<?> its = RelatrixKVTransaction.keySet for String.class;
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR7(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.keySet(xid, String.class);
		System.out.println("KV Battery1AR7 keySet "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			String nex = (String) its.next();
			// Map.Entry
			if(Integer.parseInt(nex) != i)
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
	 * Check contains key forward contains key backward and contains value for select subset
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR8(TransactionId xid) throws Exception {
		int i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(xid, fkey);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(xid, fkey);
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
			boolean bits = rkvc.containsValue(xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(xid, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of firstKey(), and firstValue()
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR9(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(xid, String.class); // first key
		System.out.println("KV Battery1AR9 firstKey "+(System.currentTimeMillis()-tims)+" ms.");
		if( Integer.parseInt((String)k) != i ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * test last and lastKey
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR10(TransactionId xid) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(xid, String.class); // key
		System.out.println("KV Battery1AR10 lastKey "+(System.currentTimeMillis()-tims)+" ms.");
		if( Long.parseLong((String) k) != (long)i ) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(xid, String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * test size
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR101(TransactionId xid) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = rkvc.size(xid, String.class);
		System.out.println("KV Battery1AR101 size "+(System.currentTimeMillis()-tims)+" ms.");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findTailMap test, basically tailmap returning keys
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR11(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its =  rkvc.findTailMap(xid, fkey);
		System.out.println("KV Battery1AR11 TailMap "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			String nex = (String) its.next();
			if(Integer.parseInt(nex) != i) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findTailMapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR12(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findTailMapKV(xid, fkey);
		System.out.println("KV Battery1AR12 TailMapKV "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR13(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findHeadMap(xid, fkey);
		System.out.println("KV Battery1AR13 HeadMap "+(System.currentTimeMillis()-tims)+" ms.");
		// with i at max, should catch them all
		i = min;
		while(its.hasNext()) {
			String nex = (String) its.next();
			if(Integer.parseInt(nex) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV- Returns a key/value view of the portion of this map whose keys are strictly less than toKey.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR14(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findHeadMapKV(xid, fkey);
		System.out.println("KV Battery1AR14 HeadMapKV "+(System.currentTimeMillis()-tims)+" ms.");
		i = min;
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR15(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Iterator its =  rkvc.findSubMap(xid, fkey, tkey);
		System.out.println("KV Battery1AR15 SubMap "+(System.currentTimeMillis()-tims)+" ms.");
		// with i at max, should catch them all
		while(its.hasNext()) {
			String nex = (String) its.next();
			if(Integer.parseInt(nex) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
				throw new Exception("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMapKV - Returns a key/value view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR16(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Iterator its = rkvc.findSubMapKV(xid, fkey, tkey);
		System.out.println("KV Battery1AR16 SubMapKV "+(System.currentTimeMillis()-tims)+" ms.");
		// with i at max, should catch them all
		while(its.hasNext()) {
			Comparable nex = (Comparable) its.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex;
			if(Integer.parseInt(nexe.getKey()) != i) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
				throw new Exception("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries using new transaction, commit new transaction. Check size, if non zero display entries then throw exception
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR17(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		TransactionId xid2 = rkvc.getTransactionId();
		System.out.println("KV Battery1AR17 current transaction:"+xid+" new transaction:"+xid2);
		Iterator its = rkvc.keySet(xid2,String.class);
		long timx = System.currentTimeMillis();
		while(its.hasNext()) {
			String fkey = (String) its.next();
			rkvc.remove(xid2,fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		rkvc.commit(xid2);
		long siz = rkvc.size(xid2, String.class);
		if(siz > 0) {
			Iterator itt = rkvc.entrySet(xid2, String.class);
			while(itt.hasNext()) {
				Object nex = itt.next();
				System.out.println(nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		rkvc.endTransaction(xid2);
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Store a subset of keys using new transaction, take a checkpoint, store from subset to max additional records
	 * then roll back to checkpoint. Get the last key, then check that it matches predicted last key and the size of current dataset is
	 * expected size, else throw exception. Then, roll back new transaction, effectively undoing all actions.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery18(TransactionId xid) throws Exception {
		System.out.println("KV Battery18 existing transaction "+xid);
		TransactionId xid2 = rkvc.getTransactionId();
		int max1 = max - (max/2);
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(xid2, fkey, new Long(i));
			++recs;
		}
		System.out.println("Checkpointing new transaction "+xid2);
		long ntim = System.currentTimeMillis();
		rkvc.checkpoint(xid2);
		System.out.println("Checkpointing new transaction took "+(System.currentTimeMillis()-ntim)+" .ms");
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(xid2, fkey, new Long(i));
			++recs;
		}
		ntim = System.currentTimeMillis();
		rkvc.rollbackToCheckpoint(xid2);
		System.out.println("Rollback to checkpoint took "+(System.currentTimeMillis()-ntim)+" .ms");
		String lkey = (String) rkvc.lastKey(xid2, String.class);
		if(Integer.parseInt(lkey.substring(0,100)) != max1-1 || rkvc.size(xid2, String.class) != max1) {
			System.out.println("KV Battery18 consistency mismatch: last record doesnt match predicted ");
			throw new Exception("KV Battery18 consistency mismatch: last record doesnt match predicted ");
		}
		ntim = System.currentTimeMillis();
		rkvc.rollback(xid2);
		System.out.println("Rollback of id "+xid2+" took "+(System.currentTimeMillis()-ntim)+" .ms");
		rkvc.endTransaction(xid2);
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored then rolled back "+recs+" records");
	}

	
}
