package com.esacinc.commons.management.logging.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.impl.AbstractEsacLoggingEventHandler;
import com.esacinc.commons.management.logging.GcLoggingEvent;
import com.esacinc.commons.management.logging.GcLoggingEventHandler;
import org.apache.commons.lang3.ArrayUtils;

public class GcLoggingEventHandlerImpl extends AbstractEsacLoggingEventHandler<GcLoggingEvent> implements GcLoggingEventHandler {
    public final static String BEAN_NAME = BEAN_NAME_PREFIX + "ManagementGc";

    public final static String FIELD_NAME = "management.gc";

    private final static String MSG_PREFIX = "Garbage collected";

    private final static String SHORT_MSG = MSG_PREFIX + EsacStringUtils.PERIOD;

    private final static String FULL_MSG_FORMAT = MSG_PREFIX + " (name=%s, action=%s, cause=%s, startTimestamp=%d, endTimestamp=%d, duration=%d).";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(FIELD_NAME, ElasticsearchDatatype.OBJECT, GcLoggingEvent.class));

    public GcLoggingEventHandlerImpl() {
        super(GcLoggingEvent.class);
    }

    @Override
    public String buildMessage(GcLoggingEvent event, boolean full) {
        return (full
            ? String.format(FULL_MSG_FORMAT, event.getName(), event.getAction(), event.getCause(), event.getStartTimestamp(), event.getEndTimestamp(),
                event.getDuration())
            : SHORT_MSG);
    }

    @Override
    public String buildFieldName(GcLoggingEvent event) {
        return FIELD_NAME;
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
