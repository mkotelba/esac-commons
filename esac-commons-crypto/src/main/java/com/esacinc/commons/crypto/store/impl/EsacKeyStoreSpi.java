package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.beans.IdentifiedBean;
import com.esacinc.commons.io.utils.EsacResourceUtils;
import com.esacinc.commons.crypto.CryptoDescriptor;
import com.esacinc.commons.crypto.CryptoException;
import com.esacinc.commons.crypto.store.KeyStoreEntryConfigBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryInfoBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.PrivateKeyKeyStoreEntryConfig;
import com.esacinc.commons.crypto.store.PrivateKeyKeyStoreEntryInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.EnumerationUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

public class EsacKeyStoreSpi extends KeyStoreSpi implements CryptoDescriptor {
    private final static String KEY_STORE_SERVICE_TYPE = KeyStore.class.getSimpleName();

    private Provider prov;
    private String algName;
    private KeyStoreSpi delegate;
    private PasswordProtection protection;
    private FileSystemResource resource;
    private Map<String, KeyStoreEntryConfigBean> entryConfigs = new LinkedHashMap<>();
    private Map<String, KeyStoreEntryInfoBean<?>> entryInfos = new LinkedHashMap<>();
    private String id;
    private String name;

    public EsacKeyStoreSpi(Provider prov, String algName, String pass, FileSystemResource resource) throws NoSuchAlgorithmException {
        this.delegate = ((KeyStoreSpi) (this.prov = prov).getService(KEY_STORE_SERVICE_TYPE, (this.algName = algName)).newInstance(null));
        this.protection = new PasswordProtection(pass.toCharArray());
        this.resource = resource;
    }

