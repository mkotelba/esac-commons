package com.esacinc.commons.crypto.ssl.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.esacinc.commons.net.logging.RestEndpointType;

public interface SslLoggingEvent extends EsacLoggingEvent {
    @ElasticsearchField(datatype = ElasticsearchDatatype.TEXT)
    @JsonProperty
    public RestEndpointType getEndpointType();

    public void setEndpointType(RestEndpointType endpointType);
}
