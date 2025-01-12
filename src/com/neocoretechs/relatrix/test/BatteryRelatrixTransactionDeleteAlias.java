package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

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
 * The set of tests verifies the delete functions in the {@link  RelatrixTransaction}<p/>
 * Create a series of nested relations and then verify that they are properly deleted when a reference to them was previously deleted.<p/>
 * This represents sets deeply nested relations introducing a heavy demand on a series of aliased databases. 
 * NOTES:
 * program argument is tablespace i.e. C:/users/you/Relatrix/ which will create databases in C:/users/you/Relatrix/ALIAS1, 2, 3..
 * optional arguments are [ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BatteryRelatrixTransactionDeleteAlias {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	private static TransactionId xid;
	private static Random rando = new Random();
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		RelatrixTransaction.setAlias(alias1,tablespace+alias1);
		RelatrixTransaction.setAlias(alias2,tablespace+alias2);
		RelatrixTransaction.setAlias(alias3,tablespace+alias3);
		xid = RelatrixTransaction.getTransactionId();
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
			// optional duplicate key rejection
			//battery11(argv, alias1, xid);
			//battery11(argv, alias2, xid);
			//battery11(argv, alias3, xid);
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6 Nested Key Removal");
		battery1AR6(argv, alias1, xid);
		battery1AR6(argv, alias2, xid);
		battery1AR6(argv, alias3, xid);
		
		RelatrixTransaction.commit(alias1,xid);
		RelatrixTransaction.commit(alias2,xid);
		RelatrixTransaction.commit(alias3,xid);
		
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
				DomainMapRange dmr1 = RelatrixTransaction.store(alias12, xid2, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				DomainMapRange dmr2 = RelatrixTransaction.store(alias12, xid2, dmr1, "Has related "+alias12, rando.nextLong());
				++recs;	
				DomainMapRange dmr3 = RelatrixTransaction.store(alias12, xid2,  dmr1, dmr2, rando.nextLong());
				++recs;
				DomainMapRange dmr4 = RelatrixTransaction.store(alias12, xid2, dmr3, dmr2, dmr3);
				++recs;
				DomainMapRange dmr5 = RelatrixTransaction.store(alias12, xid2, dmr4, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr6 = RelatrixTransaction.store(alias12, xid2, dmr5, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr7 = RelatrixTransaction.store(alias12, xid2, dmr6, dmr5, rando.nextLong());
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
				DomainMapRange dmr1 = RelatrixTransaction.store(alias12, xid2, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				DomainMapRange dmr2 = RelatrixTransaction.store(alias12, xid2, dmr1, "Has related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr3 = RelatrixTransaction.store(alias12, xid2,  dmr1, dmr2, rando.nextLong());
				++recs;
				DomainMapRange dmr4 = RelatrixTransaction.store(alias12, xid2, dmr3, dmr2, dmr3);
				++recs;
				DomainMapRange dmr5 = RelatrixTransaction.store(alias12, xid2, dmr4, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr6 = RelatrixTransaction.store(alias12, xid2, dmr5, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr7 = RelatrixTransaction.store(alias12, xid2, dmr6, dmr5, rando.nextLong());
				++recs;
				if((System.currentTimeMillis()-tims) > 1000) {
					System.out.println("SHOULD NOT BE storing "+recs+" "+fkey);
					tims = System.currentTimeMillis();
				}
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		if( recs > 0) {
			RelatrixTransaction.commit(alias12, xid2);
			throw new DuplicateKeyException(alias12+" BATTERY11 FAIL, stored "+recs+" when zero should have been stored");
		} else {
			System.out.println("BATTERY11 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
		}
	}
	
	/**
	 * Test the higher level functions in the Relatrix. Use the 'findSet' permutations to
	 * verify the previously inserted data
	 * @param argv
	 * @param alias12 
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv, Alias alias12, TransactionId xid2) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(xid2+" Battery1AR6 "+alias12);
		for(int i = min; i < max; i++) {
			Long irec = new Long(i);
			RelatrixTransaction.remove(alias12, xid2, irec);
			if((System.currentTimeMillis()-tims) > 1000) {
				System.out.println("deleting "+irec);
				tims = System.currentTimeMillis();
			}
			/*
			RelatrixTransaction.findStream(alias12, xid2,"*", "*", irec).forEach(e->{
				Result nex = (Result)e;
				System.out.println("KEY MISMATCH:"+nex);
				throw new RuntimeException("MAP KEY MISMATCH:"+nex);
			});
			*/
		}
		RelatrixTransaction.commit(alias12, xid2);
		// when finished, all records should theoretically be deleted
		if( RelatrixTransaction.size(alias12, xid2, DomainMapRange.class) > 0) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+RelatrixTransaction.size(alias12, xid2, DomainMapRange.class));
			RelatrixTransaction.findStream(alias12, xid2,"*", "*", "*").forEach(e->{
				System.out.println("Del fault:"+e);
			});
			throw new Exception("BATTERY1AR6 unexpected number of keys "+RelatrixTransaction.size(alias12, xid2, DomainMapRange.class));
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
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
