package com.esacinc.commons.crypto.ssl.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SslHelloLoggingEvent extends SslLoggingEvent {
    @JsonProperty
    public String[] getCipherSuites();

    public void setCipherSuites(String[] cipherSuites);

    @JsonProperty
    public String getProtocol();

    public void setProtocol(String protocol);
}
