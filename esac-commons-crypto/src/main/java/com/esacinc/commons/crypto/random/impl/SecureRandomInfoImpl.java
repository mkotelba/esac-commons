package com.esacinc.commons.crypto.random.impl;

import com.esacinc.commons.crypto.impl.AbstractCryptoDescriptor;
import com.esacinc.commons.crypto.random.SecureRandomInfo;
import java.security.SecureRandom;

public class SecureRandomInfoImpl extends AbstractCryptoDescriptor implements SecureRandomInfo {
    private SecureRandom service;

    public SecureRandomInfoImpl(SecureRandom service) throws Exception {
        super(null, null);

        this.service = service;

        this.initialize();
    }

    @Override
    public void reset() {
        super.reset();

        this.algName = null;
    }

    @Override
    protected void initializeInternal() throws Exception {
        this.algName = this.service.getAlgorithm();
    }

    @Override
    public SecureRandom getService() {
        return this.service;
    }
}
