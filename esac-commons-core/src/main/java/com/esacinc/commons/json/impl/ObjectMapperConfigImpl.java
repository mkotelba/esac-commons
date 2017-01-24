package com.esacinc.commons.json.impl;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.esacinc.commons.json.ObjectMapperConfig;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.FatalBeanException;

public class ObjectMapperConfigImpl implements ObjectMapperConfig {
    private DateFormat dateFormat;
    private Map<Object, Boolean> features =
        Stream.of(new ImmutablePair<>(MapperFeature.AUTO_DETECT_CREATORS, false), new ImmutablePair<>(MapperFeature.AUTO_DETECT_FIELDS, false),
            new ImmutablePair<>(MapperFeature.AUTO_DETECT_GETTERS, false), new ImmutablePair<>(MapperFeature.AUTO_DETECT_GETTERS, false),
            new ImmutablePair<>(MapperFeature.AUTO_DETECT_IS_GETTERS, false), new ImmutablePair<>(MapperFeature.AUTO_DETECT_SETTERS, false),
            new ImmutablePair<>(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)).collect(EsacStreamUtils.toMap(LinkedHashMap::new));
    private FilterProvider filterProv;
    private HandlerInstantiator handlerInstantiator;
    private Locale locale;
    private List<Module> modules = new ArrayList<>();
    private PrettyPrinter prettyPrinter;
    private Value propInclusion = Value.construct(Include.NON_DEFAULT, Include.NON_DEFAULT);
    private TimeZone timeZone;

    @Override
    public void configure(ObjectMapper objMapper) {
        if (this.hasModules()) {
            objMapper.registerModules(this.modules);
        }

        if (this.hasFeatures()) {
            this.features.forEach((featureKey, featureValue) -> configureFeature(objMapper, featureKey, featureValue));
        }

        if (this.hasDateFormat()) {
            objMapper.setDateFormat(this.dateFormat);
        }

        if (this.hasFilterProvider()) {
            objMapper.setFilterProvider(this.filterProv);
        }

        if (this.hasHandlerInstantiator()) {
            objMapper.setHandlerInstantiator(this.handlerInstantiator);
        }

        if (this.hasLocale()) {
            objMapper.setLocale(this.locale);
        }

        if (this.hasPrettyPrinter()) {
            objMapper.setDefaultPrettyPrinter(this.prettyPrinter);
        }

        objMapper.setPropertyInclusion(this.propInclusion);

        if (this.hasTimeZone()) {
            objMapper.setTimeZone(this.timeZone);
        }
    }

    @Override
    public void merge(ObjectMapperConfig config) {
        if (config.hasDateFormat()) {
            this.dateFormat = config.getDateFormat();
        }

        if (config.hasFeatures()) {
            this.features.putAll(config.getFeatures());
        }

        if (config.hasFilterProvider()) {
            this.filterProv = config.getFilterProvider();
        }

        if (config.hasHandlerInstantiator()) {
            this.handlerInstantiator = config.getHandlerInstantiator();
        }

        if (config.hasLocale()) {
            this.locale = config.getLocale();
        }

        if (config.hasModules()) {
            this.modules.addAll(config.getModules());
        }

        if (config.hasPrettyPrinter()) {
            this.prettyPrinter = config.getPrettyPrinter();
        }

        this.propInclusion = Value.merge(this.propInclusion, config.getPropertyInclusion());

        if (config.hasTimeZone()) {
            this.timeZone = config.getTimeZone();
        }
    }

    private static void configureFeature(ObjectMapper objMapper, Object featureKey, Boolean featureValue) {
        if (featureKey instanceof Feature) {
            objMapper.configure(((Feature) featureKey), featureValue);
        } else if (featureKey instanceof JsonGenerator.Feature) {
            objMapper.configure(((JsonGenerator.Feature) featureKey), featureValue);
        } else if (featureKey instanceof DeserializationFeature) {
            objMapper.configure(((DeserializationFeature) featureKey), featureValue);
        } else if (featureKey instanceof SerializationFeature) {
            objMapper.configure(((SerializationFeature) featureKey), featureValue);
        } else if (featureKey instanceof MapperFeature) {
            objMapper.configure(((MapperFeature) featureKey), featureValue);
        } else {
            throw new FatalBeanException(String.format("Unknown feature key (class=%s).", featureKey.getClass().getName()));
        }
    }

    @Override
    public Include getContentInclusion() {
        return this.propInclusion.getContentInclusion();
    }

    @Override
    public void setContentInclusion(Include contentInclusion) {
        this.propInclusion = this.propInclusion.withContentInclusion(contentInclusion);
    }

    @Override
    public boolean hasDateFormat() {
        return (this.dateFormat != null);
    }

    @Nullable
    @Override
    public DateFormat getDateFormat() {
        return this.dateFormat;
    }

    @Override
    public void setDateFormat(@Nullable DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean hasFeatures() {
        return !this.features.isEmpty();
    }

    @Override
    public Map<Object, Boolean> getFeatures() {
        return this.features;
    }

    @Override
    public void setFeatures(@Nullable Map<Object, Boolean> features) {
        this.features.clear();

        if (!MapUtils.isEmpty(features)) {
            this.features.putAll(features);
        }
    }

    @Override
    public boolean hasFilterProvider() {
        return (this.filterProv != null);
    }

    @Nullable
    @Override
    public FilterProvider getFilterProvider() {
        return this.filterProv;
    }

    @Override
    public void setFilterProvider(@Nullable FilterProvider filterProv) {
        this.filterProv = filterProv;
    }

    @Override
    public boolean hasHandlerInstantiator() {
        return (this.handlerInstantiator != null);
    }

    @Nullable
    @Override
    public HandlerInstantiator getHandlerInstantiator() {
        return this.handlerInstantiator;
    }

    @Override
    public void setHandlerInstantiator(@Nullable HandlerInstantiator handlerInstantiator) {
        this.handlerInstantiator = handlerInstantiator;
    }

    @Override
    public boolean hasLocale() {
        return (this.locale != null);
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(@Nullable Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean hasModules() {
        return !this.modules.isEmpty();
    }

    @Override
    public List<Module> getModules() {
        return this.modules;
    }

    @Override
    public void setModules(@Nullable List<Module> modules) {
        this.modules.clear();

        if (!CollectionUtils.isEmpty(modules)) {
            this.modules.addAll(modules);
        }
    }

    @Override
    public boolean hasPrettyPrinter() {
        return (this.prettyPrinter != null);
    }

    @Nullable
    @Override
    public PrettyPrinter getPrettyPrinter() {
        return this.prettyPrinter;
    }

    @Override
    public void setPrettyPrinter(@Nullable PrettyPrinter prettyPrinter) {
        this.prettyPrinter = prettyPrinter;
    }

    @Override
    public Value getPropertyInclusion() {
        return this.propInclusion;
    }

    @Override
    public boolean hasTimeZone() {
        return (this.timeZone != null);
    }

    @Nullable
    @Override
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override
    public void setTimeZone(@Nullable TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public Include getValueInclusion() {
        return this.propInclusion.getValueInclusion();
    }

    @Override
    public void setValueInclusion(Include valueInclusion) {
        this.propInclusion = this.propInclusion.withValueInclusion(valueInclusion);
    }
}
