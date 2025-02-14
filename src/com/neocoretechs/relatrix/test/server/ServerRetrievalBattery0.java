package com.neocoretechs.relatrix.test.server;

import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
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
 * and verifying findSet retrieval using the client to a remote {@link com.neocoretechs.relatrix.server.RelatrixServer}.
 * NOTES:
 * program arguments are local_node remote_node remote_port_for_database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class ServerRetrievalBattery0 {
	public static boolean DEBUG = false;
	private static RelatrixClient rkvc ;
		public static int displayLinesOn[]= {0,1000,5000,9990,15000,20000,30000,40000,50000,60000,70000,80000,90000,99000};
		public static int displayLinesOff[]= {100,1100,5100,9999,15999,20999,30999,40999,50999,60999,70999,80999,90999,100000};
		public static int displayLine = 0;
		public static int displayLineCtr = 0;
		public static long displayTimer = 0;
		public static int min = 0;
		public static int max = 100;
		static String key = "This is a test"; 
		static String uniqKeyFmt = "%0100d";
		private static boolean DISPLAY = false;
		private static boolean DISPLAYALL = true;
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
			System.out.println("TEST BATTERY COMPLETE.");	
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
			System.out.println("Iterator Battery1 ");
			String fmap;
			long tims = System.currentTimeMillis();
			int recs = 0;
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
			System.out.println("1.) findSet(*,*,*)...");
			it =  rkvc.findSet('*', '*', '*');
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
			}
			displayLine = 0;
			System.out.println("2.) findSet(*,*,?)...");		
			it = rkvc.findSet('*', '*', '?');
			while(it.hasNext()) {
				Object o = it.next();
				Result c = (Result)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				ar.add(c);
			}
			displayLine = 0;
			System.out.println("3.) findSet(*,?,*)...");		
			it = rkvc.findSet('*', '?', '*');
			while(it.hasNext()) {
				Object o = it.next();
				Result  c = (Result )o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				am.add(c);
			}
			displayLine = 0;
			System.out.println("4.) findSet(?,*,*)...");		
			it = rkvc.findSet('?', '*', '*');
			while(it.hasNext()) {
				Object o = it.next();
				Result  c = (Result )o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				ad.add(c);
			}
			displayLine=0;
			System.out.println("5.) findSet(*,?,?)...");		
			it = rkvc.findSet('*', '?', '?');
			while(it.hasNext()) {
				Object o = it.next();
				Result2 c = (Result2)o; // result2
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				ar2.add(c);
			}
			displayLine = 0;
			System.out.println("6.) findSet(?,*,?)...");		
			it = rkvc.findSet('?', '*', '?');
			while(it.hasNext()) {
				Object o = it.next();
				Result2 c = (Result2)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				ar2dr.add(c);
			}
			displayLine = 0;
			System.out.println("7.) findSet(?,?,*)...");		
			it = rkvc.findSet('?', '?', '*');
			while(it.hasNext()) {
				Object o = it.next();
				Result2 c = (Result2)o;
				displayCtrl();
				if(DISPLAY || DISPLAYALL)
					System.out.println(displayLine+"="+c);
				ar2dm.add(c);
			}
			displayLine = 0;
			System.out.println("8.) FindSet(?,?,?)...");		
			it = rkvc.findSet('?', '?', '?');
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
				System.out.println("9."+j+") findSet(<obj>,<obj>,<obj>) using ="+
						((Result)ar3.get(j)).get(0)+",("+((Result)ar3.get(j)).get(0).getClass().getName()+"),"+
						((Result)ar3.get(j)).get(1)+",("+((Result)ar3.get(j)).get(1).getClass().getName()+"),"+
						((Result)ar3.get(j)).get(2)+",("+((Result)ar3.get(j)).get(2).getClass().getName());
				it = rkvc.findSet(((Result)ar3.get(j)).get(0), ((Result)ar3.get(j)).get(1), ((Result)ar3.get(j)).get(2));
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar.size(); j++) {
				displayLine=0;
				//RelatrixHeadsetIterator.DEBUG = true;
				System.out.println("10."+j+") findSet(*,*,<obj>) using range="+((Result)ar.get(j)).get());		
				it = rkvc.findSet('*', '*', ((Result)ar.get(j)).get());
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < am.size(); j++) {
				displayLine = 0;
				//RelatrixHeadsetIterator.DEBUG = true;
				System.out.println("11."+j+") findSet(*,<obj>,*) using map="+((Result)am.get(j)).get(0));		
				it = rkvc.findSet('*', ((Result)am.get(j)).get(0), '*');
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ad.size(); j++) {
				displayLine =0;
				System.out.println("12."+j+") FindSet(<obj>,*,*) using domain="+((Result)ad.get(j)).get(0));		
				it = rkvc.findSet(((Result)ad.get(j)).get(0), '*', '*');
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			// From a Result2 we can call get(0) and get(1), like an array, we can also call toArray
			for(int j = 0; j < ar2.size(); j++) {
				displayLine = 0;
				System.out.println("13."+j+") findSet(*,<obj>,<obj>) using map="+((Result)ar2.get(j)).toArray()[0]+" range="+((Result)ar2.get(j)).toArray()[1]);		
				it = rkvc.findSet('*', ((Result)ar2.get(j)).toArray()[0], ((Result)ar2.get(j)).toArray()[1]);
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar2dr.size(); j++) {
				displayLine = 0;
				System.out.println("14."+j+") findSet(<obj>,*,<obj>) using ="+((Result)ar2dr.get(j)).toArray()[0]+", "+((Result)ar2dr.get(j)).toArray()[1]);		
				it = rkvc.findSet(((Result)ar2dr.get(j)).toArray()[0], '*', ((Result)ar2dr.get(j)).toArray()[1]);
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar2dm.size(); j++) {
				displayLine=0;
				System.out.println("15."+j+") findSet(<obj>,<obj>,*) using domain="+((Result)ar2dm.get(j)).toArray()[0]+", map="+((Result)ar2dm.get(j)).toArray()[1]);		
				it = rkvc.findSet(((Result)ar2dm.get(j)).toArray()[0], ((Result)ar2dm.get(j)).toArray()[1], '*');
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar.size(); j++) {
				displayLine=0;
				System.out.println("16."+j+") findSet(?,?,<obj>) using range="+((Result)ar.get(j)).get(0));		
				it = rkvc.findSet('?', '?', ((Result)ar.get(j)).get(0));
				//ar = new ArrayList<Comparable>();
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
					//if(ar.size() == 0 ) ar.add(c[0]);
				}
			}
			for(int j = 0; j < am.size(); j++) {
				displayLine=0;
				System.out.println("17."+j+") findSet(?,<obj>,?) using map="+((Result)am.get(j)).get(0));		
				it = rkvc.findSet('?', ((Result)am.get(j)).get(0), '?');
				while(it.hasNext()) {
					Object o = it.next();
					Result2 c = (Result2)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ad.size(); j++) {
				displayLine=0;
				System.out.println("18."+j+") findSet(<obj>,?,?) using domain="+((Result)ad.get(j)).get(0));		
				it = rkvc.findSet(((Result)ad.get(j)).get(0), '?', '?');
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar2.size(); j++) {
				displayLine=0;
				System.out.println("19."+j+") findSet(?,<obj>,<obj>) using map="+((Result)ar2.get(j)).get(0)+" range="+((Result)ar2.get(j)).get(1));		
				it = rkvc.findSet('?', ((Result)ar2.get(j)).get(0), ((Result)ar2.get(j)).get(1));
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
					//if(ar2.size() == 0) ar2.add(c);
				}
			}
			for(int j = 0; j < ar2dr.size(); j++) {
				displayLine =0;
				System.out.println("20."+j+") findSet(<obj>,?,<obj>) using domain="+((Result)ar2dr.get(j)).get(0)+" range="+ ((Result)ar2dr.get(j)).get(1));		
				it = rkvc.findSet(((Result)ar2dr.get(j)).get(0), '?', ((Result)ar2dr.get(j)).get(1));
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			for(int j = 0; j < ar2dm.size(); j++) {
				displayLine =0;
				System.out.println("21."+j+") findSet(<obj>,<obj>,?) using domain="+((Result)ar2dm.get(j)).get(0)+" map="+((Result)ar2dm.get(j)).get(1));		
				it = rkvc.findSet(((Result)ar2dm.get(j)).get(0), ((Result)ar2dm.get(j)).get(1), '?');
				while(it.hasNext()) {
					Object o = it.next();
					Result c = (Result)o;
					displayCtrl();
					if(DISPLAY || DISPLAYALL)
						System.out.println(displayLine+"="+c);
				}
			}
			System.out.println("ServerRetrievalBattery0 SUCCESS in "+(System.currentTimeMillis()-tims));
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
			System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
		}
	
}
