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
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;


/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findSubSet retrieval. We will let our samplesize be dictated by hi and low range values
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2024
 *
 */
public class EmbeddedRetrievalBattery4 {
	public static boolean DEBUG = false;
	public static boolean DISPLAY = false;
	private static boolean DISPLAYALL = true;
	public static int displayLinesOn[]= {0,1000,5000,9990,15000,20000,30000,40000,50000,60000,70000,80000,90000,99000};
	public static int displayLinesOff[]= {100,1100,5100,9999,15999,20999,30999,40999,50999,60999,70999,80999,90999,100000};
	public static int displayLine = 0;
	public static int displayLineCtr = 0;
	public static long displayTimer = 0;
	public static int min = 0;
	public static int max = 100;
	static long lorange = (max/20L);
	static long hirange = (max/10L);
	static Long lo = (long) min;
	static Long hi = (long) max/10;
	static Long increment = 10L;
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
	
	public static void displayCtrl() {
		if(displayLine == 0)
			displayLineCtr = 0;
		if(displayLine >= displayLinesOn[displayLineCtr] && displayLine <= displayLinesOff[displayLineCtr]) {
			if(!DISPLAY)
				displayTimer = System.currentTimeMillis();
			DISPLAY = true;
		} else {
			if(DISPLAY)
				System.out.println("Time between lines:"+displayLinesOn[displayLineCtr]+" and "+displayLinesOff[displayLineCtr]+" is "+(System.currentTimeMillis()-displayTimer)+" ms.");
			DISPLAY = false;
			if(displayLine > displayLinesOff[displayLineCtr] && displayLineCtr < displayLinesOff.length-1)
				++displayLineCtr;
		}
		++displayLine;
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
		long tims = System.currentTimeMillis();
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Comparable> ar = new ArrayList<Comparable>(); // range
		ArrayList<Comparable> am = new ArrayList<Comparable>(); // map
		ArrayList<Comparable> ad = new ArrayList<Comparable>(); // domain
		ArrayList<Comparable> ar2 = new ArrayList<Comparable>(); // will store 2 element result sets map, range
		ArrayList<Comparable> ar2dr = new ArrayList<Comparable>(); // will store 2 element result sets domain,range
		ArrayList<Comparable> ar2dm = new ArrayList<Comparable>(); // will store 2 element result sets domain,map
		ArrayList<Comparable> ar3 = new ArrayList<Comparable>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Wildcard queries:");
		displayLine = 0;

		System.out.println("1.) findSubSet(*,*,*,String.class, String.class,"+lo+","+hi+");");
		it =  Relatrix.findSubSet("*", "*", "*",String.class, String.class, lo,hi);
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			//ar.add(c[0]);
		}
		displayLine = 0;
		System.out.println("2.) findSubSet(*,*,?,String.class, String.class, "+lo+","+hi+");");	
		it = Relatrix.findSubSet("*", "*", "?",String.class, String.class, lo, hi);
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			// samplesize is dictated by hi and low range
			ar.add(c);
		}
		displayLine = 0;
		String slo = key + String.format(uniqKeyFmt, lo);
		String shi = key  + String.format(uniqKeyFmt, hi);
		System.out.println("3.) findSubset(*,?,*,"+slo+","+shi+", String.class, Long.class);");		
		it = Relatrix.findSubSet("*", "?", "*",slo,shi, String.class, Long.class);
		while(it.hasNext()) {
			Object o = it.next();
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			am.add(c);
		}
		displayLine = 0;
		System.out.println("4.) findSubSet(?,*,*.String.class, String.class, "+lo+","+hi+");");			
		it = Relatrix.findSubSet("?", "*", "*",String.class, String.class, lo, hi);
		while(it.hasNext()) {
			Object o = it.next();
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ad.add(c);
		}
		displayLine=0;
		System.out.println("5.) findSubSet(*,?,?,String.class, String.class, "+lo+","+hi+")...");		
		it = Relatrix.findSubSet("*", "?", "?",String.class, String.class, lo, hi);
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o; // result2
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2.add(c);
		}
		displayLine = 0;
		System.out.println("6.) findSubSet(?,*,?,"+slo+","+shi+",String.class, "+lo+","+hi+")...");		
		it = Relatrix.findSubSet("?", "*", "?",slo,shi, String.class, lo,hi);
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2dr.add(c);
		}
		displayLine = 0;
		System.out.println("7.) findSubSet(?,?,*,"+slo+","+shi+", String.class, Long.class)...");		
		it = Relatrix.findSubSet("?", "?", "*",slo,shi, String.class, Long.class);
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2dm.add(c);
		}
		displayLine = 0;
		System.out.println("8.) FindSubset(?,?,?,"+slo+","+shi+", String.class, Long.class)...");		
		it = Relatrix.findSubSet("?", "?", "?",slo,shi, String.class, Long.class);
		while(it.hasNext()) {
			Object o = it.next();
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar3.add(c);
		}
		System.out.println("----------");
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the wildcard results.");
		for(int j = 0; j < ar3.size(); j++) {
			displayLine = 0;
			System.out.println("9."+j+") findSubSet(<obj>,<obj>,<obj>) using ="+
					((Result)ar3.get(j)).get(0)+",("+((Result)ar3.get(j)).get(0).getClass().getName()+"),"+
					((Result)ar3.get(j)).get(1)+",("+((Result)ar3.get(j)).get(1).getClass().getName()+"),"+
					((Result)ar3.get(j)).get(2)+",("+((Result)ar3.get(j)).get(2).getClass().getName());
			it = Relatrix.findSubSet(((Result)ar3.get(j)).get(0), ((Result)ar3.get(j)).get(1), ((Result)ar3.get(j)).get(2));
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+c);
			}
			displayLine=0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("10."+j+") findSubSet(*,*,<obj>,String.class, String.class) using range="+((Result)ar3.get(j)).get(3));		
			it = Relatrix.findSubSet("*", "*", ((Result)ar3.get(j)).get(3), String.class, String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
		}
		System.out.println("----------");
		System.out.println("Begin 1 instance match 2 wildcard testing");
		for(int j = 0; j < ar.size(); j++) {
			displayLine = 0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("11."+j+") findSubSet(*,<obj>,*, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			it = Relatrix.findSubSet("*", ((Result)am.get(j)).get(0), "*",String.class, Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine =0;
			System.out.println("12."+j+") FindSubset(<obj>,*,*,String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			it = Relatrix.findSubSet(((Result)ad.get(j)).get(0), "*", "*", String.class, Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
		}
		System.out.println("----------");
		System.out.println("Begin 2 instance match 1 wildcard testing");
		for(int j = 0; j < ar2.size(); j++) {
			// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
			displayLine = 0;
			System.out.println("13."+j+") findSubSet(*,<obj>,<obj>,String.class) using map="+((Result)ar2.get(j)).toArray()[0]+" range="+((Result)ar2.get(j)).toArray()[1]);		
			it = Relatrix.findSubSet("*", ((Result)ar2.get(j)).toArray()[0], ((Result)ar2.get(j)).toArray()[1], String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine = 0;
			System.out.println("14."+j+") findSubSet(<obj>,*,<obj>,String.class) using ="+((Result)ar2dr.get(j)).toArray()[0]+", "+((Result)ar2dr.get(j)).toArray()[1]);		
			it = Relatrix.findSubSet(((Result)ar2dr.get(j)).toArray()[0], "*", ((Result)ar2dr.get(j)).toArray()[1], String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine =0;
			System.out.println("15."+j+") findSubSet(<obj>,<obj>,*, Long.class) using domain="+((Result)ar2dm.get(j)).toArray()[0]+", map="+((Result)ar2dm.get(j)).toArray()[1]);		
			it = Relatrix.findSubSet(((Result)ar2dm.get(j)).toArray()[0], ((Result)ar2dm.get(j)).toArray()[1], "*",Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
		}
		System.out.println("----------");
		System.out.println("Begin 1 instance match 2 element return testing");
		for(int j = 0; j < ar.size(); j++) {
			displayLine =0;
			System.out.println("16."+j+") findSubSet(?,?,<obj>, String.class, String.class) using range="+((Result)ar.get(j)).get(0));		
			it = Relatrix.findSubSet("?", "?", ((Result)ar.get(j)).get(0), String.class, String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine =0;
			System.out.println("17."+j+") findSubSet(?,<obj>,?, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			it = Relatrix.findSubSet("?", ((Result)am.get(j)).get(0), "?", String.class, Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result2 c = (Result2)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine =0;
			System.out.println("18."+j+") findSubSet(<obj>,?,?, String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			it = Relatrix.findSubSet(((Result)ad.get(j)).get(0), "?", "?", String.class, Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
		}
		System.out.println("----------");
		System.out.println("Begin 2 instance match 1 element return testing");
		for(int j = 0; j < ar2.size(); j++) {
			displayLine=0;
			System.out.println("19."+j+") findSubSet(?,<obj>,<obj>, String.class) using map="+((Result)ar2.get(j)).get(0)+" range="+((Result)ar2.get(j)).get(1));		
			it = Relatrix.findSubSet("?", ((Result)ar2.get(j)).get(0), ((Result)ar2.get(j)).get(1), String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine=0;
			System.out.println("20."+j+") findSubSet(<obj>,?,<obj>,String.class) using domain="+((Result)ar2dr.get(j)).get(0)+" range="+ ((Result)ar2dr.get(j)).get(1));		
			it = Relatrix.findSubSet(((Result)ar2dr.get(j)).get(0), "?", ((Result)ar2dr.get(j)).get(1), String.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine=0;
			System.out.println("21."+j+") findSubSet(<obj>,<obj>,?,Long.class) using domain="+((Result)ar2dm.get(j)).get(0)+" map="+((Result)ar2dm.get(j)).get(1));		
			it = Relatrix.findSubSet(((Result)ar2dm.get(j)).get(0), ((Result)ar2dm.get(j)).get(1), "?",Long.class);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
		}
		//
		// proceed with hi/lo range tests
		//
		System.out.println("----------");
		System.out.println("Begin hi/lo range testing");
		for(int j = 0; j < ar2.size(); j++) {
			lo = lorange;
			hi = hirange;
			displayLine =0;
			System.out.println("22."+j+") findSubSet(*,*,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			it = Relatrix.findSubSet("*","*","?",((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			lo+=increment;
			hi+=increment;
			System.out.println("23."+j+") findSubSet(?,?,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			it = Relatrix.findSubSet("?","?","?",((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			lo+=increment;
			hi+=increment;
			System.out.println("24."+j+") findSubSet(?,*,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			it = Relatrix.findSubSet("?","*","?",((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi);
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
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
		Iterator<?> it = Relatrix.findSet("*","*","*");
		long timx = System.currentTimeMillis();
		int i = 0;
		while(it.hasNext()) {
			Object fkey = it.next();
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
			Relatrix.remove(dmr.getDomain(), dmr.getMap());
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		Iterator<?> its = Relatrix.findSet("*","*","*");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
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
			Comparable nex = (Comparable) it.next();
			System.out.println("DomainMapRange:"+nex);
		}
		siz = Relatrix.size();
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(DomainRangeMap.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("DomainRangeMap:"+nex);
		}
		siz = Relatrix.size();
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after delete/commit");
		}

		it = RelatrixKV.entrySet(MapDomainRange.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("MapDomainRange:"+nex);
		}
		siz = RelatrixKV.size(MapDomainRange.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}

		it = RelatrixKV.entrySet(MapRangeDomain.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("MapRangeDomain:"+nex);
		}
		siz = RelatrixKV.size(MapRangeDomain.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(RangeDomainMap.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("RangeDomainMap:"+nex);
		}
		siz = RelatrixKV.size(RangeDomainMap.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(RangeMapDomain.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("RangeMapDomain:"+nex);
		}
		siz = RelatrixKV.size(RangeMapDomain.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}/*
		it = RelatrixKV.entrySet(DBKey.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("DBKey:"+nex);
		}
		siz = RelatrixKV.size(DBKey.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 DBKEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DBKEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(Long.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("Long:"+nex);
		}
		siz = RelatrixKV.size(Long.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 Long MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 Long MISMATCH:"+siz+" > 0 after delete/commit");
		}
		it = RelatrixKV.entrySet(String.class);
		while(it.hasNext()) {
			Comparable nex = (Comparable) it.next();
			System.out.println("String:"+nex);
		}
		siz = RelatrixKV.size(String.class);
		if(siz > 0) {
			System.out.println("KV RANGE 1AR17 String MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 String MISMATCH:"+siz+" > 0 after delete/commit");
		}
		*/
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}


}
