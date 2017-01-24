package com.esacinc.commons.crypto.ssl.impl;

import com.esacinc.commons.crypto.ssl.SslManager;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStore.Builder;
import java.security.KeyStore.PasswordProtection;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyStoreBuilderParameters;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;

public class EsacKeyManager extends X509ExtendedKeyManager implements SslManager {
    private Provider prov;
    private String algName;
    private KeyStore keyStore;
    private PasswordProtection protection;
    private String id;
    private String name;
    private X509ExtendedKeyManager delegate;

    public EsacKeyManager(Provider prov, String algName, KeyStore keyStore, String pass) {
        this.prov = prov;
        this.algName = algName;
        this.keyStore = keyStore;
        this.protection = new PasswordProtection(pass.toCharArray());
    }

    @Nullable
    @Override
    public String chooseEngineClientAlias(String[] keyAlgNames, @Nullable Principal[] issuers, @Nullable SSLEngine engine) {
        return this.delegate.chooseEngineClientAlias(keyAlgNames, issuers, engine);
    }

    @Nullable
    @Override
    public String chooseEngineServerAlias(String keyAlgName, @Nullable Principal[] issuers, @Nullable SSLEngine engine) {
        return this.delegate.chooseEngineServerAlias(keyAlgName, issuers, engine);
    }

    @Nullable
    @Override
    public String[] getClientAliases(String keyAlgName, @Nullable Principal[] issuers) {
        return this.delegate.getClientAliases(keyAlgName, issuers);
    }

    @Nullable
    @Override
    public String chooseClientAlias(String[] keyAlgNames, @Nullable Principal[] issuers, @Nullable Socket socket) {
        return this.delegate.chooseClientAlias(keyAlgNames, issuers, socket);
    }

    @Nullable
    @Override
    public String[] getServerAliases(String keyAlgName, @Nullable Principal[] issuers) {
        return this.delegate.getServerAliases(keyAlgName, issuers);
    }

    @Nullable
    @Override
    public String chooseServerAlias(String keyAlgName, @Nullable Principal[] issuers, @Nullable Socket socket) {
        return this.delegate.chooseServerAlias(keyAlgName, issuers, socket);
    }

    @Nullable
    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return this.delegate.getCertificateChain(alias);
    }

    @Nullable
    @Override
    public PrivateKey getPrivateKey(String alias) {
        return this.delegate.getPrivateKey(alias);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }

    @Override
    public void initialize() throws Exception {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(this.algName, this.prov);
        factory.init(new KeyStoreBuilderParameters(Builder.newInstance(this.keyStore, this.protection)));

        this.delegate = ((X509ExtendedKeyManager) factory.getKeyManagers()[0]);
    }

    @Override
    public void reset() {
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }

    @Override
    public boolean hasId() {
        return (this.id != null);
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Override
    public KeyStore getKeyStore() {
        return this.keyStore;
    }

    @Override
    public boolean hasName() {
        return (this.name != null);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }
}
