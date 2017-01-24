package com.esacinc.commons.test.context.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.ContextLoader;

public class EsacApplicationTestContextBootstrapper extends SpringBootTestContextBootstrapper {
    @Override
    protected List<ContextCustomizerFactory> getContextCustomizerFactories() {
        return Collections.emptyList();
    }

    @Override
    protected Class<? extends ContextLoader> getDefaultContextLoaderClass(Class<?> testClass) {
        return EsacApplicationContextLoader.class;
    }
}
