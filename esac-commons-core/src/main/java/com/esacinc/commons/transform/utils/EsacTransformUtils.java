package com.esacinc.commons.transform.utils;

import com.esacinc.commons.beans.utils.EsacBeanPropertyUtils;
import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.Serializer.Property;
import org.springframework.beans.BeanWrapperImpl;

public final class EsacTransformUtils {
    public final static Set<String> COLUMN_NUM_PROP_NAMES = Collections.singleton("columnNumber");
    public final static Set<String> LINE_NUM_PROP_NAMES = Collections.singleton("lineNumber");
    public final static Set<String> PUBLIC_ID_PROP_NAMES = Stream.of("publicID", "publicId").collect(Collectors.toCollection(LinkedHashSet::new));
    public final static Set<String> SYS_ID_PROP_NAMES = Stream.of("systemId", "systemID").collect(Collectors.toCollection(LinkedHashSet::new));

    private EsacTransformUtils() {
    }

    public static Properties buildOutputProperties(Properties outProps) {
        return outProps.entrySet().stream().collect(EsacStreamUtils
            .toMap(outPropEntry -> Property.get(((String) outPropEntry.getKey())).getQName().getStructuredQName(), Entry::getValue, Properties::new));
    }

    public static int getColumnNumber(Object obj) {
        // noinspection ConstantConditions
        return EsacBeanPropertyUtils.getValue(new BeanWrapperImpl(obj), Integer.class, true, null, COLUMN_NUM_PROP_NAMES, -1);
    }

    public static int getLineNumber(Object obj) {
        // noinspection ConstantConditions
        return EsacBeanPropertyUtils.getValue(new BeanWrapperImpl(obj), Integer.class, true, null, LINE_NUM_PROP_NAMES, -1);
    }

    @Nullable
    public static String getPublicId(Object obj) {
        return EsacBeanPropertyUtils.getValue(new BeanWrapperImpl(obj), String.class, true, null, PUBLIC_ID_PROP_NAMES, null);
    }

    @Nullable
    public static String getSystemId(Object obj) {
        return EsacBeanPropertyUtils.getValue(new BeanWrapperImpl(obj), String.class, true, null, SYS_ID_PROP_NAMES, null);
    }
}
