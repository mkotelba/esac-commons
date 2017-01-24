package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.net.http.logging.HttpResponseLoggingEvent;
import com.esacinc.commons.net.http.logging.HttpResponseLoggingEventHandler;
import com.esacinc.commons.net.logging.RestEndpointType;
import org.apache.commons.lang3.ArrayUtils;

public class HttpResponseLoggingEventHandlerImpl extends AbstractHttpLoggingEventHandler<HttpResponseLoggingEvent> implements HttpResponseLoggingEventHandler {
    public final static String BEAN_NAME = HTTP_BEAN_NAME_PREFIX + "Resp";

    public final static String HTTP_CLIENT_RESPONSE_FIELD_NAME = ElasticsearchFieldNames.HTTP_CLIENT_PREFIX + ElasticsearchFieldNames.RESPONSE_SUFFIX;
    public final static String HTTP_SERVER_RESPONSE_FIELD_NAME = ElasticsearchFieldNames.HTTP_SERVER_PREFIX + ElasticsearchFieldNames.RESPONSE_SUFFIX;

    private final static String FULL_MSG_FORMAT = HTTP_FULL_MSG_FORMAT_PREFIX + "headers=%s, statusCode=%d, statusMsg=%s" + HTTP_FULL_MSG_FORMAT_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(HTTP_CLIENT_RESPONSE_FIELD_NAME, ElasticsearchDatatype.OBJECT, HttpResponseLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(HTTP_SERVER_RESPONSE_FIELD_NAME, ElasticsearchDatatype.OBJECT, HttpResponseLoggingEvent.class));

    public HttpResponseLoggingEventHandlerImpl() {
        super(HttpResponseLoggingEvent.class);
    }

    @Override
    public String buildFieldName(HttpResponseLoggingEvent event) {
        return ((event.getEndpointType() == RestEndpointType.CLIENT) ? HTTP_CLIENT_RESPONSE_FIELD_NAME : HTTP_SERVER_RESPONSE_FIELD_NAME);
    }

    @Override
    protected String buildFullMessage(HttpResponseLoggingEvent event) {
        return String.format(FULL_MSG_FORMAT, event.getEndpointType().getName(), event.getEventType().getName(), event.getHeaders(), event.getStatusCode(),
            event.getStatusMessage());
    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
