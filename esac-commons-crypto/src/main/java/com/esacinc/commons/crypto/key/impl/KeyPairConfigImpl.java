package com.esacinc.commons.crypto.key.impl;

import com.esacinc.commons.crypto.key.KeyPairConfig;
import java.security.Provider;
import java.security.SecureRandom;
import javax.annotation.Nonnegative;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class KeyPairConfigImpl extends AbstractKeyPairDescriptor implements KeyPairConfig {
    private SecureRandom secureRandom;
    private Provider prov;

    public KeyPairConfigImpl(SecureRandom secureRandom, Provider prov, AlgorithmIdentifier algId, String algName, @Nonnegative int size) {
        super(null, null);

        this.secureRandom = secureRandom;
        this.prov = prov;
        this.algId = algId;
        this.algName = algName;
        this.size = size;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }

    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    @Nonnegative
    @Override
    public int getSize() {
        return this.size;
    }
}
