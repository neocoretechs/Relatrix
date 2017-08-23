package com.neocoretechs.relatrix;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.neocoretechs.bigsack.session.BufferedTreeMap;
import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.bigsack.session.TransactionalTreeMap;
import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.config.Props;
/**
 * This class links the Relatrix to the BigSack, the underlying Treeset dep K/V store upon which these theorems rest.
 * Given a set of K/V sets, categories can be formed in the Relatrix. Theoretically, any K/V store could be used.
 * 
 * The main function of this adapter is to ensure that the appropriate map or set is instantiated.
 * A map or set can be obtained by instance of Comparable to impart ordering.
 * A Buffered map or set has atomic transactions bounded automatically with each insert/delete
 * A transactional map or set requires commit/rollback and can be checkpointed.
 * In either case recovery is in effect to preserve integrity.
 * The database name is the full path of the top level tablespace and log directory, i.e.
 * /home/db/test would create a 'test' database in the /home/db directory
 * If the config is cluster,
 * the log of master node and tablespace directories on the remote machines. OR if cluster mode, a remote
 * directory can be specified and the local master log first, then remote worker node tablespace directories.
 * This can affect different OS configs for cluster testing and heterogeneous clusters.
 * The class name is translated into the appropriate file name via a simple translation table to give us a
 * database/class/tablespace identifier for each file used.
 * @author jg Copyright (C) NeoCoreTechs 2014,2015
 *
 */
public class BigSackAdapter {
	private static boolean DEBUG = false;
	private static String tableSpaceDir = "/";
	private static String remoteDir = null;
	private static final char[] ILLEGAL_CHARS = { '[', ']', '!', '+', '=', '|', ';', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	private static final char[] OK_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E' };

	private static ConcurrentHashMap<String, TransactionalTreeSet> classToIsoTreeset = new ConcurrentHashMap<String,TransactionalTreeSet>();
	
	public static String getTableSpaceDir() {
		return tableSpaceDir;
	}
	public static void setTableSpaceDir(String tableSpaceDir) {
		BigSackAdapter.tableSpaceDir = tableSpaceDir;
	}
	public static String getRemoteDir() {
		return remoteDir;
	}
	public static void setRemoteDir(String tableSpaceDir) {
		BigSackAdapter.remoteDir = tableSpaceDir;
	}

	/**
	 * Get a transactional treeset via Comparable instance
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static TransactionalTreeSet getBigSackSetTransaction(Comparable clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getClass().getName());
		if(DEBUG)
			System.out.println("BigSackAdapter.getBigSackTransaction About to return designator: "+tableSpaceDir+xClass+" formed from "+clazz.getClass().getName());
		TransactionalTreeSet ret = classToIsoTreeset.get(xClass);
		if( ret == null ) {
			// check the other arrays to make sure we are not crossing boundaries
			ret =  new TransactionalTreeSet(tableSpaceDir+xClass, (remoteDir != null ? remoteDir+xClass : null), Props.L3CacheSize);
			classToIsoTreeset.put(xClass, ret);
		}
		return ret;
	}
	/**
	 * Get a transactional treeset
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static TransactionalTreeSet getBigSackSetTransaction(Class clazz) throws IllegalAccessException, IOException {
		String xClass = translateClass(clazz.getName());
		//System.out.println("About to return BigSack "+tableSpaceDir+xClass);
		TransactionalTreeSet ret = classToIsoTreeset.get(xClass);
		if( ret == null ) {
			ret =  new TransactionalTreeSet(tableSpaceDir+xClass, (remoteDir != null ? remoteDir+xClass : null), Props.L3CacheSize);
			classToIsoTreeset.put(xClass, ret);
		}
		return ret;
	}
	/**
	 * Translate a class name into a legitimate file name with some aesthetics
	 * @param clazz
	 * @return
	 */
	public static String translateClass(String clazz) {
		//boolean hasReplaced = false; // debug
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < clazz.length(); i++) {
			char chr = clazz.charAt(i);
			for(int j = 0; j < ILLEGAL_CHARS.length; j++) {
				if( chr == ILLEGAL_CHARS[j] ) {
					chr = OK_CHARS[j];
					//hasReplaced = true;
					break;
				}
			}
			sb.append(chr);
		}
		//if( hasReplaced )
		//	System.out.println("Class name translated from "+clazz+" to "+sb.toString());
		return sb.toString();
	}
}
