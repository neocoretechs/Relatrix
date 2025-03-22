package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.AbstractRelation.displayLevels;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.IndexResolver;


/**
 * The set of tests verifies the delete functions in the {@link  Relatrix}<p/>
 * Create a series of nested relations and then verify that they are properly deleted when a reference to them was previously deleted.<p/>
 * This represents sets of deeply nested relations introducing a heavy demand. 
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2 [ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BatteryRelatrixDelete {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	private static Random rando = new Random();
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix.setTablespace(argv[0]);
		AbstractRelation.displayLevel = displayLevels.VERBOSE;
		if(argv.length > 2 && argv[1].equals("max")) {
			System.out.println("Setting max items to "+argv[2]);
			max = Integer.parseInt(argv[2]);
		} else {
			if(argv.length > 1 && argv[1].equals("init")) {
				System.out.println("Initialize database to zero items, then terminate...");
				battery1AR17(argv);
				System.exit(0);
			}
		}
		if(Relatrix.size() == 0) {
			if(DEBUG)
				System.out.println("Zero items, Begin insertion from "+min+" to "+max);
			battery1(argv);
			//if(DEBUG)
			//	System.out.println("Begin duplicate key rejection test from "+min+" to "+max);
			//battery11(argv);
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6 Nested Key Removal");
		battery1AR6(argv);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relation dmr1 = Relatrix.store(fkey, "Has unit", new Long(i));
				++recs;
				Relation dmr2 = Relatrix.store(dmr1, "Has related", rando.nextLong());
				++recs;	
				Relation dmr3 = Relatrix.store(dmr1, dmr2, rando.nextLong());
				++recs;
				Relation dmr4 = Relatrix.store(dmr3, dmr2, dmr3);
				++recs;
				Relation dmr5 = Relatrix.store(dmr4, "Is related", rando.nextLong());
				++recs;
				Relation dmr6 = Relatrix.store(dmr5, "Is related", rando.nextLong());
				++recs;
				Relation dmr7 = Relatrix.store(dmr6, dmr5, rando.nextLong());
				++recs;
				if((System.currentTimeMillis()-tims) > 1000) {
					System.out.println("storing "+recs+" "+fkey);
					tims = System.currentTimeMillis();
				}
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key that should match existing keys, should reject all.
	 * Domain/map determines unique key
	 * @param argv
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery11(String[] argv) throws Exception {
		System.out.println("Battery11 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relation dmr1 = Relatrix.store(fkey, "Has unit", new Long(i));
				++recs;
				Relation dmr2 = Relatrix.store(dmr1, "Has related", rando.nextLong());
				++recs;
				Relation dmr3 = Relatrix.store(dmr1, dmr2, rando.nextLong());
				++recs;
				Relation dmr4 = Relatrix.store(dmr3, dmr2, dmr3);
				++recs;
				Relation dmr5 = Relatrix.store(dmr4, "Is related", rando.nextLong());
				++recs;
				Relation dmr6 = Relatrix.store(dmr5, "Is related", rando.nextLong());
				++recs;
				Relation dmr7 = Relatrix.store(dmr6, dmr5, rando.nextLong());
				++recs;
				if((System.currentTimeMillis()-tims) > 1000) {
					System.out.println("SHOULD NOT BE storing "+recs+" "+fkey);
					tims = System.currentTimeMillis();
				}
			} catch(DuplicateKeyException dke) { ++dupes; }
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
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR6");
		for(int i = min; i < max; i++) {
			Long irec = new Long(i);
			Relatrix.remove(irec);
			if((System.currentTimeMillis()-tims) > 1000) {
				System.out.println("deleting "+irec);
				tims = System.currentTimeMillis();
			}
			/*
			Relatrix.findStream('*', '*', irec).forEach(e->{
				Result nex = (Result)e;
				System.out.println("KEY MISMATCH:"+nex);
				throw new RuntimeException("MAP KEY MISMATCH:"+nex);
			});
			*/
		}
		// when finished, all records should theoretically be deleted
		if( Relatrix.size(Relation.class) > 0) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+Relatrix.size(Relation.class));
			Relatrix.findStream('*', '*', '*').forEach(e->{
				System.out.println("Del fault:"+e);
			});
			throw new Exception("BATTERY1AR6 unexpected number of keys "+Relatrix.size(Relation.class));
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB DMR size="+Relatrix.size(Relation.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(RangeMapDomain.class));
		AbstractRelation.displayLevel = AbstractRelation.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet('*','*','*');
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			Relation dmr = (Relation)((Result)fkey).get(0);
			try {
				Relatrix.remove(dmr);
			} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" total, current="+fkey);
				timx = System.currentTimeMillis();
			}
		});
		Iterator<?> its = Relatrix.findSet('*','*','*');
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			//System.out.println(i+"="+nex);
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			else
				throw new Exception("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
		}
		long siz = Relatrix.size();
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(Relation.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("Relation:"+nex);
			}
		}
		if(DEBUG) {
			it = Relatrix.entrySet(DomainRangeMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("DomainRangeMap:"+nex);
			}
		}
		if(DEBUG) {
			it = Relatrix.entrySet(MapDomainRange.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapDomainRange:"+nex);
			}
		}
		siz = Relatrix.size(MapDomainRange.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(MapRangeDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapRangeDomain:"+nex);
			}
		}
		siz = Relatrix.size(MapRangeDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(RangeDomainMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeDomainMap:"+nex);
			}
		}
		siz = Relatrix.size(RangeDomainMap.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(RangeMapDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeMapDomain:"+nex);
			}
		}
		siz = Relatrix.size(RangeMapDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
