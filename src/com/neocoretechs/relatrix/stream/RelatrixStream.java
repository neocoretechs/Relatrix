package com.neocoretechs.relatrix.stream;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.rocksack.Alias;
import com.neocoretechs.relatrix.RelatrixKV;
import com.neocoretechs.relatrix.Result;
import com.neocoretechs.relatrix.Result1;
import com.neocoretechs.relatrix.Result2;
import com.neocoretechs.relatrix.Result3;
import com.neocoretechs.relatrix.iterator.RelatrixIterator;

/**
 * Implementation of the standard Stream interface which operates on Morphisms formed into a template.<p/>
 * to set the lower bound of the correct range search for the properly ordered set of AbstractRelation subclasses;
 * The N return tuple '?' elements of the query. If its an identity morphism (instance of AbstractRelation) of three keys (as in the *,*,* query)
 * then N = 1 for returned Comparable elements in next(), since 1 full tuple element at an iteration is returned, 
 * that being the identity morphism.<p/>
 * For tuples the array size is relative to the '?' query predicates. <br/>
 * Stated again, The critical element about retrieving relationships is to remember that the number of elements from each
 * RelatrixStream is dependent on the number of "?" operators in a 'findSet'. For example,
 * if we declare findHeadSetStream("*","?","*") we get back a  {@link Result} of one element. For findSetStream("?",object,"?") we
 * would get back a  {@link Result2}, with each element of the array containing the relationship returned.<br/>
 * findSetStream("*","*","*") = {@link Result1} containing identity in [0] of instance Relation<br/>
 * findSetStream("*","*",object) =  {@link Result1} identity in [0] of RangeDomainMap where 'object' is range<br/>
 * findSetStream("*",object,object) =  {@link Result1} identity in [0] of MapRangeDomain matching the 2 concrete objects<br/>
 * findSetStream(object,object,object) =  {@link Result1} identity in [0] of Relation matching 3 objects<br/>
 * and the findHeadSeStreamt and findSubSetStream work the same way.<p/>
 * findSet("?","?","?") =  {@link Result3} return all, for each element in the database.<br/>
 * findSet("?","?",object) =  {@link Result2} return all domain and map objects for a given range object<br/>
 * findSet("?","*","?") =  {@link Result2} return all elements of domain and range<br/>
 * etc.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2017,2021,2024
 *
 */
public class RelatrixStream<T> implements Stream<T>, BaseIteratorAccessInterface {
	private static boolean DEBUG = false;
	protected StreamHelper<T> stream;
    protected AbstractRelation buffer = null;
    protected AbstractRelation nextit = null;
    protected AbstractRelation base;
    protected short dmr_return[] = new short[4];

    protected boolean needsIter = true;
    protected boolean identity = false;
    
