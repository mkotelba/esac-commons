package com.esacinc.commons.crypto.key.impl;

import com.esacinc.commons.crypto.digest.DigestAlgorithms;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils;
import com.esacinc.commons.crypto.key.KeyAlgorithms;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import com.esacinc.commons.crypto.key.utils.EsacKeyUtils;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class KeyPairInfoImpl extends AbstractKeyPairDescriptor implements KeyPairInfo {
    private KeyPair service;
    private AuthorityKeyIdentifier authKeyId;
    private PrivateKeyInfo privateKeyInfo;
    private SubjectPublicKeyInfo subjPublicKeyInfo;

    public KeyPairInfoImpl(KeyPair service) throws Exception {
        super(null, null);

        this.service = service;

        this.initialize();
    }

    @Override
    public void reset() {
        super.reset();

        this.algName = null;
        this.algId = null;
        this.size = 0;
        this.authKeyId = null;
        this.privateKeyInfo = null;
        this.subjPublicKeyInfo = null;
    }

    @Override
    protected void initializeInternal() throws Exception {
        Key availableKey = this.getAvailableKey();

        this.algId = KeyAlgorithms.NAME_IDS.get((this.algName = availableKey.getAlgorithm()));
        this.size = EsacKeyUtils.extractSize(this.algName, availableKey);

        if (this.hasPublicKey()) {
            // noinspection ConstantConditions
            this.authKeyId = new AuthorityKeyIdentifier(EsacDigestUtils.CALC_PROV.digest(DigestAlgorithms.SHA_1_ID,
                (this.subjPublicKeyInfo = SubjectPublicKeyInfo.getInstance(this.getPublicKey().getEncoded())).getPublicKeyData().getBytes()));
        }

        if (this.hasPrivateKey()) {
            // noinspection ConstantConditions
            this.privateKeyInfo = PrivateKeyInfo.getInstance(this.getPrivateKey().getEncoded());
        }
    }

    @Override
    public boolean hasAuthorityKeyId() {
        return (this.authKeyId != null);
    }

    @Nullable
    @Override
    public AuthorityKeyIdentifier getAuthorityKeyId() {
        return this.authKeyId;
    }

    @Override
    public Key getAvailableKey() {
        PublicKey publicKey = this.getPublicKey();

        return ((publicKey != null) ? publicKey : this.getPrivateKey());
    }

    @Override
    public boolean hasPublicKey() {
        return (this.getPublicKey() != null);
    }

    @Nullable
    @Override
    public PublicKey getPublicKey() {
        return this.service.getPublic();
    }

    @Override
    public void setPublicKey(@Nullable PublicKey publicKey) {
        this.service = new KeyPair(publicKey, this.getPrivateKey());
    }

    @Override
    public boolean hasPrivateKey() {
        return (this.getPrivateKey() != null);
    }

    @Nullable
    @Override
    public PrivateKey getPrivateKey() {
        return this.service.getPrivate();
    }

    @Override
    public void setPrivateKey(@Nullable PrivateKey privateKey) {
        this.service = new KeyPair(this.getPublicKey(), privateKey);
    }

    @Override
    public boolean hasPrivateKeyInfo() {
        return (this.privateKeyInfo != null);
    }

    @Nullable
    @Override
    public PrivateKeyInfo getPrivateKeyInfo() {
        return this.privateKeyInfo;
    }

    @Override
    public KeyPair getService() {
        return this.service;
    }

    @Override
    public boolean hasSubjectPublicKeyInfo() {
        return (this.subjPublicKeyInfo != null);
    }

    @Nullable
    @Override
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjPublicKeyInfo;
    }
}
