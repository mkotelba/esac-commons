package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.utils.EsacOptionUtils;
import com.esacinc.commons.logging.LoggingMessages;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;

public class MessageMarkerConverter extends MessageConverter {
    public final static String WORD = "mMarker";

    private final static String FULL_OPT_NAME = "full";

    private boolean full;

    @Override
    public String convert(ILoggingEvent event) {
        LoggingMessages msgs = EsacMarkerUtils.getMessages(((LoggingEvent) event));

        return ((msgs != null) ? msgs.getMessage(this.full) : super.convert(event));
    }

    @Override
    public void start() {
        this.full = EsacOptionUtils.getBooleanValue(FULL_OPT_NAME, this.getFirstOption(), true);

        super.start();
    }
}
