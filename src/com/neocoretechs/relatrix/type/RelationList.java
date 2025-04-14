package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.Relation;
import com.neocoretechs.relatrix.TransportMorphism;
import com.neocoretechs.relatrix.TransportMorphismInterface;

public class RelationList implements Serializable, List<Comparable>, TransportMorphismInterface {
	private static final long serialVersionUID = -8973345814107305867L;
	private ArrayList<Comparable> list = new ArrayList<Comparable>();
	
	public RelationList(List list) {
		this.list.addAll((Collection) list);
	}
	
	public RelationList(Relation[] list) {
		this.list.addAll(Arrays.asList(list));
	}
	
	public RelationList(Comparable[] list) {
		for(int i = 0;  i < list.length; i++)
			this.list.add(list[i]);
	}
	
	public RelationList() {}

	@Override
	public void packForTransport() {
		this.list.replaceAll(e -> createTransport((Relation)e));	
	}

	@Override
	public void unpackFromTransport() {
		this.list.replaceAll(e -> createRelation((TransportMorphism) e));	
	}

	@Override
	public TransportMorphism createTransport(Relation o) {
		return TransportMorphism.createTransport(o);
	}

	@Override
	public Relation createRelation(TransportMorphism o) {
		return TransportMorphism.createMorphism(o);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<Comparable> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(Comparable e) {
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Comparable> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Comparable> c) {
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
		
	}

	@Override
	public Comparable get(int index) {
		return list.get(index);
	}

	@Override
	public Comparable set(int index, Comparable element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, Comparable element) {
		list.add(index, element);
		
	}

	@Override
	public Comparable remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Comparable> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Comparable> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<Comparable> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
