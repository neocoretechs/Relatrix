package com.neocoretechs.relatrix;

import java.io.Serializable;
/**
* Top level abstract class of hierarchy returned by iterators or streams of retrieval of relationships from the Relatrix.<p>
* Depending on the type of set retrieval, a class hierarchy consisting of Result can consist of {@link Result}, For retrievals of identity elements such as
* findSet('*','*','*') or<br> findSet(object, object, object) or<br> findSet ('*',object, object)<br> we get an instance of Result with the
* identity element.<br> 
* Keep in mind that any object participating in a relationship can itself be a relationship.<p>
* The queries that can produce a Result instance include:<br>
* findSet('*','*','*') - iterator or stream of all identities (instances of Relation relationship objects) <br>
* findSet(object,object,object) - iterator or stream of a single identity Relation object composed of the 3 object instances<br>
* findSet('*',object,object)- iterator or stream of all identities (relationship objects) containing map and range of the indicated objects <br>
* findSet('*','*',object)- iterator or stream of all identities (relationship objects) containing range of the indicated object <br>
* findSet('*',object,'*')- iterator or stream of all identities (relationship objects) containing map of the indicated object <br>
* findSet(object,'*','*')- iterator or stream of all identities (relationship objects) containing domain of the indicated object <br>
* findSet(object,object,'*')- iterator or stream of all identities (relationship objects) containing domain and map of the indicated objects <br>
* findSet(object,'*',object)- iterator or stream of all identities (relationship objects) containing domain and range of the indicated objects <br>
* @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2026
*
*/
public class Result1 extends Result implements Comparable, Serializable, Cloneable{
	private static final long serialVersionUID = 3809564271332319041L;

	public Result1() {}
	
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
	public void packForTransport() {
		if(one instanceof AbstractRelation)
			one = createTransport((Relation) ((AbstractRelation) one).asRelation());	
	}
	
	@Override
	public void unpackFromTransport() {
		if(one != null && one.getClass() == TransportMorphism.class)
			one = createRelation((TransportMorphism)one);	
	}
	
	@Override
	public TransportMorphism createTransport(Relation ar) {
		return TransportMorphism.createTransport(ar);
	}
	
	@Override
	public Relation createRelation(TransportMorphism tm) {
		return TransportMorphism.createMorphism(tm);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(one);
		builder.append("]");
		return builder.toString();
	}

}
