package com.esacinc.commons.net.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;

public interface RestLoggingEvent extends EsacLoggingEvent {
    @ElasticsearchField(datatype = ElasticsearchDatatype.TEXT)
    @JsonProperty
    public RestEndpointType getEndpointType();

    public void setEndpointType(RestEndpointType endpointType);

    @ElasticsearchField(datatype = ElasticsearchDatatype.TEXT)
    @JsonProperty
    public RestEventType getEventType();

    @JsonProperty
    public String getTxId();

    public void setTxId(String txId);
}
