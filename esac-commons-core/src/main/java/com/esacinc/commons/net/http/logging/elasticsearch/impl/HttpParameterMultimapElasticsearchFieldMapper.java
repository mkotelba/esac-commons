package com.esacinc.commons.net.http.logging.elasticsearch.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpParameterMultimapElasticsearchFieldMapper implements ElasticsearchFieldMapper {
    private final static ListMultimap<Class<?>, ElasticsearchFieldMapping> CLASS_FIELD_MAPPINGS =
        ImmutableListMultimap.<Class<?>, ElasticsearchFieldMapping> builder()
            .put(HttpParameterMultimap.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.NAME, ElasticsearchDatatype.TEXT))
            .put(HttpParameterMultimap.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.VALUES, ElasticsearchDatatype.TEXT)).build();

    @Override
    public ListMultimap<Class<?>, ElasticsearchFieldMapping> getClassFieldMappings() {
        return CLASS_FIELD_MAPPINGS;
    }
}
