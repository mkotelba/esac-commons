package com.esacinc.commons.crypto.ssl.revocation.impl;

import com.esacinc.commons.crypto.net.mime.EsacCryptoMediaTypes;
import com.esacinc.commons.crypto.utils.EsacCryptoEnumUtils;
import com.esacinc.commons.utils.EsacEnumUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils;
import com.esacinc.commons.crypto.ssl.impl.AbstractEsacPathChecker;
import com.esacinc.commons.crypto.ssl.revocation.OcspCertificateStatusType;
import com.esacinc.commons.crypto.ssl.revocation.OcspResponseStatusType;
import com.esacinc.commons.crypto.ssl.revocation.OcspRevokeReasonType;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.time.utils.EsacDateUtils;
import com.esacinc.commons.utils.EsacStreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.DigestCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;
import org.springframework.http.MediaType;

public class EsacRevocationChecker extends AbstractEsacPathChecker {
    private static class OcspExtension implements java.security.cert.Extension {
        private Extension ext;

        public OcspExtension(Extension ext) {
            this.ext = ext;
        }

        @Override
        public void encode(OutputStream outStream) throws IOException {
            outStream.write(this.ext.getEncoded());
        }

        @Override
        public boolean isCritical() {
            return this.ext.isCritical();
        }

        @Override
        public String getId() {
            return this.ext.getExtnId().getId();
        }

        @Override
        public byte[] getValue() {
            return this.ext.getExtnValue().getOctets();
        }
    }

    private final static Map<String, String> BASE_OCSP_REQ_HEADERS = Stream.of(new ImmutablePair<>(HttpHeaders.ACCEPT, EsacCryptoMediaTypes.OCSP_RESP_VALUE),
        new ImmutablePair<>(HttpHeaders.CONTENT_TYPE, EsacCryptoMediaTypes.OCSP_REQ_VALUE)).collect(EsacStreamUtils.toMap(LinkedHashMap::new));

    private final static Logger LOGGER = LoggerFactory.getLogger(EsacRevocationChecker.class);

    private int connectTimeout;
    private AlgorithmIdentifier digestAlgId;
    private boolean nonceOptional;
    private int nonceSize;
    private boolean optional;
    private ListOrderedSet<AlgorithmIdentifier> preferredSigAlgIds;
    private int readTimeout;
    private SecureRandom secureRandom;
    private DigestCalculator digestCalc;
    private Extension[] baseOcspReqExts;

    public EsacRevocationChecker(RestEndpointType endpointType, X509Certificate issuerCert) {
        super(endpointType, issuerCert, BasicReason.UNDETERMINED_REVOCATION_STATUS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.digestCalc = EsacDigestUtils.CALC_PROV.get(this.digestAlgId);

        Extension respTypeOcspReqExt =
            new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_response, false, new DEROctetString(new DERSequence(OCSPObjectIdentifiers.id_pkix_ocsp_basic)));

        ASN1EncodableVector preferredSigAlgsVector = new ASN1EncodableVector();
        this.preferredSigAlgIds.forEach(preferredSigAlgId -> preferredSigAlgsVector.add(new DERSequence(preferredSigAlgId)));
        Extension preferredSigAlgsOcspReqExt =
            new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_pref_sig_algs, false, new DEROctetString(new DERSequence(preferredSigAlgsVector)));

