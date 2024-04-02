package com.neocoretechs.relatrix.stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
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
import java.util.stream.StreamSupport;

/**
 * Stream helper such that we can retrieve the original base iterator rather than the Spliterator adapter from the Iterator method
 * @author Jonathan Groff Copy (C) NeoCoreTechs 2024
 *
 * @param <T>
 */
public class StreamHelper<T> implements Stream<T>, BaseIteratorAccessInterface {
	public static final int characteristics = Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED; 
	private Stream<T> stream;
	private Iterator<?> iterator;

	@SuppressWarnings("unchecked")
	public StreamHelper(Iterator<?> iterator) {
		this.iterator = iterator;
    	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
    	stream = (Stream<T>) StreamSupport.stream(spliterator, true);
	}
	
	@SuppressWarnings("unchecked")
	public StreamHelper(Iterator<?> iterator, boolean parallel) {
		this.iterator = iterator;
    	Spliterator<?> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
    	stream = (Stream<T>) StreamSupport.stream(spliterator, parallel);
	}
	
	@Override
	public Iterator<?> getBaseIterator() {
		return iterator;
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

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return stream.reduce(identity, accumulator, combiner);
	}
}