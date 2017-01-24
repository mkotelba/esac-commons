package com.esacinc.commons.crypto.cert.impl;

import com.esacinc.commons.crypto.utils.EsacCryptoEnumUtils;
import com.esacinc.commons.utils.EsacMultimapUtils;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.crypto.cert.CertificateAlgorithms;
import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.cert.ExtendedKeyUsageType;
import com.esacinc.commons.crypto.cert.KeyUsageType;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.naming.impl.DnImpl;
import com.esacinc.commons.crypto.naming.utils.EsacGeneralNameUtils;
import com.esacinc.commons.crypto.time.IntervalInfo;
import com.esacinc.commons.crypto.time.impl.IntervalInfoImpl;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

public class CertificateInfoImpl extends AbstractCertificateDescriptor<IntervalInfo> implements CertificateInfo {
    private X509Certificate service;
    private X509CertificateHolder holder;
    private ListMultimap<GeneralNameType, ASN1Encodable> issuerAltNames;
    private Dn issuerDn;

    public CertificateInfoImpl(X509Certificate service) throws Exception {
        super(null, null);

        this.service = service;

        this.initialize();
    }

    @Override
    public void reset() {
        super.reset();

        this.algId = null;
        this.algName = null;
        this.ca = false;
        this.extKeyUsages = null;
        this.holder = null;
        this.interval = null;
        this.issuerAltNames = null;
        this.issuerDn = null;
        this.keyUsages = null;
        this.ocspResponderUris = null;
        this.serialNum = null;
        this.sigAlgId = null;
        this.sigAlgName = null;
        this.subjAltNames = null;
        this.subjDn = null;
    }

    @Override
    protected void initializeInternal() throws Exception {
        this.algId = CertificateAlgorithms.NAME_IDS.get((this.algName = this.service.getType()));
        this.holder = new JcaX509CertificateHolder(this.service);
        this.ca = new BasicConstraints(this.service.getBasicConstraints()).isCA();
        this.interval = new IntervalInfoImpl(this.service.getNotBefore(), this.service.getNotAfter());
        this.issuerDn = new DnImpl(this.holder.getIssuer());
        this.subjDn = new DnImpl(this.holder.getSubject());
        this.selfIssued = this.subjDn.equals(this.issuerDn);
        this.serialNum = this.service.getSerialNumber();
        this.sigAlgId = new AlgorithmIdentifier(new ASN1ObjectIdentifier(this.service.getSigAlgOID()));
        this.sigAlgName = this.service.getSigAlgName();

        Extensions extensions = this.getExtensions();

        // noinspection ConstantConditions
        this.extKeyUsages = (this.hasExtension(Extension.extendedKeyUsage)
            ? Stream.of(ExtendedKeyUsage.fromExtensions(extensions).getUsages())
                .map(keyPurposeId -> EsacCryptoEnumUtils.findByOid(ExtendedKeyUsageType.class, keyPurposeId.toOID()))
                .collect(Collectors.toCollection(LinkedHashSet::new))
            : null);

        // noinspection ConstantConditions
        this.issuerAltNames = (this.hasExtension(Extension.issuerAlternativeName)
            ? EsacGeneralNameUtils.mapNames(GeneralNames.fromExtensions(extensions, Extension.issuerAlternativeName)) : null);

        if (this.hasExtension(Extension.keyUsage)) {
            // noinspection ConstantConditions
            KeyUsage keyUsage = KeyUsage.getInstance(this.getExtension(Extension.keyUsage).getParsedValue());

            this.keyUsages = Stream.of(KeyUsageType.values()).filter(keyUsageType -> keyUsage.hasUsages(keyUsageType.getTag()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(TaggedBean::getTag))));
        }

        // noinspection ConstantConditions
        this.ocspResponderUris = (this.hasExtension(Extension.authorityInfoAccess)
            ? Stream.of(AuthorityInformationAccess.getInstance(this.getExtension(Extension.authorityInfoAccess).getParsedValue()).getAccessDescriptions())
                .filter(accessDesc -> (accessDesc.getAccessMethod().equals(AccessDescription.id_ad_ocsp) &&
                    (accessDesc.getAccessLocation().getTagNo() == GeneralName.uniformResourceIdentifier)))
                .map(accessDesc -> URI
                    .create(DERIA5String.getInstance(((DERTaggedObject) accessDesc.getAccessLocation().toASN1Primitive()).getObject()).getString()))
                .collect(Collectors.toCollection(LinkedHashSet::new))
            : null);

        // noinspection ConstantConditions
        this.subjAltNames = (this.hasExtension(Extension.subjectAlternativeName)
            ? EsacGeneralNameUtils.mapNames(GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName)) : null);
    }

    @Override
    public boolean hasExtension(ASN1ObjectIdentifier oid) {
        return (this.getExtension(oid) != null);
    }

    @Nullable
    @Override
    public Extension getExtension(ASN1ObjectIdentifier oid) {
        return (this.hasHolder() ? this.holder.getExtension(oid) : null);
    }

    @Override
    public boolean hasExtensions() {
        return (this.hasHolder() && this.holder.hasExtensions());
    }

    @Nullable
    @Override
    public Extensions getExtensions() {
        return (this.hasHolder() ? this.holder.getExtensions() : null);
    }

    @Override
    public boolean hasHolder() {
        return (this.holder != null);
    }

    @Nullable
    @Override
    public X509CertificateHolder getHolder() {
        return this.holder;
    }

    @Override
    public boolean hasIssuerAltNames() {
        return !EsacMultimapUtils.isEmpty(this.issuerAltNames);
    }

    @Nullable
    @Override
    public ListMultimap<GeneralNameType, ASN1Encodable> getIssuerAltNames() {
        return this.issuerAltNames;
    }

    @Override
    public boolean hasIssuerDn() {
        return (this.issuerDn != null);
    }

    @Nullable
    @Override
    public Dn getIssuerDn() {
        return this.issuerDn;
    }

    @Override
    public X509Certificate getService() {
        return this.service;
    }
}
