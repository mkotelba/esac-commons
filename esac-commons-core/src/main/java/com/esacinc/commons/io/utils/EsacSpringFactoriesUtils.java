package com.esacinc.commons.io.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

public final class EsacSpringFactoriesUtils {
    private EsacSpringFactoriesUtils() {
    }

    public static <T> T loadFactory(Supplier<T> defaultFactorySupplier, Class<T> factoryClass, ClassLoader classLoader, Object ... args) {
        List<T> factories = loadFactories(factoryClass, classLoader, args);

        return (factories.isEmpty() ? defaultFactorySupplier.get() : factories.get(0));
    }

    public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader, Object ... args) {
        List<Class<? extends T>> factoryClasses = loadFactoryClasses(factoryClass, classLoader);

        if (factoryClasses.isEmpty()) {
            return Collections.emptyList();
        }

        int numFactories = factoryClasses.size();
        List<T> factories = new ArrayList<T>(numFactories);

        for (Class<? extends T> factoryClassItem : factoryClasses) {
            factories.add(instantiateFactory(factoryClass, factoryClassItem, args));
        }

        if (numFactories > 1) {
            factories.sort(AnnotationAwareOrderComparator.INSTANCE);
        }

        return factories;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> List<Class<? extends T>> loadFactoryClasses(Class<T> factoryClass, ClassLoader classLoader) {
        List<String> factoryClassNames = loadFactoryClassNames(factoryClass, classLoader);

        if (factoryClassNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<Class<? extends T>> factoryClasses = new ArrayList<>(factoryClassNames.size());
        Class<?> factoryClassItem;

        for (String factoryClassName : factoryClassNames) {
            try {
                factoryClassItem = Class.forName(factoryClassName, true, classLoader);
            } catch (ClassNotFoundException e) {
                throw new FatalBeanException(String.format("Spring factory (class=%s) item class (%s) not found.", factoryClass, factoryClassName), e);
            }

            if (!factoryClass.isAssignableFrom(factoryClassItem)) {
                throw new FatalBeanException(String.format("Spring factory (class=%s) item class (%s) is not assignable.", factoryClass, factoryClassItem));
            }

            factoryClasses.add(((Class<? extends T>) factoryClassItem));
        }

        return factoryClasses;
    }

    public static List<String> loadFactoryClassNames(Class<?> factoryClass, ClassLoader classLoader) {
        return SpringFactoriesLoader.loadFactoryNames(factoryClass, classLoader);
    }

    private static <T> T instantiateFactory(Class<T> factoryClass, Class<? extends T> factoryClassItem, Object ... args) {
        try {
            return ConstructorUtils.invokeConstructor(factoryClassItem, args);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new FatalBeanException(String.format("Unable to instantiate Spring factory (class=%s) item (class=%s).", factoryClass, factoryClassItem), e);
        }
    }
}
