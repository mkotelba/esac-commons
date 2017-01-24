package com.esacinc.commons.logging.logback.impl;

import com.esacinc.commons.logging.LoggingMessages;
import com.esacinc.commons.logging.impl.LoggingMessagesImpl;
import javax.annotation.Nullable;

public class MessageMarker extends AbstractEsacMarker {
    private final static long serialVersionUID = 0L;

    private LoggingMessages msgs = new LoggingMessagesImpl();

    public MessageMarker(@Nullable String fullMsg) {
        this(fullMsg, null);
    }

    public MessageMarker(@Nullable String fullMsg, @Nullable String shortMsg) {
        super("MSG");

        this.msgs.setMessage(true, fullMsg);
        this.msgs.setMessage(false, shortMsg);
    }

    public LoggingMessages getMessages() {
        return this.msgs;
    }
}
