package com.neocoretechs.relatrix;
/**
 * This exception is generates when an attempt is made to store a Morphism with a duplicate domain and map key.
 * Since the range depends on the result of the domain passing thru the map function, the domain and map are the primary
 * keys.
 * @author jg
 *
 */
public class DuplicateKeyException extends Exception {
	public DuplicateKeyException(Comparable domain, Comparable map) { super("Duplicate key:"+domain+","+map); }

	public DuplicateKeyException(Comparable<?> key) { super("Duplicate key:"+key);}
}
