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
 * This series of tests uses classes and concrete object instances in various findStream permutations
 * resulting in streams.
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public class EmbeddedStreamRetrievalBattery {
	public static boolean DEBUG = true;
	public static int min = 0;
	public static int max = 100;
	static String key = "This is a test"; 
	static String uniqKeyFmt = "%0100d";
	public static int recs = 0;
	private static int SAMPLESIZE = 5;
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
		System.out.println("Stream Battery1 ");
		long tims = System.currentTimeMillis();
		recs = 0;
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Comparable> ar = new ArrayList<Comparable>();
		ArrayList<Comparable> am = new ArrayList<Comparable>();
		ArrayList<Comparable> ad = new ArrayList<Comparable>();
		ArrayList<Comparable[]> ar2 = new ArrayList<Comparable[]>(); // will store 2 element result sets map range
		ArrayList<Comparable[]> ar2dr = new ArrayList<Comparable[]>(); // will store 2 element result sets domain range
		ArrayList<Comparable[]> ar2dm = new ArrayList<Comparable[]>(); // will store 2 element result sets domain map
		ArrayList<Comparable[]> ar3 = new ArrayList<Comparable[]>(); // will store 3 element result sets
	
		System.out.println("Wildcard queries. Will store samplesize of "+SAMPLESIZE+" for subsequent tests.");
		recs = 0;
		System.out.println("1.) findStream(*,*,*)...");
		Relatrix.findStream("*", "*", "*").forEach(e->{
			System.out.println(++recs+"="+e);
		});
		recs = 0;
		System.out.println("2.) findStream(*,*,?)...");		
		Relatrix.findStream("*", "*", "?").forEach(e->{
			System.out.println(++recs+"="+e);
			if(ar.size() < SAMPLESIZE  ) 
				ar.add(((Result)e).get());
		});
		recs = 0;
		System.out.println("3.) findStream(*,?,*)...");		
		Relatrix.findStream("*", "?", "*").forEach(e->{
			System.out.println(++recs+"="+e);
			if(am.size() < SAMPLESIZE ) 
				am.add(((Result)e).get());
		});
		recs = 0;
		System.out.println("4.) findStream(?,*,*)...");		
		Relatrix.findStream("?", "*", "*").forEach(e->{
			System.out.println(++recs+"="+e);
			if(ad.size() < SAMPLESIZE) 
				ad.add(((Result)e).get());
		});
		recs=0;
		System.out.println("5.) findStream(*,?,?)...");		
		Relatrix.findStream("*", "?", "?").forEach(e->{
			Comparable[] c = ((Result2)e).toArray();
			System.out.println(++recs+"="+e);
			if(ar2.size() < SAMPLESIZE) 
				ar2.add(c);
		});
		recs = 0;
		System.out.println("6.) findStream(?,*,?)...");		
		Relatrix.findStream("?", "*", "?").forEach(e->{
			Comparable[] c = ((Result2)e).toArray();
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2dr.size() < SAMPLESIZE) 
				ar2dr.add(c);
		});
		recs = 0;
		System.out.println("7.) findStream(?,?,*)...");		
		Relatrix.findStream("?", "?", "*").forEach(e->{
			Comparable[] c = ((Result2)e).toArray();
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2dm.size() < SAMPLESIZE) 
				ar2dm.add(c);
		});
		recs = 0;
		System.out.println("8.) findStream(?,?,?)...");		
		Relatrix.findStream("?", "?", "?").forEach(e->{
			Comparable[] c = ((Result3)e).toArray();
			System.out.println(++recs+"="+c[0]+" --- "+c[1]+" --- "+c[2]);
			if(ar3.size() < SAMPLESIZE) 
				ar3.add(c);
		});
		
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the results");
		System.out.println("3 object instances:");
		for(int j = 0; j < ar3.size(); j++) {
			recs = 0;
			System.out.println("9."+j+") findStream(<obj>,<obj>,<obj>) using domain="+ar3.get(j)[0]+" map="+ar3.get(j)[1]+" range="+ar3.get(j)[2]);
			Relatrix.findStream(ar3.get(j)[0], ar3.get(j)[1], ar3.get(j)[2]).forEach(e->{
				System.out.println(++recs+"="+e);
			});
		}
		System.out.println("----------");
		System.out.println("1 object instance with wildcards:");
		for(int j = 0; j < ar.size(); j++) {
			recs = 0;
			System.out.println("10."+j+") findStream(*,*,<obj>) using range="+ar.get(j));		
			Relatrix.findStream("*", "*", ar.get(j)).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("11."+j+") findStream(*,<obj>,*) using map="+am.get(j));		
			Relatrix.findStream("*", am.get(j), "*").forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("12."+j+") findStream(<obj>,*,*) using domain="+ad.get(j));		
			Relatrix.findStream(ad.get(j), "*", "*").forEach(e->{
				System.out.println(++recs+"="+e);
			});
		}
		System.out.println("----------");
		System.out.println("2 object instances with wildcards:");
		for(int j = 0; j < ar2.size(); j++) {
			recs = 0;
			System.out.println("13."+j+") findStream(*,<obj>,<obj>) using map="+ar2.get(j)[0]+" range="+ar2.get(j)[1]);		
			Relatrix.findStream("*", ar2.get(j)[0], ar2.get(j)[1]).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("14."+j+") findStream(<obj>,*,<obj>) using domain="+ar2dr.get(j)[0]+" range="+ar2dr.get(j)[1]);		
			Relatrix.findStream(ar2dr.get(j)[0], "*", ar2dr.get(j)[1]).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("15."+j+") findStream(<obj>,<obj>,*) using domain="+ar2dm.get(j)[0]+" map="+ar2dm.get(j)[1]);		
			Relatrix.findStream(ar2dm.get(j)[0], ar2dm.get(j)[1], "*").forEach(e->{
				System.out.println(++recs+"="+e);
			});
		}
		recs = 0;
		System.out.println("----------");
		System.out.println("1 object instance with 2 returns:");
		for(int j = 0; j < ar.size(); j++) {
			System.out.println("16."+j+") findStream(?,?,<obj>) using range="+ar.get(j));		
			Relatrix.findStream("?", "?", ar.get(j)).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs =0;
			System.out.println("17."+j+") findStream(?,<obj>,?) using map="+am.get(j));		
			Relatrix.findStream("?", am.get(j), "?").forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs =0;
			System.out.println("18."+j+") findStream(<obj>,?,?) using domain="+ad.get(j));		
			Relatrix.findStream(ad.get(j), "?", "?").forEach(e->{
				System.out.println(++recs+"="+e);
			});
		}
		System.out.println("----------");
		System.out.println("2 object instances with 1 return:");
		for(int j = 0; j < ar2.size(); j++) {
			recs = 0;
			System.out.println("19."+j+") findStream(?,<obj>,<obj>) using map="+ar2.get(j)[0]+" range="+ar2.get(j)[1]);		
			Relatrix.findStream("?", ar2.get(j)[0], ar2.get(j)[1]).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("20."+j+") findStream(<obj>,?,<obj>) using domain="+ar2dr.get(j)[0]+" range="+ ar2dr.get(j)[1]);		
			Relatrix.findStream(ar2dr.get(j)[0], "?", ar2dr.get(j)[1]).forEach(e->{
				System.out.println(++recs+"="+e);
			});
			recs = 0;
			System.out.println("21."+j+") findStream(<obj>,<obj>,?) using domain="+ar2dm.get(j)[0]+" map="+ar2dm.get(j)[1]);		
			Relatrix.findStream(ar2dm.get(j)[0], ar2dm.get(j)[1], "?").forEach(e->{
				System.out.println(++recs+"="+e);
			});
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
