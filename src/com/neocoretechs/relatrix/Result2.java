package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.Objects;
/**
* Some of the queries that can produce a Result2 instance include:<br/>
* (Keep in mind that any object participating in a relationship can itself be a relationship.)<p/>
* findSet('?','?','*') - iterator or stream of all domain and map components of all relationships (instances of DomainMapRange relationship objects) <br/>
* findSet(object,'?','?') - iterator or stream of all map and range components of all relationship objects with given domain object <br/>
* findSet('?','?',object)- iterator or stream of all domain and map objects of relationships with range of the indicated objects <br/>
* findSet('?','?','*')- iterator or stream of all domain and map objects of all relationships<br/>
* findSet('*','?','?')- iterator or stream of all map and range objects of all relationships <br/>
* findSet('?',object,'?')- iterator or stream of all domain and range objects of relationships containing given map object<br/>
* findSet('?','*','?')- iterator or stream of all domain and range objects of all relationships<br/>
* @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2024
*
*/
public class Result2 extends Result1 implements Comparable, Serializable, Cloneable{
	private static final long serialVersionUID = 3809564271332319041L;
	protected Comparable two;
	
	public Result2() {}
	
	public Result2(Comparable one, Comparable two) {
		super(one);
		this.two = two;
	}
	
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
	public void set(Comparable elem) {
		this.two = elem;
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
		builder.append("[").append(one).append(", ").append(two).append("]");
		return builder.toString();
	}
	

}
