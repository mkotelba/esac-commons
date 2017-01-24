package com.esacinc.commons.crypto.cert;

import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.CryptoServiceConfig;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.time.IntervalConfig;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Set;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CertificateConfig extends CertificateDescriptor<IntervalConfig>, CryptoServiceConfig {
    public void setCertificateAuthority(boolean ca);

    public void setExtendedKeyUsages(@Nullable Set<ExtendedKeyUsageType> extKeyUsages);

    public void setInterval(@Nullable IntervalConfig interval);

    public void setKeyUsages(@Nullable Set<KeyUsageType> keyUsages);

    public void setOcspResponderUris(@Nullable Set<URI> ocspRespUris);

    public SecureRandom getSecureRandom();

    public void setSelfIssued(boolean selfIssued);

    public void setSerialNumber(@Nullable BigInteger serialNum);

    public void setSignatureAlgorithmId(@Nullable AlgorithmIdentifier sigAlgId);

    public void setSignatureAlgorithmName(@Nullable String sigAlgName);

    public void setSubjectAltNames(@Nullable ListMultimap<GeneralNameType, ASN1Encodable> subjAltNames);

    public void setSubjectDn(@Nullable Dn subjDn);
}
