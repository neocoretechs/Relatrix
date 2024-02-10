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
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RemoteTailSetIterator;
import com.neocoretechs.relatrix.key.DBKey;

/**
 * NOTES:
 * program arguments are _database
 * @author Jonathan Groff C 2021
 *
 */
public class EmbeddedRetrievalBattery {
	public static boolean DEBUG = false;
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
		battery1AR17(argv);
		battery00(argv);
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
		ArrayList<Comparable[]> ar2 = new ArrayList<Comparable[]>(); // will store 2 element result sets
		ArrayList<Comparable[]> ar3 = new ArrayList<Comparable[]>(); // will store 3 element result sets
		Iterator<?> it = null;
		System.out.println("Wildcard queries:");
		recs = 0;
		System.out.println("1.) Findset(*,*,*)...");
		it =  Relatrix.findSet("*", "*", "*");
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//ar.add(c[0]);
		}
		recs = 0;
		System.out.println("2.) Findset(*,*,?)...");		
		it = Relatrix.findSet("*", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 0 ) ar.add(c[0]);
		}
		recs = 0;
		System.out.println("3.) Findset(*,?,*)...");		
		it = Relatrix.findSet("*", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 1 ) ar.add(c[0]);
		}
		recs = 0;
		System.out.println("4.) Findset(?,*,*)...");		
		it = Relatrix.findSet("?", "*", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 2) ar.add(c[0]);
		}
		recs=0;
		System.out.println("5.) Findset(*,?,?)...");		
		it = Relatrix.findSet("*", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 0) ar2.add(c);
		}
		recs = 0;
		System.out.println("6.) Findset(?,*,?)...");		
		it = Relatrix.findSet("?", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 1) ar2.add(c);
		}
		recs = 0;
		System.out.println("7.) Findset(?,?,*)...");		
		it = Relatrix.findSet("?", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 2) ar2.add(c);
		}
		recs = 0;
		System.out.println("8.) Findset(?,?,?)...");		
		it = Relatrix.findSet("?", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
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
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//ar.add(c[0]);
		}
		recs =0;
		System.out.println("10.) Findset(*,*,<obj>) using range="+ar.get(0));		
		it = Relatrix.findSet("*", "*", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		recs = 0;
		System.out.println("11.) Findset(*,<obj>,*) using map="+ar.get(1));		
		it = Relatrix.findSet("*", ar.get(1), "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		recs =0;
		System.out.println("12.) Findset(<obj>,*,*) using domain="+ar.get(2));		
		it = Relatrix.findSet(ar.get(2), "*", "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		recs = 0;
		System.out.println("13.) Findset(*,<obj>,<obj>) using map="+ar2.get(0)[0]+" range="+ar2.get(0)[1]);		
		it = Relatrix.findSet("*", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 0) ar2.add(c);
		}
		recs = 0;
		System.out.println("14.) Findset(<obj>,*,<obj>) using domain="+ar2.get(1)[0]+" range="+ar2.get(1)[1]);		
		it = Relatrix.findSet(ar2.get(1)[0], "*", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 1) ar2.add(c);
		}
		recs =0;
		System.out.println("15.) Findset(<obj>,<obj>,*) using domain="+ar2.get(2)[0]+" map="+ar2.get(2)[1]);		
		it = Relatrix.findSet(ar2.get(2)[0], ar2.get(2)[1], "*");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 2) ar2.add(c);
		}
		recs =0;
		System.out.println("16.) Findset(?,?,<obj>) using range="+ar.get(0));		
		it = Relatrix.findSet("?", "?", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		recs =0;
		System.out.println("17.) Findset(?,<obj>,?) using map="+ar.get(1));		
		it = Relatrix.findSet("?", ar.get(1), "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		recs =0;
		System.out.println("18.) Findset(<obj>,?,?) using domain="+ar.get(2));		
		it = Relatrix.findSet(ar.get(2), "?", "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		recs =0;
		System.out.println("19.) Findset(?,<obj>,<obj>) using map="+ar2.get(0)[0]+" range="+ar2.get(0)[1]);		
		it = Relatrix.findSet("?", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 0) ar2.add(c);
		}
		recs =0;
		System.out.println("20.) Findset(<obj>,?,<obj>) using domain="+ar2.get(1)[0]+" range="+ ar2.get(1)[1]);		
		it = Relatrix.findSet(ar2.get(1)[0], "?", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 1) ar2.add(c);
		}
		recs =0;
		System.out.println("21.) Findset(<obj>,<obj>,?) using domain="+ar2.get(2)[0]+" map="+ar2.get(2)[1]);		
		it = Relatrix.findSet(ar2.get(2)[0], ar2.get(2)[1], "?");
		//ar = new ArrayList<Comparable>();
		while(it.hasNext()) {
			Object o = it.next();
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
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
		long s = Relatrix.size();
		System.out.println("CleanDB of "+s+" relationships");
		Iterator it = Relatrix.findSet("*","*","*");
		long timx = System.currentTimeMillis();
		for(int i = 0; i < s; i++) {
			Object fkey = it.next();
			DomainMapRange dmr = (DomainMapRange)((Comparable[])fkey)[0];
			Relatrix.remove(dmr.getDomain(), dmr.getMap(), dmr.getRange());
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println(i+" "+fkey);
				timx = System.currentTimeMillis();
			}
		}
		long siz = Relatrix.size();
		if(siz > 0) {
			Iterator<?> its = Relatrix.findSet("*","*","*");
			while(its.hasNext()) {
				Comparable[] nex = (Comparable[]) its.next();
				//System.out.println(i+"="+nex);
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			}
			System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(DomainMapRange.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(DomainMapRange.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("DomainMapRange:"+nex);
			}
			System.out.println("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainMapRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(DomainRangeMap.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(DomainRangeMap.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("DomainRangeMap:"+nex);
			}
			System.out.println("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DomainRangeMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(MapDomainRange.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(MapDomainRange.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("MapDomainRange:"+nex);
			}
			System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(MapRangeDomain.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(MapRangeDomain.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("MapRangeDomain:"+nex);
			}
			System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(RangeDomainMap.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(RangeDomainMap.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("RangeDomainMap:"+nex);
			}
			System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(RangeMapDomain.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(RangeMapDomain.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("RangeMapDomain:"+nex);
			}
			System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(DBKey.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(DBKey.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("DBKey:"+nex);
			}
			System.out.println("KV RANGE 1AR17 DBKEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 DBKEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(Long.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(Long.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("Long:"+nex);
			}
			System.out.println("KV RANGE 1AR17 Long MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 Long MISMATCH:"+siz+" > 0 after delete/commit");
		}
		siz = RelatrixKV.size(String.class);
		if(siz > 0) {
			it = RelatrixKV.entrySet(String.class);
			while(it.hasNext()) {
				Comparable nex = (Comparable) it.next();
				System.out.println("String:"+nex);
			}
			System.out.println("KV RANGE 1AR17 String MISMATCH:"+siz+" > 0 after all deleted and committed");
			throw new Exception("KV RANGE 1AR17 String MISMATCH:"+siz+" > 0 after delete/commit");
		}
		 System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}


}
