package com.esacinc.commons.logging.elasticsearch;

import com.esacinc.commons.config.property.PropertyTrie;

public interface ElasticsearchIndexMapping {
    public boolean isAll();

    public ElasticsearchDynamicType getDynamicType();

    public PropertyTrie<ElasticsearchFieldMapping> getFieldMappings();
}
