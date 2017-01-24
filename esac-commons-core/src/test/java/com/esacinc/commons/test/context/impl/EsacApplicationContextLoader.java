package com.esacinc.commons.test.context.impl;

import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.context.impl.EsacApplicationBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;

public class EsacApplicationContextLoader extends SpringBootContextLoader {
    protected MergedContextConfiguration contextConfig;

    @Override
    public ApplicationContext loadContext(MergedContextConfiguration contextConfig) throws Exception {
        this.contextConfig = contextConfig;

        return super.loadContext(contextConfig);
    }

    @Override
    protected AbstractEsacApplication getSpringApplication() {
        return EsacApplicationBuilder.forConfigurationClass(this.contextConfig.getClasses()[0]).build();
    }

    @Override
    protected String[] getResourceSuffixes() {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }
}
