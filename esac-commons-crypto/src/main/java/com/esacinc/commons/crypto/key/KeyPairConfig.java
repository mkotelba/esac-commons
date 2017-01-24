package com.esacinc.commons.crypto.key;

import com.esacinc.commons.crypto.CryptoServiceConfig;
import java.security.SecureRandom;

public interface KeyPairConfig extends CryptoServiceConfig, KeyPairDescriptor {
    public SecureRandom getSecureRandom();
}
