package com.esacinc.commons.config.property.utils;

import com.esacinc.commons.utils.EsacDataUtils;
import com.esacinc.commons.utils.EsacStringUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public final class EsacPropertyNameUtils {
    public final static String DESCENDANT_CEILING_PROP_NAME_SUFFIX = ".\0";

    private final static ConcurrentMap<String, String> PARENT_CACHE = new ConcurrentHashMap<>((EsacDataUtils.BYTES_IN_KB / 2));

    private final static Function<String, String> COMPUTE_PARENT_FUNC = propName -> {
        int parentPropNameLen = getParentLength(propName);

        return ((parentPropNameLen > 0) ? propName.substring(0, parentPropNameLen) : StringUtils.EMPTY);
    };

    private EsacPropertyNameUtils() {
    }

    public static boolean isDescendant(String propName, String testPropName) {
        return isAncestor(testPropName, propName);
    }

    public static boolean isAncestor(String propName, String testPropName) {
        // noinspection StringEquality
        if (propName == testPropName) {
            return false;
        }

        int propNameLen = propName.length();

        if (propNameLen < 3) {
            return false;
        }

        int testPropNameLen = testPropName.length();

        if (testPropNameLen == 0) {
            return false;
        }

        int propNameLenDiff = (propNameLen - testPropNameLen);

        return ((propNameLenDiff > 0) && (propName.charAt(testPropNameLen) == EsacStringUtils.PERIOD_CHAR) &&
            ((propNameLenDiff == 1)
                ? PARENT_CACHE.computeIfAbsent(propName, propNameCompute -> propName.substring(0, testPropNameLen)) : propName.substring(0, testPropNameLen))
                    .equals(testPropName));
    }

    public static boolean isChild(String propName, String testPropName) {
        return isParent(testPropName, propName);
    }

    public static boolean isParent(String propName, String testPropName) {
        // noinspection StringEquality
        if (propName == testPropName) {
            return false;
        }

        int propNameLen = propName.length();

        if (propNameLen < 3) {
            return false;
        }

        int testPropNameLen = testPropName.length();

        return ((testPropNameLen != 0) && ((propNameLen - testPropNameLen) >= 1) && (propName.lastIndexOf(EsacStringUtils.PERIOD_CHAR) == testPropNameLen) &&
            PARENT_CACHE.computeIfAbsent(propName, propNameCompute -> propName.substring(0, testPropNameLen)).equals(testPropName));
    }

    @Nullable
    public static String getParent(String propName) {
        String parentPropName = PARENT_CACHE.computeIfAbsent(propName, COMPUTE_PARENT_FUNC);

        // noinspection StringEquality
        return ((parentPropName != StringUtils.EMPTY) ? parentPropName : null);
    }

    public static int getParentLength(String propName) {
        int propNameLen = propName.length();

        return ((propNameLen >= 3) ? propName.lastIndexOf(EsacStringUtils.PERIOD_CHAR) : -1);
    }

    public static boolean isRoot(String propName) {
        return ((propName.length() < 3) || (propName.indexOf(EsacStringUtils.PERIOD_CHAR) == -1));
    }
}
