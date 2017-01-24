package com.esacinc.commons.crypto.cert.impl;

import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.cert.CertificateConfig;
import com.esacinc.commons.crypto.cert.ExtendedKeyUsageType;
import com.esacinc.commons.crypto.cert.KeyUsageType;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.time.IntervalConfig;
import java.math.BigInteger;
import java.net.URI;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Set;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class CertificateConfigImpl extends AbstractCertificateDescriptor<IntervalConfig> implements CertificateConfig {
    private SecureRandom secureRandom;
    private Provider prov;

    public CertificateConfigImpl(SecureRandom secureRandom, Provider prov, AlgorithmIdentifier algId, String algName) {
        super(null, null);

        this.secureRandom = secureRandom;
        this.prov = prov;
        this.algId = algId;
        this.algName = algName;
    }

    @Override
    public void setCertificateAuthority(boolean ca) {
        this.ca = ca;
    }

    @Override
    public void setExtendedKeyUsages(@Nullable Set<ExtendedKeyUsageType> extKeyUsages) {
        this.extKeyUsages = extKeyUsages;
    }

    @Override
    public void setInterval(@Nullable IntervalConfig interval) {
        this.interval = interval;
    }

    @Override
    public void setKeyUsages(@Nullable Set<KeyUsageType> keyUsages) {
        this.keyUsages = keyUsages;
    }

    @Override
    public void setOcspResponderUris(@Nullable Set<URI> ocspRespUris) {
        this.ocspResponderUris = ocspRespUris;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }

    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    @Override
    public void setSelfIssued(boolean selfIssued) {
        this.selfIssued = selfIssued;
    }

    @Override
    public void setSerialNumber(@Nullable BigInteger serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public void setSignatureAlgorithmId(@Nullable AlgorithmIdentifier sigAlgId) {
        this.sigAlgId = sigAlgId;
    }

    @Override
    public void setSignatureAlgorithmName(@Nullable String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    @Override
    public void setSubjectAltNames(@Nullable ListMultimap<GeneralNameType, ASN1Encodable> subjAltNames) {
        this.subjAltNames = subjAltNames;
    }

    @Override
    public void setSubjectDn(@Nullable Dn subjDn) {
        this.subjDn = subjDn;
    }
}
