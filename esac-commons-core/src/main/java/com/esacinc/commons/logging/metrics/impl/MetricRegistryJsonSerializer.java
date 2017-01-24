package com.esacinc.commons.logging.metrics.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component("jsonSerializerMetricRegistry")
public class MetricRegistryJsonSerializer extends StdSerializer<EsacMetricRegistry> {
    private final static long serialVersionUID = 0L;

    public MetricRegistryJsonSerializer() {
        super(EsacMetricRegistry.class);
    }

    @Override
    public void serialize(EsacMetricRegistry metricRegistry, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartObject();
        
        

        jsonGen.writeEndObject();
    }
}
