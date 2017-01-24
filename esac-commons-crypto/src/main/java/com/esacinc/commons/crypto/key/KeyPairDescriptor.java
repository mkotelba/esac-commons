package com.esacinc.commons.crypto.key;

import com.esacinc.commons.crypto.CryptoDescriptor;
import com.esacinc.commons.crypto.beans.AlgorithmIdentifiedBean;
import javax.annotation.Nonnegative;

public interface KeyPairDescriptor extends AlgorithmIdentifiedBean, CryptoDescriptor {
    public boolean hasSize();

    @Nonnegative
    public int getSize();
}
