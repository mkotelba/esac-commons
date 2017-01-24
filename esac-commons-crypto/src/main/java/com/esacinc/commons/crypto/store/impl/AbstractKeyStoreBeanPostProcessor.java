package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.beans.factory.impl.AbstractEsacBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order((Ordered.HIGHEST_PRECEDENCE + 1))
public abstract class AbstractKeyStoreBeanPostProcessor extends AbstractEsacBeanPostProcessor<EsacKeyStore> {
    protected AbstractKeyStoreBeanPostProcessor() {
        super(EsacKeyStore.class);
    }
}
