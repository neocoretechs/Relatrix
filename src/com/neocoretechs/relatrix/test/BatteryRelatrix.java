package com.neocoretechs.relatrix.test;

import java.util.Iterator;

import com.neocoretechs.bigsack.iterator.KeyValuePair;
import com.neocoretechs.bigsack.session.BigSackSession;
import com.neocoretechs.bigsack.session.BufferedTreeMap;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.bigsack.session.SessionManager;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.bigsack.test.AnalyzeBlock;
import com.neocoretechs.relatrix.BigSackAdapter;
import com.neocoretechs.relatrix.DMRStruc;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.typedlambda.TemplateClassReturn;
import com.neocoretechs.relatrix.typedlambda.TemplateClassWildcard;

/**
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the battery1 testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable 
 * @author jg
 *
 */
public class BatteryRelatrix {
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 2000;
	static int numDelete = 100; // for delete test
	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix session = new Relatrix();
		 //System.out.println("Analysis of all");
		BigSackAdapter.setTableSpaceDir(argv[0]);
		//battery1(session, argv);
		//battery1A(session, argv);
		 //battery1AA(session, argv);
		//battery1AR1(session, argv);
		//battery1AR2(session, argv);
		//battery1AR3(session, argv);
		//battery1AR4(session, argv);
		//battery1AR5(session, argv);
		//battery1AR6(session, argv);
		//battery1AR7(session, argv);
		//battery1AR8(session, argv);
		//battery1AR9(session, argv);
		//battery1AR10(session, argv);
		//battery1AR11(session, argv);
		//battery1AR12(session, argv);
		battery1X(session, argv);
		//battery1B(session, argv);
		//battery1C(session, argv);
		//battery1D(session, argv);
		//battery1D1(session, argv);
		//battery1E(session, argv);
		//battery1E1(session, argv);
		//battery1F(session, argv);
		//battery1F1(session, argv);
		//battery1G(session, argv);
		//battery2(session, argv);
		//battery3(session, argv);
		//battery4(session, argv);
		//battery5(session, argv);
		
