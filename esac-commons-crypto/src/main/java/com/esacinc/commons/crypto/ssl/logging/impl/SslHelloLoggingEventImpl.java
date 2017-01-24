package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.crypto.ssl.logging.SslHelloLoggingEvent;

public class SslHelloLoggingEventImpl extends AbstractSslLoggingEvent implements SslHelloLoggingEvent {
    private String[] cipherSuites;
    private String protocol;

    @Override
    public String[] getCipherSuites() {
        return this.cipherSuites;
    }

    @Override
    public void setCipherSuites(String[] cipherSuites) {
        this.cipherSuites = cipherSuites;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
