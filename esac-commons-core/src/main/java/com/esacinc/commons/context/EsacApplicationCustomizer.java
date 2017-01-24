package com.esacinc.commons.context;

import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.context.impl.EsacApplicationBuilder;

public interface EsacApplicationCustomizer<T extends AbstractEsacApplication> {
    public void customizeApplication(EsacApplicationBuilder<T> appBuilder);
}
