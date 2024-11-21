package com.neocoretechs.relatrix.test.kv;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.client.RelatrixKVClient;

import com.neocoretechs.rocksack.Alias;

/**
 * Client side test of streams in KV server database alias using {@link RelatrixKVClient}. 
 * Yes, this should be a nice JUnit fixture someday.<p/>
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
public class BatteryRelatrixKVClientStreamAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClient rkvc;

	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	// database aliases
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	private static int numLookupByValue = 10;
	private static long timx;
	/**
	* Main test fixture driver. Prepare the alias definitions then call methods repeatedly
	* for each given alias.
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 4) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientStreamAlias <DB local client NODE> <DB remote server node> <DB PORT> <server_directory_path_to_tablespace_alias");
			System.exit(1);
		}
		System.out.println("local="+argv[0]+" remote="+argv[1]+" port="+argv[2]);
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
		battery1(alias1);	// build and store
		battery1(alias2);	// build and store
		battery1(alias3);	// build and store
		battery11(alias1);
		battery11(alias2);
		battery11(alias3);
		battery1AR6(alias1);
		battery1AR6(alias2);
		battery1AR6(alias3);
		battery1AR7(alias1);
		battery1AR7(alias2);
		battery1AR7(alias3);
		battery1AR8(argv); // search by value, slow operation no key
		battery1AR9(argv);
		battery1AR10(argv);
		battery1AR101(argv);
		battery1AR11(alias1);
		battery1AR11(alias2);
		battery1AR11(alias3);
		battery1AR12(alias1);
		battery1AR12(alias2);
		battery1AR12(alias3);
		battery1AR13(alias1);
		battery1AR13(alias2);
		battery1AR13(alias3);
		battery1AR14(alias1);
		battery1AR14(alias2);
		battery1AR14(alias3);
		battery1AR15(alias1);
		battery1AR15(alias2);
		battery1AR15(alias3);
		battery1AR16(alias1);
		battery1AR16(alias2);
		battery1AR16(alias3);
		battery1AR17(alias1);
		battery1AR17(alias2);
		battery1AR17(alias3);
		System.out.println("BatteryRelatrixKVClientStream TEST BATTERY COMPLETE.");
		rkvc.close();
		
	}
	/**
	 * Checks existing database size for passed alias. If non zero perform a clean operation to remove all elements.
	 * proceed to store test keys of key String.format(uniqKeyFmt, i)+alias, value Long(i) from min to max.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1(Alias alias12) throws Exception {
		System.out.println("KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		long j = rkvc.size(alias12, String.class);
		if(j > 0) {
			System.out.println(alias12+" Cleaning DB of "+j+" elements.");
			battery1AR17(alias12);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(alias12, fkey+alias12, new Long(i));
			++recs;
		}
		System.out.println(alias12+" KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Perform a get on each presumed stored key from min to max for passed alias. Verify against increment.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery11(Alias alias12) throws Exception {
		System.out.println(alias12+" KV Battery11 ");
		long tims = System.currentTimeMillis();
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			Object o = rkvc.get(alias12, fkey+alias12);
			if(i != ((Long)o).intValue()) {
				System.out.println("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
				throw new Exception("RANGE KEY MISMATCH for 'get':"+i+" - "+o);
			}
		}
		System.out.println(alias12+" KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Testing of entrySetStream for String.class for passed alias stored data and verify 
	 * each entry starts at min and increments properly.
	 * Throw exception if we do not end at max.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR6(Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = rkvc.entrySetStream(alias12, String.class);
		System.out.println(alias12+" KV Battery1AR6");
		stream.forEach(e ->{
			if(((Map.Entry<String,Long>)e).getValue() != i) {
				System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println(alias12+" BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of keySetStream starting at min for whole of stored dataset for passed alias;
	 * @param alias32
	 * @throws Exception
	 */
	public static void battery1AR7(Alias alias32) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = rkvc.keySetStream(alias32, String.class);
		System.out.println(alias32+" KV Battery1AR7");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias32.getAlias())) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			} else
				++i;
		});
		if( i != max ) {
			System.out.println("KV BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("KV BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("KV BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of contains forward and backward against stored data for each alias, and containsValue for a subset of the data
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv) throws Exception {
		i = min;
		System.out.println("KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits1 = rkvc.contains(alias1, fkey+alias1);
			boolean bits2 = rkvc.contains(alias2, fkey+alias2);
			boolean bits3 = rkvc.contains(alias3, fkey+alias3);
			if( !bits1 || !bits2 || !bits3 ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits1 = rkvc.contains(alias1, fkey+alias1);
				boolean bits2 = rkvc.contains(alias2, fkey+alias2);
				boolean bits3 = rkvc.contains(alias3, fkey+alias3);
				if( !bits1 || !bits2 || !bits3 ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits1 = rkvc.containsValue(alias1, String.class, (long)j);
			boolean bits2 = rkvc.containsValue(alias2, String.class, (long)j);
			boolean bits3 = rkvc.containsValue(alias3, String.class, (long)j);
			if( !bits1 || !bits2 || !bits3 ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue +" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits1 = rkvc.containsValue(alias1, String.class, (long)j);
				boolean bits2 = rkvc.containsValue(alias2, String.class, (long)j);
				boolean bits3 = rkvc.containsValue(alias3, String.class, (long)j);
				if( !bits1 || !bits2 || !bits3 ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of first, and firstValue for each alias databse of String.class, verify they are at min
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(alias1, String.class); // first key
		Object j = rkvc.firstKey(alias2, String.class); // first key
		Object l = rkvc.firstKey(alias3, String.class); // first key
		System.out.println("KV Battery1AR9");
		if( Integer.parseInt(((String)k).substring(0,100)) != i  || !((String)k).endsWith(alias1.getAlias()) ||
				Integer.parseInt(((String)j).substring(0,100)) != i  || !((String)j).endsWith(alias2.getAlias()) ||
				Integer.parseInt(((String)l).substring(0,100)) != i  || !((String)l).endsWith(alias3.getAlias())) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i+" found "+k+", "+j+", "+l);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(alias1, String.class);
		long kt = (long) rkvc.firstValue(alias2, String.class);
		long ku = (long) rkvc.firstValue(alias3, String.class);
		if( ks != i || kt != i || ku != i) {
			System.out.println("KV BATTERY1A9 cant find contains value "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of value "+i);
		}
		System.out.println("KV BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Make remote call to get lastValue and lastKey for String.class for each alias database, verify against max-1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(alias1, String.class); // first key
		Object j = rkvc.lastKey(alias2, String.class); // first key
		Object l = rkvc.lastKey(alias3, String.class); // first key
		System.out.println("KV Battery1AR10");
		if( Integer.parseInt(((String)k).substring(0,100)) != i  || !((String)k).endsWith(alias1.getAlias()) ||
				Integer.parseInt(((String)j).substring(0,100)) != i  || !((String)j).endsWith(alias2.getAlias()) ||
				Integer.parseInt(((String)l).substring(0,100)) != i  || !((String)l).endsWith(alias3.getAlias())) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(alias1, String.class);
		long kt = (long)rkvc.lastValue(alias2, String.class);
		long ku = (long)rkvc.lastValue(alias3, String.class);
		if( ks != (long)i || kt != (long)i || ku != (long)i) {
			System.out.println("KV BATTERY1AR10 cant find last value "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		System.out.println("KV BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	* Make a remote call to verify size of each String.class alias database is equal to max
	* @param argv
	* @throws Exception
	*/
	public static void battery1AR101(String[] argv) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits1 = rkvc.size(alias1, String.class);
		long bits2 = rkvc.size(alias2, String.class);
		long bits3 = rkvc.size(alias3, String.class);
		System.out.println("KV Battery1AR101");
		if( bits1 != i || bits2 != i || bits3 != i) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits1+" or "+bits2+" or "+bits3+" should be:"+i);
			throw new RuntimeException("KV BATTERY1AR101 size mismatch "+bits1+" or "+bits2+" or "+bits3+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findTailmapStream for alias- Returns a key stream of the portion of this set whose elements are greater than or equal to fromElement
	 * starting at min.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findTailMapStream(alias12, fkey); // intentionally leave off suffix for no exact match
		System.out.println(alias12+" KV Battery1AR11");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findTailmapKVStream for alias- Returns a key/value stream of the portion of this set whose elements are greater than or equal to fromElement
	 * starting at min.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR12(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findTailMapKVStream(alias12, fkey);
		System.out.println(alias12+" KV Battery1AR12");
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapStream for alias- Returns a stream view of the portion of this map whose keys are strictly less than toKey.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR13(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findHeadMapStream(alias12, fkey); // no exact match
		System.out.println(alias12+" KV Battery1AR13");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKVStream for alias-  Returns a key/value stream of the portion of this map whose keys are strictly less than toKey.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR14(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findHeadMapKVStream(alias12, fkey); // no exact match
		System.out.println(alias12+" KV Battery1AR14");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt((((Map.Entry<String,Long>)e).getKey()).substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMapStream for alias- Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR15(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = rkvc.findSubMapStream(alias12, fkey, tkey); // no exact match
		System.out.println(alias12+" KV Battery1AR15");
		stream.forEach(e ->{
			if(Integer.parseInt(((String) e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMapKVStream for alias- Returns a key/value view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param alias12
	 * @throws Exception
	 */
	public static void battery1AR16(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = rkvc.findSubMapKVStream(alias12, fkey, tkey); // no exact match
		System.out.println(alias12+" KV Battery1AR16");
		stream.forEach(e ->{
			if(Integer.parseInt((((Map.Entry<String,Long>)e).getKey()).substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println(alias12+" BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries.
	 * Obtain a keySet for String.class for given alias, remove each new entry.
	 * At 5 second intervals, print the current record being removed. At the end, check the size
	 * and if greater than zero, get an entrySet and display the records not removed.
	 */
	public static void battery1AR17(Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("KV Battery1AR17 for alias "+alias12);
		Iterator its = rkvc.keySet(alias12, String.class);
		timx = System.currentTimeMillis();
		its.forEachRemaining(e ->{	
			try {
				rkvc.remove(alias12, (Comparable)e);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(e);
				timx = System.currentTimeMillis();
			}
		});
		long siz = rkvc.size(alias12, String.class);
		if(siz > 0) {
				Stream stream =  rkvc.entrySetStream(alias12, String.class);
				stream.forEach(e ->{
					//System.out.println(i+"="+key);
					System.out.println(e);
				});
				System.out.println(alias12+" KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception(alias12+" KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println(alias12+" BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	
}
