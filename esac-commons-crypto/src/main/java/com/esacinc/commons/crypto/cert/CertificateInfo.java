package com.esacinc.commons.crypto.cert;

import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.CryptoServiceInfo;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.time.IntervalInfo;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;

public interface CertificateInfo extends CertificateDescriptor<IntervalInfo>, CryptoServiceInfo<X509Certificate> {
    public boolean hasExtension(ASN1ObjectIdentifier oid);

    @Nullable
    public Extension getExtension(ASN1ObjectIdentifier oid);

    public boolean hasExtensions();

    @Nullable
    public Extensions getExtensions();

    public boolean hasHolder();

    @Nullable
    public X509CertificateHolder getHolder();

    public boolean hasIssuerAltNames();

    @Nullable
    public ListMultimap<GeneralNameType, ASN1Encodable> getIssuerAltNames();

    public boolean hasIssuerDn();

    @Nullable
    public Dn getIssuerDn();
}
