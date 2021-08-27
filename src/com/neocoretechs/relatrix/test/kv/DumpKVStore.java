package com.neocoretechs.relatrix.test.kv;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neocoretechs.relatrix.RelatrixKV;

/**
 * program argument is database i.e. C:/users/you/Relatrix/TestDB2, class i.e. com.your.class.class
 * @author jg (C) 2021
 *
 */
public class DumpKVStore {
	public static boolean DEBUG = false;
	static int recs = 1;
	/**
	* Dump key/value store
	*/
	public static void main(String[] argv) throws Exception {
		RelatrixKV.setTablespaceDirectory(argv[0]);
		dump1(argv);
		System.out.println("Dump COMPLETE.");
		System.exit(0);
	}
	/**
	 * dumps on keys
	 * @param argv
	 * @throws Exception
	 */
	public static void dump1(String[] argv) throws Exception {
		long tims = System.currentTimeMillis();
		Class clazz = Class.forName(argv[1]);
		System.out.printf("First Key = %s, %s%n", argv[1], RelatrixKV.firstKey(clazz));
		System.out.printf("Last Key = %s, %s%n", argv[1], RelatrixKV.lastKey(clazz));
		System.out.printf("Count = %d%n", RelatrixKV.size(clazz));
		/*
		RelatrixKV.entrySetStream(clazz).forEach(e-> {
			System.out.printf("%d=%s, %s | %s, %s%n",recs++, 
			((Map.Entry<?, ?>)e).getKey().getClass().getName(),
			((Map.Entry<?, ?>)e).getKey(),
			((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue().getClass().getName(),
			((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue());
		});
		*/
		Iterator it = RelatrixKV.entrySet(clazz);
		while(it.hasNext()) {
			Map.Entry<?,?> e = (Entry<?, ?>) it.next();
			System.out.printf("%d=%s, %s | %s, %s%n",recs++, 
					((Map.Entry<?, ?>)e).getKey().getClass().getName(),
					((Map.Entry<?, ?>)e).getKey(),
					((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue().getClass().getName(),
					((Map.Entry<?, ?>)e).getValue() == null ? "NULL" : ((Map.Entry<?, ?>)e).getValue());
		}
		System.out.println("Dump in "+(System.currentTimeMillis()-tims)+" ms. retrieved "+recs+" records");
	}
		
}
