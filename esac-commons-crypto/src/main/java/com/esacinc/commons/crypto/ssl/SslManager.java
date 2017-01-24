package com.esacinc.commons.crypto.ssl;

import com.esacinc.commons.crypto.CryptoDescriptor;
import java.security.KeyStore;
import java.security.Provider;
import org.springframework.beans.factory.InitializingBean;

public interface SslManager extends CryptoDescriptor, InitializingBean {
    public KeyStore getKeyStore();

    public Provider getProvider();
}
