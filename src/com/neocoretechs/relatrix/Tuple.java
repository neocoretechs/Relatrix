package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Represents a Tuple for bulk storage. The store method will extract the List and use the first 3 element array
 * of Comparable as the initial relationship, and the subsequent 2 element arrays are the map and rage of relationships with
 * initial relationship as domain. Note that we dont implement the Comparable interface, as the intent is for this to wrap
 * Comparables for operations, rather than to be stored as an instance.
 * @author Jonathan N. Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public class Tuple implements TransportMorphismInterface, Serializable {
	private static final long serialVersionUID = 1052848381620343834L;
	private ArrayList<Comparable[]> tuples = new ArrayList<Comparable[]>();
	private transient Relation relation = null;
	private TransportMorphism transport = null;
	/**
	 * Prepare the initial primary relation for subsequent set of tuples
	 * @param d domain
	 * @param m map
	 * @param r range
	 * Creates The list of tuples to populate with first element set to d,m,r
	 */
	public Tuple(Comparable d, Comparable m, Comparable r) {
		Comparable[] tuple = new Comparable[] {d,m,r};
		tuples.add(tuple);
	}
	
	public Tuple(Relation relation) {
		this.relation = relation;
	}
	
	@Override
	public void packForTransport() {
		if(relation != null)
			transport = createTransport(relation);
		for(Comparable[] c: tuples) {
			for(int i = 0; i < c.length; i++) {
				if(c[i] instanceof AbstractRelation)
					c[i] = createTransport((AbstractRelation)c[i]);
			}
		}
	}
	
	@Override
	public void unpackFromTransport() {
		if(transport != null)
			relation = (Relation) createRelation(transport);
		for(Comparable[] c: tuples) {
			for(int i = 0; i < c.length; i++) {
				if(c[i].getClass() == TransportMorphism.class)
					c[i] = createRelation((TransportMorphism)c[i]);
			}
		}
	}
	
	@Override
	public TransportMorphism createTransport(AbstractRelation ar) {
		return TransportMorphism.createTransport(ar);
	}
	
	@Override
	public AbstractRelation createRelation(TransportMorphism tm) {
		return TransportMorphism.createMorphism(tm);
	}
	
	/**
	 * Add the subsequent tuples to be related to tuple at element 0 of list
	 * @param m map
	 * @param r range
	 */
	public void prepareTuple(Comparable m, Comparable r) {
		Comparable[] tuple = new Comparable[] {m,r};
		tuples.add(tuple);
	}

	public ArrayList<Comparable[]> getTuples() {
		return tuples;
	}
	
	public Relation getRelation() {
		return relation;
	}
}
