package com.esacinc.commons.config.property.json.impl;

import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.utils.EsacPropertyNameUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.IOException;
import java.util.LinkedList;

public class PropertyTrieJsonSerializer extends StdSerializer<PropertyTrie<?>> {
    private final static long serialVersionUID = 0L;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public PropertyTrieJsonSerializer() {
        super(((Class<PropertyTrie<?>>) ((Class<?>) PropertyTrie.class)));
    }

    @Override
    public void serialize(PropertyTrie<?> trie, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartObject();

        if (!trie.isEmpty()) {
            LinkedList<String> parentPropNames = new LinkedList<>();
            String parentPropName, fieldName, nextPropName;

            for (String propName : trie.keySet()) {
                if ((parentPropName = EsacPropertyNameUtils.getParent(propName)) != null) {
                    while (!parentPropNames.isEmpty() && !parentPropNames.getLast().equals(parentPropName)) {
                        jsonGen.writeEndObject();

                        parentPropNames.removeLast();
                    }

                    fieldName = propName.substring((parentPropName.length() + 1));
                } else {
                    while (!parentPropNames.isEmpty()) {
                        jsonGen.writeEndObject();

                        parentPropNames.removeLast();
                    }

                    fieldName = propName;
                }

                if (((nextPropName = trie.higherKey(propName)) == null) || !EsacPropertyNameUtils.isChild(propName, nextPropName)) {
                    jsonGen.writeObjectField(fieldName, trie.get(propName));
                } else {
                    jsonGen.writeObjectFieldStart(fieldName);

                    parentPropNames.add(propName);
                }
            }

            while (!parentPropNames.isEmpty()) {
                jsonGen.writeEndObject();

                parentPropNames.removeLast();
            }
        }

        jsonGen.writeEndObject();
    }
}
