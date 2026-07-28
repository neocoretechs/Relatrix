package com.neocoretechs.relatrix.iterator;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.rocksack.TransactionId;

import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.RelatrixKVTransaction;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.key.DBKey;

/**
 * Helper routines to be used with headset, subset, tailset to populate a TreeMap with DBKeys ordered by indexes in 
 * three arraylists designated dkey, mkey and rkey for domain key, map key and range key, from a range of Morphisms.<p/>
 * The Morphisms are designated by xdmr lower bound inclusive to ydmr upper bound inclusive. The order is created by using the
 * ordered positions in the 3 domain, map and range key arrays based on indexOf each AbstractRelation component
 * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap. The TreeMap then
 * becomes the basis for the iterator or stream that delivers the results.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class FindsetUtil {
	private static boolean DEBUG = false;
	private static boolean DEBUGITERATION = false;
	private static Object mutex = new Object();
	
	/**
	 * populate the TreeMap with DBKeys ordered by indexes in 
	 * three arraylists designated dkey, mkey and rkey for domain key, map key and range key, from a range of Morphisms.<p>
	 * The Morphisms are passed in the m0 parameter. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each {@link AbstractRelation} component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap. Process
	 * the m0 AbstractRelation to create the entry. The TreeMap then
	 * becomes the basis for the iterator or stream that delivers the results.
	 * @param m0 The iterated AbstractRelation to process against the three DBKey index arrays
	 * @param dkey The domain key array of instance ordered DBKeys
	 * @param mkey The map key array of instance ordered DBKeys
	 * @param rkey The range key array of instance ordered DBKeys
	 * @param resultSet The treemap to be populated with Result3 post-ordering indexes
	 */
    private static void createResultSet(AbstractRelation m0, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) {
			Result3 r = new Result3();
			boolean insert = false;
			int insd = -1;
			int insm = -1;
			int insr = -1;
			if(dkey.size() > 0) {
				// does our AbstractRelation domain key exist in headSet of designated headset domain objects, if any?
				insd = dkey.indexOf(m0.getDomainKey());
				// no, this AbstractRelation is not eligible
				if(insd != -1) {
					insert = true;
					// yes, set result index 0 to sort position of domain headset list key
					r.set(0,insd);
				}
			}
			if(mkey.size() > 0) { // should we check map, and is this AbstractRelation still eligible?
				if(insert) {
					insm = mkey.indexOf(m0.getMapKey());
					if(insm != -1) {
						r.set(1,insm);
					} else {
						insert = false;
					}
				}
			} else {
				insert = false;
			}
			if(rkey.size() > 0) {
				if(insert) {
					insr = rkey.indexOf(m0.getRangeKey());
					if(insr != -1) {
						r.set(2,insr);
					} else {
						insert = false;
					}
				}
			} else {
				insert = false;
			}
			// now we have whether we should insert the primary key DBKey for this AbstractRelation and a Result3 with ordering indexes
			// if we skipped any indexes in result3, they should be 0
			if(insert) {
				synchronized(resultSet) {
					resultSet.put(r, m0.getIdentity());
				}
			}
			//if(DEBUG)
			//	System.out.printf("FindSetUtil.createResultSet %d %d %d %s %b%n",insd,insm,insr,r,insert);
    }
    
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each {@link AbstractRelation} component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRange(DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		if(DEBUG) {
    			System.out.println("getMorphismRange tailMap from:"+dkeyLo+" "+mkeyLo+" "+rkeyLo+" to:"+dkeyHi+" "+mkeyHi+" "+rkeyHi);
    			//"\r\nDomain Array:\r\n"+Arrays.toString(dkey.toArray())+"\r\nMap array\r\n"+Arrays.toString(mkey.toArray())+"\r\nRange array\r\n"+Arrays.toString(rkey.toArray()));
    			/*
    			System.out.println("Domain array:");
    			for(DBKey d: dkey) {
    				System.out.println(RelatrixKV.get(d));
    			}
    			System.out.println("Map array:");
    			for(DBKey m: mkey) {
    				System.out.println(RelatrixKV.get(m));
    			}
    			System.out.println("Range array:");
    			for(DBKey r: rkey) {
    				System.out.println(RelatrixKV.get(r));
    			}
    			*/
    		}
    		AbstractRelation xdmr = (AbstractRelation) new Relation(true, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		// stream of DBKeys in AbstractRelation relation
    		RelatrixKV.findTailMapKVStream(xdmr)/*RelatrixKV.entrySetStream(xdmr.getClass())*/.forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
    			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
    			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
			throw new IOException(e);
		}
    }
     
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each {@link AbstractRelation} component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param alias
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRange(Alias alias, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, alias, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKV.findTailMapKVStream(alias,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
     			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
      			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchElementException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap with the Relation morphisms in the range of the DBKey low and hi ranges provided.
     * If we find the 3 morphism keys in the arrays of domain, map, and range keys we built, they are eligible for the final post-order set.
	 * <p>
	 * The low range AbstractRelation template is formed from the 3 low keys. The order is created by using the
	 * ordered positions in the 3 domain, map and range key arrays based on indexOf each {@link AbstractRelation} component
	 * retrieved from the given range in each of the 3 arrays, formed into a Result3, and used as key in the TreeMap as
	 * each morphism in range is streamed to the createResultSet method.
     * @param xid
     * @param dkeyLo
     * @param mkeyLo
     * @param rkeyLo
     * @param dkeyHi
     * @param mkeyHi
     * @param rkeyHi
     * @param dkey
     * @param mkey
     * @param rkey
     * @param resultSet
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(TransactionId xid, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, xid, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKVTransaction.findTailMapKVStream(xid,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
      			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setTransactionId(xid);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
     			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException e) {
			throw new IOException(e);
		}
    }
    /**
     * Populate the TreeMap param with DBKeys ordered by indexes in dkey, mkey and rkey from the range of Morphisms
     * designated by xdmr bound . The order is created by using the
     * ordered positions in the 3 domain, map and range key arrays based on indexOf each {@link AbstractRelation} component
     * retrieved from the given range in each of the 3 arrays formed into a Result3 used as key in the TreeMap.
     * @param alias the database alias
     * @param xid the transaction id
     * @param xdmr bound for AbstractRelation search
     * @param dkey ArrayList of domain keys in order based on endargs from findSet
     * @param mkey ArrayList of map key in order based on endargs from findSet
     * @param rkey ArrayList of range keys in order based on endargs from findSet
     * @param resultSet TreeMap to be populated with AbstractRelation primary key DBKeys ordered by Result3 of indexOf in dkey, mkey, and rkey arrays
     * @throws IOException
     */
    public static void getMorphismRangeTransaction(Alias alias, TransactionId xid, DBKey dkeyLo, DBKey mkeyLo, DBKey rkeyLo, DBKey dkeyHi, DBKey mkeyHi, DBKey rkeyHi, ArrayList<DBKey> dkey, ArrayList<DBKey> mkey, ArrayList<DBKey> rkey, TreeMap<Result,DBKey> resultSet) throws IOException {
    	try {
    		// stream of DBKeys in AbstractRelation relation
       		AbstractRelation xdmr = (AbstractRelation) new Relation(true, alias, xid, null, dkeyLo, null, mkeyLo, null, rkeyLo);
    		RelatrixKVTransaction.findTailMapKVStream(alias,xid,xdmr).forEach(e ->{
    			Map.Entry<AbstractRelation,DBKey> m = (Map.Entry<AbstractRelation,DBKey>)e;
      			AbstractRelation m0 = m.getKey();
    			m0.setIdentity(m.getValue());
    			m0.setAlias(alias);
    			m0.setTransactionId(xid);
       			if(m0.getDomainKey().compareTo(dkeyHi) > 0 && m0.getMapKey().compareTo(mkeyHi) > 0 && m0.getRangeKey().compareTo(rkeyHi) > 0)
    				return;
     			createResultSet(m0, dkey, mkey, rkey, resultSet);
    		});
		} catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchElementException e) {
			throw new IOException(e);
		}
    }
       
    /**
     * Create the result set from a query buffer.
     * @param buffer The {@link AbstractRelation} buffer holding the result set precursors.
     * @return the next location to retrieve or null, the only time its null is when we exhaust the buffered tuples
     * @throws IOException 
     * @throws IllegalAccessException 
     */
    public static Result setResult(AbstractRelation buffer) throws IllegalAccessException, IOException {
    	//synchronized(mutex) {
		Result1 r = new Result1();
		r.set(buffer);
		return r;
    }
    
	public static class Result2 extends Result1 implements Comparable, Serializable, Cloneable{
		private static final long serialVersionUID = 3809564271332319041L;
		protected Comparable two;	
		public Result2() {}	
		public Result2(Result2 r) {
			super(r);
			this.two = r.two;
		}	
		@Override
		public Comparable get(int res) {
			switch(res) {
				case 0:
					return one;
				case 1:
					return two;
				default:
					return two;
			}
		}		
		@Override
		public Comparable get() {
			return two;
		}	
		@Override
		public void set(int res, Comparable elem) {
			switch(res) {
				case 0:
					this.one = elem;
					break;
				case 1:
				default:
					this.two = elem;
					break;
			}
		}     
		@Override
		public Comparable[] toArray() {
			return new Comparable[] {one,two};
		}	
		@Override
		public int length() {
			return 2;
		}	
		@Override
		public Object clone() {
			return new Result2(this);
		}	
		@Override
		public void packForTransport() {
			if(one instanceof AbstractRelation)
				one = createTransport((Relation) ((AbstractRelation) one).asRelation());
			if(two instanceof AbstractRelation)
				two = createTransport((Relation) ((AbstractRelation) two).asRelation());
		}
		@Override
		public void unpackFromTransport() {
			if(one != null && one.getClass() == TransportMorphism.class)
				one = createRelation((TransportMorphism)one);	
			if(two != null && two.getClass() == TransportMorphism.class)
				two = createRelation((TransportMorphism)two);	
		}	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(two);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!super.equals(obj)) {
				return false;
			}
			if (!(obj instanceof Result2)) {
				return false;
			}
			Result2 other = (Result2) obj;
			return fullEquals(two, other.two);
		}
		@Override
		public int compareTo(Object o) {
			int n = super.compareTo(o);
			if(n != 0)
				return n;
			return fullCompareTo(two, ((Result2)o).two);
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append(one);
			builder.append(", ");
			builder.append(two);
			builder.append("]");
			return builder.toString();
		}	
	}
	public static class Result3 extends Result2 implements Cloneable, Comparable, Serializable {
		private static final long serialVersionUID = -8927948682023792282L;
		private Comparable three;
		public Result3() {}	
		public Result3(Result3 r) {
			super(r);
			this.three = r.three;
		}	
		@Override
		public Comparable get(int res) {
			switch(res) {
				case 0:
					return one;
				case 1:
					return two;
				case 2:
					return three;
				default:
					return three;
			}
		}	
		@Override
		public Comparable get() {
			return three;
		}	
		@Override
		public void set(int res, Comparable elem) {
			switch(res) {
				case 0:
					this.one = elem;
					break;
				case 1:
					this.two = elem;
					break;
				case 2:
				default:
					this.three = elem;
					break;
			}
		}
		@Override
		public Comparable[] toArray() {
			return new Comparable[] {one,two,three};
		}	
		@Override
		public int length() {
			return 3;
		}	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(three);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!super.equals(obj)) {
				return false;
			}
			if (!(obj instanceof Result3)) {
				return false;
			}
			Result3 other = (Result3) obj;
			return fullEquals(three, other.three);
		}	
		@Override
		public int compareTo(Object o) {
			int n = super.compareTo(o);
			if(n != 0)
				return n;
			return fullCompareTo(three, ((Result3)o).three);
		}	
		@Override
		public Object clone() {
			return new Result3(this);
		}	
		@Override
		public void packForTransport() {
			if(one instanceof AbstractRelation)
				one = createTransport((Relation) ((AbstractRelation) one).asRelation());
			if(two instanceof AbstractRelation)
				two = createTransport((Relation) ((AbstractRelation) two).asRelation());	
			if(three instanceof AbstractRelation)
				three = createTransport((Relation) ((AbstractRelation) three).asRelation());	
		}	
		@Override
		public void unpackFromTransport() {
			if(one != null && one.getClass() == TransportMorphism.class)
				one = createRelation((TransportMorphism)one);
			if(two != null && two.getClass() == TransportMorphism.class)
				two = createRelation((TransportMorphism)two);
			if(three != null && three.getClass() == TransportMorphism.class)
				three = createRelation((TransportMorphism)three);
		}		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append(one);
			builder.append(", ");
			builder.append(two);
			builder.append(", ");
			builder.append(three);
			builder.append("]");
			return builder.toString();
		}
	}
}
