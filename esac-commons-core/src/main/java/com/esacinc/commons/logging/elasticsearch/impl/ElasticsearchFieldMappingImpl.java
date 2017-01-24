package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import javax.annotation.Nullable;

public class ElasticsearchFieldMappingImpl implements ElasticsearchFieldMapping {
    private final static String TO_STR_FORMAT = "{name=%s, datatype=%s, type=%s, format=%s, indexed=%s}";

    private String name;
    private ElasticsearchDatatype datatype;
    private Class<?> type;
    private String format;
    private boolean indexed;

    public ElasticsearchFieldMappingImpl(ElasticsearchFieldMapping fieldMapping, String namePrefix) {
        this((namePrefix + fieldMapping.getName()), fieldMapping.getDatatype(), fieldMapping.getType(), fieldMapping.getFormat(), fieldMapping.isIndexed());
    }

    public ElasticsearchFieldMappingImpl(String name, ElasticsearchDatatype datatype) {
        this(name, datatype, null);
    }

    public ElasticsearchFieldMappingImpl(String name, ElasticsearchDatatype datatype, @Nullable Class<?> type) {
        this(name, datatype, type, null, true);
    }

    public ElasticsearchFieldMappingImpl(String name, ElasticsearchDatatype datatype, @Nullable Class<?> type, @Nullable String format, boolean indexed) {
        this.name = name;
        this.datatype = datatype;
        this.type = ((type != null) ? type : this.datatype.getType());
        this.format = format;
        this.indexed = indexed;
    }

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, this.name, this.datatype.getName(), this.type, this.format, this.indexed);
    }

    @Override
    public ElasticsearchDatatype getDatatype() {
        return this.datatype;
    }

    @Override
    public boolean hasFormat() {
        return (this.format != null);
    }

    @Nullable
    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public boolean isIndexed() {
        return this.indexed;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasType() {
        return (this.type != null);
    }

    @Nullable
    @Override
    public Class<?> getType() {
        return this.type;
    }
}
