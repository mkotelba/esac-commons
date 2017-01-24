package com.esacinc.commons.crypto.key;

import com.esacinc.commons.crypto.CryptoServiceInfo;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface KeyPairInfo extends CryptoServiceInfo<KeyPair>, KeyPairDescriptor {
    public boolean hasAuthorityKeyId();

    @Nullable
    public AuthorityKeyIdentifier getAuthorityKeyId();

    public Key getAvailableKey();

    public boolean hasPublicKey();

    @Nullable
    public PublicKey getPublicKey();

    public void setPublicKey(@Nullable PublicKey publicKey);

    public boolean hasPrivateKey();

    @Nullable
    public PrivateKey getPrivateKey();

    public void setPrivateKey(@Nullable PrivateKey privateKey);

    public boolean hasPrivateKeyInfo();

    @Nullable
    public PrivateKeyInfo getPrivateKeyInfo();

    public boolean hasSubjectPublicKeyInfo();

    @Nullable
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo();
}
