package com.neocoretechs.relatrix.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public final class SpliteratorsUtil {
    public static <T> Spliterator<T> spliteratorFromIteratorWithBatching(Iterator<T> it, int batchSize) {
        Objects.requireNonNull(it);
        if (batchSize <= 0) throw new IllegalArgumentException("batchSize > 0");

        return new Spliterator<T>() {
            // unknown size; could be estimated if you know it
            @Override public boolean tryAdvance(Consumer<? super T> action) {
                synchronized (it) {
                    if (!it.hasNext()) return false;
                    action.accept(it.next());
                    return true;
                }
            }

            @Override public Spliterator<T> trySplit() {
                List<T> batch = new ArrayList<>(batchSize);
                synchronized (it) {
                    for (int i = 0; i < batchSize && it.hasNext(); i++) {
                        batch.add(it.next());
                    }
                }
                return batch.isEmpty() ? null : Spliterators.spliterator(batch, Spliterator.ORDERED | Spliterator.NONNULL);
            }

            @Override public long estimateSize() { return Long.MAX_VALUE; }
            @Override public int characteristics() { return Spliterator.ORDERED | Spliterator.NONNULL; }
        };
    }
}
