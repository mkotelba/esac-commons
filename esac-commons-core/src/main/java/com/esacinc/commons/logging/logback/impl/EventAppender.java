package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LogbackException;
import com.esacinc.commons.utils.EsacStreamUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.esacinc.commons.config.property.impl.PropertyNameComparator;
import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.logback.EsacAppenderNames;
import com.esacinc.commons.logging.EsacLoggingEventHandler;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;

public class EventAppender extends AbstractEsacAppender implements ElasticsearchFieldMapper {
    private final static String APP_FIELD_NAME_PREFIX = "app.";
    private final static String APP_INSTANCE_FIELD_NAME_PREFIX = APP_FIELD_NAME_PREFIX + "instance.";

    private final static String APP_NAME_FIELD_NAME = APP_FIELD_NAME_PREFIX + ElasticsearchFieldNames.NAME;
    private final static String APP_PID_FIELD_NAME = APP_FIELD_NAME_PREFIX + "pid";
    private final static String APP_INSTANCE_ID_FIELD_NAME = APP_INSTANCE_FIELD_NAME_PREFIX + ElasticsearchFieldNames.ID_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(APP_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(APP_INSTANCE_ID_FIELD_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(APP_PID_FIELD_NAME, ElasticsearchDatatype.LONG));

    private String appName;
    private String appInstanceId;
    private long appPid;
    private List<EsacLoggingEventHandler<?>> eventHandlers;

    public EventAppender(String appName, String appInstanceId, long appPid, List<EsacLoggingEventHandler<?>> eventHandlers) {
        super(EsacAppenderNames.EVENT);

        this.appName = appName;
        this.appInstanceId = appInstanceId;
        this.appPid = appPid;
        this.eventHandlers = eventHandlers;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void doAppendInternal(LoggingEvent event) throws LogbackException {
        Map<String, Object> fields = new TreeMap<>(PropertyNameComparator.INSTANCE);
        fields.put(APP_NAME_FIELD_NAME, this.appName);
        fields.put(APP_INSTANCE_ID_FIELD_NAME, this.appInstanceId);
        fields.put(APP_PID_FIELD_NAME, this.appPid);

        LoggingEventMarker marker = EsacStreamUtils.findInstance(EsacMarkerUtils.stream(event.getMarker()), LoggingEventMarker.class);

        if (marker != null) {
            EsacLoggingEvent markerEvent = marker.getEvent();
            EsacLoggingEventHandler<EsacLoggingEvent> markerEventHandler =
                ((EsacLoggingEventHandler<EsacLoggingEvent>) this.eventHandlers.stream()
                    .filter(eventHandlerItem -> eventHandlerItem.canHandle(markerEvent)).findFirst()
                    .orElseThrow(() -> new IllegalStateException(String.format("Unable to find logging event (class=%s) handler.", markerEvent.getClass()))));

            fields.put(markerEventHandler.buildFieldName(markerEvent), markerEvent);

            EsacMarkerUtils.setMessages(event, markerEventHandler.buildMessage(markerEvent, true), markerEventHandler.buildMessage(markerEvent, false));
        }

        EsacMarkerUtils.setFields(event, fields);
    }

    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
