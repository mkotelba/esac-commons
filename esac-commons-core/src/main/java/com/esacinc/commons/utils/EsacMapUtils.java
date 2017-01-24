package com.esacinc.commons.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.text.StrBuilder;

public final class EsacMapUtils {
    public final static String DELIM = "; ";

    public final static String EMPTY_STR = "{}";

    private EsacMapUtils() {
    }

    public static String toString(Map<?, ?> map) {
        if (map.isEmpty()) {
            return EMPTY_STR;
        } else {
            StrBuilder builder = new StrBuilder();
            builder.append(EsacStringUtils.L_BRACE_CHAR);

            boolean firstKey = true;

            for (Object key : map.keySet()) {
                if (firstKey) {
                    firstKey = false;
                } else {
                    builder.append(DELIM);
                }

                builder.append(key);
                builder.append(EsacStringUtils.EQUALS_CHAR);
                builder.append(map.get(key));
            }

            builder.append(EsacStringUtils.R_BRACE_CHAR);

            return builder.build();
        }
    }

    @Nullable
    public static <T, U, V extends Map<? extends T, ? super U>> U getOrDefault(V map, T key) {
        return getOrDefault(map, key, null);
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T, U, V extends Map<? extends T, ? super U>> U getOrDefault(V map, T key, @Nullable U defaultValue) {
        return ((U) map.getOrDefault(key, defaultValue));
    }
}
