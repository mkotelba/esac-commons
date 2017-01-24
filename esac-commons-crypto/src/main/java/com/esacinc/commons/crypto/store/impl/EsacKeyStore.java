package com.esacinc.commons.crypto.store.impl;

import java.security.KeyStore;

public class EsacKeyStore extends KeyStore {
    private EsacKeyStoreSpi spi;

    public EsacKeyStore(EsacKeyStoreSpi spi) throws Exception {
        super(spi, spi.getProvider(), spi.getAlgorithmName());

        this.spi = spi;

        this.load(null, null);
    }

    public EsacKeyStoreSpi getSpi() {
        return this.spi;
    }
}
