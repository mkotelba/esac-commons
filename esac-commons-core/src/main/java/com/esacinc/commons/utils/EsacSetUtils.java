package com.esacinc.commons.utils;

import java.util.NavigableSet;

public final class EsacSetUtils {
    private EsacSetUtils() {
    }

    public <T> boolean containsHigher(NavigableSet<T> set, T item) {
        return (set.higher(item) != null);
    }

    public <T> boolean containsLower(NavigableSet<T> set, T item) {
        return (set.lower(item) != null);
    }
}
