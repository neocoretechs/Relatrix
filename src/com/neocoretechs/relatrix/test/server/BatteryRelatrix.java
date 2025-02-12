package com.neocoretechs.relatrix.test.server;

import java.util.Optional;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixClientTransaction;
import com.neocoretechs.rocksack.TransactionId;

/**
 * This series of tests loads up arrays to create a cascading set of retrievals mostly checking
 * and verifying findStream retrieval using the client to a remote {@link com.neocoretechs.relatrix.server.RelatrixServer}.
 * NOTES:
 * program arguments are local_node remote_node remote_port_for_database
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 */
public class BatteryRelatrix {
	public static boolean DEBUG = false;
	static String key = "This is a test"; // holds the base random key string for tests
	static String uniqKeyFmt = "%0100d"; // base + counter formatted with this gives equal length strings for canonical ordering
	static int min = 0;
	static int max = 2000;
	public static String DATABASE;

	/**
	* Analysis test fixture
	*/
	public static void main(String[] argv) throws Exception {
		RelatrixClient session = null;
		DATABASE = argv[0];		
		session = new RelatrixClient(argv[0], argv[1], Integer.parseInt(argv[2]) );
		if(session.size() == 0) {
			battery1(session);
		}
		battery2(session);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(0);
	}
	/**
	 * Loads up on keys
	 */
	public static void battery1(RelatrixClient rct) throws Exception {
		System.out.println("Battery0 ");
		long tims = System.currentTimeMillis();
		int dupes = 0;
		int recs = 0;
		String fkey = null;
		int i = min;
		for(; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
			DomainMapRange dmr = rct.store(fkey, "Has unit", new Long(i));
			System.out.println(i+".)"+dmr);
			DomainMapRange dmr2 = rct.store(dmr ,"has identity",new Long(i));
			System.out.println(i+".)"+dmr2);
			++recs;
		}
		System.out.println("BATTERY0 SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs+" records, rejected "+dupes+" dupes.");
	}
	
	public static void battery2(RelatrixClient rct) throws Exception {
		System.out.println("Battery2 ");
		long tims = System.currentTimeMillis();
		int recs = 0;
		String fkey = null;
		for(int i = min; i < max; i++) {
			fkey = key + String.format(uniqKeyFmt, i);
				Optional<?> o =  rct.findStream(fkey, "Has unit", new Long(i)).findFirst();
				if(o.isPresent()) {
					Optional<?> p = rct.findStream(((Result)o.get()).get(), '*', '*').findFirst();
					if(p.isPresent()) {
						Result c = (Result) p.get();
						DomainMapRange d = (DomainMapRange) c.get();
						if(!d.getDomain().equals(fkey))
							System.out.println("Domain identity doesnt match "+fkey);
						if(!d.getMap().equals("has identity"))
							System.out.println("Map identity doesnt match 'has identity'");
						if(!d.getRange().equals(new Long(i)))
							System.out.println("Range identity doesnt match "+i);
					} else
						System.out.println("Failed to find identity for "+o.get());	
				} else
					System.out.println("Failed to find identity for "+fkey);
				++recs;
		}
		System.out.println("BATTERY2 verification SUCCESS in "+(System.currentTimeMillis()-tims)+" ms. Stored "+recs);
	}

}