    public void engineStore(WritableResource resource) throws CertificateException, IOException, NoSuchAlgorithmException {
        if (resource.exists()) {
            throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) resource (class=%s, uri=%s, desc=%s) already exists.",
                this.prov.getName(), this.algName, this.id, this.name, resource.getClass(), EsacResourceUtils.extractUri(resource), resource.getDescription()));
        }

        try (OutputStream outStream = resource.getOutputStream()) {
            this.delegate.engineStore(outStream, this.protection.getPassword());

            outStream.flush();
        }
    }

    @Override
    public void engineStore(OutputStream outStream, @Nullable char[] pass) throws CertificateException, IOException, NoSuchAlgorithmException {
        throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) delegated storage is not supported.", this.prov.getName(),
            this.algName, this.id, this.name));
    }

    @Override
    public void engineStore(@Nullable LoadStoreParameter param) throws CertificateException, IOException, NoSuchAlgorithmException {
        throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) delegated storage is not supported.", this.prov.getName(),
            this.algName, this.id, this.name));
    }

    @Override
    public void engineDeleteEntry(String alias) throws KeyStoreException {
        throw new KeyStoreException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (alias=%s) deletion is not allowed.",
            this.prov.getName(), this.algName, this.id, this.name, alias));
    }

    @Override
    public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
        this.validateEntrySettable(KeyStoreEntryType.TRUSTED_CERTIFICATE, alias);

        this.delegate.engineSetCertificateEntry(alias, cert);
    }

    @Override
    public void engineSetKeyEntry(String alias, Key key, @Nullable char[] pass, Certificate[] certs) throws KeyStoreException {
        this.validateEntrySettable(KeyStoreEntryType.PRIVATE_KEY, alias);

        this.delegate.engineSetKeyEntry(alias, key, pass, certs);
    }

    @Override
    public void engineSetKeyEntry(String alias, byte[] keyData, Certificate[] certs) throws KeyStoreException {
        this.validateEntrySettable(KeyStoreEntryType.PRIVATE_KEY, alias);

        this.delegate.engineSetKeyEntry(alias, keyData, certs);
    }

    @Override
    public void engineSetEntry(String alias, Entry entry, ProtectionParameter param) throws KeyStoreException {
        this.validateEntrySettable(KeyStoreEntryType.PRIVATE_KEY, alias);

        this.delegate.engineSetEntry(alias, entry, param);
    }

    @Nullable
    @Override
    public Certificate engineGetCertificate(String alias) {
        return (this.engineContainsAlias(alias) ? this.entryInfos.get(alias).getCertificate() : null);
    }

    @Nullable
    @Override
    public Certificate[] engineGetCertificateChain(String alias) {
        return (this.engineIsKeyEntry(alias) ? ((PrivateKeyKeyStoreEntryInfo) this.entryInfos.get(alias)).getCertificates() : null);
    }

    @Nullable
    @Override
    public String engineGetCertificateAlias(Certificate cert) {
        return (this.hasEntryInfos()
            ? this.entryInfos.values().stream().filter(entryInfo -> entryInfo.getCertificate().equals(cert)).findFirst().map(IdentifiedBean::getId).orElse(null)
            : null);
    }

    @Nullable
    @Override
    public Key engineGetKey(String alias, @Nullable char[] pass) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        return (this.engineIsKeyEntry(alias) ? ((PrivateKeyKeyStoreEntryInfo) this.entryInfos.get(alias)).getPrivateKey() : null);
    }

    @Nullable
    @Override
    public Entry engineGetEntry(String alias, @Nullable ProtectionParameter param)
        throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        return (this.engineContainsAlias(alias) ? this.entryInfos.get(alias).getEntry() : null);
    }

    @Nullable
    @Override
    public Date engineGetCreationDate(String alias) {
        return (this.engineContainsAlias(alias) ? this.entryInfos.get(alias).getCreationDate() : null);
    }

    @Override
    public boolean engineIsCertificateEntry(String alias) {
        return (this.engineContainsAlias(alias) && (this.entryInfos.get(alias).getType() == KeyStoreEntryType.TRUSTED_CERTIFICATE));
    }

    @Override
    public boolean engineIsKeyEntry(String alias) {
        return (this.engineContainsAlias(alias) && (this.entryInfos.get(alias).getType() != KeyStoreEntryType.TRUSTED_CERTIFICATE));
    }

    @Override
    public boolean engineContainsAlias(String alias) {
        return this.entryInfos.containsKey(alias);
    }

    @Override
    public Enumeration<String> engineAliases() {
        return new Vector<>(this.entryInfos.keySet()).elements();
    }

    @Nonnegative
    @Override
    public int engineSize() {
        return this.entryInfos.size();
    }

    public void engineLoad(Resource resource) throws CertificateException, IOException, NoSuchAlgorithmException {
        if (!resource.exists()) {
            throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) resource (class=%s, uri=%s, desc=%s) does not exist.",
                this.prov.getName(), this.algName, this.id, this.name, resource.getClass(), EsacResourceUtils.extractUri(resource), resource.getDescription()));
        }

        try (InputStream inStream = resource.getInputStream()) {
            this.delegate.engineLoad(inStream, this.protection.getPassword());
        }
    }

    @Override
    public void engineLoad(@Nullable InputStream inStream, @Nullable char[] pass) throws CertificateException, IOException, NoSuchAlgorithmException {
        if ((inStream != null) && (pass != null)) {
            throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) delegated loading is not supported.", this.prov.getName(),
                this.algName, this.id, this.name));
        }

        this.delegate.engineLoad(inStream, pass);
    }

    @Override
    public void engineLoad(@Nullable LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        if (param != null) {
            throw new IOException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) delegated loading is not supported.", this.prov.getName(),
                this.algName, this.id, this.name));
        }

        this.delegate.engineLoad(null);
    }

    @Override
    public void initialize() throws Exception {
        this.reset();

        try {
            this.engineLoad(this.resource);

            KeyStoreEntryConfigBean entryConfig;
            KeyStoreEntryType entryType;

            for (String alias : EnumerationUtils.toList(this.delegate.engineAliases())) {
                if (this.delegate.engineEntryInstanceOf(alias, SecretKeyEntry.class)) {
                    throw new CryptoException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (type=%s, alias=%s) is not supported.",
                        this.prov.getName(), this.algName, this.id, this.name, KeyStoreEntryType.SECRET_KEY, alias));
                } else {
                    if (!this.entryConfigs.containsKey(alias)) {
                        throw new CryptoException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (alias=%s) is not configured.",
                            this.prov.getName(), this.algName, this.id, this.name, alias));
                    }

                    if ((entryType = (entryConfig = this.entryConfigs.get(alias)).getType()) == KeyStoreEntryType.PRIVATE_KEY) {
                        if (!this.delegate.engineIsKeyEntry(alias)) {
                            throw new CryptoException(
                                String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (alias=%s) is not a private key entry.",
                                    this.prov.getName(), this.algName, this.id, this.name, alias));
                        }

                        this.entryInfos.put(alias, new PrivateKeyKeyStoreEntryInfoImpl(alias, this.engineGetCreationDate(alias),
                            ((PrivateKeyEntry) this.delegate.engineGetEntry(alias, ((PrivateKeyKeyStoreEntryConfig) entryConfig).getProtection()))));
                    } else if (entryType == KeyStoreEntryType.TRUSTED_CERTIFICATE) {
                        if (!this.delegate.engineIsCertificateEntry(alias)) {
                            throw new CryptoException(
                                String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (alias=%s) is not a trusted certificate entry.",
                                    this.prov.getName(), this.algName, this.id, this.name, alias));
                        }

                        this.entryInfos.put(alias, new TrustedCertificateKeyStoreEntryInfoImpl(alias, this.engineGetCreationDate(alias),
                            ((TrustedCertificateEntry) this.delegate.engineGetEntry(alias, null))));
                    }
                }
            }
        } catch (Exception e) {
            this.reset();

            throw e;
        }
    }

    @Override
    public void reset() {
        this.entryInfos.clear();
    }

    private void validateEntrySettable(KeyStoreEntryType entryType, String alias) {
        if (this.delegate.engineContainsAlias(alias)) {
            throw new CryptoException(String.format("Key store (provName=%s, algName=%s, id=%s, name=%s) entry (type=%s, alias=%s) already exists.",
                this.prov.getName(), this.algName, this.id, this.name, entryType, alias));
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }

    public KeyStoreSpi getDelegate() {
        return this.delegate;
    }

    public boolean hasEntryConfigs() {
        return !this.entryConfigs.isEmpty();
    }

    public Map<String, KeyStoreEntryConfigBean> getEntryConfigs() {
        return this.entryConfigs;
    }

    public void setEntryConfigs(KeyStoreEntryConfigBean ... entryConfigs) {
        this.entryConfigs.clear();

        Stream.of(entryConfigs).forEach(entryConfig -> this.entryConfigs.put(entryConfig.getAlias(), entryConfig));
    }

    public boolean hasEntryInfos() {
        return !this.entryInfos.isEmpty();
    }

    public Map<String, KeyStoreEntryInfoBean<?>> getEntryInfos() {
        return this.entryInfos;
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

    public Provider getProvider() {
        return this.prov;
    }

    public FileSystemResource getResource() {
        return this.resource;
    }
}
