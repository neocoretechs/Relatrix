package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Morphism.displayLevels;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Result;

import com.neocoretechs.rocksack.Alias;

/**
 * The set of tests verifies the higher level 'findSet' functions in the {@link Relatrix} in conjunction with database alias.
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is tablespace i.e. C:/users/you/Relatrix/ which will create databases in C:/users/you/Relatrix/ALIAS1, 2, 3..
 * @author Jonathan Groff Copyright (C) NoeCoreTechs 2016,2017,2024
 *
 */
public class BatteryRelatrixAlias {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 100000;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	static Alias alias1 = new Alias("ALIAS1");
	static Alias alias2 = new Alias("ALIAS2");
	static Alias alias3 = new Alias("ALIAS3");
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		String tablespace = argv[0];
		if(!tablespace.endsWith("/"))
			tablespace += "/";
		Relatrix.setAlias(alias1,tablespace+alias1);
		Relatrix.setAlias(alias2,tablespace+alias2);
		Relatrix.setAlias(alias3,tablespace+alias3);
		Morphism.displayLevel = displayLevels.VERBOSE;
		if(argv.length > 1 && argv[2].equals("init")) {
			battery1AR17(argv, alias1);
			battery1AR17(argv, alias2);
			battery1AR17(argv, alias3);
		}
		if(Relatrix.size() == 0) {
			if(DEBUG)
				System.out.println("Begin test battery 1");
			battery1(argv, alias1);
			battery1(argv, alias2);
			battery1(argv, alias3);
			if(DEBUG)
				System.out.println("Begin test battery 11");
			battery11(argv, alias1);
			battery11(argv, alias2);
			battery11(argv, alias3);
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6");
		battery1AR6(argv, alias1);
		battery1AR6(argv, alias2);
		battery1AR6(argv, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR7");
		battery1AR7(argv,alias1);
		battery1AR7(argv,alias2);
		battery1AR7(argv,alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR8");
		battery1AR8(argv, alias1);
		battery1AR8(argv, alias2);
		battery1AR8(argv, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR9");
		battery1AR9(argv, alias1);
		battery1AR9(argv, alias2);
		battery1AR9(argv, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR10");
		battery1AR10(argv, alias1);
		battery1AR10(argv, alias2);
		battery1AR10(argv, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR101");
		battery1AR101(argv, alias1);
		battery1AR101(argv, alias2);
		battery1AR101(argv, alias3);
		if(DEBUG)
			System.out.println("Begin test battery 1AR11");
		battery1AR11(argv, alias1);
		battery1AR11(argv, alias2);
		battery1AR11(argv, alias3);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys in the designated alias
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1(String[] argv, Alias alias12) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(i));
				++recs;
				if((System.currentTimeMillis()-tims) > 1000) {
					System.out.println("storing "+recs+" "+fkey);
					tims = System.currentTimeMillis();
				}
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	/**
	 * Tries to store partial key in alias that should match existing keys, should reject all.
	 * Domain/map determines unique key
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery11(String[] argv, Alias alias12) throws Exception {
		System.out.println(alias12+" Battery11 ");
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				DomainMapRange dmr = Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(99999));
				++recs;
				System.out.println("SHOULD NOT BE storing "+recs+" "+fkey+" dmr:"+dmr);
				//if((System.currentTimeMillis()-tims) > 1000) {
				//	System.out.println("storing "+recs+" "+fkey);
				//	tims = System.currentTimeMillis();
				//}
			} catch(DuplicateKeyException d) {++dupes;}
		}
		if( recs > 0) {
			throw new DuplicateKeyException("BATTERY11 FAIL, stored "+recs+" when zero should have been stored");
		} else {
			System.out.println("BATTERY11 SUCCESS in "+(System.currentTimeMillis()-timt)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
		}
	}
	

	/**
	 * Test the higher level functions in the Relatrix. Use the 'findSet' alias permutations to
	 * verify the previously inserted data
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv, Alias alias12) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet(alias12, "?", "?", "?");
		System.out.println(alias12+" Battery1AR6 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// 3 question marks = dimension 3 in return array
				if( DEBUG ) 
					System.out.println("1AR6:"+i+" "+nex);
				//String skey = key + String.format(uniqKeyFmt, i);
				//if(!skey.equals(nex[0]) )
					//System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
				if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit "+alias12) || nex.length() != 3) {
					System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
					throw new Exception("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
				}
				//Long unit = new Long(i);
				//if(!nex[2].equals(unit))
					//System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[2]);
				++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = Relatrix.findSet(alias, "?", "*", "*");
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv, Alias alias12) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet(alias12, "?", "*", "*");
		System.out.println(alias12+" Battery1AR7 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1AR7:"+i+" "+nex);
			if(!((String) nex.get(0)).startsWith(key) || nex.length() != 1) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
			}
			//String skey = key + String.format(uniqKeyFmt, i);
			//if(!skey.equals(nex[0]) )
				//System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Iterator<?> its = Relatrix.findSet(alias, "?", "?", "*");
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv, Alias alias12) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet(alias12, "?", "?", "*");
		System.out.println(alias12+" Battery1AR8 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// two '?' in findset gives use 2 element array, the domain and map
			if( DEBUG ) System.out.println("1AR8:"+i+" "+nex);
			//String skey = key + String.format(uniqKeyFmt, i);
			//if(!skey.equals(nex[0]) )
				//System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex[0]);
			if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit "+alias12) || nex.length() != 2) {
				System.out.println("KEY MISMATCH:"+(i)+" "+nex.get(0)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
				throw new Exception("KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(1)+" length:"+nex.length());
			}
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR8 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR8 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of Iterator<?> its = Relatrix.findSet(alias, "*", "*", "*");
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv, Alias alias12) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSet(alias12, "*", "*", "*");
		System.out.println(alias12+" Batter1AR9 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// the returned array has 1 element, the identity Morphism DomainMapRange
			if( DEBUG ) System.out.println("1AR9:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
				throw new Exception("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
			//Long unit = new Long(i);
			//if(!((DomainMapRange)nex[0]).getRange().equals(unit))
				//System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != max ) {
			System.out.println("BATTERY1AR9 unexpected number of keys "+i);
			//throw new Exception("BATTERY1AR9 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Iterator<?> its = Relatrix.findSet(alias, fkey, "Has unit", "*");
	 * Should return 1 element of which 'fkey' and "Has unit" are primary key
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv, Alias alias12) throws Exception {
		int i = min;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, min);
		Iterator<?> its = Relatrix.findSet(alias12, fkey, "Has unit "+alias12, "*");
		System.out.println(alias12+" Battery1AR10 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		// return all identities with the given key for all ranges, should be 1
		while(its.hasNext()) {
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result) its.next();
			if( nex.length() != 1)
				throw new Exception("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR10:"+i+" "+nex.get(0));
			String skey = key + String.format(uniqKeyFmt, i);
			if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(skey) )
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
				throw new Exception("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
			//Long unit = new Long(i);
			//if(!((DomainMapRange)nex[0]).getRange().equals(unit))
			//	System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != 1 ) {
			System.out.println("BATTERY1AR10 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR10 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Iterator<?> its = Relatrix.findSet(fkey, "Has unit", new Long(max));
	 * Range value is max, so zero keys should be retrieved since we insert 0 to max-1
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv, Alias alias12) throws Exception {
		int i = 0;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, max);
		// Range value is max, so zero keys should be retrieved since we insert 0 to max-1
		Iterator<?> its = Relatrix.findSet(alias12, fkey, "Has unit "+alias12, new Long(max));
		System.out.println(alias12+" Battery1AR101 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result) its.next();
			if( nex.length() != 1)
				throw new Exception("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR101:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			if(!( (String)((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" "+key+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit "+alias12))
				throw new Exception("MAP KEY MISMATCH:"+(i)+" Has unit "+alias12+" - "+nex.get(0));
			//Long unit = new Long(i);
			//if(!((DomainMapRange)nex[0]).getRange().equals(unit))
				//System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		}
		if( i != 0 ) {
			System.out.println("BATTERY1AR101 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR101 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * negative assertion of above
	 * Iterator<?> its = Relatrix.findSet(fkey, "Has time", "*");
	 * map is 'Has time', which we never inserted, so no elements should come back
	 * @param session
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		// forgetful functor test
		Iterator<?> its = Relatrix.findSet(alias12, fkey, "Has time", "*");
		System.out.println(alias12+" Battery1AR11 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			if( DEBUG ) System.out.println("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
			throw new Exception("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
		}
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of remove. Remove the object and all its relationships.
	 * Perform 3 findSet with removed key to verify its gone.
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR12(String[] argv, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.remove(alias12,fkey);
		System.out.println(fkey+" removed, proceeding to verify removal of all relationships it may have been involved in");
		Iterator<?> its = Relatrix.findSet(alias12, fkey, "*", "*");
		System.out.println(alias12+" Battery1AR12 findSet in "+(System.currentTimeMillis()-tims)+" ms.");
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12 failed to delete key "+fkey+" "+(Result)its.next());
		}
		// re-insert
		Relatrix.store(alias12, fkey, "Has unit "+alias12, new Long(min));
		its = Relatrix.findSet(alias12, "*", fkey, "*");
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12-2 failed to delete key "+fkey);
		}
		its = Relatrix.findSet(alias12, "*", "*", fkey);
		if(its.hasNext()) {
			throw new Exception("BATTERY1AR12-3 failed to delete key "+fkey);
		}
		 System.out.println("BATTERY1AR12 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * remove entries
	 * @param argv
	 * @param alias12 
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv, Alias alias12) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB DMR size="+Relatrix.size(alias12, DomainMapRange.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(alias12, DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12, MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(alias12, MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(alias12, RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(alias12, RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet(alias12, "*","*","*");
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
			try {
				Relatrix.remove(alias12, dmr);
			} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | IOException e) {
				throw new RuntimeException(e);
			}
			++i;
			if((System.currentTimeMillis()-timx) > 1000) {
				System.out.println("deleting "+i+" total, current="+fkey);
				timx = System.currentTimeMillis();
			}
		});
		Iterator<?> its = Relatrix.findSet(alias12, "*","*","*");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			//System.out.println(i+"="+nex);
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
			else
				throw new Exception("KV RANGE 1AR17 KEY SHOULD BE DELETED:"+nex);
		}
		long siz = Relatrix.size(alias12);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 KEY MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, DomainMapRange.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("DomainMapRange:"+nex);
			}
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, DomainRangeMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("DomainRangeMap:"+nex);
			}
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, MapDomainRange.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapDomainRange:"+nex);
			}
		}
		siz = Relatrix.size(alias12, MapDomainRange.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapDomainRange MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, MapRangeDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("MapRangeDomain:"+nex);
			}
		}
		siz = Relatrix.size(alias12, MapRangeDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 MapRangeDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, RangeDomainMap.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeDomainMap:"+nex);
			}
		}
		siz = Relatrix.size(alias12, RangeDomainMap.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeDomainMap MISMATCH:"+siz+" > 0 after delete/commit");
		}
		if(DEBUG) {
			it = Relatrix.entrySet(alias12, RangeMapDomain.class);
			while(it.hasNext()) {
				Comparable<?> nex = (Comparable<?>) it.next();
				System.out.println("RangeMapDomain:"+nex);
			}
		}
		siz = Relatrix.size(alias12, RangeMapDomain.class);
		if(siz > 0) {
			if(DEBUG)
				System.out.println("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after all deleted and committed");
			else
				throw new Exception("KV RANGE 1AR17 RangeMapDomain MISMATCH:"+siz+" > 0 after delete/commit");
		}
		System.out.println("BATTERY1AR17 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	
}
