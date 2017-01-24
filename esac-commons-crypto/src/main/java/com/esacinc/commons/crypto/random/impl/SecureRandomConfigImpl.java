package com.esacinc.commons.crypto.random.impl;

import com.esacinc.commons.crypto.impl.AbstractCryptoDescriptor;
import com.esacinc.commons.crypto.random.SecureRandomConfig;
import java.security.Provider;

public class SecureRandomConfigImpl extends AbstractCryptoDescriptor implements SecureRandomConfig {
    private Provider prov;

    public SecureRandomConfigImpl(Provider prov, String algName) {
        super(null, null);

        this.prov = prov;
        this.algName = algName;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }
}
