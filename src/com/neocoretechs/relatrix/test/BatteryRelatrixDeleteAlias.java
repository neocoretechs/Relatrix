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
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.IndexResolver;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

/**
 * The set of tests verifies the delete functions in the {@link  Relatrix}<p/>
 * Create a series of nested relations and then verify that they are properly deleted when a reference to them was previously deleted.<p/>
 * This represents sets of deeply nested relations introducing a heavy demand. 
 * NOTES:
 * A path unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/ [ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BatteryRelatrixDeleteAlias {
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
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3"); 
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix.setTablespace(argv[0]);
		Morphism.displayLevel = displayLevels.VERBOSE;
		if(argv.length > 2 && argv[1].equals("max")) {
			System.out.println("Setting max items to "+argv[2]);
			max = Integer.parseInt(argv[2]);
		} else {
			if(argv.length > 1 && argv[1].equals("init")) {
				System.out.println("Initialize database to zero items, then terminate...");
				battery1AR17(argv, alias1);
				battery1AR17(argv, alias2);
				battery1AR17(argv, alias3);
				System.exit(0);
			}
		}
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		Relatrix.setAlias(alias1,tablespace+alias1);
		Relatrix.setAlias(alias2,tablespace+alias2);
		Relatrix.setAlias(alias3,tablespace+alias3);
		Morphism.displayLevel = displayLevels.VERBOSE;
		if(Relatrix.size(alias1) == 0) {
			if(DEBUG)
				System.out.println("Zero items, Begin insertion from "+min+" to "+max);
			battery1(argv, alias1);
			battery1(argv, alias2);
			battery1(argv, alias3);
			if(DEBUG)
				System.out.println("Begin duplicate key rejection test from "+min+" to "+max);
			//battery11(argv, alias1);
			//battery11(argv, alias2);
			//battery11(argv, alias3);
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6");
		battery1AR6(argv, alias1);
		battery1AR6(argv, alias2);
		battery1AR6(argv, alias3);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1(String[] argv, Alias alias12) throws Exception {
		System.out.println(alias12+" Battery1 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				DomainMapRange dmr1 = Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				DomainMapRange dmr2 = Relatrix.store(alias12, dmr1, "Has related "+alias12, rando.nextLong());
				++recs;	
				DomainMapRange dmr3 = Relatrix.store(alias12, dmr1, dmr2, rando.nextLong());
				++recs;
				DomainMapRange dmr4 = Relatrix.store(alias12, dmr3, dmr2, dmr3);
				++recs;
				DomainMapRange dmr5 = Relatrix.store(alias12, dmr4, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr6 = Relatrix.store(alias12, dmr5, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr7 = Relatrix.store(alias12, dmr6, dmr5, rando.nextLong());
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
	 * @param alias12 
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery11(String[] argv, Alias alias12) throws Exception {
		System.out.println(alias12+" Battery11 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				DomainMapRange dmr1 = Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				DomainMapRange dmr2 = Relatrix.store(alias12, dmr1, "Has related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr3 = Relatrix.store(alias12, dmr1, dmr2, rando.nextLong());
				++recs;
				DomainMapRange dmr4 = Relatrix.store(alias12, dmr3, dmr2, dmr3);
				++recs;
				DomainMapRange dmr5 = Relatrix.store(alias12, dmr4, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr6 = Relatrix.store(alias12, dmr5, "Is related "+alias12, rando.nextLong());
				++recs;
				DomainMapRange dmr7 = Relatrix.store(alias12, dmr6, dmr5, rando.nextLong());
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
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv, Alias alias12) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println(alias12+" Battery1AR6");
		for(int i = min; i < max; i++) {
			Long irec = new Long(i);
			Relatrix.remove(alias12, irec);
			if((System.currentTimeMillis()-tims) > 1000) {
				System.out.println("deleting "+irec);
				tims = System.currentTimeMillis();
			}
			/*
			Relatrix.findStream(alias12, "*", "*", irec).forEach(e->{
				Result nex = (Result)e;
				System.out.println("KEY MISMATCH:"+nex);
				throw new RuntimeException("MAP KEY MISMATCH:"+nex);
			});
			*/
		}
		// when finished, all records should theoretically be deleted
		if( Relatrix.size(alias12) > 0) {
			System.out.println(alias12+" BATTERY1AR6 unexpected number of keys "+Relatrix.size(alias12));
			Relatrix.findStream(alias12, "*", "*", "*").forEach(e->{
				System.out.println("Del fault:"+e);
			});
			throw new Exception(alias12+" BATTERY1AR6 unexpected number of keys "+Relatrix.size(alias12));
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * remove entries, all relationships should be recursively deleted
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println(alias12+" CleanDB DMR size="+Relatrix.size(alias12));
		System.out.println("CleanDB DRM size="+Relatrix.size(alias12,DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12,MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12,MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(alias12,RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(alias12,RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet(alias12,"*","*","*");
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
			try {
				Relatrix.remove(alias12,dmr);
			} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" total, current="+fkey);
				timx = System.currentTimeMillis();
			}
		});
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

}
