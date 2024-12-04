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
import com.neocoretechs.relatrix.Result3;


/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findHeadSet retrieval.
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021
 *
 */
public class EmbeddedRetrievalBattery2A {
	public static boolean DEBUG = false;
	public static boolean DISPLAY = false;
	public static int displayLinesOn[]= {0,1000,4500,9900};
	public static int displayLinesOff[]= {100,1100,5100,9999};
	public static int displayLine = 0;
	public static int displayLineCtr = 0;
	public static long displayTimer = 0;
	public static int min = 0;
	public static int max = 10000;
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
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Result3> ar3 = new ArrayList<Result3>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Mixed Headset queries:");
		displayLine = 0;
		System.out.println("1.) FindHeadset(?,?,?,String.class, String.class, Long.class");
		it =  Relatrix.findHeadSet("?", "?", "?",String.class, String.class, Long.class);
		while(it.hasNext()) {
			Object o = it.next();
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			ar3.add(c);
		}
		displayLine = 0;
		System.out.println("2.) FindHeadSet(?,?,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","?","?",ar3.get(0).get(0),ar3.get(0).get(1),ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine=0;
		//RelatrixHeadsetIterator.DEBUG = true;
		displayLine = 0;
		System.out.println("3.) FindHeadSet(*,?,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","?","?",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		//RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("4.) FindHeadSet(*,?,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","?","*",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("5.) FindHeadSet(?,*,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","*","*",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("6.) FindHeadSet(?,*,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","*","?",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("7.) FindHeadSet(?,?,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","?","*",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("8.) FindHeadSet(*,*,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","*","?",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("9.) FindHeadSet(*,*,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","*","*",ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		System.out.println("10.) FindHeadSet(?,?,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","?","?",String.class,ar3.get(0).get(1),ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine=0;
		//RelatrixHeadsetIterator.DEBUG = true;
		displayLine = 0;
		System.out.println("11.) FindHeadSet(*,?,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","?","?",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		//RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("12.) FindHeadSet(*,?,*,<obj>,<obj>,<obj>) using = String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","?","*",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("13.) FindHeadSet(?,*,*,<obj>,<obj>,<obj>) using = String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","*","*",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("14.) FindHeadSet(?,*,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","*","?",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("15.) FindHeadSet(?,?,*,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("?","?","*",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("16.) FindHeadSet(*,*,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","*","?",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("17.) FindHeadSet(*,*,*,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+(ar3.get(0)).get(2));
		it = Relatrix.findHeadSet("*","*","*",String.class, ar3.get(0).get(1), ar3.get(0).get(2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		//------------------------------------------------------------------
		// same but with regions
		//
		displayLine = 0;
		System.out.println("18.) FindHeadset(?,?,?,String.class, String.class,"+(new Long(max/2))+");");
		it =  Relatrix.findHeadSet("?", "?", "?",String.class, String.class, new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
			ar3.add(c);
		}
		displayLine = 0;
		System.out.println("19.) FindHeadSet(?,?,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","?","?",ar3.get(0).get(0),ar3.get(0).get(1), new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine=0;
		//RelatrixHeadsetIterator.DEBUG = true;
		displayLine = 0;
		System.out.println("20.) FindHeadSet(*,?,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","?","?",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		//RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("21.) FindHeadSet(*,?,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","?","*",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("22.) FindHeadSet(?,*,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","*","*",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("23.) FindHeadSet(?,*,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","*","?",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("24.) FindHeadSet(?,?,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","?","*",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("25.) FindHeadSet(*,*,?,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","*","?",ar3.get(0).get(0), ar3.get(0).get(1), new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("26.) FindHeadSet(*,*,*,<obj>,<obj>,<obj>) using ="+(ar3.get(0)).get(0)+","+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","*","*",ar3.get(0).get(0), ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		System.out.println("27.) FindHeadSet(?,?,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","?","?",String.class,ar3.get(0).get(1), new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine=0;
		//RelatrixHeadsetIterator.DEBUG = true;
		displayLine = 0;
		System.out.println("28.) FindHeadSet(*,?,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","?","?",String.class, ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine = 0;
		//RelatrixHeadsetIterator.DEBUG = true;
		System.out.println("29.) FindHeadSet(*,?,*,<obj>,<obj>,<obj>) using = String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","?","*",String.class, ar3.get(0).get(1), new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("30.) FindHeadSet(?,*,*,<obj>,<obj>,<obj>) using = String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","*","*",String.class, ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("31.) FindHeadSet(?,*,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","*","?",String.class, ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("32.) FindHeadSet(?,?,*,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("?","?","*",String.class, ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("33.) FindHeadSet(*,*,?,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","*","?",String.class, ar3.get(0).get(1), new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
		}
		displayLine =0;
		System.out.println("34.) FindHeadSet(*,*,*,<obj>,<obj>,<obj>) using =String.class,"+(ar3.get(0)).get(1)+","+ new Long(max/2));
		it = Relatrix.findHeadSet("*","*","*",String.class, ar3.get(0).get(1),  new Long(max/2));
		while(it.hasNext()) {
			Object o = it.next();
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY)
				System.out.println(displayLine+"="+c);
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
