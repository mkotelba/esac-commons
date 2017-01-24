package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.utils.EsacPropertyNameUtils;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDynamicType;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldFormats;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchIndexMapping;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.LinkedList;

public class ElasticsearchIndexMappingJsonSerializer extends StdSerializer<ElasticsearchIndexMapping> {
    private final static String ALL_FIELD_NAME = "_all";
    private final static String DYNAMIC_FIELD_NAME = "_dynamic";
    private final static String ENABLED_FIELD_NAME = "enabled";
    private final static String FORMAT_FIELD_NAME = "format";
    private final static String INDEX_FIELD_NAME = "index";
    private final static String PROPS_FIELD_NAME = "properties";
    private final static String TYPE_FIELD_NAME = "type";

    private final static long serialVersionUID = 0L;

    public ElasticsearchIndexMappingJsonSerializer() {
        super(ElasticsearchIndexMapping.class);
    }

    @Override
    public void serialize(ElasticsearchIndexMapping indexMapping, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartObject();

        jsonGen.writeObjectFieldStart(ALL_FIELD_NAME);
        jsonGen.writeBooleanField(ENABLED_FIELD_NAME, indexMapping.isAll());
        jsonGen.writeEndObject();

        ElasticsearchDynamicType indexMappingDynamicType = indexMapping.getDynamicType();

        if (indexMappingDynamicType == ElasticsearchDynamicType.STRICT) {
            jsonGen.writeStringField(DYNAMIC_FIELD_NAME, ElasticsearchDynamicType.STRICT.getName());
        } else {
            jsonGen.writeBooleanField(DYNAMIC_FIELD_NAME, (indexMappingDynamicType == ElasticsearchDynamicType.TRUE));
        }

        PropertyTrie<ElasticsearchFieldMapping> fieldMappings = indexMapping.getFieldMappings();

        if (!fieldMappings.isEmpty()) {
            jsonGen.writeObjectFieldStart(PROPS_FIELD_NAME);

            LinkedList<String> parentFieldPropNames = new LinkedList<>();
            String parentFieldPropName, fieldName, nextFieldPropName;
            ElasticsearchFieldMapping fieldMapping;
            ElasticsearchDatatype fieldDatatype;
            boolean fieldLeaf;

            for (String fieldPropName : fieldMappings.keySet()) {
                fieldDatatype = (fieldMapping = fieldMappings.get(fieldPropName)).getDatatype();

                if ((parentFieldPropName = EsacPropertyNameUtils.getParent(fieldPropName)) != null) {
                    while (!parentFieldPropNames.isEmpty() && !parentFieldPropNames.getLast().equals(parentFieldPropName)) {
                        jsonGen.writeEndObject();
                        jsonGen.writeEndObject();

                        parentFieldPropNames.removeLast();
                    }

                    fieldName = fieldPropName.substring((parentFieldPropName.length() + 1));
                } else {
                    while (!parentFieldPropNames.isEmpty()) {
                        jsonGen.writeEndObject();
                        jsonGen.writeEndObject();

                        parentFieldPropNames.removeLast();
                    }

                    fieldName = fieldPropName;
                }

                jsonGen.writeObjectFieldStart(fieldName);

                if ((fieldLeaf = (((nextFieldPropName = fieldMappings.higherKey(fieldPropName)) == null) ||
                    !EsacPropertyNameUtils.isChild(fieldPropName, nextFieldPropName))) && (fieldDatatype == ElasticsearchDatatype.DATE)) {
                    jsonGen.writeStringField(FORMAT_FIELD_NAME, (fieldMapping.hasFormat() ? fieldMapping.getFormat() : ElasticsearchFieldFormats.EPOCH_MILLIS));
                }

                if (!fieldMapping.isIndexed()) {
                    jsonGen.writeBooleanField(INDEX_FIELD_NAME, false);
                }

                if (fieldDatatype.isExplicit()) {
                    jsonGen.writeStringField(TYPE_FIELD_NAME, fieldDatatype.getName());
                }

                if (fieldLeaf) {
                    jsonGen.writeEndObject();
                } else {
                    jsonGen.writeObjectFieldStart(PROPS_FIELD_NAME);

                    parentFieldPropNames.add(fieldPropName);
                }
            }

            while (!parentFieldPropNames.isEmpty()) {
                jsonGen.writeEndObject();
                jsonGen.writeEndObject();

                parentFieldPropNames.removeLast();
            }

            jsonGen.writeEndObject();
        }

        jsonGen.writeEndObject();
    }
}
