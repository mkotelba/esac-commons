package com.esacinc.commons.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.util.ClassUtils;

public final class EsacClassUtils {
    public final static Function<?, String> AS_CLASS_NAME_FUNC = obj -> obj.getClass().getName();

    private EsacClassUtils() {
    }

    public static <T> Stream<Class<? extends T>> streamClasses(Stream<String> classNames) throws ClassNotFoundException {
        return streamClasses(ClassUtils.getDefaultClassLoader(), classNames);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> Stream<Class<? extends T>> streamClasses(ClassLoader classLoader, Stream<String> classNames) throws ClassNotFoundException {
        Builder<Class<? extends T>> classesBuilder = Stream.builder();

        for (String className : IteratorUtils.asIterable(classNames.iterator())) {
            classesBuilder.add(((Class<? extends T>) Class.forName(className, true, classLoader)));
        }

        return classesBuilder.build();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> Function<T, String> asClassName() {
        return ((Function<T, String>) AS_CLASS_NAME_FUNC);
    }
}
