package com.esacinc.commons.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public final class EsacExceptionUtils {
    private EsacExceptionUtils() {
    }

    public static String buildRootCauseStackTrace(Throwable exception) {
        return StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception), StringUtils.LF);
    }

    public static Throwable getRootCause(Throwable exception) {
        return ObjectUtils.defaultIfNull(ExceptionUtils.getRootCause(exception), exception);
    }
}
