package com.esacinc.commons.config.property.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.google.common.collect.ForwardingNavigableMap;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.utils.EsacPropertyNameUtils;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import org.apache.commons.collections4.OrderedMapIterator;

public class PropertyTrieImpl<T> extends ForwardingNavigableMap<String, T> implements PropertyTrie<T> {
    private NavigableMap<String, T> delegate;
    private NavigableSet<String> rootKeys = new TreeSet<>();

    public PropertyTrieImpl() {
        this(new TreeMap<>(PropertyNameComparator.INSTANCE));
    }

    public PropertyTrieImpl(NavigableMap<String, T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public NavigableMap<String, T> prefixMap(String key) {
        return (this.containsBranchKey(key)
            ? this.subMap(key, false, (key + EsacPropertyNameUtils.DESCENDANT_CEILING_PROP_NAME_SUFFIX), false) : Collections.emptyNavigableMap());
    }

    @Override
    public OrderedMapIterator<String, T> mapIterator() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T remove(Object keyObj) {
        if (!this.containsKey(keyObj)) {
            return null;
        }

        String key = ((String) keyObj), testKey;
        T prevValue = super.remove(key);

        while (((testKey = this.higherKey(key)) != null) && EsacPropertyNameUtils.isDescendant(key, testKey)) {
            // noinspection ResultOfMethodCallIgnored
            super.remove(testKey);
        }

        if ((testKey = EsacPropertyNameUtils.getParent(key)) == null) {
            this.rootKeys.remove(key);
        } else {
            String higherTestKey = this.higherKey(testKey);

            if ((higherTestKey == null) || !EsacPropertyNameUtils.isChild(testKey, higherTestKey)) {
                this.remove(testKey);
            }
        }

        return prevValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        map.forEach(this::put);
    }

    @Nullable
    @Override
    public T putIfAbsent(String key, @Nullable T value) {
        return (this.containsKey(key) ? null : this.put(key, value));
    }

    @Nullable
    @Override
    public T put(String key, @Nullable T value) {
        String parentKey = EsacPropertyNameUtils.getParent(key);

        if (parentKey == null) {
            this.rootKeys.add(key);
        } else if (!this.containsKey(parentKey)) {
            this.put(parentKey, null);
        }

        return super.put(key, value);
    }

    @Override
    public boolean containsLeafKey(String key) {
        if (!this.containsKey(key)) {
            return false;
        }

        String higherKey = this.higherKey(key);

        return ((higherKey == null) || !EsacPropertyNameUtils.isChild(key, higherKey));
    }

    @Override
    public boolean containsBranchKey(String key) {
        if (!this.containsKey(key)) {
            return false;
        }

        String higherKey = this.higherKey(key);

        return ((higherKey != null) && EsacPropertyNameUtils.isChild(key, higherKey));
    }

    @Override
    public boolean containsNextKey(String key) {
        return (this.nextKey(key) != null);
    }

    @Nullable
    @Override
    public String nextKey(String key) {
        return (this.containsKey(key) ? this.higherKey(key) : null);
    }

    @Override
    public boolean containsPreviousKey(String key) {
        return (this.previousKey(key) != null);
    }

    @Nullable
    @Override
    public String previousKey(String key) {
        return (this.containsKey(key) ? this.lowerKey(key) : null);
    }

    @Override
    public boolean containsHigherKey(String key) {
        return (this.higherKey(key) != null);
    }

    @Override
    public boolean containsLowerKey(String key) {
        return (this.lowerKey(key) != null);
    }

    @Override
    public String toString() {
        return EsacMapUtils.toString(this);
    }

    @Override
    protected NavigableMap<String, T> delegate() {
        return this.delegate;
    }

    @Override
    public NavigableSet<String> getRootKeys() {
        return this.rootKeys;
    }
}
