package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.Objects;
/**
* The query that can produce a Result3 instance:<br/>
* (Keep in mind that any object participating in a relationship can itself be a relationship.)<p/>
* findSet('?','?','?') - iterator or stream of all domain, map, and range components of all relationships (instances of Relation relationship objects, AKA Identity objects) <br/>
* Thats it! See these other results for further options for set retrieval: {@link Result1} {@link Result2}
* @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2024
*
*/
public class Result3 extends Result2 implements Cloneable, Comparable, Serializable {
	private static final long serialVersionUID = -8927948682023792282L;
	private Comparable three;

	public Result3() {}
	
	public Result3(Comparable one, Comparable two, Comparable three) {
		super(one, two);
		this.three = three;
	}
	
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
	public void set(Comparable elem) {
		this.three = elem;
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
	public void rigForTransport() {
		if(one instanceof AbstractRelation)
			one = TransportMorphism.createTransport((AbstractRelation) one);
		if(two instanceof AbstractRelation)
			two = TransportMorphism.createTransport((AbstractRelation) two);
		if(three instanceof AbstractRelation)
			three = TransportMorphism.createTransport((AbstractRelation) three);
	}
	
	@Override
	public void unpackFromTransport() {
		if(one != null && one.getClass() == TransportMorphism.class)
			one = TransportMorphism.createMorphism((TransportMorphism)one);
		if(two != null && two.getClass() == TransportMorphism.class)
			two = TransportMorphism.createMorphism((TransportMorphism)two);
		if(three != null && three.getClass() == TransportMorphism.class)
			three = TransportMorphism.createMorphism((TransportMorphism)three);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(one).append(", ").append(two).append(", ").append(three).append("]");
		return builder.toString();
	}

}
