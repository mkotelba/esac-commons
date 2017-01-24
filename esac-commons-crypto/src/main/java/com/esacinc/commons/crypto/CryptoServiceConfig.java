package com.esacinc.commons.crypto;

import java.security.Provider;

public interface CryptoServiceConfig extends CryptoConfig {
    public Provider getProvider();
}
