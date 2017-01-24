package com.esacinc.commons.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import javax.annotation.Nullable;
import org.apache.commons.lang3.text.StrBuilder;

public final class EsacMultimapUtils {
    public static class MutableMultimapView<T, U, V extends Collection<U>, W extends Multimap<T, U>> extends ForwardingMap<T, V> {
        private W delegateMap;
        private Map<T, V> delegate;

        public MutableMultimapView(W delegateMap, Map<T, V> delegate) {
            this.delegateMap = delegateMap;
            this.delegate = delegate;
        }

        @Override
        public void putAll(Map<? extends T, ? extends V> map) {
            if (map.isEmpty()) {
                return;
            }

            map.forEach(this::put);
        }

        @Nullable
        @Override
        public V put(T key, V values) {
            V prevValues = (this.delegate.containsKey(key) ? this.delegate.get(key) : null);

            this.delegateMap.putAll(key, values);

            return prevValues;
        }

        @Override
        public Map<T, V> delegate() {
            return this.delegate;
        }
    }

    public final static String VALUE_DELIM = "=[";
    public final static String VALUE_ITEM_DELIM = ", ";

    private EsacMultimapUtils() {
    }

    public static <T, U> String toString(Multimap<T, U> map) {
        if (map.isEmpty()) {
            return EsacMapUtils.EMPTY_STR;
        } else {
            StrBuilder builder = new StrBuilder();
            builder.append(EsacStringUtils.L_BRACE_CHAR);

            boolean firstKey = true, firstValue;

            for (T key : map.keySet()) {
                if (firstKey) {
                    firstKey = false;
                } else {
                    builder.append(EsacMapUtils.DELIM);
                }

                builder.append(key);
                builder.append(VALUE_DELIM);

                firstValue = true;

                for (U value : map.get(key)) {
                    if (firstValue) {
                        firstValue = false;
                    } else {
                        builder.append(VALUE_ITEM_DELIM);
                    }

                    builder.append(value);
                }

                builder.append(EsacStringUtils.R_BRACKET_CHAR);
            }

            builder.append(EsacStringUtils.R_BRACE_CHAR);

            return builder.build();
        }
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    public static <T, U, V extends Entry<? extends T, ? extends U>> Collector<V, ?, TreeMultimap<T, U>> toSortedSetMultimap() {
        return toSortedSetMultimap(() -> TreeMultimap.create(((Comparator<? super T>) Comparator.naturalOrder()), EsacComparatorUtils.unordered()));
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>, W extends SortedSetMultimap<T, U>> Collector<V, ?, W>
        toSortedSetMultimap(Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<V, T, U, SortedSet<U>, W> toMultimap(Entry::getKey, Entry::getValue, multimapSupplier);
    }

    public static <T, U, V, W extends SortedSetMultimap<U, V>> Collector<T, ?, W> toSortedSetMultimap(Function<? super T, ? extends U> keyMapper,
        Function<? super T, ? extends V> valueMapper, Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<T, U, V, SortedSet<V>, W> toMultimap(keyMapper, valueMapper, multimapSupplier);
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>> Collector<V, ?, HashMultimap<T, U>> toSetMultimap() {
        return toSetMultimap(HashMultimap::create);
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>, W extends SetMultimap<T, U>> Collector<V, ?, W>
        toSetMultimap(Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<V, T, U, Set<U>, W> toMultimap(Entry::getKey, Entry::getValue, multimapSupplier);
    }

    public static <T, U, V, W extends SetMultimap<U, V>> Collector<T, ?, W> toSetMultimap(Function<? super T, ? extends U> keyMapper,
        Function<? super T, ? extends V> valueMapper, Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<T, U, V, Set<V>, W> toMultimap(keyMapper, valueMapper, multimapSupplier);
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>> Collector<V, ?, ArrayListMultimap<T, U>> toListMultimap() {
        return toListMultimap(ArrayListMultimap::create);
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>, W extends ListMultimap<T, U>> Collector<V, ?, W>
        toListMultimap(Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<V, T, U, List<U>, W> toMultimap(Entry::getKey, Entry::getValue, multimapSupplier);
    }

    public static <T, U, V, W extends ListMultimap<U, V>> Collector<T, ?, W> toListMultimap(Function<? super T, ? extends U> keyMapper,
        Function<? super T, ? extends V> valueMapper, Supplier<W> multimapSupplier) {
        return EsacMultimapUtils.<T, U, V, List<V>, W> toMultimap(keyMapper, valueMapper, multimapSupplier);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T, U, V, W extends Collection<V>, X extends Multimap<U, V>> Collector<T, ?, X> toMultimap(Function<? super T, ? extends U> keyMapper,
        Function<? super T, ? extends V> valueMapper, Supplier<X> multimapSupplier) {
        return Collector.of(multimapSupplier, (multimap, elem) -> multimap.put(keyMapper.apply(elem), valueMapper.apply(elem)), (multimap1, multimap2) -> {
            multimap1.putAll(multimap2);

            return multimap1;
        });
    }

    public static <T, U, V extends SortedSetMultimap<T, U>> Map<T, SortedSet<U>> asMap(V multimap) {
        return EsacMultimapUtils.<T, U, SortedSet<U>, V> asMap(multimap);
    }

    public static <T, U, V extends SetMultimap<T, U>> Map<T, Set<U>> asMap(V multimap) {
        return EsacMultimapUtils.<T, U, Set<U>, V> asMap(multimap);
    }

    public static <T, U, V extends ListMultimap<T, U>> Map<T, List<U>> asMap(V multimap) {
        return EsacMultimapUtils.<T, U, List<U>, V> asMap(multimap);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T, U, V extends Collection<U>, W extends Multimap<T, U>> Map<T, V> asMap(W multimap) {
        return new MutableMultimapView<>(multimap, ((Map<T, V>) multimap.asMap()));
    }

    @Nullable
    public static <T, U, V extends ListMultimap<T, U>> U getFirst(V multimap, T key) {
        return (multimap.containsKey(key) ? multimap.get(key).get(0) : null);
    }

    @Nullable
    public static <T, U, V extends Multimap<T, U>> U getFirst(V multimap, T key) {
        return (multimap.containsKey(key) ? multimap.get(key).iterator().next() : null);
    }

    public static <T, U> void forEachSortedSet(SortedSetMultimap<T, U> multimap, BiConsumer<T, SortedSet<U>> consumer) {
        if (multimap.isEmpty()) {
            return;
        }

        multimap.keySet().forEach(key -> consumer.accept(key, multimap.get(key)));
    }

    public static <T, U> void forEachSet(SetMultimap<T, U> multimap, BiConsumer<T, Set<U>> consumer) {
        if (multimap.isEmpty()) {
            return;
        }

        multimap.keySet().forEach(key -> consumer.accept(key, multimap.get(key)));
    }

    public static <T, U> void forEachList(ListMultimap<T, U> multimap, BiConsumer<T, List<U>> consumer) {
        if (multimap.isEmpty()) {
            return;
        }

        multimap.keySet().forEach(key -> consumer.accept(key, multimap.get(key)));
    }

    public static <T, U> void forEachEntry(Multimap<T, U> multimap, BiConsumer<T, U> consumer) {
        if (multimap.isEmpty()) {
            return;
        }

        multimap.entries().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
    }

    public static boolean isEmpty(@Nullable Multimap<?, ?> multimap) {
        return ((multimap == null) || multimap.isEmpty());
    }
}
