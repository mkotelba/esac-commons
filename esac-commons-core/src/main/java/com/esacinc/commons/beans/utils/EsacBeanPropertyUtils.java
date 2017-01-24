package com.esacinc.commons.beans.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapperImpl;

public final class EsacBeanPropertyUtils {
    private EsacBeanPropertyUtils() {
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> T getValue(BeanWrapperImpl wrapper, @Nullable Class<?> propClass, @Nullable Boolean readable, @Nullable Boolean writeable,
        @Nullable Set<String> propNames, @Nullable T defaultPropValue) {
        PropertyDescriptor propDesc = find(wrapper, propClass, readable, writeable, propNames);

        return ((propDesc != null) ? getValue(wrapper, propDesc.getName(), defaultPropValue) : defaultPropValue);
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> T getValue(BeanWrapperImpl wrapper, String propName, @Nullable T defaultPropValue) {
        return ObjectUtils.defaultIfNull(((T) wrapper.getPropertyValue(propName)), defaultPropValue);
    }

    public static List<PropertyDescriptor> findAll(BeanWrapperImpl wrapper, @Nullable Class<?> propClass, @Nullable Boolean readable,
        @Nullable Boolean writeable, @Nullable Set<String> propNames) {
        return stream(wrapper, propClass, readable, writeable, propNames).collect(Collectors.toList());
    }

    @Nullable
    public static PropertyDescriptor find(BeanWrapperImpl wrapper, @Nullable Class<?> propClass, @Nullable Boolean readable, @Nullable Boolean writeable,
        @Nullable Set<String> propNames) {
        return stream(wrapper, propClass, readable, writeable, propNames).findFirst().orElse(null);
    }

    public static Stream<PropertyDescriptor> stream(BeanWrapperImpl wrapper, @Nullable Class<?> propClass, @Nullable Boolean readable,
        @Nullable Boolean writeable, @Nullable Set<String> propNames) {
        boolean propClassAvailable = (propClass != null), readableAvailable = (readable != null), writeableAvailable = (writeable != null),
            propNamesAvailable = !CollectionUtils.isEmpty(propNames);
        PropertyDescriptor[] propDescsArr = wrapper.getPropertyDescriptors();

        if (propDescsArr.length == 0) {
            return Stream.empty();
        }

        List<PropertyDescriptor> propDescs = new ArrayList<>(propDescsArr.length);
        String propName;

        for (PropertyDescriptor propDesc : propDescsArr) {
            propName = propDesc.getName();

            if ((!propClassAvailable || propClass.isAssignableFrom(propDesc.getPropertyType())) &&
                (!readableAvailable || (readable && wrapper.isReadableProperty(propName))) &&
                (!writeableAvailable || (writeable && wrapper.isWritableProperty(propName))) &&
                (!propNamesAvailable || propNames.contains(propDesc.getName()))) {
                propDescs.add(propDesc);
            }
        }

        return propDescs.stream();
    }
}