    public RelatrixStream() {}
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param template the {@link AbstractRelation} template
     * @param dmr_return the template {@link AbstractRelation} from which we extract the class to obtain a stream from {@link RelatrixKV}
     * @throws IOException 
     */
    public RelatrixStream(AbstractRelation template, short[] dmr_return) throws IOException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	stream = new StreamHelper<T>(new RelatrixIterator(template, dmr_return));
    	if( DEBUG )
			System.out.println("RelatrixStream "+stream+" BASELINE:"+base);
    }
    
    public RelatrixStream(Iterator<?> setIterator) throws IOException {
    	stream = new StreamHelper<T>(setIterator);
    }
    /**
     * Pass the array we use to indicate which values to return and element 0 counter
     * @param alias database alias
     * @param template the {@link AbstractRelation} template
     * @param dmr_return the template {@link AbstractRelation} from which we extract the class to obtain a stream from {@link RelatrixKV}
     * @throws IOException 
     * @throws NoSuchElementException if alias does not exist
     */
    public RelatrixStream(Alias alias, AbstractRelation template, short[] dmr_return) throws IOException, NoSuchElementException {
    	this.dmr_return = dmr_return;
    	this.base = template;
    	identity = isIdentity(this.dmr_return);
    	stream = new StreamHelper<T>(new RelatrixIterator(alias, template, dmr_return));
    	if( DEBUG )
			System.out.println("RelatrixStream alias:"+alias+" stream:"+stream+" template:"+base);
    }
    
	@Override
	public Iterator<?> getBaseIterator() {
		return stream.getBaseIterator();
	}
	
	@Override
	public Iterator<T> iterator() {
		return stream.iterator();
	}

	@Override
	public Spliterator<T> spliterator() {
		return stream.spliterator();
	}

	@Override
	public boolean isParallel() {
		return stream.isParallel();
	}

	@Override
	public Stream<T> sequential() {
		return (Stream<T>) stream.sequential();
	}

	@Override
	public Stream<T> parallel() {
		return (Stream<T>) stream.parallel();
	}

	@Override
	public Stream<T> unordered() {
		return (Stream<T>) stream.unordered();
	}

	@Override
	public Stream<T> onClose(Runnable closeHandler) {
		return (Stream<T>) stream.onClose(closeHandler);
	}

	@Override
	public void close() {
		stream.close();	
	}

	@Override
	public Stream<T> filter(Predicate<? super T> predicate) {
		return stream.filter(predicate);
	}

	@Override
	public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {		
		return stream.map(mapper);
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return stream.mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return stream.mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return stream.mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return stream.flatMap(mapper);
	}

	@Override
	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return stream.flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return stream.flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return stream.flatMapToDouble(mapper);
	}

	@Override
	public Stream<T> distinct() {
		return stream.distinct();
	}

	@Override
	public Stream<T> sorted() {
		return stream.sorted();
	}

	@Override
	public Stream<T> sorted(Comparator<? super T> comparator) {
		return stream.sorted(comparator);
	}

	@Override
	public Stream<T> peek(Consumer<? super T> action) {
		return stream.peek(action);
	}

	@Override
	public Stream<T> limit(long maxSize) {
		return stream.limit(maxSize);
	}

	@Override
	public Stream<T> skip(long n) {
		return stream.skip(n);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		stream.forEach(action);
	}

	@Override
	public void forEachOrdered(Consumer<? super T> action) {
		stream.forEachOrdered(action);	
	}

	@Override
	public Object[] toArray() {
		return stream.toArray();
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return (A[]) stream.toArray(generator);
	}

	@Override
	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return (T) stream.reduce(accumulator);
	}

	@Override
	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return stream.reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return stream.reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return (R) stream.collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return (R) stream.collect(collector);
	}

	@Override
	public Optional<T> min(Comparator<? super T> comparator) {
		return stream.min(comparator);
	}

	@Override
	public Optional<T> max(Comparator<? super T> comparator) {
		return stream.max(comparator);
	}

	@Override
	public long count() {
		return stream.count();
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		return stream.anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		return stream.allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		return stream.noneMatch(predicate);
	}

	@Override
	public Optional<T> findFirst() {
		return stream.findFirst();
	}

	@Override
	public Optional<T> findAny() {
		return stream.findAny();
	}
	
	/**
	 * Return the number of tuple elements to be returned from specified query in each iteration
	 * @param dmr_return
	 * @return
	 */
	protected static Result getReturnTuples(short[] dmr_return) {
		short cnt = 0;
		if( isIdentity(dmr_return) ) // return all relationship types, 1 tuple special case
			return new Result1();
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) ++cnt; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		}
		switch(cnt) {
			case 1:
				return new Result1();
			case 2:
				return new Result2();
			case 3:
				return new Result3();
		}
		throw new RuntimeException("Bad parameter to getReturnTuples:"+cnt);
	}

	/**
	 * Checks to see if our dmr_return array has any return tuple ? values, which = 1
	 * If the 0 element (the iterator over the array) is -1 or all elements are either 0 or 2 (object or wildcard)
	 * then we say its an identity, and we will return a 1 element Comparable array on each iteration.
	 * @param dmr_return
	 * @return
	 */
	protected static boolean isIdentity(short[] dmr_return) {
		if( dmr_return[0] == (-1) ) return true;
		for(int i = 1; i < 4; i++) {
			if( dmr_return[i] == 1 ) return false; // 0 means object, 1 means its a return tuple ?, 2 means its a wildcard *
		}
	    return true;
	}

}
