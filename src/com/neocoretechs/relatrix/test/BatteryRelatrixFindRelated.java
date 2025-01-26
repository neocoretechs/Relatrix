package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
import com.neocoretechs.rocksack.TransactionId;

/**
 * The set of tests verifies the findSet relation function in the {@link  Relatrix}<p/>
 * Create a series of nested relations and then verify that they are properly located when a reference to them is provided.<p/>
 * This represents sets of deeply nested relations introducing a heavy demand. 
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2 [ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BatteryRelatrixFindRelated {
	public static boolean DEBUG = false;
	static String uniqKeyFmt = "%010d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix.setTablespace(argv[0]);
		Morphism.displayLevel = displayLevels.MINIMAL;
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
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6 Nested Key");
		battery1AR6(argv);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys. Store a set of nested relationships for later retrieval.
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
			fkey = "Bone " + String.format(uniqKeyFmt, i);	
			try {
				DomainMapRange dmr1 = Relatrix.store(fkey, "part", "leg "+String.format(uniqKeyFmt, i));
				++recs;
				DomainMapRange dmr2 = Relatrix.store(dmr1, "part", "torso "+String.format(uniqKeyFmt, i));
				++recs;	
				DomainMapRange dmr3 = Relatrix.store(dmr2, "part", "body "+String.format(uniqKeyFmt, i));
				++recs;
				DomainMapRange dmr4 = Relatrix.store("dog "+String.format(uniqKeyFmt, i), "has", dmr3);
				++recs;
				DomainMapRange dmr5 = Relatrix.store(dmr4, "eats", "food");
				++recs;
				DomainMapRange dmr6 = Relatrix.store(dmr5, "but not", dmr4);
				++recs;
				DomainMapRange dmr7 = Relatrix.store(fkey, dmr6, "food");
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
	 * Test the higher level functions in the Relatrix. Use the 'findSet' permutations to
	 * verify the previously inserted data. Start from the relationship "leg "+sequence
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR6");
		for(int i = min; i < max; i++) {
			String irec = "leg "+String.format(uniqKeyFmt, i);
			Morphism m = (Morphism) ((Result)(Relatrix.findStream("*", "*", irec).findFirst().get())).get();
			List<Comparable> lm = Relatrix.findSet(m);
			System.out.println(i+".)"+Arrays.toString(lm.toArray()));
			System.out.println("----------");
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
		System.out.println("CleanDB DMR size="+Relatrix.size(DomainMapRange.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet("*","*","*");
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
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
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
}
