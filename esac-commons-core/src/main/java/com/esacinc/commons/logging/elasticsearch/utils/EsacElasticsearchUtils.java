package com.esacinc.commons.logging.elasticsearch.utils;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;

public final class EsacElasticsearchUtils {
    public final static ListMultimap<Class<?>, ElasticsearchFieldMapping> EMPTY_CLASS_FIELD_MAPPINGS = ImmutableListMultimap.of();

    public final static ElasticsearchFieldMapping[] EMPTY_FIELD_MAPPINGS = new ElasticsearchFieldMapping[0];

    public final static Map<Class<?>, ElasticsearchDatatype> CLASS_DATATYPES =
        Stream.of(new ImmutablePair<>(String.class, ElasticsearchDatatype.TEXT), new ImmutablePair<>(Boolean.class, ElasticsearchDatatype.BOOLEAN),
            new ImmutablePair<>(Boolean.TYPE, ElasticsearchDatatype.BOOLEAN), new ImmutablePair<>(Byte.class, ElasticsearchDatatype.BYTE),
            new ImmutablePair<>(Byte.TYPE, ElasticsearchDatatype.BYTE), new ImmutablePair<>(Double.class, ElasticsearchDatatype.DOUBLE),
            new ImmutablePair<>(Double.TYPE, ElasticsearchDatatype.DOUBLE), new ImmutablePair<>(Float.class, ElasticsearchDatatype.FLOAT),
            new ImmutablePair<>(Float.TYPE, ElasticsearchDatatype.FLOAT), new ImmutablePair<>(Integer.class, ElasticsearchDatatype.INTEGER),
            new ImmutablePair<>(Integer.TYPE, ElasticsearchDatatype.INTEGER), new ImmutablePair<>(Long.class, ElasticsearchDatatype.LONG),
            new ImmutablePair<>(Long.TYPE, ElasticsearchDatatype.LONG), new ImmutablePair<>(Short.class, ElasticsearchDatatype.SHORT),
            new ImmutablePair<>(Short.TYPE, ElasticsearchDatatype.SHORT)).collect(EsacStreamUtils.toMap(LinkedHashMap::new));

    private EsacElasticsearchUtils() {
    }

    public static ElasticsearchDatatype findDatatype(Class<?> clazz) {
        return (CLASS_DATATYPES.containsKey(clazz)
            ? CLASS_DATATYPES.get(clazz) : (InetAddress.class.isAssignableFrom(clazz) ? ElasticsearchDatatype.IP : ElasticsearchDatatype.OBJECT));
    }
}
