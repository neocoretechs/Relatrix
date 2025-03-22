package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;


import com.neocoretechs.rocksack.iterator.Entry;
import com.neocoretechs.relatrix.Alias;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteKVIterator;

/**
 * Client side test of KV server database alias using {@link RelatrixKVClient}. Yes, this should be a nice JUnit fixture someday.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals with
 * alias name appended to the end of the string to identify each dataset uniquely.<p/>
 * The distinction between methods like findTailMap and findTailMapKV is just one of
 * returning the keys, or the keys and values. Some performance gains can be realized by just retrieving
 * needed data.
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.<p/>
 * NOTES:
 * start server: java com.neocoretechs.relatrix.server.RelatrixKVServer DBMACHINE 9010 <p/>
 * would start the server on the node called DBMACHINE using port 9010. Note that no
 * tablespace path is specified since we are going to specify the aliases via the client, hence when starting the
 * client you would specify java com.neocoretechs.relatrix.text.kv.BatteryRelatrixKVClientAlias LOCALMACHINE DBMACHINE 9010 D:/etc/Relatrix/db
 * for a series of databases such as D:/etc/Relatrix/db/testjava.lang.String etc. using local node LOCALMACHINE remote node DBMACHINE port 9010<p/>
 * @author Jonathan Groff (C) NeoCoreTechs 2020,2022,2024
 */
public class BatteryRelatrixKVClientAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 10000;
	static int numDelete = 100; // for delete test
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
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
		if(rkvc.getAlias(alias1) == null)
			rkvc.setRelativeAlias(alias1);
		if(rkvc.getAlias(alias2) == null)
			rkvc.setRelativeAlias(alias2);
		if(rkvc.getAlias(alias3) == null)
			rkvc.setRelativeAlias(alias3);
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
	 * Check the size of test database, if non zero, proceed to delete existing data.
	 * Loads up on keys from min to max-1
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
			rkvc.store(alias1, fkey+alias1, new Long(i));
			rkvc.store(alias2, fkey+alias2, new Long(i));
			rkvc.store(alias3, fkey+alias3, new Long(i));
			++recs;
		}
		System.out.println("KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Perform a get on presumed keys, checking and verifying each
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
	 * public Set<Map.Entry<K,V>> entrySet() of class String for database alias1
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's iterator returns the entries in ascending key order. 
	 * from battery1 we should have min to max-1 keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.entrySet(alias1, String.class);
		System.out.println("KV Battery1AR6 "+alias1);
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
	 * Entry set of alias2 class String verifying each previously inserted entry.
	 */
	public static void battery1AR6A(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.entrySet(alias2, String.class);
		System.out.println("KV Battery1AR6A "+alias2);
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
			System.out.println("BATTERY1AR6A unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * entrySet of database alias3 class String verifying each entry
	 *
	 */
	public static void battery1AR6B(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its = rkvc.entrySet(alias3, String.class);
		System.out.println("KV Battery1AR6B "+alias3);
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
			System.out.println("BATTERY1AR6B unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6B SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = RelatrixKV.keySet for all 3 database aliases
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator its =  rkvc.keySet(alias1,String.class);
		Iterator itt =  rkvc.keySet(alias2,String.class);
		Iterator itu =  rkvc.keySet(alias3,String.class);
		System.out.println("KV Battery1AR7");
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			String nex1 = (String) its.next();
			String nex2 = (String) itt.next();
			String nex3 = (String) itu.next();
			if(Integer.parseInt(nex1.substring(0,100)) != i || !nex1.endsWith(alias1.getAlias()) ||
				Integer.parseInt(nex2.substring(0,100)) != i || !nex2.endsWith(alias2.getAlias()) ||
				Integer.parseInt(nex3.substring(0,100)) != i || !nex3.endsWith(alias3.getAlias()))
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
	 * perform contains for each entry in alias1 and alias2 and containsValue on subset of alias3
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
	 * Testing of firstKey and firstValue for all 3 alias
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(alias1, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias1.getAlias())) {
			System.out.println(alias1+" KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(alias1, String.class);
		if( ks != i) {
			System.out.println(alias1+" KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		k = rkvc.firstKey(alias2, String.class); // first key
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias2.getAlias())) {
			System.out.println(alias2+" KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		ks = (long) rkvc.firstValue(alias2, String.class);
		if( ks != i) {
			System.out.println(alias2+" KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		k = rkvc.firstKey(alias3, String.class); // first key
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias3.getAlias())) {
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
	 * test lastValue and lastKey on aliases
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(alias1,String.class); // key
		System.out.println("KV Battery1AR10");
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias1.getAlias())) {
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
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias2.getAlias())) {
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
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i || !((String)k).endsWith(alias3.getAlias())) {
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
	* Check size of all 3 alias
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
	 * findMap test, basically tailMap returning keys step through all 3 alias one
	 * record at a time simultaneously.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findTailMap(alias1, fkey);
		Iterator itt = rkvc.findTailMap(alias2, fkey);
		Iterator itu = rkvc.findTailMap(alias3, fkey);
		System.out.println("KV Battery1AR11");
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			String nex1 = (String) its.next();
			String nex2 = (String) itt.next();
			String nex3 = (String) itu.next();
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * tailmapKV obtain 3 iterators, step though key/value records for all 3 alias at once.
	 */
	public static void battery1AR12(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findTailMapKV(alias1,fkey);
		Iterator itt = rkvc.findTailMapKV(alias2,fkey);
		Iterator itu = rkvc.findTailMapKV(alias3,fkey);
		System.out.println("KV Battery1AR12");
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			Comparable nex1 = (Comparable) its.next();
			Comparable nex2 = (Comparable) itt.next();
			Comparable nex3 = (Comparable) itu.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey
	 * for all 3 alias one record at a time.
	 */
	public static void battery1AR13(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its = rkvc.findHeadMap(alias1,fkey);
		Iterator itt = rkvc.findHeadMap(alias2,fkey);
		Iterator itu = rkvc.findHeadMap(alias3,fkey);
		//System.out.println(its+", "+itt+", "+itu);
		System.out.println("KV Battery1AR13");
		i = min;
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			Comparable nex1 = (Comparable) its.next();
			Comparable nex2 = (Comparable) itt.next();
			Comparable nex3 = (Comparable) itu.next();
			//System.out.println(i+"="+nex1+", "+nex2+", "+nex3);
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV - Returns a view of the portion of this map whose key/value pairs are strictly less than toKey
	 * for all 3 alias one record at a time, so set key at max for retrieval.
	 */
	public static void battery1AR14(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Iterator its =  rkvc.findHeadMapKV(alias1,fkey);
		Iterator itt =  rkvc.findHeadMapKV(alias2,fkey);
		Iterator itu =  rkvc.findHeadMapKV(alias3,fkey);
		System.out.println("KV Battery1AR14");
		i = min;
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			Comparable nex1 = (Comparable) its.next();
			Comparable nex2 = (Comparable) itt.next();
			Comparable nex3 = (Comparable) itu.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * set lower bound at min, upper at max for the 3 alias then iterate through the 3.
	 */
	public static void battery1AR15(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Iterator its = rkvc.findSubMap(alias1, fkey, tkey);
		Iterator itt = rkvc.findSubMap(alias2, fkey, tkey);
		Iterator itu = rkvc.findSubMap(alias3, fkey, tkey);
		System.out.println("KV Battery1AR15");
		// with i at max, should catch them all
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			Comparable nex1 = (Comparable) its.next();
			Comparable nex2 = (Comparable) itt.next();
			Comparable nex3 = (Comparable) itu.next();
			if( Long.parseLong(((String)nex1).substring(0,100)) != (long)i || !((String)nex1).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nex2).substring(0,100)) != (long)i || !((String)nex2).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nex3).substring(0,100)) != (long)i || !((String)nex3).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nex1+" or "+nex2+" or "+nex3);
			}
			++i;
		}
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMapKV - Returns a view of the portion of this map whose key/value pairs range from fromKey, inclusive, to toKey, exclusive.
	 * set lower bound at min, upper at max.
	 */
	public static void battery1AR16(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		int i = min;
		int j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Iterator its = rkvc.findSubMapKV(alias1, fkey, tkey);
		Iterator itt = rkvc.findSubMapKV(alias2, fkey, tkey);
		Iterator itu = rkvc.findSubMapKV(alias3, fkey, tkey);
		System.out.println("KV Battery1AR16");
		// with i at max, should catch them all
		while(its.hasNext() && itt.hasNext() && itu.hasNext()) {
			Comparable nex1 = (Comparable) its.next();
			Comparable nex2 = (Comparable) itt.next();
			Comparable nex3 = (Comparable) itu.next();
			Map.Entry<String, Long> nexe = (Map.Entry<String,Long>)nex1;
			Map.Entry<String, Long> nexf = (Map.Entry<String,Long>)nex2;
			Map.Entry<String, Long> nexg = (Map.Entry<String,Long>)nex3;
			if( Long.parseLong(((String)nexe.getKey()).substring(0,100)) != (long)i || !((String)nexe.getKey()).endsWith(alias1.getAlias()) ||
				Long.parseLong(((String)nexf.getKey()).substring(0,100)) != (long)i || !((String)nexf.getKey()).endsWith(alias2.getAlias()) ||
				Long.parseLong(((String)nexg.getKey()).substring(0,100)) != (long)i || !((String)nexg.getKey()).endsWith(alias3.getAlias()) ) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
				throw new Exception("KV RANGE KEY MISMATCH:"+i+" - "+nexe+" or "+nexf+" or "+nexg);
			}
			++i;
		}
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries. Pass the Alias to operate upon.
	 * Obtain a keySet for String.class for the given alias, iterate each removing each new entry.
	 * At 5 second intervals, print the current record being removed. At the end, check the size
	 * and if greater than zero, get an entrySet and display the records not removed.
	 */
	public static void battery1AR17(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17");
		Iterator its = rkvc.keySet(alias12, String.class);
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
		long siz = rkvc.size(alias12, String.class);
		if(siz > 0) {
			Iterator ets = rkvc.entrySet(alias12, String.class);
			while(ets.hasNext()) {
				Object nex = ets.next();
				System.out.println(nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
