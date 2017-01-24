package com.esacinc.commons.logging.logback.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import net.logstash.logback.marker.LogstashMarker;

public abstract class AbstractEsacMarker extends LogstashMarker {
    private final static long serialVersionUID = 0L;

    protected AbstractEsacMarker(String nameSuffix) {
        super((MARKER_NAME_PREFIX + nameSuffix));
    }

    @Override
    public void writeTo(JsonGenerator jsonGen) throws IOException {
    }
}
