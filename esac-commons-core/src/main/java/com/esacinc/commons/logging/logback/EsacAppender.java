package com.esacinc.commons.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.esacinc.commons.beans.NamedBean;

public interface EsacAppender extends Appender<ILoggingEvent>, NamedBean {
}
