package com.esacinc.commons.test.crypto.store.impl;

import com.esacinc.commons.crypto.cert.CertificateFactory;
import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.cert.impl.CertificateFactoryRequestImpl;
import com.esacinc.commons.crypto.credential.Credential;
import com.esacinc.commons.crypto.credential.CredentialConfig;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.credential.impl.CredentialInfoImpl;
import com.esacinc.commons.crypto.key.KeyPairFactory;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import com.esacinc.commons.crypto.store.KeyStoreEntryConfigBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.PrivateKeyKeyStoreEntryConfig;
import com.esacinc.commons.crypto.store.TrustedCertificateKeyStoreEntryConfig;
import com.esacinc.commons.crypto.store.impl.AbstractKeyStoreBeanPostProcessor;
import com.esacinc.commons.crypto.store.impl.EsacKeyStore;
import com.esacinc.commons.crypto.store.impl.EsacKeyStoreSpi;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

public class GeneratedKeyStoreBeanPostProcessor extends AbstractKeyStoreBeanPostProcessor {
    @Autowired
    private KeyPairFactory keyPairFactory;

    @Autowired
    private CertificateFactory certFactory;

    @Override
    protected EsacKeyStore postProcessBeforeInitializationInternal(EsacKeyStore bean, String beanName) throws Exception {
        EsacKeyStoreSpi spi = bean.getSpi();

        if (!spi.hasEntryConfigs()) {
            return super.postProcessBeforeInitializationInternal(bean, beanName);
        }

        SetMultimap<Credential, KeyStoreEntryConfigBean> credEntryConfigs = LinkedHashMultimap.create();

        for (KeyStoreEntryConfigBean entryConfig : spi.getEntryConfigs().values()) {
            if (!entryConfig.hasCredential()) {
                continue;
            }

            credEntryConfigs.put(entryConfig.getCredential(), entryConfig);
        }

        for (Credential cred : credEntryConfigs.keySet()) {
            this.generateCredential(credEntryConfigs, cred);
        }

        FileSystemResource resource = spi.getResource();

        if (resource.exists()) {
            return super.postProcessBeforeInitializationInternal(bean, beanName);
        }

        String alias;
        PrivateKeyKeyStoreEntryConfig privateKeyEntryConfig;

        for (Credential cred : credEntryConfigs.keySet()) {
            for (KeyStoreEntryConfigBean entryConfig : credEntryConfigs.get(cred)) {
                alias = entryConfig.getAlias();

                if (entryConfig.getType() == KeyStoreEntryType.PRIVATE_KEY) {
                    bean.setEntry(alias, new PrivateKeyEntry((privateKeyEntryConfig = ((PrivateKeyKeyStoreEntryConfig) entryConfig)).getPrivateKey(),
                        privateKeyEntryConfig.getCertificates()), privateKeyEntryConfig.getProtection());
                } else {
                    bean.setEntry(alias, new TrustedCertificateEntry(entryConfig.getCertificate()), new PasswordProtection(null));
                }
            }
        }

        spi.engineStore(resource);

        return super.postProcessBeforeInitializationInternal(bean, beanName);
    }

    private CredentialInfo generateCredential(SetMultimap<Credential, KeyStoreEntryConfigBean> credEntryConfigs, Credential cred) throws Exception {
        CredentialInfo credInfo;
        KeyPairInfo keyPairInfo;
        CertificateInfo certInfo;

        if (cred.hasInfo()) {
            // noinspection ConstantConditions
            keyPairInfo = (credInfo = cred.getInfo()).getKeyPairDescriptor();
            certInfo = credInfo.getCertificateDescriptor();
        } else {
            CredentialInfo issuerCredInfo = null;

            if (cred.hasIssuerCredential()) {
                issuerCredInfo = this.generateCredential(credEntryConfigs, cred.getIssuerCredential());
            }

            CredentialConfig credConfig;

            cred.setInfo((credInfo = new CredentialInfoImpl((keyPairInfo = this.keyPairFactory.build((credConfig = cred.getConfig()).getKeyPairDescriptor())),
                (certInfo = this.certFactory.build(new CertificateFactoryRequestImpl(issuerCredInfo, keyPairInfo, credConfig.getCertificateDescriptor()))
                    .getDescriptor()))));
        }

        if (!credEntryConfigs.containsKey(cred)) {
            return credInfo;
        }

        PrivateKey privateKey = keyPairInfo.getPrivateKey();
        X509Certificate cert = certInfo.getService();
        PrivateKeyKeyStoreEntryConfig privateKeyEntryConfig;

        for (KeyStoreEntryConfigBean entryConfig : credEntryConfigs.get(cred)) {
            if (entryConfig.getType() == KeyStoreEntryType.PRIVATE_KEY) {
                (privateKeyEntryConfig = ((PrivateKeyKeyStoreEntryConfig) entryConfig)).setPrivateKey(privateKey);
                privateKeyEntryConfig.setCertificates(cert);
            } else {
                ((TrustedCertificateKeyStoreEntryConfig) entryConfig).setCertificate(cert);
            }
        }

        return credInfo;
    }
}
