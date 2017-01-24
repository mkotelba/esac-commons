package com.esacinc.commons.crypto.store.impl;

public class LoadKeyStoreBeanPostProcessor extends AbstractKeyStoreBeanPostProcessor {
    @Override
    protected EsacKeyStore postProcessAfterInitializationInternal(EsacKeyStore bean, String beanName) throws Exception {
        bean.getSpi().initialize();

        return super.postProcessAfterInitializationInternal(bean, beanName);
    }
}
