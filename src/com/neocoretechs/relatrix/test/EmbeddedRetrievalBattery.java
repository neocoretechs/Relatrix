package com.neocoretechs.relatrix.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.neocoretechs.bigsack.keyvaluepages.KeyValue;
import com.neocoretechs.bigsack.session.BigSackAdapter;
import com.neocoretechs.relatrix.DuplicateKeyException;

import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.client.RelatrixClient;
import com.neocoretechs.relatrix.client.RelatrixKVClient;
import com.neocoretechs.relatrix.client.RemoteTailSetIterator;

/**
 * NOTES:
 * program arguments are local_node remote_node remote_port_for_database
 * @author Jonathan Groff C 2021
 *
 */
public class EmbeddedRetrievalBattery {
	public static boolean DEBUG = false;
	private static RelatrixClient rkvc;

	/**
	*/
	public static void main(String[] argv) throws Exception {
		 //System.out.println("Analysis of all");
		battery1(argv);
		System.out.println("TEST BATTERY COMPLETE.");	
		System.exit(1);
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
		rkvc = new RelatrixClient(argv[0], argv[1], Integer.parseInt(argv[2]) );
		RemoteTailSetIterator it = null;
		System.out.println("Wildcard queries:");
		
		System.out.println("2.) Findset(*,*,?)...");		
		it = rkvc.findSet("*", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 0 ) ar.add(c[0]);
		}
		
		System.out.println("1.) Findset(*,*,*)...");
		it =  rkvc.findSet("*", "*", "*");
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//ar.add(c[0]);
		}
		
		System.out.println("3.) Findset(*,?,*)...");		
		it = rkvc.findSet("*", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 1 ) ar.add(c[0]);
		}
		System.out.println("4.) Findset(?,*,*)...");		
		it = rkvc.findSet("?", "*", "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			if(ar.size() == 2) ar.add(c[0]);
		}
		System.out.println("5.) Findset(*,?,?)...");		
		it = rkvc.findSet("*", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 0) ar2.add(c);
		}
		System.out.println("6.) Findset(?,*,?)...");		
		it = rkvc.findSet("?", "*", "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 1) ar2.add(c);
		}
		System.out.println("7.) Findset(?,?,*)...");		
		it = rkvc.findSet("?", "?", "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]);
			if(ar2.size() == 2) ar2.add(c);
		}
		System.out.println("8.) Findset(?,?,?)...");		
		it = rkvc.findSet("?", "?", "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" --- "+c[1]+" --- "+c[2]);
			if(ar3.size() == 0) ar3.add(c);
		}
		
		System.out.println("Above are all the wildcard permutations. Now retrieve those with object references using the");
		System.out.println("wildcard results. They should produce relationships with these elements");
		
		System.out.println("9.) Findset(<obj>,<obj>,<obj>)...");
		it = rkvc.findSet(ar3.get(0)[0], ar3.get(0)[1], ar3.get(0)[2]);
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//ar.add(c[0]);
		}
		System.out.println("10.) Findset(*,*,<obj>)...");		
		it = rkvc.findSet("*", "*", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		System.out.println("11.) Findset(*,<obj>,*)...");		
		it = rkvc.findSet("*", ar.get(1), "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		System.out.println("12.) Findset(<obj>,*,*)...");		
		it = rkvc.findSet(ar.get(2), "*", "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		System.out.println("13.) Findset(*,<obj>,<obj>)...");		
		it = rkvc.findSet("*", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 0) ar2.add(c);
		}
		System.out.println("14.) Findset(<obj>,*,<obj>)...");		
		it = rkvc.findSet(ar2.get(1)[0], "*", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 1) ar2.add(c);
		}
		System.out.println("15.) Findset(<obj>,<obj>,*)...");		
		it = rkvc.findSet(ar2.get(2)[0], ar2.get(2)[1], "*");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 2) ar2.add(c);
		}
		
		System.out.println("16.) Findset(?,?,<obj>)...");		
		it = rkvc.findSet("?", "?", ar.get(0));
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 0 ) ar.add(c[0]);
		}
		System.out.println("17.) Findset(?,<obj>,?)...");		
		it = rkvc.findSet("?", ar.get(1), "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 1 ) ar.add(c[0]);
		}
		System.out.println("18.) Findset(<obj>,?,?)...");		
		it = rkvc.findSet(ar.get(2), "?", "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]+" -- "+c[1]);
			//if(ar.size() == 2) ar.add(c[0]);
		}
		System.out.println("19.) Findset(?,<obj>,<obj>)...");		
		it = rkvc.findSet("?", ar2.get(0)[0], ar2.get(0)[1]);
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 0) ar2.add(c);
		}
		System.out.println("20.) Findset(<obj>,?,<obj>)...");		
		it = rkvc.findSet(ar2.get(1)[0], "?", ar2.get(1)[1]);
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 1) ar2.add(c);
		}
		System.out.println("21.) Findset(<obj>,<obj>,?)...");		
		it = rkvc.findSet(ar2.get(2)[0], ar2.get(2)[1], "?");
		//ar = new ArrayList<Comparable>();
		while(rkvc.hasNext(it)) {
			Object o = rkvc.next(it);
			Comparable[] c = (Comparable[])o;
			System.out.println(++recs+"="+c[0]);
			//if(ar2.size() == 2) ar2.add(c);
		}
		System.out.println("BATTERY1 SUCCESS in "+(System.currentTimeMillis()-tims));
	}
	

}
