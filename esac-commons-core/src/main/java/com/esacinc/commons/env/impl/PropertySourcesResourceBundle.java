package com.esacinc.commons.env.impl;

import com.esacinc.commons.env.utils.EsacPropertySourceUtils;
import com.esacinc.commons.utils.EsacIteratorUtils;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.annotation.Nullable;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.springframework.core.env.PropertySources;

public class PropertySourcesResourceBundle extends ResourceBundle {
    private PropertySources propSrcs;

    public PropertySourcesResourceBundle(PropertySources propSrcs) {
        this.propSrcs = propSrcs;
    }

    @Nullable
    @Override
    protected Object handleGetObject(String key) {
        return EsacIteratorUtils.stream(this.propSrcs.iterator()).filter(propSrc -> propSrc.containsProperty(key)).findFirst()
            .map(propSrc -> propSrc.getProperty(key)).orElse(null);
    }

    @Override
    public Enumeration<String> getKeys() {
        return new IteratorEnumeration<>(EsacPropertySourceUtils.streamNames(this.propSrcs).iterator());
    }
}
