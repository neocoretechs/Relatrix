package com.neocoretechs.relatrix;

import java.io.Serializable;
/**
* Top level abstract class of hierarchy returned by iterators or streams of retrieval of Morphisms from the Relatrix.<p/>
* Depending on the type of set retrieval, a class hierarchy consisting of Result can consist of {@link Result1}, {@link Result2} or {@link Result3}.
* Variations of 'get' methods can be used retrieve the elements from the hierarchy. A total order is imposed consisting of
* Result1, Result2, and Result3. For instance, if we call findSet("?","*","?") we would receive iterators or streams of Result2 since
* we have specified 2 return elements from the retrieval as denoted by the ? directive. For retrievals of identity elements such as
* findSet("*","*","*") or findSet(object, object, object) or findSet ("*",object, object) we get an instance of Result1 with the
* identity element. Should we request findSet("?",object,"?") or findSet ("?","?","*") we would get an instance of Result2.
* A Result3 is returned when we ask for findSet("?","?","?") exclusively.<p/>
* Keep in mind that any object participating in a relationship can itself be a relationship.<p/>
* Some of the queries that can produce a Result1 instance include:<br/>
* findSet("*","*","*") - iterator or stream of all identities (instances of DomainMapRange relationship objects) <br/>
* findSet(object,object,object) - iterator or stream of a single identity DomainMapRange object composed of the 3 object instances<br/>
* findSet("*",object,object)- iterator or stream of all identities (relationship objects) containing map and range of the indicated objects <br/>
* findSet("*","*",object)- iterator or stream of all identities (relationship objects) containing range of the indicated object <br/>
* findSet("*",object,"*")- iterator or stream of all identities (relationship objects) containing map of the indicated object <br/>
* findSet(object,"*","*")- iterator or stream of all identities (relationship objects) containing domain of the indicated object <br/>
* findSet(object,object,"*")- iterator or stream of all identities (relationship objects) containing domain and map of the indicated objects <br/>
* findSet(object,"*",object)- iterator or stream of all identities (relationship objects) containing domain and range of the indicated objects <br/>
* findSet("?","*","*")- iterator or stream of all domain objects (Comparable object component of relationship) of all relationships<br/>
* findSet("*","*","?")- iterator or stream of all range objects (Comparable object component of relationship) of all relationships<br/>
* findSet("*","?","*")- iterator or stream of all map objects (Comparable object component of relationship) of all relationships<br/>
* findSet("?",object,object)- iterator or stream of all domain objects (Comparable object component of relationship) of relationships with given map and range objects<br/>
* findSet("*","?",object)- iterator or stream of all map objects (Comparable object component of relationship) of relationships with given range object<br/>
* findSet("*",object,"?")- iterator or stream of all range objects (Comparable object component of relationship) of relationships with given map object<br/>
* findSet(object,"?","*")- iterator or stream of all map objects (Comparable object component of relationship) of relationships with given domain object<br/>
* findSet(object,"*","?")- iterator or stream of all range objects (Comparable object component of relationship) of relationships with given domain object<br/>
* findSet(object,object,"?")- iterator or stream of all range objects (Comparable object component of relationship) of relationships with given domain and map objects<br/>
* findSet(object,"?",object)- iterator or stream of all map objects (Comparable object component of relationship) of relationships with given domain and range objects<br/>
* @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2024
*
*/
public class Result1 extends Result implements Comparable, Serializable, Cloneable{
	private static final long serialVersionUID = 3809564271332319041L;

	public Result1() {}
	
	public Result1(Comparable r) {
		super(r);
	}
	
	public Result1(Result r) {
		super(r);	
	}

	@Override
	public Comparable get(int res) {
		switch(res) {
			case 0:
				return one;
			default:
				return one;
		}
	}
	
	@Override
	public Comparable get() {
		return one;
	}
	
	@Override
	public void set(int res, Comparable elem) {
		switch(res) {
			case 0:
			default:
				this.one = elem;
				break;
		}
	}

	@Override
	public void set(Comparable elem) {
		this.one = elem;
	}

	@Override
	public Comparable[] toArray() {
		return new Comparable[] {one};
	}

	@Override
	public int length() {
		return 1;
	}
	
	@Override
	public Object clone() {
		return new Result1(this);
	}

	@Override
	public int compareTo(Object o) {
		return fullCompareTo(one, (Result1)o);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(one).append("]");
		return builder.toString();
	}

}
