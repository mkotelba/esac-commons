package com.esacinc.commons.crypto.ssl.logging;

public interface SslLoggingEventProcessor<T extends SslLoggingEvent> {
    public void processEvent(StackTraceElement[] frames, String msg);

    public boolean canProcessEvent(StackTraceElement[] frames);
}
