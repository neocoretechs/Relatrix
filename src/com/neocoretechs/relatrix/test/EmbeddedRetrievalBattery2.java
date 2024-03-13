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
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RemoteTailSetIterator;
import com.neocoretechs.relatrix.iterator.RelatrixHeadsetIterator;
import com.neocoretechs.relatrix.key.DBKey;

/**
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff C 2021
 *
 */
public class EmbeddedRetrievalBattery2 {
	public static boolean DEBUG = false;
	public static boolean DISPLAY = false;
	public static int displayLinesOn[]= {0,1000,99900};
	public static int displayLinesOff[]= {100,1100,99999};
	public static int displayLine = 0;
	public static int displayLineCtr = 0;
	public static long displayTimer = 0;
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
			battery00(argv);
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
			if(displayLine > displayLinesOff[displayLineCtr] && displayLineCtr < displayLinesOff.length)
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
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery00(String[] argv) throws Exception {
		System.out.println("Battery00 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		DomainMapRange dmr = null;
		int modu = 0;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, modu++);
			try {
				dmr = Relatrix.store(fkey, i, "Has unit");
				++recs;
				if(modu % 5 == 0)
					modu = 0;
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
		ArrayList<Comparable> ar2 = new ArrayList<Comparable>(); // will store 2 element result sets
		ArrayList<Comparable> ar3 = new ArrayList<Comparable>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Wildcard queries:");
		displayLine = 0;
		System.out.println("1.) FindHeadset(*,*,*,String.class, String.class, Long.class)...");
		it =  Relatrix.findHeadSet("*", "*", "*",String.class, String.class, Long.class);
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//ar.add(c[0]);
		}
		displayLine = 0;
		System.out.println("2.) FindHeadset(*,*,?,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("*", "*", "?",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar.size() == 0 ) {
				ar.add(c);
			}
		}
		displayLine = 0;
		System.out.println("3.) FindHeadSet(*,?,*,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("*", "?", "*",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar.size() == 1 ) {
				ar.add(c);
			}
		}
		displayLine = 0;
		System.out.println("4.) FindHeadSet(?,*,*.String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("?", "*", "*",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar.size() == 2) {
				ar.add(c);
			}
		}
		displayLine=0;
		System.out.println("5.) FindHeadSet(*,?,?,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("*", "?", "?",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o; // result2
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar2.size() == 0) {
				ar2.add(c);
				if(!DISPLAY) {
					System.out.println("DISPLAY reduced, breaking from retrieval");
					break;
				}
			}
		}
		displayLine = 0;
		System.out.println("6.) FindHeadSet(?,*,?,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("?", "*", "?",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar2.size() == 1) {
				ar2.add(c);
			}
		}
		displayLine = 0;
		System.out.println("7.) FindHeadSet(?,?,*,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("?", "?", "*",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar2.size() == 2) {
				ar2.add(c);
			}
		}
		displayLine = 0;
		System.out.println("8.) FindHeadet(?,?,?,String.class, String.class, Long.class)...");		
		it = Relatrix.findHeadSet("?", "?", "?",String.class, String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			if(ar3.size() == 0) {
				ar3.add(c);
			}
		}
		
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the");
		System.out.println("wildcard results. They should produce relationships with these elements");
		displayLine = 0;
		System.out.println("9.) FindHeadSet(<obj>,<obj>,<obj>) using ="+((Result)ar3.get(0)).get(0)+","+((Result)ar3.get(0)).get(1)+","+((Result)ar3.get(0)).get(2));
		it = Relatrix.findHeadSet(((Result)ar3.get(0)).get(0), ((Result)ar3.get(0)).get(1), ((Result)ar3.get(0)).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//ar.add(c[0]);
		}
		displayLine=0;
		RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("10.) FindHeadSet(*,*,<obj>,String.class, String.class) using range="+((Result)ar3.get(0)).get(3));		
		it = Relatrix.findHeadSet("*", "*", ((Result)ar3.get(0)).get(3), String.class, String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		displayLine = 0;
		//RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("11.) FindHeadSet(*,<obj>,*, String.class, Long.class) using map="+((Result)ar.get(1)).get(0));		
		it = Relatrix.findHeadSet("*", ((Result)ar.get(1)).get(0), "*",String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		displayLine =0;
		System.out.println("12.) FindHeadet(<obj>,*,*,String.class, Long.class) using domain="+((Result)ar.get(2)).get(0));		
		it = Relatrix.findHeadSet(((Result)ar.get(2)).get(0), "*", "*",String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
		displayLine = 0;
		System.out.println("13.) FindHeadset(*,<obj>,<obj>,String.class) using map="+((Result)ar2.get(0)).toArray()[0]+" range="+((Result)ar2.get(0)).toArray()[1]);		
		it = Relatrix.findHeadSet("*", ((Result)ar2.get(0)).toArray()[0], ((Result)ar2.get(0)).toArray()[1], String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 0) ar2.add(c);
		}
		displayLine = 0;
		System.out.println("14.) FindHeadSet(<obj>,*,<obj>,String.class) using ="+((Result)ar2.get(1)).toArray()[0]+", "+((Result)ar2.get(1)).toArray()[1]);		
		it = Relatrix.findHeadSet(((Result)ar2.get(1)).toArray()[0], "*", ((Result)ar2.get(1)).toArray()[1], String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 1) ar2.add(c);
		}
		displayLine =0;
		System.out.println("15.) FindHeadSet(<obj>,<obj>,*, Long.class) using domain="+((Result)ar2.get(2)).toArray()[0]+", map="+((Result)ar2.get(2)).toArray()[1]);		
		it = Relatrix.findHeadSet(((Result)ar2.get(2)).toArray()[0], ((Result)ar2.get(2)).toArray()[1], "*", Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 2) ar2.add(c);
		}
		displayLine =0;
		System.out.println("16.) FindHeadSet(?,?,<obj>, String.class, String.class) using range="+((Result)ar.get(0)).get(0));		
		it = Relatrix.findHeadSet("?", "?", ((Result)ar.get(0)).get(0), String.class, String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		displayLine =0;
		System.out.println("17.) FindHeadSet(?,<obj>,?, String.class, Long.class) using map="+((Result)ar.get(1)).get(0));		
		it = Relatrix.findHeadSet("?", ((Result)ar.get(1)).get(0), "?", String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		displayLine =0;
		System.out.println("18.) FindHeadSet(<obj>,?,?, String.class, Long.class) using domain="+((Result)ar.get(2)).get(0));		
		it = Relatrix.findHeadSet(((Result)ar.get(2)).get(0), "?", "?", String.class, Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		displayLine =0;
		System.out.println("19.) FindHeadSet(?,<obj>,<obj>, String.class) using map="+((Result)ar2.get(0)).get(0)+" range="+((Result)ar2.get(0)).get(1));		
		it = Relatrix.findHeadSet("?", ((Result)ar2.get(0)).get(0), ((Result)ar2.get(0)).get(1), String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 0) ar2.add(c);
		}
		displayLine =0;
		System.out.println("20.) FindHeadSet(<obj>,?,<obj>,String.class) using domain="+((Result)ar2.get(1)).get(0)+" range="+ ((Result)ar2.get(1)).get(1));		
		it = Relatrix.findHeadSet(((Result)ar2.get(1)).get(0), "?", ((Result)ar2.get(1)).get(1), String.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 1) ar2.add(c);
		}
		displayLine =0;
		System.out.println("21.) FindHeadSet(<obj>,<obj>,?,Long.class) using domain="+((Result)ar2.get(2)).get(0)+" map="+((Result)ar2.get(2)).get(1));		
		it = Relatrix.findHeadSet(((Result)ar2.get(2)).get(0), ((Result)ar2.get(2)).get(1), "?",Long.class);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			//if(ar2.size() == 2) ar2.add(c);
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
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
			Relatrix.remove(dmr.getDomain(), dmr.getMap(), dmr.getRange());
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
