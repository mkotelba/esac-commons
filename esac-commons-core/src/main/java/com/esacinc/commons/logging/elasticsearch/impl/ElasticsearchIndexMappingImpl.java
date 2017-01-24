package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.impl.PropertyTrieImpl;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDynamicType;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchIndexMapping;

public class ElasticsearchIndexMappingImpl implements ElasticsearchIndexMapping {
    private final static String TO_STR_FORMAT = "{all=%s, dynamicType=%s, fieldMappings=%s}";

    private boolean all;
    private ElasticsearchDynamicType dynamicType;
    private PropertyTrie<ElasticsearchFieldMapping> fieldMappings = new PropertyTrieImpl<>();

    public ElasticsearchIndexMappingImpl(boolean all, ElasticsearchDynamicType dynamicType) {
        this.all = all;
        this.dynamicType = dynamicType;
    }

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, this.all, this.dynamicType.getName(), this.fieldMappings);
    }

    @Override
    public boolean isAll() {
        return this.all;
    }

    @Override
    public ElasticsearchDynamicType getDynamicType() {
        return this.dynamicType;
    }

    @Override
    public PropertyTrie<ElasticsearchFieldMapping> getFieldMappings() {
        return this.fieldMappings;
    }
}
