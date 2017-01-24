package com.esacinc.commons.crypto.factory.impl;

import com.esacinc.commons.beans.factory.impl.AbstractFactoryRequest;
import com.esacinc.commons.crypto.CryptoConfig;
import com.esacinc.commons.crypto.factory.CryptoFactoryRequest;

public abstract class AbstractCryptoFactoryRequest<T extends CryptoConfig> extends AbstractFactoryRequest<T> implements CryptoFactoryRequest<T> {
    protected AbstractCryptoFactoryRequest(T descriptor) {
        super(descriptor);
    }
}
