package com.esacinc.commons.utils;

import com.esacinc.commons.beans.IdentifiedBean;
import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.beans.TypedBean;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;

public final class EsacEnumUtils {
    private EsacEnumUtils() {
    }

    @Nullable
    public static <T extends Enum<T> & IdentifiedBean> T findById(Class<T> enumClass, String id) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getId(), id));
    }

    @Nullable
    public static <T extends Enum<T> & IdentifiedBean> T findById(Stream<T> enumItems, String id) {
        return findByPredicate(enumItems, enumItem -> Objects.equals(enumItem.getId(), id));
    }

    @Nullable
    public static <T extends Enum<T> & NamedBean> T findByName(Class<T> enumClass, String name) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getName(), name));
    }

    @Nullable
    public static <T extends Enum<T> & NamedBean> T findByName(Stream<T> enumItems, String name) {
        return findByPredicate(enumItems, enumItem -> Objects.equals(enumItem.getName(), name));
    }

    @Nullable
    public static <T extends Enum<T> & TypedBean> T findByType(Class<T> enumClass, Class<?> type) {
        return findByPredicate(enumClass, enumItem -> ClassUtils.isAssignable(type, enumItem.getType(), true));
    }

    @Nullable
    public static <T extends Enum<T> & TypedBean> T findByType(Stream<T> enumItems, Class<?> type) {
        return findByPredicate(enumItems, enumItem -> ClassUtils.isAssignable(type, enumItem.getType(), true));
    }

    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<? super T> predicate) {
        return findByPredicate(Stream.of(enumClass.getEnumConstants()), predicate);
    }

    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Stream<T> enumItems, Predicate<? super T> predicate) {
        return enumItems.filter(predicate).findFirst().orElse(null);
    }
}
