package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;



import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.TransactionId;

/**
 * Yes, this should be a nice JUnit fixture someday. Test of embedded KV server with alias.
 * NOTE: rather than a database, specify only the PATH for the series of databases that will be 
 * designated ALIAS1java.lang.String, ALIAS2java.lang.String and ALIAS3java.lang.String<p/>
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * NOTES:
 * The database aliases define db names and program argument defines tablespace, alias is prepended for fully qualified tablespace names
 * C:/users/you/Relatrix should be valid path as program arg. C:/users/you/Relatrix/ALIAS1java.lang.String through ALIAS3... will be created.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2023
 */
public class BatteryRelatrixKVTransactionAlias {
	public static boolean DEBUG = false;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 1) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVTransactionAlias <directory_tablespace_path>");
			System.exit(1);
		}
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixKV.setAlias(alias1,tablespace+alias1);
		RelatrixKV.setAlias(alias2,tablespace+alias2);
		RelatrixKV.setAlias(alias3,tablespace+alias3);
		TransactionId xid = RelatrixKVTransaction.getTransactionId();
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
		battery1AR17(alias1, xid);
		battery1AR17(alias2, xid);
		battery1AR17(alias3, xid);
		battery18(xid);
		RelatrixKVTransaction.commit(alias1, xid);
		RelatrixKVTransaction.commit(alias2, xid);
		RelatrixKVTransaction.commit(alias3, xid);
		 System.out.println("BatteryRelatrixKVTransactionAlias TEST BATTERY COMPLETE.");
		RelatrixKVTransaction.endTransaction(xid);
		RelatrixKVTransaction.endTransaction(xid);
		RelatrixKVTransaction.endTransaction(xid);
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * Ensure that we start with known baseline number of keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(TransactionId xid) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) RelatrixKVTransaction.size(alias1, xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias1+" "+RelatrixKV.getAlias(alias1)+" of "+j+" elements.");
			battery1AR17(alias1, xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid, fkey+alias1, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		//
		j = (int) RelatrixKVTransaction.size(alias2, xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias2+" "+RelatrixKV.getAlias(alias2)+" of "+j+" elements.");
			battery1AR17(alias2, xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias2, xid, fkey+alias2, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		//
		j = (int) RelatrixKVTransaction.size(alias3, xid, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias3+" "+RelatrixKV.getAlias(alias3)+" of "+j+" elements.");
			battery1AR17(alias3, xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias3, xid, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	

	/**
	 * Tries to store partial key that should match existing keys, should reject all
	 * @param xid
	 * @throws Exception
	 */
	public static void battery11(TransactionId xid) throws Exception {
		System.out.println("KV Battery11 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
				Object o = RelatrixKVTransaction.get(alias1,xid,fkey+alias1);
				if(i != ((Long)o).intValue()) {
					System.out.println(alias1+" RANGE KEY MISMATCH for 'get':"+i+" - "+o);
					++recs;
				}
				o = RelatrixKVTransaction.get(alias2,xid,fkey+alias2);
				if(i != ((Long)o).intValue()) {
					System.out.println(alias2+" RANGE KEY MISMATCH for 'get':"+i+" - "+o);
					++recs;
				}
				o = RelatrixKVTransaction.get(alias3,xid,fkey+alias3);
				if(i != ((Long)o).intValue()) {
					System.out.println(alias3+" RANGE KEY MISMATCH for 'get':"+i+" - "+o);
					++recs;
				}
		}
		if( recs > 0) {
			System.out.println("KV BATTERY11 FAIL, failed to get "+recs);
		} else {
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
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
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR6(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its1 = RelatrixKVTransaction.entrySet(alias1,xid,String.class);
		Iterator<?> its2 = RelatrixKVTransaction.entrySet(alias2,xid,String.class);
		Iterator<?> its3 = RelatrixKVTransaction.entrySet(alias3,xid,String.class);
		System.out.println("KV Battery1AR6");
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			Entry nex1 = (Entry) its1.next();
			Entry nex2 = (Entry) its2.next();
			Entry nex3 = (Entry) its3.next();
			//System.out.println(i+"="+nex);
			if(((Long)nex1.getValue()).intValue() != i ||
				((Long)nex2.getValue()).intValue() != i	||
				((Long)nex3.getValue()).intValue() != i)
					System.out.println(alias1+" RANGE KEY MISMATCH:"+i+" - "+nex1+"| "+nex2+"| "+nex3);
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
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR7(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its1 = RelatrixKVTransaction.keySet(alias1, xid, String.class);
		Iterator<?> its2 = RelatrixKVTransaction.keySet(alias2, xid, String.class);
		Iterator<?> its3 = RelatrixKVTransaction.keySet(alias3, xid, String.class);
		System.out.println("KV Battery1AR7");
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			String nex1 = (String) its1.next();
			String nex2 = (String) its2.next();
			String nex3 = (String) its3.next();
			// Map.Entry
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1.getAlias()) ||
					Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2.getAlias()) ||
					Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3.getAlias()))
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
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
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR8(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		String fkey = String.format(uniqKeyFmt, i);
		boolean bits1 = RelatrixKVTransaction.contains(alias1,xid,fkey+alias1);
		boolean bits2 = RelatrixKVTransaction.contains(alias2,xid,fkey+alias2);
		boolean bits3 = RelatrixKVTransaction.contains(alias3,xid,fkey+alias3);
		System.out.println("KV Battery1AR8");
		if( !bits1 || !bits2 || !bits3 ) {
			System.out.println("KV BATTERY1A8 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
		}
		i = max-1;
		// careful here, have to do the conversion explicitly
		bits1 = RelatrixKVTransaction.containsValue(alias1, xid, String.class, (long)i);
		bits2 = RelatrixKVTransaction.containsValue(alias2, xid, String.class, (long)i);
		bits3 = RelatrixKVTransaction.containsValue(alias3, xid, String.class, (long)i);
		if( !bits1 || !bits2 || !bits3 ) {
			System.out.println("KV BATTERY1AR8 unexpected cant find contains key "+i);
			throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
		}
		 System.out.println("KV BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of first(), and firstValue
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR9(TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k1 = RelatrixKVTransaction.firstKey(alias1, xid, String.class); // first key
		Object k2 = RelatrixKVTransaction.firstKey(alias2, xid, String.class); // first key
		Object k3 = RelatrixKVTransaction.firstKey(alias3, xid, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt(((String)k1).substring(0,100)) != i || !((String)k1).endsWith(alias1.getAlias()) ||
				Integer.parseInt(((String)k2).substring(0,100)) != i || !((String)k2).endsWith(alias2.getAlias()) ||
				Integer.parseInt(((String)k3).substring(0,100)) != i || !((String)k3).endsWith(alias3.getAlias())) {
			System.out.println("KV BATTERY1A9 cant find first key "+i+" from "+k1+"|"+k2+"|"+k3);
			throw new Exception("KV BATTERY1AR9 unexpected cant find first key "+i);
		}
		long ks1 = (long) RelatrixKVTransaction.firstValue(alias1, xid,String.class);
		long ks2 = (long) RelatrixKVTransaction.firstValue(alias2, xid, String.class);
		long ks3 = (long) RelatrixKVTransaction.firstValue(alias3, xid, String.class);
		if( ks1 != i || ks2 != i || ks3 != i) {
			System.out.println("KV BATTERY1AR9 cant find first value from key "+i+" from "+ks1+"|"+ks2+"|"+ks3);
			throw new Exception("KV BATTERY1AR9 unexpected cant find first value from key "+i);
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
		Object k1 = RelatrixKVTransaction.lastKey(alias1, xid, String.class); // last key
		Object k2 = RelatrixKVTransaction.lastKey(alias2, xid, String.class); // last key
		Object k3 = RelatrixKVTransaction.lastKey(alias3, xid, String.class); // last key
		System.out.println("KV Battery1AR10");
		if( Integer.parseInt(((String)k1).substring(0,100)) != i || !((String)k1).endsWith(alias1.getAlias()) ||
				Integer.parseInt(((String)k2).substring(0,100)) != i || !((String)k2).endsWith(alias2.getAlias()) ||
				Integer.parseInt(((String)k3).substring(0,100)) != i || !((String)k3).endsWith(alias3.getAlias())) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i+" from "+k1+"|"+k2+"|"+k3);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last key "+i);
		}
		long ks1 = (long) RelatrixKVTransaction.lastValue(alias1,xid,String.class);
		long ks2 = (long) RelatrixKVTransaction.lastValue(alias2,xid,String.class);
		long ks3 = (long) RelatrixKVTransaction.lastValue(alias3,xid,String.class);
		if( ks1 != i || ks2 != i || ks3 != i) {
			System.out.println("KV BATTERY1AR10 cant find last value from key "+i+" from "+ks1+"|"+ks2+"|"+ks3);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last value from key "+i);
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
		long bits1 = RelatrixKVTransaction.size(alias1, xid, String.class);
		long bits2 = RelatrixKVTransaction.size(alias2, xid, String.class);
		long bits3 = RelatrixKVTransaction.size(alias3, xid, String.class);
		System.out.println("KV Battery1AR101");
		if( bits1 != i || bits2 != i || bits3 != i) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits1+"|"+bits2+"|"+bits3+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits1+"|"+bits2+"|"+bits3+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys indicated by partial key retrieval of numerical part
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR11(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator<?> its1 = RelatrixKVTransaction.findTailMap(alias1, xid, fkey);
		Iterator<?> its2 = RelatrixKVTransaction.findTailMap(alias2, xid, fkey);
		Iterator<?> its3 = RelatrixKVTransaction.findTailMap(alias3, xid, fkey);
		System.out.println("KV Battery1AR11");
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			String nex1 = (String) its1.next();
			String nex2 = (String) its2.next();
			String nex3 = (String) its3.next();
			// Map.Entry
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1.getAlias()) 
					|| Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2.getAlias()) 
					|| Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3.getAlias())) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR12(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator<?> its1 = RelatrixKVTransaction.findTailMapKV(alias1,xid,fkey);
		Iterator<?> its2 = RelatrixKVTransaction.findTailMapKV(alias2,xid,fkey);
		Iterator<?> its3 = RelatrixKVTransaction.findTailMapKV(alias3,xid,fkey);
		System.out.println("KV Battery1AR12");
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			Comparable nex1 = (Comparable) its1.next();
			Comparable nex2 = (Comparable) its2.next();
			Comparable nex3 = (Comparable) its3.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias1.getAlias()) 
					|| Integer.parseInt(nexf.getKey().substring(0,100)) != i || !nexf.getKey().endsWith(alias2.getAlias()) 
					|| Integer.parseInt(nexg.getKey().substring(0,100)) != i || !nexg.getKey().endsWith(alias3.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR13(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator<?> its1 = RelatrixKVTransaction.findHeadMap(alias1,xid,fkey);
		Iterator<?> its2 = RelatrixKVTransaction.findHeadMap(alias2,xid,fkey);
		Iterator<?> its3 = RelatrixKVTransaction.findHeadMap(alias3,xid,fkey);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			String nex1 = (String) its1.next();
			String nex2 = (String) its2.next();
			String nex3 = (String) its3.next();
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1.getAlias()) ||
					Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2.getAlias()) ||
					Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3.getAlias())) {
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
				throw new Exception("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 *  findHeadMapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR14(TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator<?> its1 = RelatrixKVTransaction.findHeadMapKV(alias1, xid, fkey);
		Iterator<?> its2 = RelatrixKVTransaction.findHeadMapKV(alias2, xid, fkey);
		Iterator<?> its3 = RelatrixKVTransaction.findHeadMapKV(alias3, xid, fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			Comparable nex1 = (Comparable) its1.next();
			Comparable nex2 = (Comparable) its2.next();
			Comparable nex3 = (Comparable) its3.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias1.getAlias()) 
					|| Integer.parseInt(nexf.getKey().substring(0,100)) != i || !nexf.getKey().endsWith(alias2.getAlias()) 
					|| Integer.parseInt(nexg.getKey().substring(0,100)) != i || !nexg.getKey().endsWith(alias3.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
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
		Iterator<?> its1 = RelatrixKVTransaction.findSubMap(alias1, xid, fkey, tkey);
		Iterator<?> its2 = RelatrixKVTransaction.findSubMap(alias2, xid, fkey, tkey);
		Iterator<?> its3 = RelatrixKVTransaction.findSubMap(alias3, xid, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			String nex1 = (String) its1.next();
			String nex2 = (String) its2.next();
			String nex3 = (String) its3.next();
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1.getAlias()) ||
					Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2.getAlias()) ||
					Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
				throw new Exception("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+nex1+"|"+nex2+"|"+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
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
		Iterator<?> its1 = RelatrixKVTransaction.findSubMapKV(alias1, xid, fkey, tkey);
		Iterator<?> its2 = RelatrixKVTransaction.findSubMapKV(alias2, xid, fkey, tkey);
		Iterator<?> its3 = RelatrixKVTransaction.findSubMapKV(alias3, xid, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		while(its1.hasNext() && its2.hasNext() && its3.hasNext()) {
			Comparable nex1 = (Comparable) its1.next();
			Comparable nex2 = (Comparable) its2.next();
			Comparable nex3 = (Comparable) its3.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if(Integer.parseInt(nexe.getKey().substring(0,100)) != i || !nexe.getKey().endsWith(alias1.getAlias()) 
					|| Integer.parseInt(nexf.getKey().substring(0,100)) != i || !nexf.getKey().endsWith(alias2.getAlias()) 
					|| Integer.parseInt(nexg.getKey().substring(0,100)) != i || !nexg.getKey().endsWith(alias3.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
				throw new Exception("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+nexe+"|"+nexf+"|"+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param alias12
	 * @param xid 
	 * @throws Exception
	 */
	public static void battery1AR17(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17 for alias:"+alias12);
		System.out.println("CleanDB");
		long s = RelatrixKVTransaction.size(alias12, xid, String.class);
		Iterator it = RelatrixKVTransaction.keySet(alias12, xid, String.class);
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			RelatrixKVTransaction.remove(alias12, xid, (Comparable) fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}

		long siz = RelatrixKVTransaction.size(alias12, xid, String.class);
		if(siz > 0) {
			Iterator<?> its = RelatrixKVTransaction.entrySet(alias12, xid, String.class);
			while(its.hasNext()) {
				Comparable nex = (Comparable) its.next();
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery18(TransactionId xid) throws Exception {
		System.out.println("KV Battery18 ");
		int max1 = max - 50000;
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max1; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid, fkey+alias1, new Long(i));
				RelatrixKVTransaction.store(alias2, xid, fkey+alias2, new Long(i));
				RelatrixKVTransaction.store(alias3, xid, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		long s = RelatrixKVTransaction.size(alias1, xid, String.class);
		if(s != max1)
			System.out.println("Size at halway point of restore incorrect:"+s+" should be "+max1);
		for(int i = max1; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				RelatrixKVTransaction.store(alias1, xid, fkey+alias1, new Long(i));
				RelatrixKVTransaction.store(alias2, xid, fkey+alias2, new Long(i));
				RelatrixKVTransaction.store(alias3, xid, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY18 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records in 3 alias, rejected "+dupes+" dupes.");
	}

}
