package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
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
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Used by the RelatrixServer and RelatrixKVServer to produce and consume streams for remote delivery and retrieval.<p/>
 * There is no persistent contract here and no need to implement RemoteObjectInterface for a 'close' operation nor
 * extend RelatrixStatement for a 'process' operation since the entire payload is built here for delivery in one operation.<p/>
 * Unlike an iterator, a stream is atomic and requires no further calls to the server. Indeed, it must be so to follow the stream paradigm.
 * @author Jonathan Groff (C) NeoCoreTechs 2021
 *
 */
public class RemoteStream implements Stream,Serializable {
	private static final long serialVersionUID = 3064585530528835745L;
	private static boolean DEBUG = false;
	Object[] retArray;
	
	public RemoteStream() {}
	/**
	 * 
	 * @param result instance of stream to build collection that is serializable to return to client for
	 * construction of client side stream
	 */
	public RemoteStream(Object result) {
		retArray = ((Stream)result).toArray();
		if(DEBUG)
			System.out.printf("Setting return object:%s length:%d%n", (retArray != null ? retArray : "NULL"), (retArray != null ? retArray.length : 0));
	}

	public Stream<?> of() {
		return Stream.of(retArray);
	}
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Spliterator spliterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isParallel() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public BaseStream sequential() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseStream parallel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseStream unordered() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseStream onClose(Runnable closeHandler) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Stream filter(Predicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream map(Function mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IntStream mapToInt(ToIntFunction mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LongStream mapToLong(ToLongFunction mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DoubleStream mapToDouble(ToDoubleFunction mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream flatMap(Function mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IntStream flatMapToInt(Function mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LongStream flatMapToLong(Function mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DoubleStream flatMapToDouble(Function mapper) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream distinct() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream sorted() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream sorted(Comparator comparator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream peek(Consumer action) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream limit(long maxSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stream skip(long n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void forEach(Consumer action) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void forEachOrdered(Consumer action) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object[] toArray(IntFunction generator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object reduce(Object identity, BinaryOperator accumulator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional reduce(BinaryOperator accumulator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object reduce(Object identity, BiFunction accumulator, BinaryOperator combiner) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object collect(Supplier supplier, BiConsumer accumulator, BiConsumer combiner) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object collect(Collector collector) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional min(Comparator comparator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional max(Comparator comparator) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean anyMatch(Predicate predicate) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean allMatch(Predicate predicate) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean noneMatch(Predicate predicate) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Optional findFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional findAny() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
