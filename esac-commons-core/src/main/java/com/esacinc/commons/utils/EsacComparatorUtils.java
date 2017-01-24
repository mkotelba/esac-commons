package com.esacinc.commons.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.ImmutablePair;

public final class EsacComparatorUtils {
    public static class MapOrderedComparator<T> implements Comparator<T> {
        private Map<T, Integer> order;

        public MapOrderedComparator(Map<T, Integer> order) {
            this.order = order;
        }

        @Override
        public int compare(T obj1, T obj2) {
            int objIndex1 = this.order.getOrDefault(obj1, -1), objIndex2 = this.order.getOrDefault(obj2, -1);

            if (objIndex1 != -1) {
                return ((objIndex2 != -1) ? Integer.compare(objIndex1, objIndex2) : -1);
            } else if (objIndex2 != -1) {
                return 1;
            } else {
                return Objects.equals(obj1, obj2) ? 0 : 1;
            }
        }
    }

    public final static Comparator<?> UNORDERED = (obj1, obj2) -> 0;

    private EsacComparatorUtils() {
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> MapOrderedComparator<T> comparingOrder(T ... orderItems) {
        return new MapOrderedComparator<>(IntStream.range(0, orderItems.length)
            .mapToObj(orderItemIndex -> new ImmutablePair<>(orderItems[orderItemIndex], orderItemIndex)).collect(EsacStreamUtils.toMap()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> Comparator<T> unordered() {
        return ((Comparator<T>) UNORDERED);
    }
}
