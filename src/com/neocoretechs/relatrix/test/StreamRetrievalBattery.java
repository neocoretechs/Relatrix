package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.client.RelatrixClient;

/**
 * NOTES: program args are local_node remote_node remote_database_port
 * @author Jonathan Groff (c) 2021
 *
 */
public class StreamRetrievalBattery {
	public static boolean DEBUG = false;
	private static RelatrixClient rkvc;

	/**
	*/
	public static void main(String[] argv) throws Exception {
		Morphism.displayLevel = Morphism.displayLevels.MINIMAL;
		battery1(argv);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(1);
	}
	/**
	 * @param argv
	 * @throws Exception
	 */
	public static void battery1(String[] argv) throws Exception {
		System.out.println("Stream Battery1 ");
		String fmap;
		long tims = System.currentTimeMillis();
		int recs = 0;
		rkvc = new RelatrixClient(argv[0], argv[1], Integer.parseInt(argv[2]) );
		// this list will store an object used to test subsequent queries where a named object is needed
		// it will be extracted from the wildcard queries
		ArrayList<Result> ar = new ArrayList<Result>();
		ArrayList<Result> ar2 = new ArrayList<Result>(); // will store 2 element result sets
		ArrayList<Result> ar3 = new ArrayList<Result>(); // will store 3 element result sets
	
		Stream it = null;
		System.out.println("Wildcard queries:");
	
		System.out.println("1.) FindStream(*,*,*)...");
		rkvc.findStream("*", "*", "*").forEach(System.out::println);
		
		System.out.println("2.) FindStream(*,*,?)...");		
		rkvc.findStream("*", "*", "?").forEach(e -> {
			if(ar.size() == 0 ) ar.add( (Result) e);	
			System.out.println(e);
		});
		
		System.out.println("3.) FindStream(*,?,*)...");		
		rkvc.findStream("*", "?", "*").forEach(e -> {
			if(ar.size() == 1 ) ar.add((Result) e);
			System.out.println(e);
		});
		
		System.out.println("4.) FindStream(?,*,*)...");		
		rkvc.findStream("?", "*", "*").forEach(e -> {
			if(ar.size() == 2 ) ar.add( (Result) e);
			System.out.println(e);
		});
		
		System.out.println("5.) FindStream(*,?,?)...");		
		rkvc.findStream("*", "?", "?").forEach(e -> {
			if(ar2.size() == 0) ar2.add( (Result) e);
			System.out.println(e);
		});
		
		System.out.println("6.) FindStream(?,*,?)...");		
		rkvc.findStream("?", "*", "?").forEach(e -> {
			if(ar2.size() == 1) ar2.add((Result) e);
			System.out.println(e);
		});
		
		System.out.println("7.) FindStream(?,?,*)...");		
		rkvc.findStream("?", "?", "*").forEach(e -> {
			if(ar2.size() == 2) ar2.add((Result) e);
			System.out.println(e);
		});
		
		System.out.println("8.) FindStream(?,?,?)...");		
		rkvc.findStream("?", "?", "?").forEach(e -> {
			if(ar3.size() == 0) ar3.add((Result) e);
			System.out.println(e);
		});
		
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the");
		System.out.println("wildcard results. They should produce relationships with these elements");
		
		System.out.println("9.) FindStream(<obj>,<obj>,<obj>)...");
		rkvc.findStream(ar3.get(0).get(0), ar3.get(0).get(1), ar3.get(0).get(2)).forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("10.) FindStream(*,*,<obj>)...");		
		rkvc.findStream("*", "*", ar.get(0).get(0)).forEach(e -> System.out.println(e.getClass().getName()+","+e));

		System.out.println("11.) FindStream(*,<obj>,*)...");		
		rkvc.findStream("*", ar.get(1).get(0), "*").forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("12.) FindStream(<obj>,*,*)...");		
		rkvc.findStream(ar.get(2).get(0), "*", "*").forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("13.) FindStream(*,<obj>,<obj>)...");		
		rkvc.findStream("*", ar2.get(0).get(0), ar2.get(0).get(1)).forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("14.) FindStream(<obj>,*,<obj>)...");		
		rkvc.findStream(ar2.get(1).get(0), "*", ar2.get(1).get(1)).forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("17.) FindStream(?,<obj>,?)...");		
		rkvc.findStream("?", ar.get(1).get(0), "?").forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("18.) FindStream(<obj>,?,?)...");		
		rkvc.findStream(ar.get(2).get(0), "?", "?").forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("19.) FindStream(?,<obj>,<obj>)...");		
		rkvc.findStream("?", ar2.get(0).get(0), ar2.get(0).get(1)).forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("20.) FindStream(<obj>,?,<obj>)...");		
		rkvc.findStream(ar2.get(1).get(0), "?", ar2.get(1).get(1)).forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("21.) FindStream(<obj>,<obj>,?)...");		
		rkvc.findStream(ar2.get(2).get(0), ar2.get(2).get(1), "?").forEach(e -> System.out.println(e.getClass().getName()+","+e));
		
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims));
	}
	

}