		 System.out.println("TEST BATTERY COMPLETE.");
		
	}
	/**
	 * Loads up on key/value pairs
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		String fkey = null;
		BufferedTreeMap btm = BigSackAdapter.getBigSackMap(String.class);
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			btm.put(fkey, val+String.format(uniqKeyFmt, i));
			Relatrix.store(fkey, "Has time", new Long(System.currentTimeMillis()));
		}
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1X(Relatrix session, String argv[]) throws Exception {
		//RangeDomainMap tkey = new RangeDomainMap();
		DomainMapRange tkey = new DomainMapRange();
		TransactionalTreeSet btm = BigSackAdapter.getBigSackSetTransaction(DomainMapRange.class);
		Iterator it = btm.tailSet(tkey);
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	/**
	 * Loads up on key/value pairs
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AA(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		String fkey = null;
		BufferedTreeMap btm = BigSackAdapter.getBigSackMap(String.class);
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			btm.put(fkey, val+String.format(uniqKeyFmt, i));
			Relatrix.store(fkey, "Has time", String.valueOf((System.currentTimeMillis())));
		}
		 System.out.println("BATTERY1AA SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Does a simple 'get' of the elements inserted before
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1A(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		DomainMapRange fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(DomainMapRange.class);
		System.out.println(btm.getDBName());
		DomainMapRange fdmr = (DomainMapRange) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1A:"+it.next());
		}
		/*
		for(int i = min; i < max; i++) {
			Object o = session.get(key + String.format(uniqKeyFmt, i));
			if( !(val+String.format(uniqKeyFmt, i)).equals(o) ) {
				 System.out.println("BATTERY1A FAIL "+o);
				throw new Exception("B1A Fail on get with "+o);
			}
		}
		*/
		 System.out.println("BATTERY1A SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR1(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		DomainRangeMap fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(DomainRangeMap.class);
		System.out.println(btm.getDBName());
		DomainRangeMap fdmr = (DomainRangeMap) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1AR1:"+it.next());
		}
		/*
		for(int i = min; i < max; i++) {
			Object o = session.get(key + String.format(uniqKeyFmt, i));
			if( !(val+String.format(uniqKeyFmt, i)).equals(o) ) {
				 System.out.println("BATTERY1A FAIL "+o);
				throw new Exception("B1A Fail on get with "+o);
			}
		}
		*/
		 System.out.println("BATTERY1AR1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR2(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		MapDomainRange fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(MapDomainRange.class);
		System.out.println(btm.getDBName());
		MapDomainRange fdmr = (MapDomainRange) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1AR2:"+it.next());
		}
		/*
		for(int i = min; i < max; i++) {
			Object o = session.get(key + String.format(uniqKeyFmt, i));
			if( !(val+String.format(uniqKeyFmt, i)).equals(o) ) {
				 System.out.println("BATTERY1A FAIL "+o);
				throw new Exception("B1A Fail on get with "+o);
			}
		}
		*/
		 System.out.println("BATTERY1AR@ SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR3(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		MapRangeDomain fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(MapRangeDomain.class);
		System.out.println(btm.getDBName());
		MapRangeDomain fdmr = (MapRangeDomain) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1AR3:"+it.next());
		}
		/*
		for(int i = min; i < max; i++) {
			Object o = session.get(key + String.format(uniqKeyFmt, i));
			if( !(val+String.format(uniqKeyFmt, i)).equals(o) ) {
				 System.out.println("BATTERY1A FAIL "+o);
				throw new Exception("B1A Fail on get with "+o);
			}
		}
		*/
		 System.out.println("BATTERY1AR3 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR4(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		RangeDomainMap fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(RangeDomainMap.class);
		System.out.println(btm.getDBName());
		RangeDomainMap fdmr = (RangeDomainMap) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1AR4:"+it.next());
		}
		/*
		for(int i = min; i < max; i++) {
			Object o = session.get(key + String.format(uniqKeyFmt, i));
			if( !(val+String.format(uniqKeyFmt, i)).equals(o) ) {
				 System.out.println("BATTERY1A FAIL "+o);
				throw new Exception("B1A Fail on get with "+o);
			}
		}
		*/
		 System.out.println("BATTERY1AR4 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR5(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		RangeMapDomain fkey = null;
		BufferedTreeSet btm = BigSackAdapter.getBigSackSet(RangeMapDomain.class);
		System.out.println(btm.getDBName());
		RangeMapDomain fdmr = (RangeMapDomain) btm.first();
		Iterator<?> it = btm.tailSet(fdmr);
		while(it.hasNext()) {
			System.out.println("1AR5:"+it.next());
		}
		 System.out.println("BATTERY1AR5 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR6(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "?", "?");

		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR6:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR7(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "*", "*");

		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR7:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR8(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("?", "?", "*");

		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR8:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery1AR9(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet("*", "*", "*");
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR9:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * store and verify relations
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, max);
		DMRStruc d = Relatrix.store(fkey, "Has time", new Long(tims));
		System.out.println("1AR10:"+d);
		Iterator<?> its = Relatrix.findSet(fkey, "Has time", new Long(tims));
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR10:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	public static void battery1AR11(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		// forgetful functor test
		Iterator<?> its = Relatrix.findSet(fkey, "Has time", new TemplateClassWildcard(Long.class));
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR11:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * negative assertion of above
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		// forgetful functor test
		Iterator<?> its = Relatrix.findSet(fkey, "Has time", new TemplateClassWildcard(Integer.class));
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR12:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of TemplateClassReturn
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR13(Relatrix session, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		// forgetful functor test
		Iterator<?> its = Relatrix.findSet(fkey, "Has time", new TemplateClassReturn(Long.class));
		while(its.hasNext()) {
			Comparable[] nex = (Comparable[]) its.next();
			for(int i = 0; i < nex.length; i++)
				System.out.println("1AR13:"+i+" "+nex[i]);
		}
		 System.out.println("BATTERY1AR13 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Check the string table we made in battery1
	 * @param rel
	 * @param argv
	 * @throws Exception
	 */
	public static void battery2(Relatrix rel, String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		BufferedTreeMap bs = BigSackAdapter.getBigSackMap(String.class);
		Iterator it = bs.tailMapKV("");
		while(it.hasNext()) {
			System.out.println("B2:"+it.next());
		}
		 System.out.println("BATTERY2 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	public static void battery4(Relatrix rel, String[] argv) throws Exception {
		 argv = new String[3];
		 argv[0] = "/C:/users/jg/Relatrix/com.neocoretechs.relatrix.MapRangeDomain";
		 argv[1] = "4";
		 argv[2] = "2421760";
		AnalyzeBlock.main(argv);
	}
}
