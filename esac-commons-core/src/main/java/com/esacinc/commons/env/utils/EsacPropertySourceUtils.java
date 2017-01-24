package com.esacinc.commons.env.utils;

import com.esacinc.commons.utils.EsacIteratorUtils;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

public final class EsacPropertySourceUtils {
    private EsacPropertySourceUtils() {
    }

    public static Stream<Entry<String, Object>> streamEntries(PropertySources propSrcs) {
        return streamEntries(streamSources(propSrcs));
    }

    public static Stream<Entry<String, Object>> streamEntries(Stream<PropertySource<?>> propSrcs) {
        return propSrcs.flatMap(EsacPropertySourceUtils::streamEntries);
    }

    public static Stream<Entry<String, Object>> streamEntries(PropertySource<?> procSrc) {
        if (!(procSrc instanceof EnumerablePropertySource<?>)) {
            return Stream.empty();
        }

        EnumerablePropertySource<?> enumerablePropSrc = ((EnumerablePropertySource<?>) procSrc);
        String[] propNames = enumerablePropSrc.getPropertyNames();

        return ((propNames.length > 0)
            ? ((propNames.length > 1)
                ? Stream.of(propNames).map(propName -> new ImmutablePair<>(propName, enumerablePropSrc.getProperty(propName)))
                : Stream.of(new ImmutablePair<>(propNames[0], enumerablePropSrc.getProperty(propNames[0]))))
            : Stream.empty());
    }

    public static Stream<String> streamNames(PropertySources propSrcs) {
        return streamNames(streamSources(propSrcs));
    }

    public static Stream<String> streamNames(Stream<PropertySource<?>> propSrcs) {
        return propSrcs.flatMap(EsacPropertySourceUtils::streamNames);
    }

    public static Stream<String> streamNames(PropertySource<?> procSrc) {
        if (!(procSrc instanceof EnumerablePropertySource<?>)) {
            return Stream.empty();
        }

        EnumerablePropertySource<?> enumerablePropSrc = ((EnumerablePropertySource<?>) procSrc);
        String[] propNames = enumerablePropSrc.getPropertyNames();

        return ((propNames.length > 0) ? ((propNames.length > 1) ? Stream.of(propNames) : Stream.of(propNames[0])) : Stream.empty());
    }

    public static Stream<PropertySource<?>> streamSources(PropertySources propSrcs) {
        return EsacIteratorUtils.stream(propSrcs.iterator());
    }

    public static boolean isEmpty(PropertySources propSrcs) {
        return ((propSrcs instanceof MutablePropertySources) ? (((MutablePropertySources) propSrcs).size() == 0) : !propSrcs.iterator().hasNext());
    }
}
