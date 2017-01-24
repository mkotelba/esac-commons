package com.esacinc.commons.crypto.impl;

import com.esacinc.commons.beans.factory.impl.AbstractEsacFactoryBean;
import com.esacinc.commons.crypto.CryptoService;
import java.security.Provider;

public abstract class AbstractCryptoServiceFactoryBean<T> extends AbstractEsacFactoryBean<T> implements CryptoService {
    protected String algName;
    protected Provider prov;

    protected AbstractCryptoServiceFactoryBean(Class<T> objClass) {
        super(objClass);
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }

    @Override
    public void setAlgorithmName(String algName) {
        this.algName = algName;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }

    @Override
    public void setProvider(Provider prov) {
        this.prov = prov;
    }
}
