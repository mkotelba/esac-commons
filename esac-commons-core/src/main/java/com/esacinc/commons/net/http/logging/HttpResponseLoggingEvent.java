package com.esacinc.commons.net.http.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface HttpResponseLoggingEvent extends HttpLoggingEvent {
    @JsonProperty
    public Integer getStatusCode();

    public void setStatusCode(Integer statusCode);

    @JsonProperty
    public String getStatusMessage();

    public void setStatusMessage(String statusMsg);
}
