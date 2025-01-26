package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Morphism.displayLevels;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * The set of tests verifies the higher level transaction 'findStream' functions in the {@link  RelatrixTransaction}<p/>
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking number of retrieved items against expected value
 * since findStream retrieves sets in no particular order. This permutation tests the alias functionality as well.
 * NOTES:
 * program argument is tablespace where alias DB's are created. i.e. C:/users/you/Relatrix/[ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017,2025
 *
 */
public class BatteryRelatrixTransactionStreamAlias {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	private static TransactionId xid;
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		xid = RelatrixTransaction.getTransactionId();
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixTransaction.setAlias(alias1,tablespace+alias1);
		RelatrixTransaction.setAlias(alias2,tablespace+alias2);
		RelatrixTransaction.setAlias(alias3,tablespace+alias3);
		Morphism.displayLevel = displayLevels.VERBOSE;
		if(argv.length > 2 && argv[1].equals("max")) {
			System.out.println("Setting max items to "+argv[2]);
			max = Integer.parseInt(argv[2]);
		} else {
			if(argv.length > 1 && argv[1].equals("init")) {
				System.out.println("Initialize database to zero items, then terminate...");
				battery1AR17(argv, alias1, xid);
				battery1AR17(argv, alias2, xid);
				battery1AR17(argv, alias3, xid);
				System.exit(0);
			}
		}
		if(RelatrixTransaction.size(alias1,xid) == 0 && RelatrixTransaction.size(alias2,xid) == 0 && RelatrixTransaction.size(alias3,xid) == 0) {
			if(DEBUG)
				System.out.println("Zero items, Begin insertion test from "+min+" to "+max);
			battery1(argv, alias1, xid);
			battery1(argv, alias2, xid);
			battery1(argv, alias3, xid);
			RelatrixTransaction.commit(alias1,xid);
			RelatrixTransaction.commit(alias2,xid);
			RelatrixTransaction.commit(alias3,xid);
			if(DEBUG)
				System.out.println("Begin duplicate key rejection test from "+min+" to "+max);
			battery11(argv, alias1, xid);
			battery11(argv, alias2, xid);
			battery11(argv, alias3, xid);
		}

