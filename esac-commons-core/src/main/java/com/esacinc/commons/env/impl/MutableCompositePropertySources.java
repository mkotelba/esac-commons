package com.esacinc.commons.env.impl;

import com.esacinc.commons.env.utils.EsacPropertySourceUtils;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

public class MutableCompositePropertySources extends EnumerablePropertySource<MutablePropertySources> implements PropertySources {
    public class MutableCompositePropertyEntrySet extends AbstractSet<Entry<String, Object>> {
        @Override
        public Iterator<Entry<String, Object>> iterator() {
            MutablePropertySources propSrcs = MutableCompositePropertySources.this.getSource();

            return (!EsacPropertySourceUtils.isEmpty(propSrcs) ? EsacPropertySourceUtils.streamEntries(propSrcs).iterator() : IteratorUtils.emptyIterator());
        }

        @Override
        public boolean isEmpty() {
            return MutableCompositePropertySources.this.isEmpty();
        }

        @Nonnegative
        @Override
        public int size() {
            return MutableCompositePropertySources.this.size();
        }
    }

    public class MutableCompositePropertyMap extends AbstractMap<String, Object> {
        private MutableCompositePropertyEntrySet entries = new MutableCompositePropertyEntrySet();

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return this.entries;
        }
    }

    private MutableCompositePropertyMap map = new MutableCompositePropertyMap();

    public MutableCompositePropertySources(String srcName) {
        this(srcName, null);
    }

    public MutableCompositePropertySources(String srcName, @Nullable PropertySources propSrcs) {
        super(srcName, ((propSrcs != null) ? new MutablePropertySources(propSrcs) : new MutablePropertySources()));
    }

    public MutableCompositePropertyMap asMap() {
        return this.map;
    }

    public void clear() {
        MutablePropertySources propSrcs = this.getSource();

        if (EsacPropertySourceUtils.isEmpty(propSrcs)) {
            return;
        }

        EsacPropertySourceUtils.streamSources(propSrcs).forEach(propSrc -> this.remove(propSrc.getName()));
    }

    public void replace(String propSrcName, PropertySource<?> propSrc) {
        this.getSource().replace(propSrcName, propSrc);
    }

    public void removeAll(PropertySources propSrcs) {
        removeAll(EsacPropertySourceUtils.streamSources(propSrcs));
    }

    public void removeAll(Stream<PropertySource<?>> propSrcs) {
        propSrcs.forEach(propSrc -> this.remove(propSrc.getName()));
    }

    public void remove(String propSrcName) {
        this.getSource().remove(propSrcName);
    }

    public void addAllAfter(String propSrcName, PropertySources propSrcs) {
        addAllAfter(propSrcName, EsacPropertySourceUtils.streamSources(propSrcs));
    }

    public void addAllAfter(String propSrcName, Stream<PropertySource<?>> propSrcs) {
        propSrcs.forEach(propSrc -> this.addAfter(propSrcName, propSrc));
    }

    public void addAfter(String propSrcName, PropertySource<?> propSrc) {
        this.getSource().addAfter(propSrcName, propSrc);
    }

    public void addAllBefore(String propSrcName, PropertySources propSrcs) {
        addAllBefore(propSrcName, EsacPropertySourceUtils.streamSources(propSrcs));
    }

    public void addAllBefore(String propSrcName, Stream<PropertySource<?>> propSrcs) {
        propSrcs.forEach(propSrc -> this.addBefore(propSrcName, propSrc));
    }

    public void addBefore(String propSrcName, PropertySource<?> propSrc) {
        this.getSource().addBefore(propSrcName, propSrc);
    }

    public void addAllLast(PropertySources propSrcs) {
        addAllLast(EsacPropertySourceUtils.streamSources(propSrcs));
    }

    public void addAllLast(Stream<PropertySource<?>> propSrcs) {
        propSrcs.forEach(this::addLast);
    }

    public void addLast(PropertySource<?> propSrc) {
        this.getSource().addLast(propSrc);
    }

    public void addAllFirst(PropertySources propSrcs) {
        addAllFirst(EsacPropertySourceUtils.streamSources(propSrcs));
    }

    public void addAllFirst(Stream<PropertySource<?>> propSrcs) {
        propSrcs.forEach(this::addFirst);
    }

    public void addFirst(PropertySource<?> propSrc) {
        this.getSource().addFirst(propSrc);
    }

    @Override
    public Iterator<PropertySource<?>> iterator() {
        return this.getSource().iterator();
    }

    @Nullable
    @Override
    public PropertySource<?> get(String propSrcName) {
        MutablePropertySources propSrcs = this.getSource();

        return (!EsacPropertySourceUtils.isEmpty(propSrcs)
            ? EsacPropertySourceUtils.streamSources(propSrcs).filter(propSrc -> propSrc.getName().equals(propSrcName)).findFirst().orElse(null) : null);
    }

    @Override
    public boolean contains(String propSrcName) {
        MutablePropertySources propSrcs = this.getSource();

        return (!EsacPropertySourceUtils.isEmpty(propSrcs) &&
            EsacPropertySourceUtils.streamSources(propSrcs).noneMatch(propSrc -> propSrc.getName().equals(propSrcName)));
    }

    public boolean isEmpty() {
        MutablePropertySources propSrcs = this.getSource();

        return (EsacPropertySourceUtils.isEmpty(propSrcs) || EsacPropertySourceUtils.streamSources(propSrcs)
            .noneMatch(propSrc -> ((propSrc instanceof EnumerablePropertySource<?>) && ((EnumerablePropertySource<?>) propSrc).getPropertyNames().length > 0)));
    }

    @Nonnegative
    public int size() {
        MutablePropertySources propSrcs = this.getSource();

        if (EsacPropertySourceUtils.isEmpty(propSrcs)) {
            return 0;
        }

        return EsacPropertySourceUtils.streamSources(propSrcs)
            .mapToInt(propSrc -> ((propSrc instanceof EnumerablePropertySource<?>) ? ((EnumerablePropertySource<?>) propSrc).getPropertyNames().length : 0))
            .sum();
    }

    @Nullable
    @Override
    public Object getProperty(String propName) {
        MutablePropertySources propSrcs = this.getSource();

        return (!EsacPropertySourceUtils.isEmpty(propSrcs)
            ? EsacPropertySourceUtils.streamSources(propSrcs)
                .filter(propSrc -> ((propSrc instanceof EnumerablePropertySource<?>) &&
                    ArrayUtils.contains(((EnumerablePropertySource<?>) propSrc).getPropertyNames(), propName)))
                .findFirst().map(propSrc -> propSrc.getProperty(propName)).orElse(null)
            : null);
    }

    @Override
    public String[] getPropertyNames() {
        MutablePropertySources propSrcs = this.getSource();

        if (EsacPropertySourceUtils.isEmpty(propSrcs)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return EsacPropertySourceUtils.streamNames(propSrcs).toArray(String[]::new);
    }
}
