package com.esacinc.commons.json.impl;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.module.SimpleValueInstantiators;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.esacinc.commons.utils.EsacStringUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

public class EsacModule extends Module {
    private final static String DEFAULT_NAME_PREFIX = EsacModule.class.getSimpleName() + EsacStringUtils.HYPHEN;

    private String name;
    private Version version;
    private Map<Class<?>, Class<?>> abstractTypes;
    private AnnotationIntrospector[] annoIntrospectors;
    private BeanDeserializerModifier[] beanDeserializerModifiers;
    private BeanSerializerModifier[] beanSerializerModifiers;
    private ClassIntrospector classIntrospector;
    private DeserializationProblemHandler[] deserializationProblemHandlers;
    private JsonDeserializer<?>[] deserializers;
    private KeyDeserializer[] keyDeserializers;
    private JsonSerializer<?>[] keySerializers;
    private Map<Class<?>, Class<?>> mixInAnnos;
    private PropertyNamingStrategy namingStrategy;
    private JsonSerializer<?>[] serializers;
    private NamedType[] subtypes;
    private TypeModifier[] typeModifiers;
    private ValueInstantiator[] valueInstantiators;

    public EsacModule() {
        this(null);
    }

    public EsacModule(@Nullable String name) {
        this(name, Version.unknownVersion());
    }

