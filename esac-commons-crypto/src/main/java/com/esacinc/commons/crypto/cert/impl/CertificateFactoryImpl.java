package com.esacinc.commons.crypto.cert.impl;

import com.esacinc.commons.crypto.CryptoException;
import com.esacinc.commons.crypto.CryptoProviders;
import com.esacinc.commons.crypto.cert.CertificateConfig;
import com.esacinc.commons.crypto.cert.CertificateDescriptor;
import com.esacinc.commons.crypto.cert.CertificateFactory;
import com.esacinc.commons.crypto.cert.CertificateFactoryRequest;
import com.esacinc.commons.crypto.cert.CertificateFactoryResponse;
import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.cert.KeyUsageType;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils.EsacDigestAlgorithmIdentifierFinder;
import com.esacinc.commons.crypto.factory.impl.AbstractCryptoServiceFactory;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import com.esacinc.commons.crypto.naming.utils.EsacGeneralNameUtils;
import com.esacinc.commons.crypto.time.IntervalConfig;
import com.esacinc.commons.utils.EsacComparatorUtils;
import com.esacinc.commons.utils.EsacComparatorUtils.MapOrderedComparator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateFactoryImpl
    extends AbstractCryptoServiceFactory<CertificateConfig, CertificateFactoryRequest, X509Certificate, CertificateInfo, CertificateFactoryResponse>
    implements CertificateFactory {
    public final static MapOrderedComparator<ASN1ObjectIdentifier> ATTR_OID_COMPARATOR =
        EsacComparatorUtils.comparingOrder(BCStrictStyle.C, BCStrictStyle.ST, BCStrictStyle.L, BCStrictStyle.O, BCStrictStyle.OU, BCStrictStyle.CN);

    private final static Logger LOGGER = LoggerFactory.getLogger(CertificateFactoryImpl.class);

    private Map<Dn, BigInteger> issuedSerialNums = new LinkedHashMap<>();

    @Override
    protected CertificateFactoryResponse buildInternal(CertificateFactoryRequest req, CertificateConfig config, @Nullable String id, @Nullable String name)
        throws Exception {
        Provider prov = config.getProvider();
        String algName = config.getAlgorithmName();
        boolean hasIssuerCredInfo = req.hasIssuerCredentialInfo();
        CredentialInfo issuerCredInfo = req.getIssuerCredentialInfo();
        // noinspection ConstantConditions
        KeyPairInfo keyPairInfo = req.getKeyPairInfo(), issuerKeyPairInfo = (hasIssuerCredInfo ? issuerCredInfo.getKeyPairDescriptor() : keyPairInfo);
        CertificateDescriptor<?> issuerCertDesc = (hasIssuerCredInfo ? issuerCredInfo.getCertificateDescriptor() : config);
        JcaX509v3CertificateBuilder certBuilder = this.buildCertificateBuilder(issuerKeyPairInfo, issuerCertDesc, keyPairInfo, config);
        String certSigAlgName = config.getSignatureAlgorithmName();

        // noinspection ConstantConditions
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(certSigAlgName);
        contentSignerBuilder.setProvider(CryptoProviders.SUN_RSA_SIGN);
        contentSignerBuilder.setSecureRandom(config.getSecureRandom());

        // noinspection ConstantConditions
        X509CertificateHolder certHolder = certBuilder.build(contentSignerBuilder.build(issuerKeyPairInfo.getPrivateKey()));

        java.security.cert.CertificateFactory factory = java.security.cert.CertificateFactory.getInstance(algName, prov);
        CertificateInfo info;

        try (InputStream certInStream = new ByteArrayInputStream(certHolder.getEncoded())) {
            info = new CertificateInfoImpl((X509Certificate) factory.generateCertificate(certInStream));
        }

        CertificateFactoryResponse resp = super.buildInternal(req, config, id, name);
        resp.setDescriptor(info);

        // noinspection ConstantConditions
        LOGGER.info(String.format(
            "Processed certificate factory request (provName=%s, algOid=%s, algName=%s, id=%s, name=%s, issuerDn={%s}, subjDn={%s}, serialNum=%s).",
            prov.getName(), info.getAlgorithmId().getAlgorithm(), algName, id, name, info.getIssuerDn(), info.getSubjectDn(), info.getSerialNumber()));

        return resp;
    }

    @Override
    protected CertificateFactoryResponse buildResponse() throws Exception {
        return new CertificateFactoryResponseImpl();
    }

    @Override
    protected void validate(CertificateFactoryRequest req, CertificateConfig config, @Nullable String id, @Nullable String name) throws Exception {
        if (!req.hasIssuerCredentialInfo() && !config.isRootCertificateAuthority()) {
            throw new CryptoException(
                String.format("Certificate factory request (provName=%s, algOid=%s, algName=%s, id=%s, name=%s) for %s does not contain issuer information.",
                    config.getProvider().getName(), config.getAlgorithmId().getAlgorithm(), config.getAlgorithmName(), id, name,
                    (config.isCertificateAuthority() ? "non-root certificate authority" : "leaf certificate")));
        }

        if (!config.hasInterval()) {
            throw new CryptoException(String.format("Invalid certificate factory request (provName=%s, algOid=%s, algName=%s, id=%s, name=%s) interval: %s",
                config.getProvider().getName(), config.getAlgorithmId().getAlgorithm(), config.getAlgorithmName(), id, name, config.getInterval()));
        }
    }

    private <T extends CertificateDescriptor<?>> JcaX509v3CertificateBuilder buildCertificateBuilder(KeyPairInfo issuerKeyPairInfo, T issuerCertDesc,
        KeyPairInfo keyPairInfo, CertificateConfig certConfig) throws Exception {
        boolean certCa = certConfig.isCertificateAuthority();
        IntervalConfig certIntervalConfig = certConfig.getInterval();
        // noinspection ConstantConditions
        long certIntervalNotBeforeTime = (new Date().getTime() + certIntervalConfig.getOffset());

        if (!certConfig.hasSerialNumber()) {
            Dn issuerDn = issuerCertDesc.getSubjectDn();
            BigInteger certSerialNumValue;

            this.issuedSerialNums.put(issuerDn,
                (this.issuedSerialNums.containsKey(issuerDn)
                    ? (certSerialNumValue = BigInteger.valueOf((this.issuedSerialNums.get(issuerDn).longValue() + 1)))
                    : (certSerialNumValue = BigInteger.valueOf(1))));

            certConfig.setSerialNumber(certSerialNumValue);
        }

        // noinspection ConstantConditions
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(issuerCertDesc.getSubjectDn().toX500Name(ATTR_OID_COMPARATOR),
            certConfig.getSerialNumber(), new Date(certIntervalNotBeforeTime), new Date((certIntervalNotBeforeTime + certIntervalConfig.getDuration())),
            certConfig.getSubjectDn().toX500Name(ATTR_OID_COMPARATOR), keyPairInfo.getPublicKey());

        if (!certCa) {
            certBuilder.addExtension(Extension.authorityKeyIdentifier, false, issuerKeyPairInfo.getAuthorityKeyId());
        }

        // noinspection ConstantConditions
        certBuilder.addExtension(Extension.subjectKeyIdentifier, false, new SubjectKeyIdentifier(EsacDigestUtils.CALC_PROV
            .digest(EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(certConfig.getSignatureAlgorithmId()), keyPairInfo.getPublicKey().getEncoded())));

        certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(certCa));

        if (certConfig.hasKeyUsages()) {
            int keyUsage = 0;

            // noinspection ConstantConditions
            for (KeyUsageType keyUsageType : certConfig.getKeyUsages()) {
                keyUsage |= keyUsageType.getTag();
            }

            certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsage));
        }

        if (certConfig.hasExtendedKeyUsages()) {
            // noinspection ConstantConditions
            certBuilder.addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(certConfig.getExtendedKeyUsages().stream()
                .map(extKeyUsageType -> KeyPurposeId.getInstance(extKeyUsageType.getOid())).toArray(KeyPurposeId[]::new)));
        }

        if (certConfig.hasOcspResponderUris()) {
            ASN1EncodableVector certOcspResponderAccessDescVector = new ASN1EncodableVector();

            // noinspection ConstantConditions
            certConfig.getOcspResponderUris()
                .forEach(certOcspResponderUri -> certOcspResponderAccessDescVector.add(new AccessDescription(AccessDescription.id_ad_ocsp,
                    new GeneralName(GeneralNameType.UNIFORM_RESOURCE_IDENTIFIER.getTag(), certOcspResponderUri.toString()))));

            certBuilder.addExtension(Extension.authorityInfoAccess, false,
                AuthorityInformationAccess.getInstance(new DERSequence(certOcspResponderAccessDescVector)));
        }

        if (certConfig.hasSubjectAltNames()) {
            certBuilder.addExtension(Extension.subjectAlternativeName, false, EsacGeneralNameUtils.buildNames(certConfig.getSubjectAltNames()));
        }

        return certBuilder;
    }
}
