package com.esacinc.commons.ws.logging.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.impl.AbstractEsacLoggingEventHandler;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.ws.logging.WsLoggingEvent;
import com.esacinc.commons.ws.logging.WsLoggingEventHandler;
import com.esacinc.commons.ws.logging.WsRequestLoggingEvent;
import com.esacinc.commons.ws.logging.WsResponseLoggingEvent;
import org.apache.commons.lang3.ArrayUtils;

public class WsLoggingEventHandlerImpl extends AbstractEsacLoggingEventHandler<WsLoggingEvent> implements WsLoggingEventHandler {
    public final static String BEAN_NAME = BEAN_NAME_PREFIX + "Ws";

    public final static String WS_CLIENT_REQUEST_FIELD_NAME = ElasticsearchFieldNames.WS_CLIENT_PREFIX + ElasticsearchFieldNames.REQUEST_SUFFIX;
    public final static String WS_CLIENT_RESPONSE_FIELD_NAME = ElasticsearchFieldNames.WS_CLIENT_PREFIX + ElasticsearchFieldNames.RESPONSE_SUFFIX;
    public final static String WS_SERVER_REQUEST_FIELD_NAME = ElasticsearchFieldNames.WS_SERVER_PREFIX + ElasticsearchFieldNames.REQUEST_SUFFIX;
    public final static String WS_SERVER_RESPONSE_FIELD_NAME = ElasticsearchFieldNames.WS_SERVER_PREFIX + ElasticsearchFieldNames.RESPONSE_SUFFIX;

    private final static String MSG_FORMAT_PREFIX = "Web service %s %s";

    private final static String SHORT_MSG_FORMAT = MSG_FORMAT_PREFIX + EsacStringUtils.PERIOD;
    private final static String FULL_MSG_FORMAT = MSG_FORMAT_PREFIX +
        " (bindingName=%s, direction=%s, endpointAddr=%s, endpointName=%s, portName=%s, portTypeName=%s, serviceName=%s, soapHeaders=%s, soapFault=%s):\n%s";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(WS_CLIENT_REQUEST_FIELD_NAME, ElasticsearchDatatype.OBJECT, WsRequestLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(WS_CLIENT_RESPONSE_FIELD_NAME, ElasticsearchDatatype.OBJECT, WsResponseLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(WS_SERVER_REQUEST_FIELD_NAME, ElasticsearchDatatype.OBJECT, WsRequestLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(WS_SERVER_RESPONSE_FIELD_NAME, ElasticsearchDatatype.OBJECT, WsResponseLoggingEvent.class));

    public WsLoggingEventHandlerImpl() {
        super(WsLoggingEvent.class);
    }

    @Override
    public String buildMessage(WsLoggingEvent event, boolean full) {
        String eventEndpointTypeName = event.getEndpointType().getName(), eventTypeName = event.getEventType().getName();

        return (full
            ? String.format(FULL_MSG_FORMAT, eventEndpointTypeName, eventTypeName, event.getBindingName(), event.getDirection().getName(),
                event.getEndpointAddress(), event.getEndpointName(), event.getPortName(), event.getPortTypeName(), event.getServiceName(),
                (event.hasPrettyPayload() ? event.getPrettyPayload() : event.getPayload()))
            : String.format(SHORT_MSG_FORMAT, eventEndpointTypeName, eventTypeName));
    }

    @Override
    public String buildFieldName(WsLoggingEvent event) {
        boolean reqEvent = (event.getEventType() == RestEventType.REQUEST);

        return ((event.getEndpointType() == RestEndpointType.CLIENT)
            ? (reqEvent ? WS_CLIENT_REQUEST_FIELD_NAME : WS_CLIENT_RESPONSE_FIELD_NAME)
            : (reqEvent ? WS_SERVER_REQUEST_FIELD_NAME : WS_SERVER_RESPONSE_FIELD_NAME));
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