        this.baseOcspReqExts = ArrayUtils.toArray(respTypeOcspReqExt, preferredSigAlgsOcspReqExt);
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public EsacRevocationChecker clone() {
        return this;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private static Map<String, java.security.cert.Extension> mapOcspResponseExtensions(BasicOCSPResp ocspResp) {
        return ((List<ASN1ObjectIdentifier>) ocspResp.getExtensionOIDs()).stream()
            .collect(Collectors.toMap(ASN1ObjectIdentifier::getId, ocspCertRespExtOid -> new OcspExtension(ocspResp.getExtension(ocspCertRespExtOid))));
    }

    @Nullable
    private static URL findOcspResponderUrl(X509Certificate cert) throws IOException {
        byte[] authorityInfoAccessExtContent = cert.getExtensionValue(Extension.authorityInfoAccess.getId());

        if (authorityInfoAccessExtContent == null) {
            return null;
        }

        // noinspection ConstantConditions
        GeneralName ocspResponderUrlName = Stream
            .of(AuthorityInformationAccess
                .getInstance(ASN1Primitive.fromByteArray(((DEROctetString) ASN1Primitive.fromByteArray(authorityInfoAccessExtContent)).getOctets()))
                .getAccessDescriptions())
            .filter(accessDesc -> accessDesc.getAccessMethod().getId().equals(OCSPObjectIdentifiers.id_pkix_ocsp.getId()))
            .map(AccessDescription::getAccessLocation).filter(accessLoc -> (accessLoc.getTagNo() == GeneralName.uniformResourceIdentifier)).findFirst()
            .orElse(null);

        // noinspection ConstantConditions
        return ((ocspResponderUrlName != null)
            ? new URL(DERIA5String.getInstance(((DERTaggedObject) ocspResponderUrlName.toASN1Primitive()).getObject()).getString()) : null);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void checkInternal(X509Certificate cert, String certSubjectDnNameStr, String certIssuerDnNameStr, BigInteger certSerialNum)
        throws CertPathValidatorException {
        URL ocspResponderUrl;

        try {
            ocspResponderUrl = findOcspResponderUrl(cert);
        } catch (IOException e) {
            throw this.buildException(String.format("Unable to determine SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP URL.",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum), e);
        }

        if (ocspResponderUrl == null) {
            if (!this.optional) {
                throw this.buildException(String.format("SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) does not specify an OCSP URL.",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum));
            } else {
                LOGGER.info(String.format("Skipping SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) revocation checking.",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum));

                return;
            }
        }

        X509CertificateHolder issuerCertHolder;

        try {
            issuerCertHolder = new JcaX509CertificateHolder(this.issuerCert);
        } catch (CertificateEncodingException e) {
            throw this.buildException(String.format(
                "Unable to build SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) issuer certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) holder.",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certIssuerDnNameStr,
                this.issuerCert.getIssuerX500Principal().getName(), this.issuerCert.getSerialNumber()), e);
        }

        EsacCertificateId ocspReqCertId;

        try {
            ocspReqCertId = new EsacCertificateId(this.digestCalc, issuerCertHolder, certSerialNum);
        } catch (OCSPException e) {
            throw this.buildException(String.format("Unable to determine SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP ID.",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum), e);
        }

        byte[] nonceOcspReqExtContent = this.generateNonce();
        Extension nonceOcspReqExt = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, new DEROctetString(nonceOcspReqExtContent));

        OCSPReqBuilder ocspReqBuilder = new OCSPReqBuilder();
        ocspReqBuilder.setRequestExtensions(new Extensions(ArrayUtils.add(this.baseOcspReqExts, nonceOcspReqExt)));
        ocspReqBuilder.addRequest(ocspReqCertId);

        byte[] ocspReqContent;

        try {
            ocspReqContent = ocspReqBuilder.build().getEncoded();
        } catch (IOException | OCSPException e) {
            throw this.buildException(String.format("Unable to build SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP request.",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum), e);
        }

        OCSPResp ocspRespWrapper;

        try {
            ocspRespWrapper = this.queryOcspResponder(ocspResponderUrl, ocspReqContent);
        } catch (IOException e) {
            throw this
                .buildException(String.format("Unable to query SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl), e);
        }

        OcspResponseStatusType ocspRespStatus =
            EsacCryptoEnumUtils.findByTag(Stream.of(OcspResponseStatusType.class.getEnumConstants()).sorted(OrderComparator.INSTANCE), ocspRespWrapper.getStatus());

        if (ocspRespStatus != OcspResponseStatusType.SUCCESSFUL) {
            throw this.buildException(
                String.format("Invalid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response status (%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespStatus));
        }

        ASN1ObjectIdentifier ocspRespType = ocspRespWrapper.toASN1Structure().getResponseBytes().getResponseType();

        if (!ocspRespType.equals(OCSPObjectIdentifiers.id_pkix_ocsp_basic)) {
            throw this.buildException(
                String.format("Invalid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response type (oid=%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespType.getId()));
        }

        BasicOCSPResp ocspResp;

        try {
            ocspResp = ((BasicOCSPResp) ocspRespWrapper.getResponseObject());
        } catch (OCSPException e) {
            throw this.buildException(
                String.format("Unable to build SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response.",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl),
                e);
        }

        String ocspRespProducedAtTimeStr = EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(ocspResp.getProducedAt());
        Extension nonceOcspRespExt = ocspResp.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);

        if (nonceOcspRespExt == null) {
            if (!this.nonceOptional) {
                throw this.buildException(String.format(
                    "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) does not contain a nonce extension (oid=%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                    OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId()));
            }
        } else {
            byte[] nonceOcspRespExtContent = nonceOcspRespExt.getExtnValue().getOctets();

            if (!Arrays.equals(nonceOcspReqExtContent, nonceOcspRespExtContent)) {
                throw this.buildException(String.format(
                    "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) nonce extension (oid=%s) value does not match (%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                    OCSPObjectIdentifiers.id_pkix_ocsp_nonce.getId(), Hex.encodeHexString(nonceOcspRespExtContent)));
            }
        }

        SingleResp ocspCertResp = null;
        EsacCertificateId availableOcspCertRespId;

        for (SingleResp availableOcspCertResp : ocspResp.getResponses()) {
            availableOcspCertRespId = new EsacCertificateId(availableOcspCertResp.getCertID());

            try {
                if (availableOcspCertRespId.matches(ocspReqCertId)) {
                    ocspCertResp = availableOcspCertResp;
                }
            } catch (OCSPException e) {
                throw this.buildException(String.format(
                    "Unable to match SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) certificate (serialNum=%d) status.",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                    availableOcspCertRespId.getSerialNumber()), e);
            }
        }

        if (ocspCertResp == null) {
            throw this.buildException(String.format(
                "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) does not contain matching certificate status.",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr));
        }

        Date ocspCertRespNextUpdateTime = ocspCertResp.getNextUpdate();
        String ocspCertRespThisUpdateTimeStr = EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(ocspCertResp.getThisUpdate()), ocspCertRespNextUpdateTimeStr =
            ((ocspCertRespNextUpdateTime != null) ? EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(ocspCertRespNextUpdateTime) : null);
        CertificateStatus ocspCertRespStatusObj = ocspCertResp.getCertStatus();
        OcspCertificateStatusType ocspCertRespStatus =
            EsacEnumUtils.findByType(Stream.of(OcspCertificateStatusType.class.getEnumConstants()).sorted(OrderComparator.INSTANCE),
                ((ocspCertRespStatusObj != null) ? ocspCertRespStatusObj.getClass() : CertificateStatus.class));

        // noinspection ConstantConditions
        switch (ocspCertRespStatus) {
            case GOOD:
                LOGGER.info(new MarkerBuilder(EsacLogstashTags.SSL).build(),
                    String.format(
                        "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) certificate status (thisUpdate=%s, nextUpdate=%s) is good.",
                        this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                        ocspCertRespThisUpdateTimeStr, ocspCertRespNextUpdateTimeStr));
                break;
            case REVOKED:
                RevokedStatus ocspCertRespRevokedStatus = ((RevokedStatus) ocspCertRespStatusObj);
                // noinspection ConstantConditions
                Date ocspCertRespRevokeTime = ocspCertRespRevokedStatus.getRevocationTime();
                OcspRevokeReasonType ocspCertRespRevokeReason = (ocspCertRespRevokedStatus.hasRevocationReason()
                    ? EsacCryptoEnumUtils.findByTag(Stream.of(OcspRevokeReasonType.class.getEnumConstants()).sorted(OrderComparator.INSTANCE),
                        ocspCertRespRevokedStatus.getRevocationReason())
                    : OcspRevokeReasonType.UNSPECIFIED);

                // noinspection ConstantConditions
                throw this.buildException(
                    String.format(
                        "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) certificate status (thisUpdate=%s, nextUpdate=%s) is revoked (time=%s, reason=%s).",
                        this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                        ocspCertRespThisUpdateTimeStr, ocspCertRespNextUpdateTimeStr, EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(ocspCertRespRevokeTime),
                        ocspCertRespRevokeReason.name()),
                    new CertificateRevokedException(ocspCertRespRevokeTime, ocspCertRespRevokeReason.getReason(), this.issuerCert.getSubjectX500Principal(),
                        mapOcspResponseExtensions(ocspResp)));
            case UNKNOWN:
                throw this.buildException(String.format(
                    "SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) OCSP responder (url=%s) response (producedAt=%s) certificate status (thisUpdate=%s, nextUpdate=%s) is unknown.",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, ocspResponderUrl, ocspRespProducedAtTimeStr,
                    ocspCertRespThisUpdateTimeStr, ocspCertRespNextUpdateTimeStr));
        }
    }

    private OCSPResp queryOcspResponder(URL ocspResponderUrl, byte[] ocspReqContent) throws IOException {
        HttpURLConnection ocspResponderConn = ((HttpURLConnection) ocspResponderUrl.openConnection());
        ocspResponderConn.setDoInput(true);
        ocspResponderConn.setDoOutput(true);
        ocspResponderConn.setUseCaches(false);
        ocspResponderConn.setConnectTimeout(this.connectTimeout);
        ocspResponderConn.setReadTimeout(this.readTimeout);
        ocspResponderConn.setRequestMethod(HttpPost.METHOD_NAME);

        BASE_OCSP_REQ_HEADERS.forEach(ocspResponderConn::setRequestProperty);
        ocspResponderConn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Integer.toString(ocspReqContent.length));

        try (OutputStream ocspResponderOutStream = ocspResponderConn.getOutputStream()) {
            ocspResponderOutStream.write(ocspReqContent);
            ocspResponderOutStream.flush();
        }

        OCSPResp ocspRespWrapper;

        try (InputStream ocspResponderInStream = ocspResponderConn.getInputStream()) {
            ocspRespWrapper = new OCSPResp(IOUtils.toByteArray(ocspResponderInStream));
        }

        int ocspRespStatus = ocspResponderConn.getResponseCode();

        if (ocspRespStatus != HttpURLConnection.HTTP_OK) {
            throw new IOException(String.format("Invalid OCSP responder (url=%s) response status (code=%s, msg=%s).", ocspResponderUrl, ocspRespStatus,
                ocspResponderConn.getResponseMessage()));
        }

        String ocspRespContentType = ocspResponderConn.getContentType();

        if ((ocspRespContentType == null) || !MediaType.valueOf(ocspRespContentType).equals(EsacCryptoMediaTypes.OCSP_RESP)) {
            throw new IOException(String.format("Invalid OCSP responder (url=%s) response content type (%s).", ocspResponderUrl, ocspRespContentType));
        }

        return ocspRespWrapper;
    }

    private byte[] generateNonce() {
        byte[] ocspNonce = new byte[this.nonceSize];

        this.secureRandom.nextBytes(ocspNonce);

        return ocspNonce;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public AlgorithmIdentifier getDigestAlgorithmId() {
        return this.digestAlgId;
    }

    public void setDigestAlgorithmId(AlgorithmIdentifier digestAlgId) {
        this.digestAlgId = digestAlgId;
    }

    public boolean isNonceOptional() {
        return this.nonceOptional;
    }

    public void setNonceOptional(boolean nonceOptional) {
        this.nonceOptional = nonceOptional;
    }

    @Nonnegative
    public int getNonceSize() {
        return this.nonceSize;
    }

    public void setNonceSize(@Nonnegative int nonceSize) {
        this.nonceSize = nonceSize;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public ListOrderedSet<AlgorithmIdentifier> getPreferredSignatureAlgorithmIds() {
        return this.preferredSigAlgIds;
    }

    public void setPreferredSignatureAlgorithmIds(List<AlgorithmIdentifier> preferredSigAlgIds) {
        this.preferredSigAlgIds = ListOrderedSet.listOrderedSet(preferredSigAlgIds);
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }
}
