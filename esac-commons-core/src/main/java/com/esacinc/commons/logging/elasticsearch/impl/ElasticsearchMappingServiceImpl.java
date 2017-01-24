package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.utils.EsacStringUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.impl.PropertyTrieImpl;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchIndexMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchMappingService;
import com.esacinc.commons.logging.elasticsearch.utils.EsacElasticsearchUtils;
import com.esacinc.commons.utils.EsacMultimapUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class ElasticsearchMappingServiceImpl implements ElasticsearchMappingService {
    @Autowired
    private List<ElasticsearchFieldMapper> fieldMappers;

    private TypeFactory typeFactory;
    private SerializationConfig serializationConfig;
    private JsonFactory jsonFactory;
    private ElasticsearchIndexMapping indexMapping;
    private NavigableMap<Class<?>, PropertyTrie<ElasticsearchFieldMapping>> classFieldMappings = new TreeMap<>(Comparator.comparing(Class::getName));

    public ElasticsearchMappingServiceImpl(ObjectMapper objMapper) {
        this.typeFactory = objMapper.getTypeFactory();
        this.serializationConfig = objMapper.getSerializationConfig();
        this.jsonFactory = objMapper.getFactory();
    }

    @Override
    public byte[] encodeIndexMapping() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try (JsonGenerator jsonGen = this.jsonFactory.createGenerator(outStream, JsonEncoding.UTF8)) {
            jsonGen.writeObject(this.indexMapping);
            jsonGen.flush();
        }

        return outStream.toByteArray();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationAwareOrderComparator.sort(this.fieldMappers);

        PropertyTrie<ElasticsearchFieldMapping> fieldMappings = this.indexMapping.getFieldMappings();
        String fieldName;
        ElasticsearchDatatype fieldDatatype;

        for (ElasticsearchFieldMapper fieldMapper : this.fieldMappers) {
            if (fieldMapper.hasClassFieldMappings()) {
                EsacMultimapUtils.forEachList(fieldMapper.getClassFieldMappings(),
                    (classFieldClass, classFieldMappingItems) -> this.classFieldMappings.put(classFieldClass,
                        classFieldMappingItems.stream().collect(EsacStreamUtils.toMap(NamedBean::getName, Function.identity(), PropertyTrieImpl::new))));
            }

            if (fieldMapper.hasFieldMappings()) {
                for (ElasticsearchFieldMapping fieldMapping : fieldMapper.getFieldMappings()) {
                    fieldName = fieldMapping.getName();

                    if ((((fieldDatatype = fieldMapping.getDatatype()) == ElasticsearchDatatype.OBJECT) || (fieldDatatype == ElasticsearchDatatype.NESTED)) &&
                        fieldMapping.hasType()) {
                        fieldMappings.putAll(this.buildClassFieldMappings(fieldMapping.getType(), (fieldName + EsacStringUtils.PERIOD)));
                    } else {
                        fieldMappings.put(fieldName, fieldMapping);
                    }
                }
            }
        }

        fieldMappings.entrySet().stream().filter(fieldMappingEntry -> (fieldMappingEntry.getValue() == null)).forEach(
            fieldMappingEntry -> fieldMappingEntry.setValue(new ElasticsearchFieldMappingImpl(fieldMappingEntry.getKey(), ElasticsearchDatatype.OBJECT)));
    }

    private PropertyTrie<ElasticsearchFieldMapping> buildClassFieldMappings(Class<?> clazz, String classFieldNamePrefix) {
        return this.buildClassFieldMappings(clazz).entrySet().stream()
            .map(classFieldMappingItemEntry -> new ElasticsearchFieldMappingImpl(classFieldMappingItemEntry.getValue(), classFieldNamePrefix))
            .collect(EsacStreamUtils.toMap(NamedBean::getName, Function.identity(), PropertyTrieImpl::new));
    }

    private PropertyTrie<ElasticsearchFieldMapping> buildClassFieldMappings(Class<?> clazz) {
        if (this.classFieldMappings.containsKey(clazz)) {
            return this.classFieldMappings.get(clazz);
        } else {
            PropertyTrie<ElasticsearchFieldMapping> classFieldMappingItems = new PropertyTrieImpl<>();
            this.classFieldMappings.put(clazz, classFieldMappingItems);

            String classFieldName, classFieldFormat;
            AnnotatedMethod classFieldGetterMethod;
            ElasticsearchField classFieldAnno;
            ElasticsearchDatatype classFieldDatatype;
            boolean classFieldIndexed;
            JavaType classFieldType;
            Class<?> classFieldClass;

            for (BeanPropertyDefinition classPropDef : this.serializationConfig.introspect(this.typeFactory.constructType(clazz)).findProperties().stream()
                .filter(BeanPropertyDefinition::hasGetter).sorted(Comparator.comparing(BeanPropertyDefinition::getName))
                .toArray(BeanPropertyDefinition[]::new)) {
                classFieldName = classPropDef.getName();

                if ((classFieldAnno = (classFieldGetterMethod = classPropDef.getGetter()).getAnnotation(ElasticsearchField.class)) != null) {
                    classFieldDatatype = classFieldAnno.datatype();
                    classFieldFormat = StringUtils.defaultIfEmpty(classFieldAnno.format(), null);
                    classFieldIndexed = classFieldAnno.indexed();
                } else {
                    classFieldDatatype = ElasticsearchDatatype.DEFAULT;
                    classFieldFormat = null;
                    classFieldIndexed = true;
                }

                classFieldClass = (((classFieldType = classFieldGetterMethod.getType()).isArrayType() || classFieldType.isCollectionLikeType())
                    ? classFieldType.getContentType().getRawClass() : classFieldType.getRawClass());

                if (classFieldDatatype == ElasticsearchDatatype.DEFAULT) {
                    classFieldDatatype = EsacElasticsearchUtils.findDatatype(classFieldClass);
                }

                if ((classFieldDatatype == ElasticsearchDatatype.OBJECT) || (classFieldDatatype == ElasticsearchDatatype.NESTED)) {
                    if (classFieldGetterMethod.hasAnnotation(JsonUnwrapped.class)) {
                        this.buildClassFieldMappings(classFieldClass).forEach(classFieldMappingItems::putIfAbsent);
                    } else {
                        classFieldMappingItems.put(classFieldName,
                            new ElasticsearchFieldMappingImpl(classFieldName, classFieldDatatype, classFieldClass, classFieldFormat, classFieldIndexed));

                        this.buildClassFieldMappings(classFieldClass, (classFieldName + EsacStringUtils.PERIOD)).forEach(classFieldMappingItems::putIfAbsent);
                    }
                } else {
                    classFieldMappingItems.put(classFieldName,
                        new ElasticsearchFieldMappingImpl(classFieldName, classFieldDatatype, null, classFieldFormat, classFieldIndexed));
                }
            }

            classFieldMappingItems.entrySet().stream().filter(classFieldMappingItemEntry -> (classFieldMappingItemEntry.getValue() == null))
                .forEach(classFieldMappingItemEntry -> classFieldMappingItemEntry
                    .setValue(new ElasticsearchFieldMappingImpl(classFieldMappingItemEntry.getKey(), ElasticsearchDatatype.OBJECT)));

            return classFieldMappingItems;
        }
    }

    @Override
    public ElasticsearchIndexMapping getIndexMapping() {
        return this.indexMapping;
    }

    @Override
    public void setIndexMapping(ElasticsearchIndexMapping indexMapping) {
        this.indexMapping = indexMapping;
    }
}
