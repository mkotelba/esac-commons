package com.esacinc.commons.config.impl;

import com.esacinc.commons.config.EsacOption;

public class EsacOptionImpl<T> implements EsacOption<T> {
    private String name;
    private Class<T> valueClass;

    public EsacOptionImpl(String name, Class<T> valueClass) {
        this.name = name;
        this.valueClass = valueClass;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<T> getValueClass() {
        return this.valueClass;
    }
}
