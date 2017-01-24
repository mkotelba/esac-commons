package com.esacinc.commons.crypto.ssl.constraints.impl;

import com.esacinc.commons.crypto.key.utils.EsacKeyUtils;
import com.esacinc.commons.crypto.sign.utils.EsacSignatureUtils.EsacSignatureAlgorithmIdentifierFinder;
import com.esacinc.commons.crypto.ssl.impl.AbstractEsacPathChecker;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.net.logging.RestEndpointType;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsacConstraintsChecker extends AbstractEsacPathChecker {
    private final static Logger LOGGER = LoggerFactory.getLogger(EsacConstraintsChecker.class);

    private Map<String, AlgorithmIdentifier> keyAlgs;
    private Map<String, Integer> minKeyAlgSizes;
    private Set<AlgorithmIdentifier> sigAlgIds;

    public EsacConstraintsChecker(RestEndpointType endpointType, X509Certificate issuerCert) {
        super(endpointType, issuerCert, BasicReason.ALGORITHM_CONSTRAINED);
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public EsacConstraintsChecker clone() {
        return this;
    }

    @Override
    protected void checkInternal(X509Certificate cert, String certSubjectDnNameStr, String certIssuerDnNameStr, BigInteger certSerialNum)
        throws CertPathValidatorException {
        PublicKey certPublicKey = cert.getPublicKey();
        String certKeyAlgName = certPublicKey.getAlgorithm();

        if (!this.keyAlgs.containsKey(certKeyAlgName)) {
            throw this.buildException(String.format("Invalid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) key type (algName=%s).",
                this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certKeyAlgName));
        }

        ASN1ObjectIdentifier certKeyAlgOid = this.keyAlgs.get(certKeyAlgName).getAlgorithm();

        if (this.minKeyAlgSizes.containsKey(certKeyAlgName)) {
            int minKeyAlgSize = this.minKeyAlgSizes.get(certKeyAlgName), certKeySize = EsacKeyUtils.extractSize(certKeyAlgName, certPublicKey);

            if (certKeySize >= minKeyAlgSize) {
                LOGGER.debug(new MarkerBuilder(EsacLogstashTags.SSL).build(),
                    String.format("Valid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) key (algName=%s, algOid=%s) size (%d >= %d).",
                        this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certKeyAlgName, certKeyAlgOid, certKeySize,
                        minKeyAlgSize));
            } else if (certKeySize == -1) {
                throw this.buildException(
                    String.format("Unknown SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) key (algName=%s, algOid=%s) size.",
                        this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certKeyAlgName, certKeyAlgOid));
            } else {
                throw this.buildException(
                    String.format("Invalid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) key (algName=%s, algOid=%s) size (%d < %d).",
                        this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certKeyAlgName, certKeyAlgOid, certKeySize,
                        minKeyAlgSize));
            }
        }

        String certSigAlgName = cert.getSigAlgName(), certSigAlgOid = cert.getSigAlgOID();
        AlgorithmIdentifier certSigAlgId = EsacSignatureAlgorithmIdentifierFinder.INSTANCE.find(certSigAlgName);

        if (this.sigAlgIds.contains(certSigAlgId)) {
            LOGGER.debug(new MarkerBuilder(EsacLogstashTags.SSL).build(),
                String.format("Valid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) signature algorithm (name=%s, oid=%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certSigAlgName, certSigAlgOid));
        } else {
            throw this.buildException(
                String.format("Invalid SSL %s certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) signature algorithm (name=%s, oid=%s).",
                    this.endpointType.getName(), certSubjectDnNameStr, certIssuerDnNameStr, certSerialNum, certSigAlgName, certSigAlgOid));
        }
    }

    public Map<String, AlgorithmIdentifier> getKeyAlgorithms() {
        return this.keyAlgs;
    }

    public void setKeyAlgorithms(Map<String, AlgorithmIdentifier> keyAlgs) {
        this.keyAlgs = keyAlgs;
    }

    public Map<String, Integer> getMinimumKeyAlgorithmSizes() {
        return this.minKeyAlgSizes;
    }

    public void setMinimumKeyAlgorithmSizes(Map<String, Integer> minKeyAlgSizes) {
        this.minKeyAlgSizes = minKeyAlgSizes;
    }

    public Set<AlgorithmIdentifier> getSignatureAlgorithmIds() {
        return this.sigAlgIds;
    }

    public void setSignatureAlgorithmIds(Set<AlgorithmIdentifier> sigAlgIds) {
        this.sigAlgIds = sigAlgIds;
    }
}
