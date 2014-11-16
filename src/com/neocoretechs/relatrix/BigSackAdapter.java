package com.neocoretechs.relatrix;

import java.io.IOException;

import com.neocoretechs.bigsack.session.BufferedTreeMap;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.config.Props;

public class BigSackAdapter {
	private static String tableSpaceDir = "C:/users/jg/Relatrix/";
	private static final char[] ILLEGAL_CHARS = { '[', ']', '!', '+', '=', '|', ';', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	private static final char[] OK_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E' };
	
	public static String getTableSpaceDir() {
		return tableSpaceDir;
	}
	public static void setTableSpaceDir(String tableSpaceDir) {
		BigSackAdapter.tableSpaceDir = tableSpaceDir;
	}
	
	public static BufferedTreeMap getBigSackMap(Comparable clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getClass().getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new BufferedTreeMap(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static BufferedTreeMap getBigSackMap(Class clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new BufferedTreeMap(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static BufferedTreeSet getBigSackSet(Comparable clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getClass().getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new BufferedTreeSet(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static BufferedTreeSet getBigSackSet(Class clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new BufferedTreeSet(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	//
	// The following provide transactional instances
	//
	public static TransactionalTreeMap getBigSackMapTransaction(Comparable clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getClass().getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new TransactionalTreeMap(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static TransactionalTreeMap getBigSackMapTransaction(Class clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new TransactionalTreeMap(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static TransactionalTreeSet getBigSackSetTransaction(Comparable clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getClass().getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new TransactionalTreeSet(tableSpaceDir+xClass, Props.L3CacheSize);
	}
	public static TransactionalTreeSet getBigSackSetTransaction(Class clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		return new TransactionalTreeSet(tableSpaceDir+xClass, Props.L3CacheSize);
	}

	public static String translateClass(String clazz) {
		boolean hasReplaced = false; // debug
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < clazz.length(); i++) {
			char chr = clazz.charAt(i);
			for(int j = 0; j < ILLEGAL_CHARS.length; j++) {
				if( chr == ILLEGAL_CHARS[j] ) {
					chr = OK_CHARS[j];
					hasReplaced = true;
					break;
				}
			}
			sb.append(chr);
		}
		if( hasReplaced )
			System.out.println("Class name translated from "+clazz+" to "+sb.toString());
		return sb.toString();
	}
}
