package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.crypto.beans.AlgorithmIdentifiedBean;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.CryptoDescriptor;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.time.IntervalDescriptor;
import java.math.BigInteger;
import java.net.URI;
import java.util.Set;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CertificateDescriptor<T extends IntervalDescriptor> extends AlgorithmIdentifiedBean, CryptoDescriptor {
    public boolean isRootCertificateAuthority();

    public boolean isCertificateAuthority();

    public boolean hasExtendedKeyUsages();

    @Nullable
    public Set<ExtendedKeyUsageType> getExtendedKeyUsages();

    public boolean hasInterval();

    @Nullable
    public T getInterval();

    public boolean hasKeyUsages();

    @Nullable
    public Set<KeyUsageType> getKeyUsages();

    public boolean hasOcspResponderUris();

    @Nullable
    public Set<URI> getOcspResponderUris();

    public boolean isSelfIssued();

    public boolean hasSerialNumber();

    @Nullable
    public BigInteger getSerialNumber();

    public boolean hasSignatureAlgorithmId();

    @Nullable
    public AlgorithmIdentifier getSignatureAlgorithmId();

    public boolean hasSignatureAlgorithmName();

    @Nullable
    public String getSignatureAlgorithmName();

    public boolean hasSubjectAltNames();

    @Nullable
    public ListMultimap<GeneralNameType, ASN1Encodable> getSubjectAltNames();

    public boolean hasSubjectDn();

    @Nullable
    public Dn getSubjectDn();
}
