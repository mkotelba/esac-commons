package com.esacinc.commons.utils;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class EsacIteratorUtils {
    public final static int DEFAULT_ITERATOR_SPLITERATOR_FLAGS = (Spliterator.CONCURRENT | Spliterator.ORDERED);

    private EsacIteratorUtils() {
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return stream(iterator, -1);
    }

    public static <T> Stream<T> stream(Iterator<T> iterator, long size) {
        return StreamSupport.stream(((size >= 0)
            ? Spliterators.spliterator(iterator, size, (DEFAULT_ITERATOR_SPLITERATOR_FLAGS | Spliterator.SIZED))
            : Spliterators.spliteratorUnknownSize(iterator, DEFAULT_ITERATOR_SPLITERATOR_FLAGS)), false);
    }
}
