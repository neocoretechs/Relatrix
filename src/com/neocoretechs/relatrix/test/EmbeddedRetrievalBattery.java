package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;

/**
 * This series of tests uses classes and concrete object instances in various FindSet permutations.
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public class EmbeddedRetrievalBattery {
	public static boolean DEBUG = true;
	public static int min = 0;
	public static int max = 100;
	static String key = "This is a test"; 
	static String uniqKeyFmt = "%0100d";
	/**
	*/
	public static void main(String[] argv) throws Exception {
		 //System.out.println("Analysis of all");
		Relatrix.setTablespace(argv[0]);
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		if(argv.length == 2 && argv[1].equals("init")) {
				battery1AR17(argv);
		}
		if(Relatrix.size() == 0) {
			battery0(argv);
		}
		battery1(argv);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(1);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery0(String[] argv) throws Exception {
		System.out.println("Battery0 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		DomainMapRange dmr = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				dmr = Relatrix.store(fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		 System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}

	/**
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Iterator Battery1 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Comparable> ar = new ArrayList<Comparable>();
		ArrayList<Comparable[]> ar2 = new ArrayList<Comparable[]>(); // will store 2 element result sets
		ArrayList<Comparable[]> ar3 = new ArrayList<Comparable[]>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Wildcard queries:");
		recs = 0;
		System.out.println("1.) Findset(*,*,*)...");
		it =  Relatrix.findSet("*", "*", "*");
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//ar.add(c[0]);
		}
		recs = 0;
		System.out.println("2.) Findset(*,*,?)...");		
		it = Relatrix.findSet("*", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			if(ar.size() == 0 ) ar.add(((Result)o).get());
		}
		recs = 0;
		System.out.println("3.) Findset(*,?,*)...");		
		it = Relatrix.findSet("*", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			if(ar.size() == 1 ) ar.add(((Result)o).get());
		}
		recs = 0;
		System.out.println("4.) Findset(?,*,*)...");		
		it = Relatrix.findSet("?", "*", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			System.out.println(++recs+"="+o);
			if(ar.size() == 2) ar.add(((Result)o).get());
		}
		recs=0;
		System.out.println("5.) Findset(*,?,?)...");		
		it = Relatrix.findSet("*", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = ((Result2)o).toArray();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			if(ar2.size() == 0) ar2.add(c);
		}
		recs = 0;
		System.out.println("6.) Findset(?,*,?)...");		
		it = Relatrix.findSet("?", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = ((Result2)o).toArray();
			if(DEBUG)
				System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 1) ar2.add(c);
		}
		recs = 0;
		System.out.println("7.) Findset(?,?,*)...");		
		it = Relatrix.findSet("?", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = ((Result2)o).toArray();
			if(DEBUG)
				System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 2) ar2.add(c);
		}
		recs = 0;
		System.out.println("8.) Findset(?,?,?)...");		
		it = Relatrix.findSet("?", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = ((Result3)o).toArray();
			if(DEBUG)
				System.out.println(++recs+"="+c[0]+" --- "+c[1]+" --- "+c[2]);
			if(ar3.size() == 0) ar3.add(c);
		}
		
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the");
		System.out.println("wildcard results. They should produce relationships with these elements");
		recs = 0;
		System.out.println("9.) Findset(<obj>,<obj>,<obj>) using domain="+ar3.get(0)[0]+" map="+ar3.get(0)[1]+" range="+ar3.get(0)[2]);
		it = Relatrix.findSet(ar3.get(0)[0], ar3.get(0)[1], ar3.get(0)[2]);
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//ar.add(c[0]);
		}
		recs =0;
		System.out.println("10.) Findset(*,*,<obj>) using range="+ar.get(0));		
		it = Relatrix.findSet("*", "*", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar.size() == 0 ) ar.add(((Result1)o).get());
		}
		recs = 0;
		System.out.println("11.) Findset(*,<obj>,*) using map="+ar.get(1));		
		it = Relatrix.findSet("*", ar.get(1), "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar.size() == 1 ) ar.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("12.) Findset(<obj>,*,*) using domain="+ar.get(2));		
		it = Relatrix.findSet(ar.get(2), "*", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar.size() == 2) ar.add(((Result1)o).get());
		}
		recs = 0;
		System.out.println("13.) Findset(*,<obj>,<obj>) using map="+ar2.get(0)[0]+" range="+ar2.get(0)[1]);		
		it = Relatrix.findSet("*", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar2.size() == 0) ar2.add(((Result1)o).get());
		}
		recs = 0;
		System.out.println("14.) Findset(<obj>,*,<obj>) using domain="+ar2.get(1)[0]+" range="+ar2.get(1)[1]);		
		it = Relatrix.findSet(ar2.get(1)[0], "*", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar2.size() == 1) ar2.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("15.) Findset(<obj>,<obj>,*) using domain="+ar2.get(2)[0]+" map="+ar2.get(2)[1]);		
		it = Relatrix.findSet(ar2.get(2)[0], ar2.get(2)[1], "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			System.out.println(++recs+"="+o);
			//if(ar2.size() == 2) ar2.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("16.) Findset(?,?,<obj>) using range="+ar.get(0));		
		it = Relatrix.findSet("?", "?", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar.size() == 0 ) ar.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("17.) Findset(?,<obj>,?) using map="+ar.get(1));		
		it = Relatrix.findSet("?", ar.get(1), "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar.size() == 1 ) ar.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("18.) Findset(<obj>,?,?) using domain="+ar.get(2));		
		it = Relatrix.findSet(ar.get(2), "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			System.out.println(++recs+"="+o);
			//if(ar.size() == 2) ar.add(((Result2)o).get(0)); ar.add(((Result2)o).get(1));
		}
		recs =0;
		System.out.println("19.) Findset(?,<obj>,<obj>) using map="+ar2.get(0)[0]+" range="+ar2.get(0)[1]);		
		it = Relatrix.findSet("?", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar2.size() == 0) ar2.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("20.) Findset(<obj>,?,<obj>) using domain="+ar2.get(1)[0]+" range="+ ar2.get(1)[1]);		
		it = Relatrix.findSet(ar2.get(1)[0], "?", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar2.size() == 1) ar2.add(((Result1)o).get());
		}
		recs =0;
		System.out.println("21.) Findset(<obj>,<obj>,?) using domain="+ar2.get(2)[0]+" map="+ar2.get(2)[1]);		
		it = Relatrix.findSet(ar2.get(2)[0], ar2.get(2)[1], "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			if(DEBUG)
				System.out.println(++recs+"="+o);
			//if(ar2.size() == 2) ar2.add(((Result1)o).get());
		}
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims));
	}
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB");
		Iterator it = Relatrix.findSet("*","*","*");
		long timx = System.currentTimeMillis();
		int i = 0;
		while(it.hasNext()) {
			Object fkey = it.next();
			DomainMapRange dmr = (DomainMapRange)((Result1)fkey).get();
			Relatrix.remove(dmr);
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		Iterator<?> its = Relatrix.findSet("*","*","*");
		while(its.hasNext()) {
			Object nex = its.next();
			//System.out.println(i+"="+nex);
			System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
		}
		long siz = Relatrix.size();
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(DomainMapRange.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("DomainMapRange:"+nex);
		}
		siz = Relatrix.size();
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(DomainRangeMap.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("DomainRangeMap:"+nex);
		}
		siz = Relatrix.size();
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after delete/commit");
		}

		it = RelatrixKV.entrySet(MapDomainRange.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("MapDomainRange:"+nex);
		}
		siz = RelatrixKV.size(MapDomainRange.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}

		it = RelatrixKV.entrySet(MapRangeDomain.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("MapRangeDomain:"+nex);
		}
		siz = RelatrixKV.size(MapRangeDomain.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(RangeDomainMap.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("RangeDomainMap:"+nex);
		}
		siz = RelatrixKV.size(RangeDomainMap.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(RangeMapDomain.class);
		while(it.hasNext()) {
			Object nex = it.next();
			System.out.println("RangeMapDomain:"+nex);
		}
		siz = RelatrixKV.size(RangeMapDomain.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}


}
