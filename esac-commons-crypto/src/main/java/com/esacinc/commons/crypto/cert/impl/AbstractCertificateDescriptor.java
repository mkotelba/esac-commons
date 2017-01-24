package com.esacinc.commons.crypto.cert.impl;

import com.esacinc.commons.utils.EsacMultimapUtils;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.cert.CertificateDescriptor;
import com.esacinc.commons.crypto.cert.ExtendedKeyUsageType;
import com.esacinc.commons.crypto.cert.KeyUsageType;
import com.esacinc.commons.crypto.impl.AbstractCryptoDescriptor;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.time.IntervalDescriptor;
import java.math.BigInteger;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public abstract class AbstractCertificateDescriptor<T extends IntervalDescriptor> extends AbstractCryptoDescriptor
    implements CertificateDescriptor<T> {
    protected AlgorithmIdentifier algId;
    protected boolean ca;
    protected Set<ExtendedKeyUsageType> extKeyUsages;
    protected T interval;
    protected Set<KeyUsageType> keyUsages;
    protected Set<URI> ocspResponderUris;
    protected boolean selfIssued;
    protected BigInteger serialNum;
    protected AlgorithmIdentifier sigAlgId;
    protected String sigAlgName;
    protected ListMultimap<GeneralNameType, ASN1Encodable> subjAltNames;
    protected Dn subjDn;

    protected AbstractCertificateDescriptor(@Nullable String id, @Nullable String name) {
        super(id, name);
    }

    @Override
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }

    @Override
    public boolean isRootCertificateAuthority() {
        return (this.ca && this.selfIssued);
    }

    @Override
    public boolean isCertificateAuthority() {
        return this.ca;
    }

    @Override
    public boolean hasExtendedKeyUsages() {
        return !CollectionUtils.isEmpty(this.extKeyUsages);
    }

    @Nullable
    @Override
    public Set<ExtendedKeyUsageType> getExtendedKeyUsages() {
        return this.extKeyUsages;
    }

    @Override
    public boolean hasInterval() {
        return (this.interval != null);
    }

    @Nullable
    @Override
    public T getInterval() {
        return this.interval;
    }

    @Override
    public boolean hasKeyUsages() {
        return !CollectionUtils.isEmpty(this.keyUsages);
    }

    @Nullable
    @Override
    public Set<KeyUsageType> getKeyUsages() {
        return this.keyUsages;
    }

    @Override
    public boolean hasOcspResponderUris() {
        return !CollectionUtils.isEmpty(this.ocspResponderUris);
    }

    @Nullable
    @Override
    public Set<URI> getOcspResponderUris() {
        return this.ocspResponderUris;
    }

    @Override
    public boolean isSelfIssued() {
        return this.selfIssued;
    }

    @Override
    public boolean hasSerialNumber() {
        return (this.serialNum != null);
    }

    @Nullable
    @Override
    public BigInteger getSerialNumber() {
        return this.serialNum;
    }

    @Override
    public boolean hasSignatureAlgorithmId() {
        return this.sigAlgId != null;
    }

    @Nullable
    @Override
    public AlgorithmIdentifier getSignatureAlgorithmId() {
        return this.sigAlgId;
    }

    @Override
    public boolean hasSignatureAlgorithmName() {
        return this.sigAlgName != null;
    }

    @Nullable
    @Override
    public String getSignatureAlgorithmName() {
        return this.sigAlgName;
    }

    @Override
    public boolean hasSubjectAltNames() {
        return !EsacMultimapUtils.isEmpty(this.subjAltNames);
    }

    @Nullable
    @Override
    public ListMultimap<GeneralNameType, ASN1Encodable> getSubjectAltNames() {
        return this.subjAltNames;
    }

    @Override
    public boolean hasSubjectDn() {
        return (this.subjDn != null);
    }

    @Nullable
    @Override
    public Dn getSubjectDn() {
        return this.subjDn;
    }
}
