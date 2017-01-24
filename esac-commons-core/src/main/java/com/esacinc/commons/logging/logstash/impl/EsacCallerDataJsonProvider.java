package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class EsacCallerDataJsonProvider extends AbstractEsacJsonProvider {
    private final static String CALLER_FIELD_NAME_PREFIX = "caller.";
    private final static String CALLER_METHOD_FIELD_NAME_PREFIX = CALLER_FIELD_NAME_PREFIX + "method.";

    private final static String CALLER_CLASS_NAME_FIELD_NAME = CALLER_FIELD_NAME_PREFIX + "class." + ElasticsearchFieldNames.NAME;
    private final static String CALLER_FILE_NAME_FIELD_NAME = CALLER_FIELD_NAME_PREFIX + "file." + ElasticsearchFieldNames.NAME;
    private final static String CALLER_LINE_NUM_FIELD_NAME = CALLER_FIELD_NAME_PREFIX + "line.number";
    private final static String CALLER_METHOD_NAME_FIELD_NAME = CALLER_METHOD_FIELD_NAME_PREFIX + ElasticsearchFieldNames.NAME;
    private final static String CALLER_METHOD_NATIVE_FIELD_NAME = CALLER_METHOD_FIELD_NAME_PREFIX + "native";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(CALLER_CLASS_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(CALLER_FILE_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(CALLER_LINE_NUM_FIELD_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(CALLER_METHOD_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(CALLER_METHOD_NATIVE_FIELD_NAME, ElasticsearchDatatype.BOOLEAN));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        StackTraceElement[] stackTraceElems = event.getCallerData();
        boolean stackTraceElemAvailable = !ArrayUtils.isEmpty(stackTraceElems);
        StackTraceElement stackTraceElem = (stackTraceElemAvailable ? stackTraceElems[0] : null);

        fields.put(CALLER_CLASS_NAME_FIELD_NAME, (stackTraceElemAvailable ? stackTraceElem.getClassName() : null));
        fields.put(CALLER_FILE_NAME_FIELD_NAME, (stackTraceElemAvailable ? stackTraceElem.getFileName() : null));
        fields.put(CALLER_LINE_NUM_FIELD_NAME, (stackTraceElemAvailable ? stackTraceElem.getLineNumber() : null));
        fields.put(CALLER_METHOD_NAME_FIELD_NAME, (stackTraceElemAvailable ? stackTraceElem.getMethodName() : null));
        fields.put(CALLER_METHOD_NATIVE_FIELD_NAME, (stackTraceElemAvailable ? stackTraceElem.isNativeMethod() : null));
    }

    @Override
    public void prepareForDeferredProcessing(ILoggingEvent event) {
        event.getCallerData();
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
