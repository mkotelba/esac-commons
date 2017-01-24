package com.esacinc.commons.net.http.impl;

import com.google.common.collect.ForwardingListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.utils.EsacMultimapUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class HttpParameterMultimapImpl extends ForwardingListMultimap<String, Object> implements HttpParameterMultimap {
    private ListMultimap<String, Object> delegate;

    public HttpParameterMultimapImpl(Map<String, ? extends Iterable<?>> props) {
        this();

        this.putAll(props);
    }

    public HttpParameterMultimapImpl() {
        this.delegate = Multimaps.newListMultimap(new TreeMap<>(), ArrayList::new);
    }

    @Override
    public String toString() {
        return EsacMultimapUtils.toString(this);
    }

    @Override
    public boolean putAll(Map<String, ? extends Iterable<?>> map) {
        if (map.isEmpty()) {
            return false;
        }

        boolean modified = false;

        for (String key : map.keySet()) {
            modified |= this.putAll(key, map.get(key));
        }

        return modified;
    }

    @Override
    public boolean putAll(String key, Object ... values) {
        if (values.length == 0) {
            return false;
        }

        boolean modified = false;

        for (Object value : values) {
            modified |= this.put(key, value);
        }

        return modified;
    }

    @Override
    protected ListMultimap<String, Object> delegate() {
        return this.delegate;
    }
}
