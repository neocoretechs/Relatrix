package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.RelatrixTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.rocksack.TransactionId;


/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findSubStream retrieval in a transaction context. We will let our samplesize be dictated by hi and low range values
 * Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2021,2024
 *
 */
public class EmbeddedStreamRetrievalBatteryTransaction4 {
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
	private static long timx;
	private static int i;
	private static TransactionId xid;

	/**
	*/
	public static void main(String[] argv) throws Exception {
		System.out.println("Substream Provides a persistent collection iterator of keys 'from' element inclusive, 'to' element exclusive of the keys specified");
		RelatrixTransaction.setTablespace(argv[0]);
		AbstractRelation.displayLevel = AbstractRelation.displayLevels.MINIMAL;
		xid = RelatrixTransaction.getTransactionId();
		if(argv.length == 2 && argv[1].equals("init")) {
				battery1AR17(argv, xid);
		}
		if(RelatrixTransaction.size(xid) == 0) {
			battery0(argv, xid);
		}
		battery1(argv, xid);
		RelatrixTransaction.commit(xid);
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
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery0(String[] argv, TransactionId xid2) throws Exception {
		System.out.println("Battery0 "+xid2);
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		Relation dmr = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				dmr = RelatrixTransaction.store(xid2, fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		RelatrixTransaction.commit(xid2);
		System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}

	/**
	 * @param argv
	 * @param xid2 
	 * @throws Exception
	 */
	public static void battery1(String[] argv, TransactionId xid2) throws Exception {
		System.out.println("Stream Battery1 "+xid2);
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

		System.out.println("1.) findSubStream(xid,*,*,*,String.class, String.class,"+lo+","+hi+");");
		RelatrixTransaction.findSubStream(xid2,'*', '*', '*',String.class, String.class, lo,hi).forEach(o->{
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
		});
		displayLine = 0;
		System.out.println("2.) findSubStream(xid,*,*,?,String.class, String.class, "+lo+","+hi+");");	
		RelatrixTransaction.findSubStream(xid2,'*', '*', '?',String.class, String.class, lo, hi).forEach(o->{
			Result c = (Result)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			// samplesize is dictated by hi and low range
			ar.add(c);
		});
		displayLine = 0;
		String slo = key + String.format(uniqKeyFmt, lo);
		String shi = key  + String.format(uniqKeyFmt, hi);
		System.out.println("3.) findSubStream(xid,*,?,*,"+slo+","+shi+", String.class, Long.class);");		
		RelatrixTransaction.findSubStream(xid2,'*', '?', '*',slo,shi, String.class, Long.class).forEach(o->{
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			am.add(c);
		});
		displayLine = 0;
		System.out.println("4.) findSubStream(xid,?,*,*.String.class, String.class, "+lo+","+hi+");");			
		RelatrixTransaction.findSubStream(xid2,'?', '*', '*',String.class, String.class, lo, hi).forEach(o->{
			Result  c = (Result )o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ad.add(c);
		});
		displayLine=0;
		System.out.println("5.) findSubStream(xid,*,?,?,String.class, String.class, "+lo+","+hi+")...");		
		RelatrixTransaction.findSubStream(xid2,'*', '?', '?',String.class, String.class, lo, hi).forEach(o->{
			Result2 c = (Result2)o; // result2
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2.add(c);
		});
		displayLine = 0;
		System.out.println("6.) findSubStream(xid,?,*,?,"+slo+","+shi+",String.class, "+lo+","+hi+")...");		
		RelatrixTransaction.findSubStream(xid2,'?', '*', '?',slo,shi, String.class, lo,hi).forEach(o->{
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2dr.add(c);
		});
		displayLine = 0;
		System.out.println("7.) findSubStream(xid,?,?,*,"+slo+","+shi+", String.class, Long.class)...");		
		RelatrixTransaction.findSubStream(xid2,'?', '?', '*',slo,shi, String.class, Long.class).forEach(o->{
			Result2 c = (Result2)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar2dm.add(c);
		});
		displayLine = 0;
		System.out.println("8.) findSubStream(xid,?,?,?,"+slo+","+shi+", String.class, Long.class)...");		
		RelatrixTransaction.findSubStream(xid2,'?', '?', '?',slo,shi, String.class, Long.class).forEach(o->{
			Result3 c = (Result3)o;
			displayCtrl();
			if(DISPLAY || DISPLAYALL)
				System.out.println(displayLine+"="+c);
			ar3.add(c);
		});
		System.out.println("----------");
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the wildcard results.");
		for(int j = 0; j < ar3.size(); j++) {
			displayLine = 0;
			System.out.println("9."+j+") findSubStream(xid,<obj>,<obj>,<obj>) using ="+
					((Result)ar3.get(j)).get(0)+",("+((Result)ar3.get(j)).get(0).getClass().getName()+"),"+
					((Result)ar3.get(j)).get(1)+",("+((Result)ar3.get(j)).get(1).getClass().getName()+"),"+
					((Result)ar3.get(j)).get(2)+",("+((Result)ar3.get(j)).get(2).getClass().getName());
			RelatrixTransaction.findSubStream(xid2,((Result)ar3.get(j)).get(0), ((Result)ar3.get(j)).get(1), ((Result)ar3.get(j)).get(2)).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("10."+j+") findSubStream(xid,*,*,<obj>,String.class, String.class) using range="+((Result)ar3.get(j)).get(3));		
			RelatrixTransaction.findSubStream(xid2,'*', '*', ((Result)ar3.get(j)).get(3), String.class, String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("----------");
		System.out.println("Begin 1 instance match 2 wildcard testing");
		for(int j = 0; j < ar.size(); j++) {
			displayLine = 0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("11."+j+") findSubStream(xid,*,<obj>,*, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			RelatrixTransaction.findSubStream(xid2,'*', ((Result)am.get(j)).get(0), '*',String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("12."+j+") findSubStream(xid,<obj>,*,*,String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			RelatrixTransaction.findSubStream(xid2,((Result)ad.get(j)).get(0), '*', '*', String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("----------");
		System.out.println("Begin 2 instance match 1 wildcard testing");
		for(int j = 0; j < ar2.size(); j++) {
			// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
			displayLine = 0;
			System.out.println("13."+j+") findSubStream(xid,*,<obj>,<obj>,String.class) using map="+((Result)ar2.get(j)).toArray()[0]+" range="+((Result)ar2.get(j)).toArray()[1]);		
			RelatrixTransaction.findSubStream(xid2,'*', ((Result)ar2.get(j)).toArray()[0], ((Result)ar2.get(j)).toArray()[1], String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine = 0;
			System.out.println("14."+j+") findSubStream(xid,<obj>,*,<obj>,String.class) using ="+((Result)ar2dr.get(j)).toArray()[0]+", "+((Result)ar2dr.get(j)).toArray()[1]);		
			RelatrixTransaction.findSubStream(xid2,((Result)ar2dr.get(j)).toArray()[0], '*', ((Result)ar2dr.get(j)).toArray()[1], String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("15."+j+") findSubStream(xid,<obj>,<obj>,*, Long.class) using domain="+((Result)ar2dm.get(j)).toArray()[0]+", map="+((Result)ar2dm.get(j)).toArray()[1]);		
			RelatrixTransaction.findSubStream(xid2,((Result)ar2dm.get(j)).toArray()[0], ((Result)ar2dm.get(j)).toArray()[1], '*',Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("----------");
		System.out.println("Begin 1 instance match 2 element return testing");
		for(int j = 0; j < ar.size(); j++) {
			displayLine =0;
			System.out.println("16."+j+") findSubStream(xid,?,?,<obj>, String.class, String.class) using range="+((Result)ar.get(j)).get(0));		
			RelatrixTransaction.findSubStream(xid2,'?', '?', ((Result)ar.get(j)).get(0), String.class, String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("17."+j+") findSubStream(xid,?,<obj>,?, String.class, Long.class) using map="+((Result)am.get(j)).get(0));		
			RelatrixTransaction.findSubStream(xid2,'?', ((Result)am.get(j)).get(0), '?', String.class, Long.class).forEach(o->{
				Result2 c = (Result2)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine =0;
			System.out.println("18."+j+") findSubStream(xid,<obj>,?,?, String.class, Long.class) using domain="+((Result)ad.get(j)).get(0));		
			RelatrixTransaction.findSubStream(xid2,((Result)ad.get(j)).get(0), '?', '?', String.class, Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
		}
		System.out.println("----------");
		System.out.println("Begin 2 instance match 1 element return testing");
		for(int j = 0; j < ar2.size(); j++) {
			displayLine=0;
			System.out.println("19."+j+") findSubStream(xid,?,<obj>,<obj>, String.class) using map="+((Result)ar2.get(j)).get(0)+" range="+((Result)ar2.get(j)).get(1));		
			RelatrixTransaction.findSubStream(xid2,'?', ((Result)ar2.get(j)).get(0), ((Result)ar2.get(j)).get(1), String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			System.out.println("20."+j+") findSubStream(xid,<obj>,?,<obj>,String.class) using domain="+((Result)ar2dr.get(j)).get(0)+" range="+ ((Result)ar2dr.get(j)).get(1));		
			RelatrixTransaction.findSubStream(xid2,((Result)ar2dr.get(j)).get(0), '?', ((Result)ar2dr.get(j)).get(1), String.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			displayLine=0;
			System.out.println("21."+j+") findSubStream(xid,<obj>,<obj>,?,Long.class) using domain="+((Result)ar2dm.get(j)).get(0)+" map="+((Result)ar2dm.get(j)).get(1));		
			RelatrixTransaction.findSubStream(xid2,((Result)ar2dm.get(j)).get(0), ((Result)ar2dm.get(j)).get(1), '?',Long.class).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
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
			System.out.println("22."+j+") findSubStream(xid,*,*,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			RelatrixTransaction.findSubStream(xid2,'*','*','?',((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			lo+=increment;
			hi+=increment;
			System.out.println("23."+j+") findSubStream(xid,?,?,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			RelatrixTransaction.findSubStream(xid2,'?','?','?',((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi).forEach(o->{
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			});
			lo+=increment;
			hi+=increment;
			System.out.println("24."+j+") findSubStream(xid,?,*,?,<class>,<class>,<obj>,<obj>) using domain="+((Result)ar2dm.get(j)).get(0).getClass()+" map="+((Result)ar2dm.get(j)).get(1).getClass()+" range="+lo+" to "+hi);		
			RelatrixTransaction.findSubStream(xid2,'?','*','?',((Result)ar2dm.get(j)).get(0).getClass(), ((Result)ar2dm.get(j)).get(1).getClass(),lo,hi).forEach(o->{
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
	public static void battery1AR17(String[] argv, TransactionId xid) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println(xid+" CleanDB DMR size="+RelatrixTransaction.size(xid,Relation.class));
		System.out.println("CleanDB DRM size="+RelatrixTransaction.size(xid,DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+RelatrixTransaction.size(xid,MapDomainRange.class));
		System.out.println("CleanDB MDR size="+RelatrixTransaction.size(xid,MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+RelatrixTransaction.size(xid,RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+RelatrixTransaction.size(xid,RangeMapDomain.class));
		AbstractRelation.displayLevel = AbstractRelation.displayLevels.MINIMAL;
		Iterator<?> it = RelatrixTransaction.findSet(xid,'*','*','*');
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			Relation dmr = (Relation)((Result)fkey).get(0);
			try {
				RelatrixTransaction.remove(xid,dmr);
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
