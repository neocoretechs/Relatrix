package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

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
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.IndexResolver;

/**
 * Stream version of BatteryRelatrix.<p/>
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals
 * In general most of the testing relies on checking order against expected values hence the importance of
 * canonical ordering in the sample strings.
 * Of course, you can substitute any class for the Strings here providing its Comparable.
 * The set of tests verifies the higher level 'transactionalStore' and 'findSet' functors in the Relatrix, which can be used
 * as examples of Relatrix processing.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2016,2017,2024
 *
 */
public class BatteryRelatrixStream {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 1000;
	static int numDelete = 100; // for delete test
	static int i = 0;
	private static long timx;
	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix.setTablespace(argv[0]);
		Morphism.displayLevel = displayLevels.VERBOSE;
		System.out.println("BatteryRelatrixStream");
		
		if(Relatrix.size() == 0) {
			if(DEBUG)
				System.out.println("Begin test battery 1");
			battery1(argv);
			if(DEBUG)
				System.out.println("Begin test battery 11");
			battery11(argv);
		}
		if(DEBUG)
			System.out.println("Begin test battery 1AR6");
		battery1AR6(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR7");
		battery1AR7(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR8");
		battery1AR8(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR9");
		battery1AR9(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR10");
		battery1AR10(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR101");
		battery1AR101(argv);
		if(DEBUG)
			System.out.println("Begin test battery 1AR11");
		battery1AR11(argv);
	
		System.out.println("TEST BATTERY COMPLETE.");
		System.exit(0);
	}
	/**
	 * Loads up on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Battery1 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.store(fkey, "Has unit", new Long(i));
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
	 * Tries to store partial key that should match existing keys, should reject all.
	 * Domain/map determines unique key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery11(String[] argv) throws Exception {
		System.out.println("Battery11 ");
		long tims = System.currentTimeMillis();
		long timt = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				DomainMapRange dmr = Relatrix.store(fkey, "Has unit", new Long(99999));
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
	 * Test the higher level functions in the Relatrix. Use the 'findStream' permutations to
	 * verify the previously inserted data
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR6(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR6");
		Relatrix.findStream("?", "?", "?").forEach(e->{
			Result nex = (Result)e;
			// 3 question marks = dimension 3 in return array
			if( DEBUG ) System.out.println("1AR6:"+i+" "+nex);
			if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit") || nex.length() != 3) {
				System.out.println("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(1)+" length:"+nex.length());
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(1)+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR6 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR6 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR6 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Stream Relatrix.findStream("?", "*", "*");
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR7");
		Relatrix.findStream("?", "*", "*").forEach(e->{
			Result nex = (Result)e;
			// one '?' in findStream gives us one element returned
			if(DEBUG ) System.out.println("1AR7:"+i+" "+nex);
			if(!((String) nex.get(0)).startsWith(key) || nex.length() != 1) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+"  "+nex+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR7 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR7 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR7 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Testing of Stream Relatrix.findStream("?", "?", "*");
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Relatrix.findStream("?", "?", "*").forEach(e ->{
			Result nex = (Result)e;
			// two '?' in findStream gives use 2 element array, the domain and map
			if( DEBUG ) System.out.println("1AR8:"+i+" "+nex);
			if(!((String) nex.get(0)).startsWith(key) || !nex.get(1).equals("Has unit") || nex.length() != 2) {
				System.out.println("KEY MISMATCH:"+(i)+" "+nex.get(0)+" Has unit - "+nex.get(1)+" length:"+nex.length());
				throw new RuntimeException("KEY MISMATCH:"+(i)+" Has unit - "+nex.get(1)+" length:"+nex.length());
			}
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR8 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR8 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR8 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * 
	 * Testing of Relatrix.findStream("*", "*", "*");
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		Relatrix.findStream("*", "*", "*").forEach(e->{
			Result nex = (Result)e;
			// the returned array has 1 element, the identity Morphism DomainMapRange
			if( DEBUG ) System.out.println("1AR9:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit"))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(0));
			++i;
		});
		if( i != max ) {
			System.out.println("BATTERY1AR9 unexpected number of keys "+i);
			//throw new Exception("BATTERY1AR9 unexpected number of keys "+i);
		}
		 System.out.println("BATTERY1AR9 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * Relatrix.findSetStream(fkey, "Has unit", "*");
	 * Should return 1 element of which 'fkey' and "Has unit" are primary key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.findStream(fkey, "Has unit", "*").forEach(e-> {
		// return all identities with the given key for all ranges, should be 1
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result)e;
			if( nex.length() != 1)
				throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR10:"+i+" "+nex.get(0));
			String skey = key + String.format(uniqKeyFmt, i);
			if(!((String) ((DomainMapRange)nex.get(0)).getDomain() ).startsWith(skey) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit"))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(0));
			++i;
		});
		if( i != 1 ) {
			System.out.println("BATTERY1AR10 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR10 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR10 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Relatrix.findStream(fkey, "Has unit", new Long(max));
	 * Range value is max, so zero keys should be retrieved since we insert 0 to max-1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		i = 0;
		long tims = System.currentTimeMillis();
		String fkey = key + String.format(uniqKeyFmt, max);
		// Range value is max, so zero keys should be retrieved since we insert 0 to max-1
		Relatrix.findStream(fkey, "Has unit", new Long(max)).forEach(e->{
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result) e;
			if( nex.length() != 1)
				throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR101:"+i+" "+nex.get(0));
			String skey = key + String.format(uniqKeyFmt, i);
			if(!( (String)((DomainMapRange)nex.get(0)).getDomain() ).startsWith(skey) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex.get(0));
			if(!((DomainMapRange)nex.get(0)).getMap().equals("Has unit"))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(0));
			//Long unit = new Long(i);
			//if(!((DomainMapRange)nex[0]).getRange().equals(unit))
				//System.out.println("RANGE KEY MISMATCH:"+(i)+" "+i+" - "+nex[0]);
			++i;
		});
		if( i != 0 ) {
			System.out.println("BATTERY1AR101 unexpected number of keys "+i);
			throw new Exception("BATTERY1AR101 unexpected number of keys "+i);
		}
		System.out.println("BATTERY1AR101 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * negative assertion of above
	 * Relatrix.findStream(fkey, "Has time", "*");
	 * map is 'Has time', which we never inserted, so no elements should come back
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
	
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.findStream(fkey, "Has time", "*").forEach(e->{
			Result nex = (Result)e;
			if( DEBUG ) System.out.println("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
			throw new RuntimeException("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
		});
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}


	public static void displayMorphism(Object fkey) throws IllegalAccessException, ClassNotFoundException, IOException {
		System.out.println(" class:"+fkey.getClass()+" value:"+fkey);
		if(fkey.getClass().isArray()) {
			for(int j = 0; j < ((Comparable[])fkey).length; j++) {
				System.out.println(j+"="+((Comparable[])fkey)[j].getClass()+" "+((Comparable[])fkey)[j]);
				Morphism dmr = (Morphism) ((Comparable[])fkey)[j];
				System.out.println("Keys:"+dmr.getDomainKey()+", "+dmr.getMapKey()+", "+dmr.getRangeKey());
				if(dmr.isDomainKeyValid())
					System.out.println("Domain:"+dmr.getDomain());
				if(dmr.getDomain() == null)
					System.out.println(IndexResolver.getIndexInstanceTable().getByIndex(dmr.getDomainKey()));
				if(dmr.isMapKeyValid())
					System.out.println("Map:"+dmr.getMap());
				if(dmr.getMap() == null)
					System.out.println(IndexResolver.getIndexInstanceTable().getByIndex(dmr.getMapKey()));
				if(dmr.isRangeKeyValid())
					System.out.println("Range:"+dmr.getRange());
				if(dmr.getRange() == null)
					System.out.println(IndexResolver.getIndexInstanceTable().getByIndex(dmr.getRangeKey()));
			}
		}
	}
	
}