    public EsacModule(@Nullable String name, Version version) {
        this.name = ((name != null) ? name : (DEFAULT_NAME_PREFIX + System.identityHashCode(this)));
        this.version = version;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setupModule(SetupContext context) {
        if (this.hasAbstractTypes()) {
            SimpleAbstractTypeResolver abstractTypeResolver = new SimpleAbstractTypeResolver();

            this.abstractTypes.forEach((superType, subType) -> abstractTypeResolver.addMapping(((Class<Object>) superType), subType));

            context.addAbstractTypeResolver(abstractTypeResolver);
        }

        if (this.hasAnnotationIntrospectors()) {
            Stream.of(this.annoIntrospectors).forEach(context::appendAnnotationIntrospector);
        }

        if (this.hasBeanDeserializerModifiers()) {
            Stream.of(this.beanDeserializerModifiers).forEach(context::addBeanDeserializerModifier);
        }

        if (this.hasBeanSerializerModifiers()) {
            Stream.of(this.beanSerializerModifiers).forEach(context::addBeanSerializerModifier);
        }

        if (this.hasClassIntrospector()) {
            context.setClassIntrospector(this.classIntrospector);
        }

        if (this.hasDeserializationProblemHandlers()) {
            Stream.of(this.deserializationProblemHandlers).forEach(context::addDeserializationProblemHandler);
        }

        if (this.hasDeserializers()) {
            context.addDeserializers(new SimpleDeserializers(
                Stream.of(this.deserializers).collect(EsacStreamUtils.toMap(JsonDeserializer::handledType, Function.identity(), LinkedHashMap::new))));
        }

        if (this.hasKeyDeserializers()) {
            SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();

            Stream.of(this.keyDeserializers).forEach(
                keyDeserializer -> keyDeserializers.addDeserializer(((JsonDeserializer<?>) ((Object) keyDeserializer)).handledType(), keyDeserializer));

            context.addKeyDeserializers(keyDeserializers);
        }

        if (this.hasKeySerializers()) {
            context.addKeySerializers(new SimpleSerializers(Stream.of(this.keySerializers).collect(Collectors.toList())));
        }

        if (this.hasMixInAnnotations()) {
            this.mixInAnnos.forEach(context::setMixInAnnotations);
        }

        if (this.hasNamingStrategy()) {
            context.setNamingStrategy(this.namingStrategy);
        }

        if (this.hasSerializers()) {
            context.addSerializers(new SimpleSerializers(Stream.of(this.serializers).collect(Collectors.toList())));
        }

        if (this.hasSubtypes()) {
            context.registerSubtypes(this.subtypes);
        }

        if (this.hasTypeModifiers()) {
            Stream.of(this.typeModifiers).forEach(context::addTypeModifier);
        }

        if (this.hasValueInstantiators()) {
            SimpleValueInstantiators valueInstantiators = new SimpleValueInstantiators();

            Stream.of(this.valueInstantiators)
                .forEach(valueInstantiator -> valueInstantiators.addValueInstantiator(valueInstantiator.getValueClass(), valueInstantiator));

            context.addValueInstantiators(valueInstantiators);
        }
    }

    public boolean hasAbstractTypes() {
        return !MapUtils.isEmpty(this.abstractTypes);
    }

    @Nullable
    public Map<Class<?>, Class<?>> getAbstractTypes() {
        return this.abstractTypes;
    }

    public void setAbstractTypes(@Nullable Map<Class<?>, Class<?>> abstractTypes) {
        this.abstractTypes = abstractTypes;
    }

    public boolean hasAnnotationIntrospectors() {
        return !ArrayUtils.isEmpty(this.annoIntrospectors);
    }

    @Nullable
    public AnnotationIntrospector[] getAnnotationIntrospectors() {
        return this.annoIntrospectors;
    }

    public void setAnnotationIntrospectors(AnnotationIntrospector ... annoIntrospectors) {
        this.annoIntrospectors = annoIntrospectors;
    }

    public boolean hasBeanDeserializerModifiers() {
        return !ArrayUtils.isEmpty(this.beanDeserializerModifiers);
    }

    @Nullable
    public BeanDeserializerModifier[] getBeanDeserializerModifiers() {
        return this.beanDeserializerModifiers;
    }

    public void setBeanDeserializerModifiers(BeanDeserializerModifier ... beanDeserializerModifiers) {
        this.beanDeserializerModifiers = beanDeserializerModifiers;
    }

    public boolean hasBeanSerializerModifiers() {
        return !ArrayUtils.isEmpty(this.beanSerializerModifiers);
    }

    @Nullable
    public BeanSerializerModifier[] getBeanSerializerModifiers() {
        return this.beanSerializerModifiers;
    }

    public void setBeanSerializerModifiers(BeanSerializerModifier ... beanSerializerModifiers) {
        this.beanSerializerModifiers = beanSerializerModifiers;
    }

    public boolean hasClassIntrospector() {
        return (this.classIntrospector != null);
    }

    @Nullable
    public ClassIntrospector getClassIntrospector() {
        return this.classIntrospector;
    }

    public void setClassIntrospector(@Nullable ClassIntrospector classIntrospector) {
        this.classIntrospector = classIntrospector;
    }

    public boolean hasDeserializationProblemHandlers() {
        return !ArrayUtils.isEmpty(this.deserializationProblemHandlers);
    }

    @Nullable
    public DeserializationProblemHandler[] getDeserializationProblemHandlers() {
        return this.deserializationProblemHandlers;
    }

    public void setDeserializationProblemHandlers(DeserializationProblemHandler ... deserializationProblemHandlers) {
        this.deserializationProblemHandlers = deserializationProblemHandlers;
    }

    public boolean hasDeserializers() {
        return !ArrayUtils.isEmpty(this.deserializers);
    }

    @Nullable
    public JsonDeserializer<?>[] getDeserializers() {
        return this.deserializers;
    }

    public void setDeserializers(JsonDeserializer<?> ... deserializers) {
        this.deserializers = deserializers;
    }

    public boolean hasKeyDeserializers() {
        return !ArrayUtils.isEmpty(this.keyDeserializers);
    }

    @Nullable
    public KeyDeserializer[] getKeyDeserializers() {
        return this.keyDeserializers;
    }

    public void setKeyDeserializers(KeyDeserializer ... keyDeserializers) {
        this.keyDeserializers = keyDeserializers;
    }

    public boolean hasKeySerializers() {
        return !ArrayUtils.isEmpty(this.keySerializers);
    }

    @Nullable
    public JsonSerializer<?>[] getKeySerializers() {
        return this.keySerializers;
    }

    public void setKeySerializers(JsonSerializer<?> ... keySerializers) {
        this.keySerializers = keySerializers;
    }

    public boolean hasMixInAnnotations() {
        return !MapUtils.isEmpty(this.mixInAnnos);
    }

    @Nullable
    public Map<Class<?>, Class<?>> getMixInAnnotations() {
        return this.mixInAnnos;
    }

    public void setMixInAnnotations(@Nullable Map<Class<?>, Class<?>> mixInAnnos) {
        this.mixInAnnos = mixInAnnos;
    }

    @Override
    public String getModuleName() {
        return this.name;
    }

    public boolean hasNamingStrategy() {
        return (this.namingStrategy != null);
    }

    @Nullable
    public PropertyNamingStrategy getNamingStrategy() {
        return this.namingStrategy;
    }

    public void setNamingStrategy(@Nullable PropertyNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public boolean hasSerializers() {
        return !ArrayUtils.isEmpty(this.serializers);
    }

    @Nullable
    public JsonSerializer<?>[] getSerializers() {
        return this.serializers;
    }

    public void setSerializers(JsonSerializer<?> ... serializers) {
        this.serializers = serializers;
    }

    public boolean hasSubtypes() {
        return !ArrayUtils.isEmpty(this.subtypes);
    }

    @Nullable
    public NamedType[] getSubtypes() {
        return this.subtypes;
    }

    public void setSubtypes(Class<?> ... subtypes) {
        this.setSubtypes(Stream.of(subtypes).map(NamedType::new).toArray(NamedType[]::new));
    }

    public void setSubtypes(NamedType ... subtypes) {
        this.subtypes = subtypes;
    }

    @Nullable
    @Override
    public Object getTypeId() {
        return null;
    }

    public boolean hasTypeModifiers() {
        return !ArrayUtils.isEmpty(this.typeModifiers);
    }

    @Nullable
    public TypeModifier[] getTypeModifiers() {
        return this.typeModifiers;
    }

    public void setTypeModifiers(TypeModifier ... typeModifiers) {
        this.typeModifiers = typeModifiers;
    }

    public boolean hasValueInstantiators() {
        return !ArrayUtils.isEmpty(this.valueInstantiators);
    }

    @Nullable
    public ValueInstantiator[] getValueInstantiators() {
        return this.valueInstantiators;
    }

    public void setValueInstantiators(ValueInstantiator ... valueInstantiators) {
        this.valueInstantiators = valueInstantiators;
    }

    @Override
    public Version version() {
        return this.version;
    }
}
