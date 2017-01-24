package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.pattern.ThreadConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.utils.EsacStringUtils;
import org.apache.commons.lang3.StringUtils;

public class ThreadSectionConverter extends ThreadConverter {
    public final static String WORD = "xT";

    private boolean enabled;

    @Override
    public String convert(ILoggingEvent event) {
        return (this.enabled ? (EsacStringUtils.L_BRACKET + super.convert(event) + EsacStringUtils.R_BRACKET) : StringUtils.EMPTY);
    }

    @Override
    public void start() {
        this.enabled = ((AbstractEsacApplication) this.getContext().getObject(AbstractEsacApplication.BEAN_NAME)).isLoggingConsoleThreadName();

        super.start();
    }
}
