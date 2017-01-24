package com.esacinc.commons.crypto.cert.logging.elasticsearch.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.utils.EsacStringUtils;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import java.security.cert.X509Certificate;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class CertificateElasticsearchFieldMapper implements ElasticsearchFieldMapper {
    private final static ListMultimap<Class<?>, ElasticsearchFieldMapping> CLASS_FIELD_MAPPINGS =
        ImmutableListMultimap.<Class<?>, ElasticsearchFieldMapping> builder()
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.CONTENT, ElasticsearchDatatype.BINARY))
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.INTERVAL, ElasticsearchDatatype.OBJECT))
            .put(X509Certificate.class,
                new ElasticsearchFieldMappingImpl((ElasticsearchFieldNames.INTERVAL + EsacStringUtils.PERIOD + ElasticsearchFieldNames.END),
                    ElasticsearchDatatype.DATE))
            .put(X509Certificate.class,
                new ElasticsearchFieldMappingImpl((ElasticsearchFieldNames.INTERVAL + EsacStringUtils.PERIOD + ElasticsearchFieldNames.START),
                    ElasticsearchDatatype.DATE))
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.ISSUER, ElasticsearchDatatype.OBJECT))
            .put(X509Certificate.class,
                new ElasticsearchFieldMappingImpl((ElasticsearchFieldNames.ISSUER + EsacStringUtils.PERIOD + ElasticsearchFieldNames.DN),
                    ElasticsearchDatatype.TEXT))
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.SERIAL_NUM, ElasticsearchDatatype.INTEGER))
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.SUBJECT, ElasticsearchDatatype.OBJECT))
            .put(X509Certificate.class,
                new ElasticsearchFieldMappingImpl((ElasticsearchFieldNames.SUBJECT + EsacStringUtils.PERIOD + ElasticsearchFieldNames.DN),
                    ElasticsearchDatatype.TEXT))
            .put(X509Certificate.class, new ElasticsearchFieldMappingImpl(ElasticsearchFieldNames.VERSION, ElasticsearchDatatype.INTEGER)).build();

    @Override
    public ListMultimap<Class<?>, ElasticsearchFieldMapping> getClassFieldMappings() {
        return CLASS_FIELD_MAPPINGS;
    }
}
