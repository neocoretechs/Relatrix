package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.AbstractRelation.displayLevels;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.key.IndexResolver;

/**
 * Stream version of BatteryRelatrix.<p/>
 * The set of tests verifies the higher level 'findSet' functions in the {@link Relatrix}, which can be used
 * as examples of Relatrix processing.
 * In general the tests compare the number of items retrieved 
 * against expected value since findSet retrieves items in no particular order.
 * NOTES:
 * A database unique to this test module should be used.
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2 [ [init] [max nnn] ]
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
		AbstractRelation.displayLevel = displayLevels.VERBOSE;
		System.out.println("BatteryRelatrixStream");
		if(argv.length > 2 && argv[1].equals("max")) {
			System.out.println("Setting max items to "+argv[2]);
			max = Integer.parseInt(argv[2]);
		} else {
			if(argv.length > 1 && argv[1].equals("init")) {
				System.out.println("Initialize database to zero items, then terminate...");
				battery1AR17(argv);
				System.exit(0);
			}
		}
		if(Relatrix.size() == 0) {
			if(DEBUG)
				System.out.println("Zero items, Begin insertion from "+min+" to "+max);
			battery1(argv);
			if(DEBUG)
				System.out.println("Begin duplicate key rejection test from "+min+" to "+max);
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
				Relatrix.store(fkey, "Has unit", Long.valueOf(i));
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
				Relation dmr = Relatrix.store(fkey, "Has unit", Long.valueOf(99999));
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
		Relatrix.findStream('?', '?', '?').forEach(e->{
			Result nex = (Result)e;
			// 3 question marks = dimension 3 in return array
			if( DEBUG ) System.out.println("1AR6:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
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
	 * Testing of Stream Relatrix.findStream('?', '*', '*');
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR7(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR7");
		Relatrix.findStream('?', '*', '*').forEach(e->{
			Result nex = (Result)e;
			// one '?' in findStream gives us one element returned
			if(DEBUG ) System.out.println("1AR7:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// No guarantee of order with unqualified findSet/findStream
			if(!((String)nex.get(0)).startsWith(key) || nex.length() != 1) {
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
	 * Testing of Stream Relatrix.findStream('?', '?', '*');
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR8(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR8");
		Relatrix.findStream('?', '?', '*').forEach(e ->{
			Result nex = (Result)e;
			// two '?' in findStream gives use 2 element array, the domain and map
			if( DEBUG ) System.out.println("1AR8:"+i+" "+nex);
			//String fkey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
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
	 * Testing of Relatrix.findStream('*', '*', '*');
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR9(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR9");
		Relatrix.findStream('*', '*', '*').forEach(e->{
			Result nex = (Result)e;
			// the returned array has 1 element, the identity AbstractRelation Relation
			if( DEBUG ) System.out.println("1AR9:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!((String) ((Relation)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" - "+nex.get(0));
			if(!((Relation)nex.get(0)).getMap().equals("Has unit"))
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
	 * Relatrix.findSetStream(fkey, "Has unit", '*');
	 * Should return 1 element of which 'fkey' and "Has unit" are primary key
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR10(String[] argv) throws Exception {
		i = min;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR10");
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.findStream(fkey, "Has unit", '*').forEach(e-> {
		// return all identities with the given key for all ranges, should be 1
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result)e;
			if( nex.length() != 1)
				throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR10:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!((String) ((Relation)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+key+" - "+nex.get(0));
			if(!((Relation)nex.get(0)).getMap().equals("Has unit"))
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
	 * Relatrix.findStream(fkey, "Has unit", Long.valueOf(max));
	 * Range value is max, so zero keys should be retrieved since we insert 0 to max-1
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR101(String[] argv) throws Exception {
		i = 0;
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR101");
		String fkey = key + String.format(uniqKeyFmt, max);
		// Range value is max, so zero keys should be retrieved since we insert 0 to max-1
		Relatrix.findStream(fkey, "Has unit", Long.valueOf(max)).forEach(e->{
			// In this case, the set of identities of type Long that have stated domain and map should be returned
			// since we supply a fixed domain and map object with a wildcard range, we should get one element back; the identity
			Result nex = (Result) e;
			if( nex.length() != 1)
				throw new RuntimeException("RETURNED ARRAY TUPLE LENGTH INCORRECT, SHOULD BE 1, is "+nex.length());
			if(DEBUG) System.out.println("1AR101:"+i+" "+nex.get(0));
			//String skey = key + String.format(uniqKeyFmt, i);
			// no guarantee of ordering with unqualified findSet/findStream
			if(!( (String)((Relation)nex.get(0)).getDomain() ).startsWith(key) )
				throw new RuntimeException("DOMAIN KEY MISMATCH:"+(i)+" "+key+" - "+nex.get(0));
			if(!((Relation)nex.get(0)).getMap().equals("Has unit"))
				throw new RuntimeException("MAP KEY MISMATCH:"+(i)+" Has unit - "+nex.get(0));
			//Long unit = Long.valueOf(i);
			//if(!((Relation)nex[0]).getRange().equals(unit))
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
	 * Relatrix.findStream(fkey, "Has time", '*');
	 * map is 'Has time', which we never inserted, so no elements should come back
	 * @param session
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR11(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("Battery1AR11");
		String fkey = key + String.format(uniqKeyFmt, min);
		Relatrix.findStream(fkey, "Has time", '*').forEach(e->{
			Result nex = (Result)e;
			if( DEBUG ) System.out.println("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
			throw new RuntimeException("1AR11: SHOULD NOT HAVE ENCOUNTERED:"+nex.get(0));
		});
		 System.out.println("BATTERY1AR11 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}

	/**
	 * remove entries, all relationships should be recursively deleted
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB DMR size="+Relatrix.size(Relation.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(RangeMapDomain.class));
		AbstractRelation.displayLevel = AbstractRelation.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet('*','*','*');
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			Relation dmr = (Relation)((Result)fkey).get(0);
			try {
				Relatrix.remove(dmr);
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
