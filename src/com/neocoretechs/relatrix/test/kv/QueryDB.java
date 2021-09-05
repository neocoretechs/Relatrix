package com.neocoretechs.relatrix.test.kv;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.Relatrix;

/**
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2, domain wildcard, map wildcard, range wildcard
 * @author jg (C) 2021
 *
 */
public class QueryDB {
	public static boolean DEBUG = false;
	static int recs = 1;
	/**
	* Dump key/value store
	*/
	public static void main(String[] argv) throws Exception {
		Relatrix.setTablespaceDirectory(argv[0]);
		dump1(argv[1],argv[2],argv[3]);
		System.out.println("Dump COMPLETE.");
		System.exit(0);
	}
	/**
	 * dumps on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void dump1(String domain, String map, String range) throws Exception {
		long tims = System.currentTimeMillis();
		/*
		Relatrix.entrySetStream(clazz).forEach(e-> {
			System.out.printf("%d=%s, %s | %s, %s%n",recs++, 
			((Map.Entry<?, ?>)e).getKey().getClass().getName(),
			((Map.Entry<?, ?>)e).getKey(),
			((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue().getClass().getName(),
			((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue());
		});
		*/
		Iterator<?> it = Relatrix.findSet(domain,map,range);
		while(it.hasNext()) {
			Comparable[] e = (Comparable[]) it.next();
			switch(e.length) {
			case 1:
			System.out.printf("%d=%s,%s%n",recs++, 
					e[0].getClass().getName(),e[0]);
			break;
			case 2:
				System.out.printf("%d=%s,%s->%s,%s%n",recs++, 
						e[0].getClass().getName(),e[0],e[1].getClass().getName(),e[1]);
				break;
			case 3:
				System.out.printf("%d=%s,%s->%s,%s->%s,%s%n",recs++, 
						e[0].getClass().getName(),e[0],e[1].getClass().getName(),e[1],e[2].getClass().getName(),e[2]);
				break;
			default:
				System.out.println("ZERO OR UNDEFINED LENGH RESULT SET");
					
			}
		}
		System.out.println("Dump in "+(System.currentTimeMillis()-tims)+" ms. retrieved "+recs+" records");
	}
		
}
