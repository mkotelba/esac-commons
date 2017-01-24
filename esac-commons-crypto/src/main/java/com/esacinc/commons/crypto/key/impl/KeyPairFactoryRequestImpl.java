package com.esacinc.commons.crypto.key.impl;

import com.esacinc.commons.crypto.factory.impl.AbstractCryptoFactoryRequest;
import com.esacinc.commons.crypto.key.KeyPairConfig;
import com.esacinc.commons.crypto.key.KeyPairFactoryRequest;

public class KeyPairFactoryRequestImpl extends AbstractCryptoFactoryRequest<KeyPairConfig> implements KeyPairFactoryRequest {
    public KeyPairFactoryRequestImpl(KeyPairConfig descriptor) {
        super(descriptor);
    }
}
