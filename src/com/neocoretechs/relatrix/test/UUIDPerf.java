package com.neocoretechs.relatrix.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.RelatrixKV;

public class UUIDPerf {
	private static int max = 100000;
	public static void main(String[] args) throws IOException, IllegalAccessException, DuplicateKeyException, IllegalArgumentException, ClassNotFoundException {
		RelatrixKV.setTablespace(args[0]);
		long tim = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			RelatrixKV.store(UUID.randomUUID(), i);
		}
		System.out.println("Store "+max+" UUID "+(System.currentTimeMillis()-tim)+" ms.");
		tim = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			RelatrixKV.store(UUID.randomUUID().toString(), i);
		}
		System.out.println("Store "+max+" UUID String "+(System.currentTimeMillis()-tim)+" ms.");
		tim = System.currentTimeMillis();
		Object o = null;
		Iterator<?> its = RelatrixKV.findTailMapKV((Comparable) RelatrixKV.firstKey(String.class));
		while(its.hasNext()) {
			o = its.next();
		}
		System.out.println("Get "+o+" UUID String "+(System.currentTimeMillis()-tim)+" ms.");
		tim = System.currentTimeMillis();
		its = RelatrixKV.findTailMapKV((Comparable) RelatrixKV.firstKey(UUID.class));
		while(its.hasNext()) {
			o = its.next();
		}
		System.out.println("Get "+o+" UUID "+(System.currentTimeMillis()-tim)+" ms.");

	}

}
