package com.esacinc.commons.net.http.json.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class HttpParameterMultimapJsonSerializer extends StdSerializer<HttpParameterMultimap> {
    private final static long serialVersionUID = 0L;

    public HttpParameterMultimapJsonSerializer() {
        super(HttpParameterMultimap.class);
    }

    @Override
    public void serialize(HttpParameterMultimap multimap, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartArray();

        if (!multimap.isEmpty()) {
            for (String key : multimap.keySet()) {
                jsonGen.writeStartObject();

                jsonGen.writeStringField(ElasticsearchFieldNames.NAME, key);

                jsonGen.writeArrayFieldStart(ElasticsearchFieldNames.VALUES);

                for (Object value : multimap.get(key)) {
                    jsonGen.writeObject(value);
                }

                jsonGen.writeEndArray();

                jsonGen.writeEndObject();
            }
        }

        jsonGen.writeEndArray();
    }
}
