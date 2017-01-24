package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import com.esacinc.commons.utils.EsacExceptionUtils;
import org.apache.commons.lang3.StringUtils;

public class RootCauseThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    public final static String WORD = "exRoot";

    public String throwableProxyToString(IThrowableProxy throwableProxy) {
        return (CoreConstants.CAUSED_BY + EsacExceptionUtils.buildRootCauseStackTrace(((ThrowableProxy) throwableProxy).getThrowable()) + StringUtils.LF);
    }
}
