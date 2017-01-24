package com.esacinc.commons.crypto.random.impl;

import com.esacinc.commons.crypto.factory.impl.AbstractCryptoFactoryRequest;
import com.esacinc.commons.crypto.random.SecureRandomConfig;
import com.esacinc.commons.crypto.random.SecureRandomFactoryRequest;

public class SecureRandomFactoryRequestImpl extends AbstractCryptoFactoryRequest<SecureRandomConfig> implements SecureRandomFactoryRequest {
    public SecureRandomFactoryRequestImpl(SecureRandomConfig descriptor) {
        super(descriptor);
    }
}
