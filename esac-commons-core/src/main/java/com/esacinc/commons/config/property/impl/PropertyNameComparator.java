package com.esacinc.commons.config.property.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import java.util.Comparator;

public class PropertyNameComparator implements Comparator<String> {
    public final static PropertyNameComparator INSTANCE = new PropertyNameComparator();

    @Override
    public int compare(String propName1, String propName2) {
        // noinspection StringEquality
        if (propName1 == propName2) {
            return 0;
        }

        int propNameLen1 = propName1.length(), propNameLen2 = propName2.length();

        if ((propNameLen1 == 0) || (propNameLen2 == 0)) {
            return (propNameLen1 - propNameLen2);
        }

        int maxPropNameLen = Math.min(propNameLen1, propNameLen2);
        char propNameChar1, propNameChar2;

        for (int a = 0; a < maxPropNameLen; a++) {
            if ((propNameChar1 = propName1.charAt(a)) != (propNameChar2 = propName2.charAt(a))) {
                if (propNameChar1 == EsacStringUtils.PERIOD_CHAR) {
                    return -1;
                } else if (propNameChar2 == EsacStringUtils.PERIOD_CHAR) {
                    return 1;
                } else if (propNameChar1 == EsacStringUtils.NULL_CHAR) {
                    return 1;
                } else if (propNameChar2 == EsacStringUtils.NULL_CHAR) {
                    return -1;
                } else {
                    return (propNameChar1 - propNameChar2);
                }
            }
        }

        return (propNameLen1 - propNameLen2);
    }
}
