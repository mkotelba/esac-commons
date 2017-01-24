package com.esacinc.commons.crypto.key.impl;

import com.esacinc.commons.crypto.impl.AbstractCryptoDescriptor;
import com.esacinc.commons.crypto.key.KeyPairDescriptor;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public abstract class AbstractKeyPairDescriptor extends AbstractCryptoDescriptor implements KeyPairDescriptor {
    protected AlgorithmIdentifier algId;
    protected int size;

    protected AbstractKeyPairDescriptor(@Nullable String id, @Nullable String name) {
        super(id, name);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    @Override
    public boolean hasSize() {
        return (this.size > 0);
    }

    @Nonnegative
    @Override
    public int getSize() {
        return this.size;
    }
}
