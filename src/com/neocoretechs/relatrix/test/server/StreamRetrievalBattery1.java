package com.neocoretechs.relatrix.test.server;

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
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.client.RelatrixClient;

/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findSubStream retrieval using the client to a remote {@link com.neocoretechs.relatrix.server.RelatrixServer}.
 * NOTES:
 * program arguments are local_node remote_node remote_port_for_database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class StreamRetrievalBattery1 {
	public static boolean DEBUG = false;
	private static RelatrixClient rkvc ;
		public static int displayLinesOn[]= {0,1000,5000,9990,15000,20000,30000,40000,50000,60000,70000,80000,90000,99000};
		public static int displayLinesOff[]= {100,1100,5100,9999,15999,20999,30999,40999,50999,60999,70999,80999,90999,100000};
		public static int displayLine = 0;
		public static int displayLineCtr = 0;
		public static long displayTimer = 0;
		public static int min = 0;
		public static int max = 100000;
		static String key = "This is a test"; 
		static String uniqKeyFmt = "%0100d";
		private static boolean DISPLAY = false;;
		/**
		*/
		public static void main(String[] argv) throws Exception {
			 //System.out.println("Analysis of all");
			if(argv.length < 3) {
				System.out.println("Usage: <bootNode> <remoteNode> <remotePort> [init]");
			}
			rkvc = new RelatrixClient(argv[0], argv[1], Integer.parseInt(argv[2]) );
			Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
			if(argv.length == 4 && argv[3].equals("init")) {
					battery1AR17(argv);
			}
			if(rkvc.size() == 0) {
				battery0(argv);
			}
			battery1(argv);
			System.out.println("StreamRetrievalBattery1 COMPLETE.");	
			System.exit(1);
		}
		
		public static void displayCtrl() {
			if(displayLine == 0)
				displayLineCtr = 0;
			if(displayLine >= displayLinesOn[displayLineCtr] && displayLine <= displayLinesOff[displayLineCtr]) {
				if(!DISPLAY)
					displayTimer = System.currentTimeMillis();
				DISPLAY  = true;
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
			int recs = 0;
			String fkey = null;
			DomainMapRange dmr = null;
			for(int i = min; i < max; i++) {
				fkey = key + String.format(uniqKeyFmt, i);
				dmr = rkvc.store(fkey, "Has unit", new Long(i));
				++recs;
			}
			 System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records");
		}

		/**
		 * @param argv
		 * @throws Exception
		 */
		public static void battery1(String[] argv) throws Exception {
			System.out.println("Stream Retrieval Battery1 ");
			String fmap;
			long tims = System.currentTimeMillis();
			int recs = 0;
			// this list will store an object used to test subsequent queries where a named object is needed
			// it will be extracted from the wildcard queries
			ArrayList<Result> ar = new ArrayList<Result>();
			ArrayList<Result> ar2 = new ArrayList<Result>(); // will store 2 element result sets
			ArrayList<Result> ar3 = new ArrayList<Result>(); // will store 3 element result sets

			System.out.println("Wildcard queries:");
			displayLine = 0;
			System.out.println("1.) findSubStream(*,*,*,String.class, String.class, Long.class)...");
			rkvc.findSubStream('*', '*', '*',String.class, String.class, Long.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine = 0;
			System.out.println("2.) findSubStream(*,*,?,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('*', '*', '?',String.class, String.class, Long.class).forEach(e-> {
			//ar = new ArrayList<Comparable>();
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar.size() == 0 )
					ar.add((Result) e);
			});
			displayLine = 0;
			System.out.println("3.) findSubStream(*,?,*,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('*', '?', '*',String.class, String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar.size() == 1 ) {
					ar.add((Result) e);
				}
			});
			displayLine = 0;
			System.out.println("4.) findSubStream(?,*,*.String.class, String.class, Long.class)...");		
			rkvc.findSubStream('?', '*', '*',String.class, String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar.size() == 2) {
					ar.add((Result) e);
				}
			});
			displayLine=0;
			System.out.println("5.) findSubStream(*,?,?,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('*', '?', '?',String.class, String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar2.size() == 0) {
					ar2.add((Result) e);
				}
			});
			displayLine = 0;
			System.out.println("6.) findSubStream(?,*,?,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('?', '*', '?',String.class, String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar2.size() == 1) {
					ar2.add((Result) e);
				}
			});
			displayLine = 0;
			System.out.println("7.) findSubStream(?,?,*,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('?', '?', '*',String.class, String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar2.size() == 2) {
					ar2.add((Result) e);
				}
			});
			displayLine = 0;
			System.out.println("8.) FindSubStream(?,?,?,String.class, String.class, Long.class)...");		
			rkvc.findSubStream('?', '?', '?',String.class, String.class, Long.class).forEach(e -> {;
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
				if(ar3.size() == 0) {
					ar3.add((Result) e);
				}
			});
			
			System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the");
			System.out.println("wildcard results. They should produce relationships with these elements");
			displayLine = 0;
			System.out.println("9.) findSubSet(<obj>,<obj>,<obj>) using ="+
			((Result)ar3.get(0)).get(0)+",("+((Result)ar3.get(0)).get(0).getClass().getName()+"),"+
			((Result)ar3.get(0)).get(1)+",("+((Result)ar3.get(0)).get(1).getClass().getName()+"),"+
			((Result)ar3.get(0)).get(2)+",("+((Result)ar3.get(0)).get(2).getClass().getName());
			rkvc.findSubStream(((Result)ar3.get(0)).get(0), ((Result)ar3.get(0)).get(1), ((Result)ar3.get(0)).get(2)).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine=0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("10.) findSubStream(*,*,<obj>,String.class, String.class) using range="+((Result)ar3.get(0)).get(3));		
			rkvc.findSubStream('*', '*', ((Result)ar3.get(0)).get(3), String.class, String.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine = 0;
			//RelatrixHeadsetIterator.DEBUG = true;
			System.out.println("11.) findSubStream(*,<obj>,*, String.class, Long.class) using map="+((Result)ar.get(1)).get(0));		
			rkvc.findSubStream('*', ((Result)ar.get(1)).get(0), '*',String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("12.) FindSubStream(<obj>,*,*,String.class, Long.class) using domain="+((Result)ar.get(2)).get(0));		
			rkvc.findSubStream(((Result)ar.get(2)).get(0), '*', '*', String.class, Long.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
			displayLine = 0;
			System.out.println("13.) findSubStream(*,<obj>,<obj>,String.class) using map="+((Result)ar2.get(0)).toArray()[0]+" range="+((Result)ar2.get(0)).toArray()[1]);		
			rkvc.findSubStream('*', ((Result)ar2.get(0)).toArray()[0], ((Result)ar2.get(0)).toArray()[1], String.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine = 0;
			System.out.println("14.) findSubStream(<obj>,*,<obj>,String.class) using ="+((Result)ar2.get(1)).toArray()[0]+", "+((Result)ar2.get(1)).toArray()[1]);		
			rkvc.findSubStream(((Result)ar2.get(1)).toArray()[0], '*', ((Result)ar2.get(1)).toArray()[1], String.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("15.) findSubStream(<obj>,<obj>,*, Long.class) using domain="+((Result)ar2.get(2)).toArray()[0]+", map="+((Result)ar2.get(2)).toArray()[1]);		
			rkvc.findSubStream(((Result)ar2.get(2)).toArray()[0], ((Result)ar2.get(2)).toArray()[1], '*',Long.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("16.) findSubStream(?,?,<obj>, String.class, String.class) using range="+((Result)ar.get(0)).get(0));		
			rkvc.findSubStream('?', '?', ((Result)ar.get(0)).get(0), String.class, String.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("17.) findSubStream(?,<obj>,?, String.class, Long.class) using map="+((Result)ar.get(1)).get(0));		
			rkvc.findSubStream('?', ((Result)ar.get(1)).get(0), '?', String.class, Long.class).forEach(e->{
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("18.) findSubStream(<obj>,?,?, String.class, Long.class) using domain="+((Result)ar.get(2)).get(0));		
			rkvc.findSubStream(((Result)ar.get(2)).get(0), '?', '?', String.class, Long.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("19.) findSubStream(?,<obj>,<obj>, String.class) using map="+((Result)ar2.get(0)).get(0)+" range="+((Result)ar2.get(0)).get(1));		
			rkvc.findSubSet('?', ((Result)ar2.get(0)).get(0), ((Result)ar2.get(0)).get(1), String.class).forEachRemaining(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("20.) findSubStream(<obj>,?,<obj>,String.class) using domain="+((Result)ar2.get(1)).get(0)+" range="+ ((Result)ar2.get(1)).get(1));		
			rkvc.findSubStream(((Result)ar2.get(1)).get(0), '?', ((Result)ar2.get(1)).get(1), String.class).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			displayLine =0;
			System.out.println("21.) findSubStream(<obj>,<obj>,?,Long.class) using domain="+((Result)ar2.get(2)).get(0)+" map="+((Result)ar2.get(2)).get(1));		
			rkvc.findSubStream(((Result)ar2.get(2)).get(0), ((Result)ar2.get(2)).get(1), '?',Long.class).forEach(e-> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			Long lo = (max/2000L);
			Long hi = (max/1000L);
			displayLine =0;
			System.out.println("22.) findSubStream(*,*,?,String.class, String.class,<obj>,<obj>) using range="+lo+" to "+hi);		
			rkvc.findSubStream('*','*','?',String.class, String.class, lo,hi).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			lo+=1000L;
			hi+=1000L;
			System.out.println("23.) findSubStream(*,*,?,String.class,String.class,<obj>,obj>) using range="+lo+" to "+hi);		
			rkvc.findSubStream('*','*','?',String.class, String.class, lo,hi).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			lo+=1000L;
			hi+=1000L;
			System.out.println("24.) findSubStream(*,*,?,<obj>,<obj>) using range="+lo+" to "+hi);		
			rkvc.findSubStream('*','*','?',String.class, String.class, lo,hi).forEach(e -> {
				displayCtrl();
				if(DISPLAY)
					System.out.println(displayLine+"="+e);
			});
			System.out.println(" SUCCESS in "+(System.currentTimeMillis()-tims));
		}
		/**
		 * remove entries
		 * @param argv
		 * @throws Exception
		 */
		public static void battery1AR17(String[] argv) throws Exception {
			long tims = System.currentTimeMillis();
			System.out.println("CleanDB");
			Iterator it = rkvc.findSet('*','*','*');
			long timx = System.currentTimeMillis();
			int i = 0;
			while(it.hasNext()) {
				Object fkey = it.next();
				DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
				rkvc.remove(dmr.getDomain(), dmr.getMap());
				++i;
				if((System.currentTimeMillis()-timx) > 1000) {
					System.out.println("deleting "+i+" "+fkey);
					timx = System.currentTimeMillis();
				}
			}
			Iterator<?> its = rkvc.findSet('*','*','*');
			while(its.hasNext()) {
				Result nex = (Result) its.next();
				//System.out.println(i+"="+nex);
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
			long siz = rkvc.size();
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
			}
			it = rkvc.entrySet(DomainMapRange.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("DomainMapRange:"+nex);
			}
			siz = rkvc.size();
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after delete/commit");
			}
			it = rkvc.entrySet(DomainRangeMap.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("DomainRangeMap:"+nex);
			}
			siz = rkvc.size();
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after delete/commit");
			}

			it = rkvc.entrySet(MapDomainRange.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("MapDomainRange:"+nex);
			}
			siz = rkvc.size(MapDomainRange.class);
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
			}

			it = rkvc.entrySet(MapRangeDomain.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("MapRangeDomain:"+nex);
			}
			siz = rkvc.size(MapRangeDomain.class);
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
			}
			it = rkvc.entrySet(RangeDomainMap.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("RangeDomainMap:"+nex);
			}
			siz = rkvc.size(RangeDomainMap.class);
			if(siz > 0) {
				System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
				throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
			}
			it = rkvc.entrySet(RangeMapDomain.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("RangeMapDomain:"+nex);
			}
			siz = rkvc.size(RangeMapDomain.class);
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
