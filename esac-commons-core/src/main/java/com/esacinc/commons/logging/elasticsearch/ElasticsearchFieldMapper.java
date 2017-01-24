package com.esacinc.commons.logging.elasticsearch;

import com.google.common.collect.ListMultimap;
import com.esacinc.commons.logging.elasticsearch.utils.EsacElasticsearchUtils;

public interface ElasticsearchFieldMapper {
    public default boolean hasClassFieldMappings() {
        return !this.getClassFieldMappings().isEmpty();
    }

    public default ListMultimap<Class<?>, ElasticsearchFieldMapping> getClassFieldMappings() {
        return EsacElasticsearchUtils.EMPTY_CLASS_FIELD_MAPPINGS;
    }

    public default boolean hasFieldMappings() {
        return (this.getFieldMappings().length > 0);
    }

    public default ElasticsearchFieldMapping[] getFieldMappings() {
        return EsacElasticsearchUtils.EMPTY_FIELD_MAPPINGS;
    }
}
