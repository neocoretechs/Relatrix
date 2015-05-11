package com.neocoretechs.relatrix.typedlambda;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * I suppose the best way to describe this package is 'down conversion wrappers' to support the typed lambda calculus.
 * To fit in our framework, classes have to implement Comparable, period.. 
 * Some classes, though, are not so endowed, like java.lang.Class.
 * So to deal with any templating based on class, or indeed to use any object without a Comparable implementation,
 * We form a template class using only what we have; java.lang.Class. 
 * How to order retrieval based on this? Classes that have a MIN_VALUE field al la
 * Integer, Long, etc, provide it natively. Option is to supply via constructor.
 * Like java lets you assign a componentized Comparator interface for TreeMap and TreeSet. 
 * Since we want to use this via a template Class type, but java.lang.Class has
 * no Comparator, we need to wrap it (Class is final, so we cant subclass) 
 * In the end, when we store this, its identity represents a global relation over the class of the elements in the morphism.
 * And hence, an identity to allow it to fit in the categorical framework.
 * At any rate, the down conversion refers to the process of adding a minimum value to create a pseudo concrete
 * class from the template so we can order our retrieval. 
 * @author jg
 *
 */
@SuppressWarnings("rawtypes")
public class TemplateClass implements Serializable, Comparable{
	private static final long serialVersionUID = 5259801291637024619L;
	private Class<?> theClass;
	private Object minVal = null; // minimum value for the class for search
	private Comparator userCompare = null;
	public TemplateClass() {}
	public TemplateClass(Class<?> theClass) {
		this.theClass = theClass;
	}
	/**
	 * User supplies minimum value for class
	 * @param theClass
	 * @param minVal
	 */
	public TemplateClass(Class<?> theClass, Object minVal) {
		if( minVal instanceof Comparator ) {
			this.userCompare = (Comparator) minVal;
		} else {
			this.minVal = minVal;
		}
		this.theClass = theClass;
	}
	public Class<?> getComparableClass() { return theClass; }
	
	public char getRetrievalPredicate() throws IOException {
		throw new IOException("Must use a subclass of TemplateClass for retrieval operation");
	}
	
	public String toString() {
		return getClass().getName()+":"+theClass.getName()+">"+getMinimumValue();
	}
	/**
	 * Get the minimum value for the enclosed class. this is so we can determine proper
	 * sort order for wildcards in the event they are unequal to target. Attempt to 
	 * extract it from various attributes and fields of the class although we have no
	 * instance to invoke.
	 * @return The object representing the minimum value for this class
	 */
	public Object getMinimumValue() {
		if( minVal != null ) {
			return minVal;
		}
		Field field = null;
		try {
			field = theClass.getField("MIN_VALUE");
		} catch (NoSuchFieldException | SecurityException e) {
			if( theClass.equals(String.class)) {
				minVal = "";
				return minVal; // ubiquitous String class min val, if we happen to be dealing with strings
			}
			System.out.println("Cant find a min val for class :"+theClass);
			return null;
		}
		try {
			minVal = field.get(null);// must be static, we have no instances to play with here
			return minVal;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.out.println("Cant find a min val for class :"+theClass);
			return null;
		} 
	}
	@Override
	public boolean equals(Object arg0) {
		if( userCompare == null )
			return theClass.getName().equals( ((TemplateClass)arg0).getComparableClass().getName() );
		return userCompare.compare(theClass, arg0)==0;
	}
	@Override
	public int compareTo(Object arg0) {
		if( userCompare == null )
			return theClass.getName().compareTo( ((TemplateClass)arg0).getComparableClass().getName() );
		return userCompare.compare(theClass,  arg0);
	}
}
