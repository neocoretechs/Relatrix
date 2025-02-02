package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.DomainRangeMap;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.MapDomainRange;
import com.neocoretechs.relatrix.MapRangeDomain;
import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.RangeDomainMap;
import com.neocoretechs.relatrix.RangeMapDomain;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Morphism.displayLevels;


/**
 * The set of tests verifies the higher level 'findSubSet' and 'findHeadSet' 'findTailSet'
 * retrieval functions in the {@link Relatrix}.<p/>
 * Yes, this should be a nice JUnit fixture someday
 * The static constant fields in the class control the key generation for the tests
 * In general, the keys and values are formatted according to uniqKeyFmt to produce
 * a series of canonically correct sort order strings for the DB in the range of min to max vals.
 * In general, most of the battery testing relies on checking order against expected values, hence the importance of
 * canonical ordering in the sample strings.
 * NOTES:
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2, which would create a series of database TestDB2 class tables
 * @author Jonathan Groff (C) Copyright NeoCoreTechs 2016,2017,2024
 *
 */
public class BatteryRelatrix2 {
	public static boolean DEBUG = true;
	static String key = "This is a test"; // holds the base random key string for tests
	static String val = "Of a Relatrix element!"; // holds base random value string
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static long timx;
	static long i;
	static int min = 0;
	static int max = 1000;

	/**
	* Main test fixture driver
	*/
	public static void main(String[] argv) throws Exception {
		DatabaseManager.setTableSpaceDir(argv[0]);
		//Morphism.displayLevel = displayLevels.VERBOSE;
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
		}

