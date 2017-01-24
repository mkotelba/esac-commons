package com.esacinc.commons.crypto.impl;

import com.esacinc.commons.beans.impl.AbstractDescriptorBean;
import com.esacinc.commons.crypto.CryptoDescriptor;
import javax.annotation.Nullable;

public abstract class AbstractCryptoDescriptor extends AbstractDescriptorBean implements CryptoDescriptor {
    protected String algName;

    protected AbstractCryptoDescriptor(@Nullable String id, @Nullable String name) {
        super(id, name);
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }
}