		if(DEBUG)
			System.out.println("Begin test battery 1AR6");
		battery1AR6(argv, xid, alias1);
		battery1AR6(argv, xid, alias2);
		battery1AR6(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR7");
		battery1AR7(argv, xid, alias1);
		battery1AR7(argv, xid, alias2);
		battery1AR7(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR8");
		battery1AR8(argv, xid, alias1);
		battery1AR8(argv, xid, alias2);
		battery1AR8(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR9");
		battery1AR9(argv, xid, alias1);
		battery1AR9(argv, xid, alias2);
		battery1AR9(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR10");
		battery1AR10(argv, xid, alias1);
		battery1AR10(argv, xid, alias2);
		battery1AR10(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR101");
		battery1AR101(argv, xid, alias1);
		battery1AR101(argv, xid, alias2);
		battery1AR101(argv, xid, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR11");
		battery1AR11(argv, xid, alias1);
		battery1AR11(argv, xid, alias2);
		battery1AR11(argv, xid, alias3);
		//if(DEBUG)
		//	System.out.println("Begin test battery 1AR12");
		//battery1AR12(argv, xid alias1);
		//battery1AR12(argv, xid alias2);
		//battery1AR12(argv, xid alias3);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @param alias12 
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery1(String[] argv, Alias alias12, TransactionId xid2) throws Exception {
		System.out.println(xid2+" Battery1 "+alias12);
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				RelatrixTransaction.store(alias12, xid2, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				if((System.currentTimeMillis()-tims) > 1000) {
					System.out.println("storing "+recs+" "+fkey);
					tims = System.currentTimeMillis();
				}
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		RelatrixTransaction.commit(alias12, xid2);
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all.
	 * Domain/map determines unique key
	 * @param argv
	 * @param alias12 
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery11(String[] argv, Alias alias12, TransactionId xid2) throws Exception {
		System.out.println(xid2+" Battery11 "+alias12);
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				DomainMapRange dmr = RelatrixTransaction.store(alias12, xid2, fkey, "Has unit "+alias12, new Long(99999));
				++recs;
				System.out.println("SHOULD NOT BE storing "+recs+" "+fkey+" dmr:"+dmr);
				//if((System.currentTimeMillis()-tims) > 1000) {
				//	System.out.println("storing "+recs+" "+fkey);
				//	tims = System.currentTimeMillis();
				//}
			} catch(DuplicateKeyException d) {++dupes;}
		}
		if( recs > 0) {
			throw new DuplicateKeyException("BATTERY11 FAIL, stored "+recs+" when zero should have been stored");
		} else {
			System.out.println("BATTERY11 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
		}
	}
	
	/**
	 * Test the higher level functions in the Relatrix. Use the 'findSet' permutations to
	 * verify the previously inserted data
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" Battery1AR6 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2,"?", "?", "?").forEach(e->{
			Result nex = (Result)e;
			// 3 question marks = dimension 3 in return array
			if( DEBUG ) System.out.println("1AR6:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit "+alias12) || nex.length() != 3) {
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+"-"+nex.get(1)+" length:"+nex.length());
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+"-"+nex.get(1)+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys, expected "+max+" got "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys, expected "+max+" got "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = Relatrix.findSet("?", "*", "*");
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" Battery1AR7 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2, "?", "*", "*").forEach(e->{
			Result nex = (Result)e;
			// one '?' in findStream gives us one element returned
			if(DEBUG ) System.out.println("1AR7:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// No guarantee of order with unqualified findSet/findStream
			if(!((String)nex.get(0)).startsWith(key) || nex.length() != 1) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR7 unexpected number of keys, expected "+max+" got "+i);
			throw new Exception("BATTERY1AR7 unexpected number of keys, expected "+max+" got "+i);
		}
		 System.out.println("BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = Relatrix.findSet("?", "?", "*");
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" Battery1AR8 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2,"?", "?", "*").forEach(e ->{
			Result nex = (Result)e;
			// two '?' in findStream gives use 2 element array, the domain and map
			if( DEBUG ) System.out.println("1AR8:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit "+alias12) || nex.length() != 2) {
				System.out.println("KEY MISMATCH:"+(i)+" "+nex.get(0)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
				throw new RuntimeException("KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR8 unexpected number of keys, expected "+max+" got "+i);
			throw new Exception("BATTERY1AR8 unexpected number of keys, expected "+max+" got "+i);
		}
		 System.out.println("BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of Iterator<?> its = Relatrix.findSet("*", "*", "*");
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" Battery1AR9 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2, "*", "*", "*").forEach(e->{
			Result nex = (Result)e;
			// the returned array has 1 element, the identity Morphism DomainMapRange
			if( DEBUG ) System.out.println("1AR9:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR9 unexpected number of keys, expected "+max+" got "+i);
			throw new Exception("BATTERY1AR9 unexpected number of keys, expected "+max+" got "+i);
		}
		 System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Iterator<?> its = Relatrix.findSet(fkey, "Has unit", "*");
	 * Should return 1 element of which 'fkey' and "Has unit" are primary key
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, min);
		System.out.println(xid2+" Battery1AR10 "+alias12);
		// return all identities with the given key for all ranges, should be 1
		RelatrixTransaction.findStream(alias12, xid2, fkey, "Has unit "+alias12, "*").forEach(e-> {
			// return all identities with the given key for all ranges, should be 1
				// In this case, the set of identities of type Long that have stated domain and map should be returned
				// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
				Result nex = (Result)e;
				if(nex.length() != 1)
					throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
				if(DEBUG) System.out.println("1AR10:"+i+" "+nex.get(0));
				//String skey = key + String.format(uniqKeyFmt, i);
				// no guarantee of ordering with unqualified findSet/findStream
				if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
					throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+key+" - "+nex.get(0));
				if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
					throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
				++i;
		});
		if( i != 1 ) {
			System.out.println("BATTERY1AR10 unexpected number of keys returned from verification, expected 1, got "+i);
			throw new Exception("BATTERY1AR10 unexpected number of keys returned from verification, expected 1, got "+i);
		}
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Iterator<?> its = Relatrix.findSet(fkey, "Has unit", new Long(max));
	 * Range value is max, so zero keys should be retrieved since we insert 0 to max-1
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		i = 0;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, max);
		// Range value is max, so zero keys should be retrieved since we insert 0 to max-1
		Iterator<?> its = RelatrixTransaction.findSet(alias12, xid2, fkey, "Has unit "+alias12, new Long(max));
		System.out.println(xid2+" Batteryt1AR101 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2, fkey, "Has unit "+alias12, new Long(max)).forEach(e->{
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a instances, we should get one element back; the identity
			Result nex = (Result) e;
			if( nex.length() != 1)
				throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR101:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!( (String)((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+key+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
			//Long unit = new Long(i);
			//if(!((DomainMapRange)nex[0]).getRange().equals(unit))
				//System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		});
		if( i != 0 ) {
			System.out.println("BATTERY1AR101 unexpected number of keys returned from verification, expected 0, got "+i);
			throw new Exception("BATTERY1AR101 unexpected number of keys returned from verification, expected 0, got"+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * negative assertion of above
	 * Iterator<?> its = Relatrix.findSet(fkey, "Has time", "*");
	 * map is 'Has time', which we never inserted, so no elements should come back
	 * @param session
	 * @param argv
	 * @param xid2 
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, min);
		System.out.println(xid2+" Battery1AR11 "+alias12);
		RelatrixTransaction.findStream(alias12, xid2, fkey, "Has time", "*").forEach(e->{
			Result nex = (Result)e;
			if( DEBUG ) System.out.println("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
			throw new RuntimeException("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
		});
		System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of remove. Remove the object and all its relationships.
	 * Perform 3 findSet with removed key to verify its gone.
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String[] argv, TransactionId xid2, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" BAttery1AR12 "+alias12);
		String fkey = key + String.format(uniqKeyFmt, min);
		RelatrixTransaction.remove(alias12, xid2, fkey);
		System.out.println(fkey+" removed, proceeding to verify removal of all relationships it may have been involved in");
		Iterator<?> its = RelatrixTransaction.findSet(alias12, xid2, fkey, "*", "*");
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12-1 failed to delete key "+fkey+" "+(Result)its.next());
		}
		// re-insert
		RelatrixTransaction.store(alias12, xid2, fkey, "Has unit "+alias12, new Long(min));
		its = RelatrixTransaction.findSet(alias12, xid2, "*", fkey, "*");
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12-2 failed to delete key "+fkey);
		}
		its = RelatrixTransaction.findSet(alias12, xid2, "*", "*", fkey);
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12-3 failed to delete key "+fkey);
		}
		System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * remove entries
	 * @param argv
	 * @param alias12 
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv, Alias alias12, TransactionId xid2) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println(xid+" CleanDB DMR size="+RelatrixTransaction.size(alias12, xid, DomainMapRange.class));
		System.out.println("CleanDB DRM size="+RelatrixTransaction.size(alias12, xid, DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+RelatrixTransaction.size(alias12, xid, MapDomainRange.class));
		System.out.println("CleanDB MDR size="+RelatrixTransaction.size(alias12, xid, MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+RelatrixTransaction.size(alias12, xid, RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+RelatrixTransaction.size(alias12, xid, RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = RelatrixTransaction.findSet(alias12, xid, "*","*","*");
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
			try {
				RelatrixTransaction.remove(alias12, xid, dmr);
			} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" total, current="+fkey);
				timx = System.currentTimeMillis();
			}
		});
		Iterator<?> its = RelatrixTransaction.findSet(alias12, xid, "*","*","*");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			//System.out.println(i+"="+nex);
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			else
				throw new Exception("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
		}
		long siz = RelatrixTransaction.size(alias12, xid);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, DomainMapRange.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("DomainMapRange:"+nex);
			}
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, DomainRangeMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("DomainRangeMap:"+nex);
			}
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, MapDomainRange.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapDomainRange:"+nex);
			}
		}
		siz = RelatrixTransaction.size(alias12, xid, MapDomainRange.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, MapRangeDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapRangeDomain:"+nex);
			}
		}
		siz = RelatrixTransaction.size(alias12,xid,  MapRangeDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, RangeDomainMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeDomainMap:"+nex);
			}
		}
		siz = RelatrixTransaction.size(alias12, xid, RangeDomainMap.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = RelatrixTransaction.entrySet(alias12, xid, RangeMapDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeMapDomain:"+nex);
			}
		}
		siz = RelatrixTransaction.size(alias12, xid, RangeMapDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
