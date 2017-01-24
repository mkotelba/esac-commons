package com.esacinc.commons.crypto.ssl.revocation.impl;

import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils;
import java.math.BigInteger;
import java.util.Comparator;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ocsp.CertID;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.operator.DigestCalculator;

public class EsacCertificateId extends CertificateID implements Comparable<EsacCertificateId> {
    private X509CertificateHolder issuerCertHolder;

    public EsacCertificateId(DigestCalculator digestCalc, X509CertificateHolder issuerCertHolder, BigInteger certSerialNum) throws OCSPException {
        super(digestCalc, issuerCertHolder, certSerialNum);

        this.issuerCertHolder = issuerCertHolder;
    }

    public EsacCertificateId(CertificateID certId) {
        this(certId.toASN1Primitive());
    }

    public EsacCertificateId(CertID certId) {
        super(certId);
    }

    public boolean matches(EsacCertificateId certId) throws OCSPException {
        return this.matches(certId.getIssuerCertificateHolder(), certId.getSerialNumber());
    }

    public boolean matches(X509CertificateHolder issuerCertHolder, BigInteger certSerialNum) throws OCSPException {
        return (this.matchesIssuer(issuerCertHolder, EsacDigestUtils.CALC_PROV) && this.getSerialNumber().equals(certSerialNum));
    }

    @Override
    public int compareTo(EsacCertificateId obj) {
        return Comparator.comparing(Object::hashCode).compare(this, obj);
    }

    @Nullable
    public X509CertificateHolder getIssuerCertificateHolder() {
        return this.issuerCertHolder;
    }
}