		battery1A1(argv);
		battery1B(argv);
		battery1C(argv);
		battery1D(argv);
		battery1E(argv);
		battery1F(argv);
	
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
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			try {
				Relatrix.store(fkey, "Has unit", new Long(i));
				++recs;
			} catch(DuplicateKeyException dke) { ++dupes; }
		}
		 System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	
	/**
	 * Test the findTailSet
	 * Returns a view of the portion of this set whose elements are strictly greater or equal to toElement.
	 * Extract the key from domain and compare it to incrementing result
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1A1(String[] argv) throws Exception {
		int i = max/2;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findTailSet('*', '*', '*',skey, String.class, Long.class);
		System.out.println("Battery1A1");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			if(DEBUG)
				System.out.println("1A1:"+i+" "+nex);
			if(skey.compareTo((String)((Morphism)nex.get()).getDomain()) != 0 ) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex);
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex);
			}
			++i;
			skey = key + String.format(uniqKeyFmt, i);
		}
		if( i != max)
		 throw new Exception("BATTERY1A1 number of keys mismatched:"+i+" should be "+max);
		 System.out.println("BATTERY1A1 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Test the findHeadSet
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1B(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findHeadSet('*', '*', '*', skey, String.class, Long.class);
		System.out.println("Battery1B");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			if(DEBUG) 
				System.out.println("1B:"+j+" "+nex);
			skey = key + String.format(uniqKeyFmt, j);
			if(skey.compareTo((String)(((Morphism)nex.get()).getDomain())) != 0 ) {
				System.out.println("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex);
				throw new Exception("DOMAIN KEY MISMATCH:"+(i)+" "+skey+" - "+nex);
			}
			++j;
		}
		if(j != i)
		 throw new Exception("BATTERY1B number of keys mismatched:"+j+" should be "+(i));
		 System.out.println("BATTERY1B SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findHeadSet
	 * Returns a view of the portion of this set whose elements are strictly less than toElement.
	 * return range element for verification, a series of Long instances
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1C(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		Iterator<?> its = Relatrix.findHeadSet('*', '*','?' , skey, String.class, Long.class);
		System.out.println("Battery1C");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1C:"+i+" "+j+" "+nex);
			if(j != ((Long)nex.get()) ) {
				System.out.println("RANGE KEY MISMATCH:"+(i+j)+" "+skey+" - "+nex);
				throw new Exception("RANGE KEY MISMATCH:"+(i+j)+" "+skey+" - "+nex);
			}
			++j;
		}
		if(j != i)
		 throw new Exception("BATTERY1C number of keys mismatched:"+j+" should be "+(i));
		 System.out.println("BATTERY1C SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * Check for empty set with same keys
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1D(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		int range = 10;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		String skey2 = key + String.format(uniqKeyFmt, i+range); // if 500 and 510 we should get 500 to 509
		Iterator<?> its = Relatrix.findSubSet('?', '*', '*', skey, skey2, String.class, Long.class);
		System.out.println("Battery1D");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG) 
				System.out.println("1D:"+j+" "+nex);
			skey = key + String.format(uniqKeyFmt, (j+i));
			if(skey.compareTo((String)nex.get()) != 0 ) {
				System.out.println("SUBSET MISMATCH:"+(i)+" "+skey+" - "+nex);
				throw new Exception("SUBSET MISMATCH:"+(i)+" "+skey+" - "+nex);
			}
			++j;
		}
		if(j != range)
		 throw new Exception("BATTERY1D number of keys mismatched:"+j+" should be "+range);
		System.out.println("BATTERY1D SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1E(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		int range = 10;
		long tims = System.currentTimeMillis();
		Iterator<?> its = Relatrix.findSubSet('*', '*', '?', String.class, String.class, new Long(i), new Long(i+range));
		System.out.println("Battery1E");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) System.out.println("1E:"+i+" "+nex);
			if(i+j != ((Long)nex.get()) ) {
				System.out.println("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" - "+nex);
				throw new Exception("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" - "+nex);
			}
			++j;
		}
		if(j != range)
		 throw new Exception("BATTERY1E number of keys mismatched:"+j+" should be "+range);
		 System.out.println("BATTERY1E SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * Test the findSubSet
	 * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * In this case we are retrieving via a concrete instance of the map.
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1F(String[] argv) throws Exception {
		int i = max/2;
		int j = 0;
		int range = 10;
		long tims = System.currentTimeMillis();
		String skey = key + String.format(uniqKeyFmt, i);
		String skey2 = key + String.format(uniqKeyFmt, i+range); // if 500 and 510 we should get 500 to 509
		// Notice how we qualify the ranges here for subset. Should we have one range lesser, that range would supersede the greater.
		Iterator<?> its = Relatrix.findSubSet('*', "Has unit", '?',skey, skey2, new Long(i), new Long(i+range));
		System.out.println("Battery1F");
		while(its.hasNext()) {
			Result nex = (Result) its.next();
			// one '?' in findset gives us one element returned
			if(DEBUG ) 
				System.out.println("1F:"+j+" "+nex);
			if(i+j != ((Long)nex.get()) ) {
				System.out.println("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex);
				throw new Exception("SUBSET MISMATCH:"+(i)+" "+j+" "+(i+j)+" "+skey+" - "+nex);
			}
			++j;
		}
		if( j != range)
		 throw new Exception("BATTERY1F number of keys mismatched:"+j+" should be "+range);
		 System.out.println("BATTERY1F SUCCESS in "+(System.currentTimeMillis()-tims)+" ms.");
	}
	/**
	 * remove entries
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1AR17(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		System.out.println("CleanDB DMR size="+Relatrix.size(DomainMapRange.class));
		System.out.println("CleanDB DRM size="+Relatrix.size(DomainRangeMap.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapDomainRange.class));
		System.out.println("CleanDB MDR size="+Relatrix.size(MapRangeDomain.class));
		System.out.println("CleanDB RDM size="+Relatrix.size(RangeDomainMap.class));
		System.out.println("CleanDB RMD size="+Relatrix.size(RangeMapDomain.class));
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		Iterator<?> it = Relatrix.findSet('*','*','*');
		timx = System.currentTimeMillis();
		it.forEachRemaining(fkey-> {
			DomainMapRange dmr = (DomainMapRange)((Result)fkey).get(0);
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
	}
}
