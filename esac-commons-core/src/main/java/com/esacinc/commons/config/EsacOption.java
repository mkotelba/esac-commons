package com.esacinc.commons.config;

import com.esacinc.commons.beans.NamedBean;

public interface EsacOption<T> extends NamedBean {
    public Class<T> getValueClass();
}
