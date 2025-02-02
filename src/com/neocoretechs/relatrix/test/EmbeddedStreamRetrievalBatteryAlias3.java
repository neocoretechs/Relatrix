package com.neocoretechs.relatrix.test;

import java.io.IOException;
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
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.Morphism.displayLevels;
import com.neocoretechs.rocksack.Alias;


/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findTailStream retrieval for alias functionality.
 * NOTES:
 * program argument is tablespace i.e. C:/users/you/Relatrix/ which will create databases in C:/users/you/Relatrix/ALIAS1, 2, 3..
 * optional arguments are [ [init] [max nnn] ]
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2025
 *
 */
public class EmbeddedStreamRetrievalBatteryAlias3 {
	public static boolean DEBUG = false;
	public static boolean DISPLAY = false;
	public static boolean DISPLAYALL = true;
	public static int displayLinesOn[]= {0,1000,99900};
	public static int displayLinesOff[]= {100,1100,99999};
	public static int displayLine = 0;
	public static int displayLineCtr = 0;
	public static long displayTimer = 0;
	public static int min = 0;
	public static int max = 100;
	static String key = "This is a test"; 
	static String uniqKeyFmt = "%0100d";
	private static int SAMPLESIZE = 5;
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	private static long timx;
	private static int i;
	/**
	*/
	public static void main(String[] argv) throws Exception {
		Morphism.displayLevel = Morphism.displayLevels.VERBOSE;
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		Relatrix.setAlias(alias1,tablespace+alias1);
		Relatrix.setAlias(alias2,tablespace+alias2);
		Relatrix.setAlias(alias3,tablespace+alias3);
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
		if(Relatrix.size(alias1) == 0) {
			if(DEBUG)
				System.out.println("Zero items, Begin insertion from "+min+" to "+max);
			battery0(argv, alias1);
			battery0(argv, alias2);
			battery0(argv, alias3);
		}
		battery1(argv, alias1);
		battery1(argv, alias2);
		battery1(argv, alias3);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(0);
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
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery0(String[] argv, Alias alias12) throws Exception {
		System.out.println("Battery0 "+alias12);
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		DomainMapRange dmr = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				dmr = Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		 System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}

	/**
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1(String[] argv, Alias alias12) throws Exception {
		System.out.println("Stream Battery1 "+alias12);
		long tims = System.currentTimeMillis();
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Comparable> ar = new ArrayList<Comparable>();
		ArrayList<Comparable> ad = new ArrayList<Comparable>();
		ArrayList<Comparable> am = new ArrayList<Comparable>();
		ArrayList<Comparable> ar2 = new ArrayList<Comparable>(); // will store 2 element result sets map range
		ArrayList<Comparable> ar2dm = new ArrayList<Comparable>(); // will store 2 element result sets domain map
		ArrayList<Comparable> ar2dr = new ArrayList<Comparable>(); // will store 2 element result sets domain range
		ArrayList<Comparable> ar3 = new ArrayList<Comparable>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Wildcard queries:");
		displayLine = 0;
		System.out.println("1.) findTailStream("+alias12+",*,*,*,String.class, String.class, Long.class)...");
		Relatrix.findTailStream(alias12, '*', '*', '*',String.class, String.class, Long.class).forEach(o->{
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
		});
		displayLine = 0;
		System.out.println("2.) findTailStream("+alias12+",*,*,?,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '*', '*', '?',String.class, String.class, Long.class).forEach(o->{
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ar.size() < SAMPLESIZE ) {
				ar.add(c);
			}
		});
		displayLine = 0;
		System.out.println("3.) findTailStream("+alias12+",*,?,*,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '*', '?', '*',String.class, String.class, Long.class).forEach(o->{
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(am.size() < SAMPLESIZE ) {
				am.add(c);
			}
		});
		displayLine = 0;
		System.out.println("4.) findTailStream("+alias12+",?,*,*.String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '?', '*', '*',String.class, String.class, Long.class).forEach(o->{
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ad.size() < SAMPLESIZE) {
				ad.add(c);
			}
		});
		displayLine=0;
		System.out.println("5.) findTailStream("+alias12+",*,?,?,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '*', '?', '?',String.class, String.class, Long.class).forEach(o->{
			Result2 c = (Result2)o; // result2
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ar2.size() < SAMPLESIZE) {
				ar2.add(c);
			}
		});
		displayLine = 0;
		System.out.println("6.) findTailStream("+alias12+",?,*,?,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '?', '*', '?',String.class, String.class, Long.class).forEach(o->{
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ar2dr.size() < SAMPLESIZE) {
				ar2dr.add(c);
			}
		});
		displayLine = 0;
		System.out.println("7.) findTailStream("+alias12+",?,?,*,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '?', '?', '*',String.class, String.class, Long.class).forEach(o->{
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ar2dm.size() < SAMPLESIZE) {
				ar2dm.add(c);
			}
		});
		displayLine = 0;
		System.out.println("8.) findTailStream("+alias12+",?,?,?,String.class, String.class, Long.class)...");		
		Relatrix.findTailStream(alias12, '?', '?', '?',String.class, String.class, Long.class).forEach(o->{
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			if(ar3.size() < SAMPLESIZE) {
				ar3.add(c);
			}
		});
		for(int j = 0; j < ar3.size(); j++) {
			displayLine = 0;
			System.out.println("8."+j+") findTailStream("+alias12+",?,?,?,<obj>,<obj>,<obj>) using domain="+((Result)ar3.get(j)).get(0)+",map="+((Result)ar3.get(j)).get(1)+",range="+((Result)ar3.get(j)).get(2));
			Relatrix.findTailStream(alias12, '?','?','?',((Result)ar3.get(j)).get(0), ((Result)ar3.get(j)).get(1), ((Result)ar3.get(j)).get(2)).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("Should retrieve none, since range is specified as String and we only stored Long...");
			System.out.println("8A."+j+") findTailStream("+alias12+",?,*,*,<obj>,String.class, String.class) using domain="+((Result)ar3.get(j)).get(0));		
			Relatrix.findTailStream(alias12, '?','*', '*', ((Result)ar3.get(j)).get(0), String.class, String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("----------\r\nAbove are wildcard permutations. Now retrieve those with object references using the");
		System.out.println("wildcard results. Recall tailset is greater or equal to 'from' element...");
		for(int j = 0; j < ar3.size(); j++) {
			displayLine = 0;
			System.out.println("9."+j+") findTailStream("+alias12+",<obj>,<obj>,<obj>) using domain="+((Result)ar3.get(j)).get(0)+",map="+((Result)ar3.get(j)).get(1)+",range="+((Result)ar3.get(j)).get(2));
			Relatrix.findTailStream(alias12, ((Result)ar3.get(j)).get(0), ((Result)ar3.get(j)).get(1), ((Result)ar3.get(j)).get(2)).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("10."+j+") findTailStream("+alias12+",*,*,<obj>,String.class, String.class) using range="+((Result)ar.get(j)).get(0));		
			Relatrix.findTailStream(alias12, '*', '*', ((Result)ar.get(j)).get(0), String.class, String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		for(int j = 0; j < ar.size(); j++) {
			displayLine = 0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("11."+j+") findTailStream("+alias12+",*,<obj>,*, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			Relatrix.findTailStream(alias12, '*', ((Result)am.get(j)).get(0), '*',String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("12."+j+") findTailStream("+alias12+",<obj>,*,*,String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			Relatrix.findTailStream(alias12, ((Result)ad.get(j)).get(0), '*', '*',String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		for(int j = 0; j < ar2.size(); j++) {
			// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
			displayLine = 0;
			System.out.println("13."+j+") findTailStream("+alias12+",*,<obj>,<obj>,String.class) using map="+((Result)ar2.get(j)).toArray()[0]+" range="+((Result)ar2.get(j)).toArray()[1]);		
			Relatrix.findTailStream(alias12, '*', ((Result)ar2.get(j)).toArray()[0], ((Result)ar2.get(j)).toArray()[1], String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine = 0;
			System.out.println("14."+j+") findTailStream("+alias12+",<obj>,*,<obj>,String.class) using domain="+((Result)ar2dr.get(j)).toArray()[0]+", range="+((Result)ar2dr.get(j)).toArray()[1]);		
			Relatrix.findTailStream(alias12, ((Result)ar2dr.get(j)).toArray()[0], '*', ((Result)ar2dr.get(j)).toArray()[1], String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		for(int j = 0; j < ar2.size(); j++) {
			displayLine=0;
			System.out.println("15."+j+") findTailStream("+alias12+",<obj>,<obj>,*, Long.class) using domain="+((Result)ar2dm.get(j)).toArray()[0]+", map="+((Result)ar2dm.get(j)).toArray()[1]);		
			Relatrix.findTailStream(alias12, ((Result)ar2dm.get(j)).toArray()[0], ((Result)ar2dm.get(j)).toArray()[1], '*', Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		for(int j = 0; j < ar.size(); j++) {
			displayLine=0;
			System.out.println("16."+j+") findTailStream("+alias12+",?,?,<obj>, String.class, String.class) using range="+((Result)ar.get(j)).get(0));		
			Relatrix.findTailStream(alias12, '?', '?', ((Result)ar.get(j)).get(0), String.class, String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			System.out.println("17."+j+") findTailStream("+alias12+",?,<obj>,?, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			Relatrix.findTailStream(alias12, '?', ((Result)am.get(j)).get(0), '?', String.class, Long.class).forEach(o->{
				Result2 c = (Result2)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			System.out.println("18."+j+") findTailStream("+alias12+",<obj>,?,?, String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			Relatrix.findTailStream(alias12, ((Result)ad.get(j)).get(0), '?', '?', String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		for(int j = 0; j < ar2.size(); j++) {
			displayLine=0;
			System.out.println("19."+j+") findTailStream("+alias12+",?,<obj>,<obj>, String.class) using map="+((Result)ar2.get(j)).get(0)+" range="+((Result)ar2.get(j)).get(1));		
			Relatrix.findTailStream(alias12, '?', ((Result)ar2.get(j)).get(0), ((Result)ar2.get(j)).get(1), String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("20."+j+") findTailStream("+alias12+",<obj>,?,<obj>,String.class) using domain="+((Result)ar2dr.get(j)).get(0)+" range="+ ((Result)ar2dr.get(j)).get(1));		
			Relatrix.findTailStream(alias12, ((Result)ar2dr.get(j)).get(0), '?', ((Result)ar2dr.get(j)).get(1), String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("21."+j+") findTailStream("+alias12+",<obj>,<obj>,?,Long.class) using domain="+((Result)ar2dm.get(j)).get(0)+" map="+((Result)ar2dm.get(j)).get(1));		
			Relatrix.findTailStream(alias12, ((Result)ar2dm.get(j)).get(0), ((Result)ar2dm.get(j)).get(1), '?',Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims));
	}
	/**
	 * remove entries, all relationships should be recursively deleted
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println(alias12+" CleanDB DMR size="+Relatrix.size(alias12,DomainMapRange.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(alias12,DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12,MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12,MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(alias12,RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(alias12,RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet(alias12,'*','*','*');
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
