package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.Objects;
/**
 * Top level abstract class of hierarchy returned by iterators or streams of retrieval of Morphisms from the Relatrix.<p/>
 * Depending on the type of set retrieval, a class hierarchy consisting of Result can consist of {@link Result1}, {@link Result2} or {@link Result3}.
 * Variations of 'get' methods can be used retrieve the elements from the hierarchy. A total order is imposed consisting of
 * Result1, Result2, and Result3. For instance, if we call<br/> findSet('?','*','?') we would receive iterators or streams of Result2 since
 * we have specified 2 return elements from the retrieval as denoted by the ? directive.<br/> For retrievals of identity elements such as
 * <br/>findSet('*','*','*') or <br/>findSet(object, object, object) or <br/>findSet('*',object, object) we get an instance of Result1 with the
 * identity element. Should we request <br/>findSet('?',object,'?') or <br/>findSet('?','?','*')<br/> we would get an instance of Result2.
 * A Result3 is returned when we ask for <br/>findSet('?','?','?') exclusively.<br/> Keep in mind that any object participating in a relationship
 * can itself be a relationship.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public abstract class Result implements Comparable, Serializable, Cloneable {
	private static final long serialVersionUID = -3876100246517492961L;
	private static boolean DEBUG;
	public static boolean STRICT_SCHEMA = false; // if true, enforce type-based comparison on first element inserted, else can mix types with string basis for incompatible class types
	public static boolean ENFORCE_TYPE_CHECK = true; // if true, enforces type compatibility in relationships, if false, user must supply compareTo that spans all types used. STRICT_SCHEMA ignored
	
	protected Comparable one;
	
	public Result() {}
	
	public Result(Comparable one) {
		this.one = one;
	}
	public Result(Result r) {
		one = r.one;
	}
    /**
     * If true, enforces type checking for components of relationships. If classes are incompatible,
     * an attempt is made to use a string representation as a default method of providing ordering. If false, STRICT_SCHEMA ignored.
     * If false, User is responsible for providing compareTo in every class used in relationships that is compatible
     * with every other class used in relationships for a given database. Default is true.
     * @param bypass
     */
    public static void enforceTypeCheck(boolean enforce) {
    	ENFORCE_TYPE_CHECK = enforce;
    }
    /**
     * If true, schema if restricted to first relationship type inserted, which means sort
     * ordering and comparison is based on the domain, map, and range elements remaining of consistent types
     * which also restricts using relationships as components of other relationships. Default is false.
     * @param strict
     */
    public static void enforceStrictSchema(boolean strict) {
    	STRICT_SCHEMA = strict;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(one);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Result)) {
			return false;
		}
		Result other = (Result) obj;
		return fullEquals(one, other.one);
	}
	
	@Override
	public int compareTo(Object obj) {
		if (this == obj) {
			return 0;
		}
		Result other = (Result) obj;
		return fullCompareTo(one, other.one);
	}
	
	@Override
	public abstract Object clone(); 
	
	public abstract Comparable get(int res);
	public abstract Comparable get();
	public abstract void set(int res, Comparable elem);
	public abstract void set(Comparable elem);
	public abstract Comparable[] toArray();
	public abstract int length();
	
    /**
     * Failsafe compareTo.
     * If classes are not the same and the target is not assignable from the source, that is, not a subclass, toss an error
     * If none of the above conditions apply, the default is to perform a straight up 'compareTo' as we all implement Comparable.
     * 
     * @param from DBKey wrapping instance If null, this element 'less than' to element, for template use
     * @param to DBKey wrapping instance If null, throw an error, as should never be null
     * @return result of compareTo unless classes differ, then result of string-based compareTo as we need some form of default comparison
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static int fullCompareTo(Comparable from, Comparable to) {
    	//if( to == null )
    		//throw new RuntimeException("Result.fullCompareTo 'to' element is null, from is "+from);
    	//if(from == null)
    		//return -1;
       	if(to == null)
    		if(from == null)
    			return 0;
    		else
    			return 1;
    	if(from == null)
    		return -1;
     	// now see if the classes are compatible for comparison, if not, convert them to strings and compare
    	// the string representations as a failsafe.
    	if(ENFORCE_TYPE_CHECK) {
    		Class toClass = to.getClass();
    		if( !from.getClass().equals(toClass) && !toClass.isAssignableFrom(from.getClass())) {
    			if(STRICT_SCHEMA)
    				throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
    						" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
    			else
    				//compare a universal string representation as a unifying datatype for typed class templates
    				return from.toString().compareTo(to.toString());
    		}
    	}
    	// Otherwise, use the standard compareTo for all objects which invokes our indicies
    	// DBKey comapreTo handles resolution of key to instance
    	return from.compareTo(to);
    }
    /**
     * This mechanism needs refinement to allow incompatible classes in relationships to work under
     * comparison. ultimately the user may need schema-specific implementations.
     * This implementation merely compares the string representation.
     * @param from
     * @param to
     * @return
     */
    public static int partialCompareTo(Comparable from, Comparable to) {
		//if( to == null )
			//throw new RuntimeException("Result.partialCompareTo 'to' element is null, from is "+from);
		//if(from == null)
			//return -1;
     	if(to == null)
    		if(from == null)
    			return 0;
    		else
    			return 1;
    	if(from == null)
    		return -1;
		// the string representations as a failsafe
		//compare a universal string representation as a unifying datatype for typed class templates
		return from.toString().compareTo(to.toString());
    }
    /**
     * we check whether the enclosed class equals the target class.
     * If classes are not the same and the target is not assignable from the source, throw runtime exception
     * If none of the above conditions apply, perform a straight up 'equals'
     * @param from
     * @param to
     * @return
     */
    public static boolean fullEquals(Comparable<?> from, Comparable<?> to) {
      	if( DEBUG )
    		System.out.println("Result.fullEquals equals:"+(from != null ? (from.getClass().getName()+":"+from) : "NULL")+" "+(to != null ? (to.getClass().getName()+":"+to) : "NULL"));
    	if(to == null)
    		if(from == null)
    			return true;
    		else
    			return false;
    	if(from == null)
    		return false;
		if (from == to) {
			return true;
		}
       	Class<?> toClass = to.getClass();
    	// If classes are not the same try a comparison of the string representations as a unifying type
    	if( !from.getClass().equals(toClass) && !toClass.isAssignableFrom(from.getClass()) ) {
  			if(STRICT_SCHEMA)
				throw new RuntimeException("Classes are incompatible and the schema would be violated for "+from+" and "+to+
						" whose classes are "+from.getClass().getName()+" and "+to.getClass().getName());
			else
				//compare a universal string representation as a unifying datatype for typed class templates
				return from.toString().equals(to.toString());
    	}
    	return from.equals(to);
    }
    
    public static boolean partialEquals(Comparable from, Comparable to) {
       	if( DEBUG )
    		System.out.println("Result.partialEquals equals:"+(from != null ? (from.getClass().getName()+":"+from) : "NULL")+" "+(to != null ? (to.getClass().getName()+":"+to) : "NULL"));
    	if(to == null)
    		if(from == null)
    			return true;
    		else
    			return false;
 		if(from == null)
 			return false;
		if (from == to) {
			return true;
		}
 		// the string representations as a failsafe
 		//compare a universal string representation as a unifying datatype for typed class templates
 		return from.toString().equals(to.toString());
     }
    
}
