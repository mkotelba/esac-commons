package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.net.http.logging.HttpRequestLoggingEvent;
import com.esacinc.commons.net.http.logging.HttpRequestLoggingEventHandler;
import com.esacinc.commons.net.logging.RestEndpointType;
import org.apache.commons.lang3.ArrayUtils;

public class HttpRequestLoggingEventHandlerImpl extends AbstractHttpLoggingEventHandler<HttpRequestLoggingEvent> implements HttpRequestLoggingEventHandler {
    public final static String BEAN_NAME = HTTP_BEAN_NAME_PREFIX + "Req";

    public final static String HTTP_CLIENT_REQUEST_FIELD_NAME = ElasticsearchFieldNames.HTTP_CLIENT_PREFIX + ElasticsearchFieldNames.REQUEST_SUFFIX;
    public final static String HTTP_SERVER_REQUEST_FIELD_NAME = ElasticsearchFieldNames.HTTP_SERVER_PREFIX + ElasticsearchFieldNames.REQUEST_SUFFIX;

    private final static String FULL_MSG_FORMAT = HTTP_FULL_MSG_FORMAT_PREFIX +
        "method=%s, uri=%s, headers=%s, localName=%s, localPort=%d, remoteAddr=%s, remoteHost=%s, remotePort=%d" + HTTP_FULL_MSG_FORMAT_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(HTTP_CLIENT_REQUEST_FIELD_NAME, ElasticsearchDatatype.OBJECT, HttpRequestLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(HTTP_SERVER_REQUEST_FIELD_NAME, ElasticsearchDatatype.OBJECT, HttpRequestLoggingEvent.class));

    public HttpRequestLoggingEventHandlerImpl() {
        super(HttpRequestLoggingEvent.class);
    }

    @Override
    public String buildFieldName(HttpRequestLoggingEvent event) {
        return ((event.getEndpointType() == RestEndpointType.CLIENT) ? HTTP_CLIENT_REQUEST_FIELD_NAME : HTTP_SERVER_REQUEST_FIELD_NAME);
    }

    @Override
    protected String buildFullMessage(HttpRequestLoggingEvent event) {
        return String.format(FULL_MSG_FORMAT, event.getEndpointType().getName(), event.getEventType().getName(), event.getMethod(), event.getUri(),
            event.getHeaders(), event.getLocalName(), event.getLocalPort(), event.getRemoteAddr(), event.getRemoteHost(), event.getRemotePort());
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
