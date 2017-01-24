package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.logback.impl.FieldsMarker;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;
import com.esacinc.commons.utils.EsacStreamUtils;

public class FieldsMarkerJsonProvider extends AbstractEsacJsonProvider {
    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        FieldsMarker marker = EsacStreamUtils.findInstance(EsacMarkerUtils.stream(event.getMarker()), FieldsMarker.class);

        if ((marker != null) && marker.hasFields()) {
            fields.putAll(marker.getFields());
        }
    }
}
