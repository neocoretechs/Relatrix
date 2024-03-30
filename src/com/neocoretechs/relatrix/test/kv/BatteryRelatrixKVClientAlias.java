package com.neocoretechs.relatrix.test.kv;

import java.util.Map;

import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteEntrySetKVIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapIterator;
import com.neocoretechs.relatrix.client.RemoteHeadMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteKVIterator;
import com.neocoretechs.relatrix.client.RemoteKeySetIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapIterator;
import com.neocoretechs.relatrix.client.RemoteSubMapKVIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapIterator;
import com.neocoretechs.relatrix.client.RemoteTailMapKVIterator;

/**
 * Client side test of KV server. Yes, this should be a nice JUnit fixture someday.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The set of tests verifies the higher level 'transactionalStore' and 'findSet' functors in the Relatrix, which can be used
 * as examples of Relatrix processing.
 * NOTES:
 * start server RelatrixKVServer.
 * A database unique to this test module should be used. When starting the server to allow alias, a default tablespace isnt necessary.
 * program argument is node of local client, node server is running on, port of server and remote tablespace alias designator.
 * i.e. java BatteryRelatrixKVClientAlias localnode remotenode 9010 "C:/etc/db/test"
 * @author jg (C) 2020,2022
 *
 */
public class BatteryRelatrixKVClientAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static String alias1 = "ALIAS1";
	static String alias2 = "ALIAS2";
	static String alias3 = "ALIAS3";
	private static int numLookupByValue = 10;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 4) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientAlias <DB local client NODE> <DB remote server node> <DB PORT> <server_directory_path_to_tablespace_alias");
			System.exit(1);
		}
		rkvc = new RelatrixKVClient(argv[0], argv[1], Integer.parseInt(argv[2]));
		String tablespace = argv[3];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		if(rkvc.getAlias(alias1) == null)
			rkvc.setAlias(alias1,tablespace+alias1);
		if(rkvc.getAlias(alias2) == null)
			rkvc.setAlias(alias2,tablespace+alias2);
		if(rkvc.getAlias(alias3) == null)
			rkvc.setAlias(alias3,tablespace+alias3);
		battery1(argv);	
		battery11(argv);
		battery1AR6(argv);
		battery1AR6A(argv);
		battery1AR6B(argv);
		battery1AR7(argv);
		battery1AR8(argv);
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(argv);
		battery1AR12(argv);
		battery1AR13(argv);
		battery1AR14(argv);
		battery1AR15(argv);
		battery1AR16(argv);
		battery1AR17(alias1);
		battery1AR17(alias2);
		battery1AR17(alias3);
		System.out.println("BatteryRelatrixKVClientAlias TEST BATTERY COMPLETE.");
		rkvc.close();
		
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) rkvc.size(alias1, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias1+" DB of "+j+" elements.");
			battery1AR17(alias1);
		}
		j = (int) rkvc.size(alias2, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias2+" DB of "+j+" elements.");
			battery1AR17(alias2);
		}
		j = (int) rkvc.size(alias3, String.class);
		if(j > 0) {
			System.out.println("Cleaning "+alias3+" DB of "+j+" elements.");
			battery1AR17(alias3);
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			try {
				rkvc.store(alias1, fkey+alias1, new Long(i));
				rkvc.store(alias2, fkey+alias2, new Long(i));
				rkvc.store(alias3, fkey+alias3, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String[] argv) throws Exception {
		System.out.println("KV Battery11 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
				Object o1 = rkvc.get(alias1,fkey+alias1);
				Object o2 = rkvc.get(alias2,fkey+alias2);
				Object o3 = rkvc.get(alias3,fkey+alias3);
				//System.out.println(i+"="+o1+", "+o2+", "+o3);
				if(i != ((Long)o1).intValue() || i != ((Long)o2).intValue() || i != ((Long)o3).intValue()) {
					System.out.println("RANGE KEY MISMATCH for 'get':"+i+" - "+o1+" or "+o2+" or "+o3);
					++recs;
				}
		}
		if( recs > 0) {
			System.out.println("KV BATTERY11 FAIL, failed to get "+recs);
			throw new Exception("KV BATTERY11 FAIL, failed to get "+recs);
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteEntrySetKVIterator its = (RemoteEntrySetKVIterator) rkvc.entrySet(alias1, String.class);
		System.out.println("KV Battery1AR6 "+alias1);
		while(rkvc.hasNext(its)) {
			Object nex =  rkvc.next(its);
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
	public static void battery1AR6A(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteEntrySetKVIterator its = (RemoteEntrySetKVIterator) rkvc.entrySet(alias2, String.class);
		System.out.println("KV Battery1AR6A "+alias2);
		while(rkvc.hasNext(its)) {
			Object nex =  rkvc.next(its);
			Entry enex = (Entry)nex;
			//System.out.println(i+"="+nex);
			if(((Long)enex.getValue()).intValue() != i)
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+nex);
			else
				++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR6A unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR6B(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteEntrySetKVIterator its = (RemoteEntrySetKVIterator) rkvc.entrySet(alias3, String.class);
		System.out.println("KV Battery1AR6B "+alias3);
		while(rkvc.hasNext(its)) {
			Object nex =  rkvc.next(its);
			Entry enex = (Entry)nex;
			//System.out.println(i+"="+nex);
			if(((Long)enex.getValue()).intValue() != i)
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+nex);
			else
				++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR6B unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6B SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet;
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		RemoteKVIterator its = (RemoteKVIterator) rkvc.keySet(alias1,String.class);
		RemoteKVIterator itt = (RemoteKVIterator) rkvc.keySet(alias2,String.class);
		RemoteKVIterator itu = (RemoteKVIterator) rkvc.keySet(alias3,String.class);
		System.out.println("KV Battery1AR7");
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			String nex1 = (String) its.next();
			String nex2 = (String) itt.next();
			String nex3 = (String) itu.next();
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1) ||
				Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2) ||
				Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3))
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
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
	public static void battery1AR8(String[] argv) throws Exception {
		int i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(alias1, fkey+alias1);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(alias2, fkey+alias2);
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
			boolean bits = rkvc.containsValue(alias3, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 "+alias3+"  unexpected cant find contains value "+j);
				//throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(alias3, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 "+alias3+" unexpected cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
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
		Object k = rkvc.firstKey(alias1, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias1)) {
			System.out.println(alias1+" KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(alias1, String.class);
		if( ks != i) {
			System.out.println(alias1+" KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		k = rkvc.firstKey(alias2, String.class); // first key
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias2)) {
			System.out.println(alias2+" KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		ks = (long) rkvc.firstValue(alias2, String.class);
		if( ks != i) {
			System.out.println(alias2+" KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		k = rkvc.firstKey(alias3, String.class); // first key
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias3)) {
			System.out.println(alias3+" KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		ks = (long) rkvc.firstValue(alias3, String.class);
		if( ks != i) {
			System.out.println(alias3+" KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
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
		Object k = rkvc.lastKey(alias1,String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias1)) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(alias1,String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		k = rkvc.lastKey(alias2,String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias2)) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		ks = (long)rkvc.lastValue(alias2,String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		k = rkvc.lastKey(alias3,String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias3)) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		ks = (long)rkvc.lastValue(alias3,String.class);
		if( ks != i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
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
		long bits = rkvc.size(alias1, String.class);
		System.out.println("KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		bits = rkvc.size(alias2, String.class);
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		bits = rkvc.size(alias2, String.class);
		System.out.println("KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteKVIterator its = (RemoteKVIterator) rkvc.findTailMap(alias1, fkey);
		RemoteKVIterator itt = (RemoteKVIterator) rkvc.findTailMap(alias2, fkey);
		RemoteKVIterator itu = (RemoteKVIterator) rkvc.findTailMap(alias3, fkey);
		System.out.println("KV Battery1AR11");
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			String nex1 = (String) its.next();
			String nex2 = (String) itt.next();
			String nex3 = (String) itu.next();
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
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
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteTailMapKVIterator its = (RemoteTailMapKVIterator) rkvc.findTailMapKV(alias1,fkey);
		RemoteTailMapKVIterator itt = (RemoteTailMapKVIterator) rkvc.findTailMapKV(alias2,fkey);
		RemoteTailMapKVIterator itu = (RemoteTailMapKVIterator) rkvc.findTailMapKV(alias3,fkey);
		System.out.println("KV Battery1AR12");
		while(rkvc.hasNext(its) && rkvc.hasNext(itt) && rkvc.hasNext(itu)) {
			Comparable nex1 = (Comparable) rkvc.next(its);
			Comparable nex2 = (Comparable) rkvc.next(itt);
			Comparable nex3 = (Comparable) rkvc.next(itu);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
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
	public static void battery1AR13(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapIterator its = (RemoteHeadMapIterator) rkvc.findHeadMap(alias1,fkey);
		RemoteHeadMapIterator itt = (RemoteHeadMapIterator) rkvc.findHeadMap(alias2,fkey);
		RemoteHeadMapIterator itu = (RemoteHeadMapIterator) rkvc.findHeadMap(alias3,fkey);
		//System.out.println(its+", "+itt+", "+itu);
		System.out.println("KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		while(rkvc.hasNext(its) && rkvc.hasNext(itt) && rkvc.hasNext(itu)) {
			Comparable nex1 = (Comparable) rkvc.next(its);
			Comparable nex2 = (Comparable) rkvc.next(itt);
			Comparable nex3 = (Comparable) rkvc.next(itu);
			//System.out.println(i+"="+nex1+", "+nex2+", "+nex3);
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		RemoteHeadMapKVIterator its = (RemoteHeadMapKVIterator) rkvc.findHeadMapKV(alias1,fkey);
		RemoteHeadMapKVIterator itt = (RemoteHeadMapKVIterator) rkvc.findHeadMapKV(alias2,fkey);
		RemoteHeadMapKVIterator itu = (RemoteHeadMapKVIterator) rkvc.findHeadMapKV(alias3,fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		while(rkvc.hasNext(its) && rkvc.hasNext(itt) && rkvc.hasNext(itu)) {
			Comparable nex1 = (Comparable) rkvc.next(its);
			Comparable nex2 = (Comparable) rkvc.next(itt);
			Comparable nex3 = (Comparable) rkvc.next(itu);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR15(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteSubMapIterator its = (RemoteSubMapIterator) rkvc.findSubMap(alias1, fkey, tkey);
		RemoteSubMapIterator itt = (RemoteSubMapIterator) rkvc.findSubMap(alias2, fkey, tkey);
		RemoteSubMapIterator itu = (RemoteSubMapIterator) rkvc.findSubMap(alias3, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		while(rkvc.hasNext(its) && rkvc.hasNext(itt) && rkvc.hasNext(itu)) {
			Comparable nex1 = (Comparable) rkvc.next(its);
			Comparable nex2 = (Comparable) rkvc.next(itt);
			Comparable nex3 = (Comparable) rkvc.next(itu);
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR16(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		RemoteKVIterator its = (RemoteKVIterator) rkvc.findSubMapKV(alias1, fkey, tkey);
		RemoteKVIterator itt = (RemoteKVIterator) rkvc.findSubMapKV(alias2, fkey, tkey);
		RemoteKVIterator itu = (RemoteKVIterator) rkvc.findSubMapKV(alias3, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		while(rkvc.hasNext(its) && rkvc.hasNext(itt) && rkvc.hasNext(itu)) {
			Comparable nex1 = (Comparable) rkvc.next(its);
			Comparable nex2 = (Comparable) rkvc.next(itt);
			Comparable nex3 = (Comparable) rkvc.next(itu);
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17");
		RemoteKeySetIterator its = (RemoteKeySetIterator) rkvc.keySet(alias12, String.class);
		System.out.println("KV Battery1AR7");
		long timx = System.currentTimeMillis();
		while(its.hasNext()) {
			String fkey = (String) its.next();
			rkvc.remove(alias12, fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
			// Map.Entry
			if(rkvc.contains(alias12, fkey)) { 
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+fkey);
				throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+fkey);
			}
		}
		its.close();
		long siz = rkvc.size(alias12, String.class);
		if(siz > 0) {
			RemoteEntrySetKVIterator ets = (RemoteEntrySetKVIterator) rkvc.entrySet(alias12, String.class);
			while(rkvc.hasNext(ets)) {
				Object nex = rkvc.next(ets);
				System.out.println(nex);
			}
			ets.close();
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
