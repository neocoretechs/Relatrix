package com.neocoretechs.relatrix.test.kv;

import java.util.Map;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.client.RelatrixKVClientTransaction;
import com.neocoretechs.relatrix.client.RemoteKVIteratorTransaction;
import com.neocoretechs.relatrix.client.RemoteStream;
import com.neocoretechs.relatrix.server.remoteiterator.RemoteKeySetIteratorTransaction;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * Test of client side KV server stream transaction ops for multiple aliased databases.
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings, differentiated by appending the alias of the db to the end of key.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * This test the client side Java 8 streams obtained from the server
 * NOTES:
 * start server RelatrixKVTransactionServer.
 * A database unique to this test module should be used.
 * program argument is node of local client, node server is running on, port of server and remote tablespace alias designator.
 * i.e. java BatteryRelatrixKVClientTransactionStreamAlias localnode remotenode 9010 "C:/etc/db/test"
 * @author Jonathan Groff (C) NeoCoreTechs 2022,2023
 *
 */
public class BatteryRelatrixKVClientTransactionStreamAlias {
	public static boolean DEBUG = false;
	public static RelatrixKVClientTransaction rkvc;
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i;
	static int j;
	private static int dupes;
	private static int numLookupByValue = 10;
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		if(argv.length < 4) {
			System.out.println("Usage: java com.neocoretechs.relatrix.test.kv.BatteryRelatrixKVClientTransactionStreamAlias <DB local client NODE> <DB remote server node> <DB PORT> <server_directory_path_to_tablespace_alias");
			System.exit(1);
		}
		System.out.println("local="+argv[0]+" remote="+argv[0]+" port="+argv[1]);
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
		TransactionId xid = rkvc.getTransactionId();
		battery1(alias1, xid);
		battery1(alias2, xid);	
		battery1(alias3, xid);	
		battery11(alias1,xid); 
		battery11(alias2,xid); 
		battery11(alias3,xid); 
		battery1AR6(alias1,xid);
		battery1AR6(alias2,xid);
		battery1AR6(alias3,xid);
		battery1AR7(alias1,xid);
		battery1AR7(alias2,xid);
		battery1AR7(alias3,xid);
		battery1AR8(alias1,xid); // search by value, slow operation no key
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
		System.out.println("BatteryRelatrixKVClientTransactionStreamAlias TEST BATTERY COMPLETE.");
		rkvc.endTransaction(xid);
		rkvc.removeAlias(alias1);
		rkvc.removeAlias(alias2);
		rkvc.removeAlias(alias3);
		rkvc.close();	
	}
	/**
	 * Loads up on keys, should be 0 to max-1, or min, to max -1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(Alias alias12, TransactionId xid) throws Exception {
		System.out.println(alias12+" KV Battery1 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int j = min;
		j = (int) rkvc.size(alias12, xid, String.class);
		if(j > 0) {
			System.out.println(alias12+" Cleaning DB of "+j+" elements.");
			battery1AR17(alias12, xid);		
		}
		for(int i = min; i < max; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(alias12, xid, fkey+alias12, new Long(i));
			++recs;
		}
		rkvc.commit(alias12, xid);
		System.out.println(alias12+" KV BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Store another transaction then roll it back.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(Alias alias12, TransactionId xid) throws Exception {
		System.out.println(alias12+" KV Battery11 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		TransactionId xid2 = rkvc.getTransactionId();
		for(int i = max; i < max*2; i++) {
			fkey = String.format(uniqKeyFmt, i);
			rkvc.store(alias12, xid2, fkey+alias12, new Long(fkey));
			++recs;
		}
		if( recs > 0) {
			rkvc.rollback(alias12,xid2);
			System.out.println("KV BATTERY11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
		rkvc.endTransaction(xid2);
	}
	
	/**
	 * Test the higher level functions in the RelatrixKV.
	 * public Set<Map.Entry<K,V>> entrySet()
	 * Returns a Set view of the mappings contained in this map. 
	 * The set's stream returns the entries in ascending key order. 
	 * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
	 * If the map is modified while an iteration over the set is in progress (except through the stream's 
	 * own remove operation, or through the setValue operation on a map entry returned by the stream) the results
	 * of the streaming are undefined. The set supports element removal, which removes the corresponding mapping from the map, 
	 * via the stream. Remove, Set.remove, removeAll, retainAll and clear operations. 
	 * It does not support the add or addAll operations.
	 * from battery1 we should have 0 to max, say 1000 keys of length 100
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR6(Alias alias12, TransactionId xid) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream = rkvc.entrySetStream(alias12,xid,String.class);
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
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Stream<?> its = RelatrixKV.keySet;
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR7(Alias alias12, TransactionId xid) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Stream stream =  rkvc.keySetStream(alias12, xid, String.class);
		System.out.println(alias12+" KV Battery1AR7");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
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
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(Alias alias12, TransactionId xid) throws Exception {
		i = min;
		System.out.println(alias12+" KV Battery1AR8");
		long tims = System.currentTimeMillis();
		for(int j = min; j < max; j++) {
			String fkey = String.format(uniqKeyFmt, j);
			boolean bits = rkvc.contains(alias12, xid, fkey+alias12);
			if( !bits ) {
				System.out.println("KV BATTERY1A8 cant find contains key "+j);
				throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
			}
		}
		 System.out.println("KV BATTERY1AR8 FORWARD CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		 tims = System.currentTimeMillis();
		 for(int j = max-1; j > min; j--) {
				String fkey = String.format(uniqKeyFmt, j);
				boolean bits = rkvc.contains(alias12, xid, fkey+alias12);
				if( !bits ) {
					System.out.println("KV BATTERY1A8 cant find contains key "+j);
					throw new Exception("KV BATTERY1AR8 unexpected cant find contains of key "+fkey);
				}
			}
			 System.out.println("KV BATTERY1AR8 REVERSE CONTAINS KEY TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		//i = max-1;
		tims = System.currentTimeMillis();
		for(int j = min; j < min+numLookupByValue; j++) {
			// careful here, have to do the conversion explicitly
			boolean bits = rkvc.containsValue(alias12, xid, String.class, (long)j);
			if( !bits ) {
				System.out.println("KV BATTERY1AR8 cant find contains value "+j);
				throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
			}
		}
		System.out.println("KV BATTERY1AR8 FORWARD "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
		tims = System.currentTimeMillis();
		for(int j = max-1; j > max-numLookupByValue  ; j--) {
				// careful here, have to do the conversion explicitly
				boolean bits = rkvc.containsValue(alias12, xid, String.class, (long)j);
				if( !bits ) {
					System.out.println("KV BATTERY1AR8 cant find contains value "+j);
					throw new Exception("KV BATTERY1AR8 unexpected number cant find contains of value "+i);
				}
		}
		System.out.println("KV BATTERY1AR8 REVERSE "+numLookupByValue+" CONTAINS VALUE TOOK "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of first(), and firstValue
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR9(Alias alias12, TransactionId xid) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Object k = rkvc.firstKey(alias12, xid, String.class); // first key
		System.out.println(alias12+" KV Battery1AR9");
		if( Integer.parseInt(((String)k).substring(0,100)) != i || !((String)k).endsWith(alias12.getAlias()) ) {
			System.out.println("KV BATTERY1A9 cant find contains key "+i);
			throw new Exception("KV BATTERY1AR9 unexpected cant find contains of key "+i);
		}
		long ks = (long) rkvc.firstValue(alias12, xid, String.class);
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
	public static void battery1AR10(Alias alias12, TransactionId xid) throws Exception {
		int i = max-1;
		long tims = System.currentTimeMillis();
		Object k = rkvc.lastKey(alias12, xid, String.class); // key
		System.out.println(alias12+" KV Battery1AR10");
		if( Long.parseLong(((String)k).substring(0,100)) != (long)i  || !((String)k).endsWith(alias12.getAlias())) {
			System.out.println("KV BATTERY1AR10 cant find last key "+i);
			throw new Exception("KV BATTERY1AR10 unexpected cant find last of key "+i);
		}
		long ks = (long)rkvc.lastValue(alias12, xid, String.class);
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
	public static void battery1AR101(Alias alias12, TransactionId xid) throws Exception {
		int i = max;
		long tims = System.currentTimeMillis();
		long bits = rkvc.size(alias12, xid, String.class);
		System.out.println(alias12+" KV Battery1AR101");
		if( bits != i ) {
			System.out.println("KV BATTERY1AR101 size mismatch "+bits+" should be:"+i);
			throw new Exception("KV BATTERY1AR101 size mismatch "+bits+" should be "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMap test, basically tailmap returning keys
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR11(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findTailMapStream(alias12, xid, fkey);
		System.out.println(alias12+" KV Battery1AR11");
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * findMapKV tailmapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR12(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findTailMapKVStream(alias12, xid, fkey);
		System.out.println(alias12+" KV Battery1AR12");
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findMapKV findHeadMap - Returns a view of the portion of this map whose keys are strictly less than toKey.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR13(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream = rkvc.findHeadMapStream(alias12, xid, fkey);
		System.out.println(alias12+" KV Battery1AR13");
		// with i at max, should catch them all
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR13 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findHeadMapKV
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR14(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = max;
		String fkey = String.format(uniqKeyFmt, i);
		Stream stream =  rkvc.findHeadMapKVStream(alias12, xid, fkey);
		System.out.println(alias12+" KV Battery1AR14");
		i = min;
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR14 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMap - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR15(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream =  rkvc.findSubMapStream(alias12, xid, fkey, tkey);
		System.out.println(alias12+" KV Battery1AR15");
		// with i at max, should catch them all
		stream.forEach(e ->{
			if(Integer.parseInt(((String)e).substring(0,100)) != i || !((String)e).endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR15 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR15 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * findSubMap findSubMapKV - Returns a view of the portion of this map whose keys range from fromKey, inclusive, to toKey, exclusive.
	 * @param xid
	 * @throws Exception
	 */
	public static void battery1AR16(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		i = min;
		j = max;
		String fkey = String.format(uniqKeyFmt, i);
		// with j at max, should get them all since we stored to max -1
		String tkey = String.format(uniqKeyFmt, j);
		Stream stream = rkvc.findSubMapKVStream(alias12, xid, fkey, tkey);
		System.out.println(alias12+" KV Battery1AR16");
		// with i at max, should catch them all
		stream.forEach(e ->{
			if(Integer.parseInt(((Map.Entry<String,Long>)e).getKey().substring(0,100)) != i || !((Map.Entry<String,Long>)e).getKey().endsWith(alias12.getAlias())) {
			// Map.Entry
				System.out.println("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
				throw new RuntimeException("KV RANGE 1AR16 KEY MISMATCH:"+i+" - "+e);
			}
			++i;
		});
		 System.out.println("BATTERY1AR16 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries, this is done in a new transaction
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(Alias alias12, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		TransactionId xid2 = rkvc.getTransactionId();
		System.out.println(alias12+" KV Battery1AR17");
		long timx = System.currentTimeMillis();
		RemoteKVIteratorTransaction its = (RemoteKVIteratorTransaction) rkvc.keySet(alias12,xid2,String.class);
		while(its.hasNext()) {
			String fkey = (String) its.next();
			rkvc.remove(alias12,xid2, fkey);
			if((System.currentTimeMillis()-timx) > 5000) {
				System.out.println(fkey);
				timx = System.currentTimeMillis();
			}
		}
		its.close();
		rkvc.commit(alias12, xid2);
		long siz = rkvc.size(alias12, xid2, String.class);
		i = 0;
		if(siz > 0) {
			Stream stream =  rkvc.entrySetStream(alias12,xid,String.class);
			stream.forEach(e ->{
				if(((Map.Entry<String,Long>)e).getValue() != i) {
					System.out.println("RANGE KEY MISMATCH:"+i+" - "+e);
					throw new RuntimeException("RANGE KEY MISMATCH:"+i+" - "+e);
				}
				System.out.println(i+"="+e);
				++i;
			});
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed. Total="+i);
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		rkvc.endTransaction(xid2);
		System.out.println(alias12+" BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	
}
