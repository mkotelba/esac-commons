package com.esacinc.commons.crypto.ssl.impl;

import com.esacinc.commons.net.logging.RestEndpointType;
import java.math.BigInteger;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.CertPathValidatorException.Reason;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractEsacPathChecker extends PKIXCertPathChecker implements InitializingBean {
    protected final static Map<Class<? extends CertificateException>, Reason> CAUSE_REASON_MAP =
        Stream.of(new ImmutablePair<>(CertificateExpiredException.class, BasicReason.EXPIRED),
            new ImmutablePair<>(CertificateNotYetValidException.class, BasicReason.NOT_YET_VALID),
            new ImmutablePair<>(CertificateRevokedException.class, BasicReason.REVOKED)).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    protected RestEndpointType endpointType;
    protected X509Certificate issuerCert;
    protected Reason defaultReason;

    protected AbstractEsacPathChecker(RestEndpointType endpointType, @Nullable X509Certificate issuerCert, Reason defaultReason) {
        this.endpointType = endpointType;
        this.issuerCert = issuerCert;
        this.defaultReason = defaultReason;
    }

    @Override
    public void check(Certificate baseCert, Collection<String> unresolvedCriticalExts) throws CertPathValidatorException {
        X509Certificate cert = ((X509Certificate) baseCert);

        this.checkInternal(cert, cert.getSubjectX500Principal().getName(), cert.getIssuerX500Principal().getName(), cert.getSerialNumber());
    }

    @Override
    public void init(boolean forward) throws CertPathValidatorException {
        if (forward) {
            throw new CertPathValidatorException("Forward certificate path checking is not supported.");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    protected abstract void checkInternal(X509Certificate cert, String certSubjectDnNameStr, String certIssuerDnNameStr, BigInteger certSerialNum)
        throws CertPathValidatorException;

    protected CertPathValidatorException buildException(String msg) {
        return this.buildException(msg, null);
    }

    protected CertPathValidatorException buildException(String msg, @Nullable Throwable cause) {
        Reason reason = this.defaultReason;

        if (cause != null) {
            final Class<? extends Throwable> causeClass = cause.getClass();

            reason = CAUSE_REASON_MAP.entrySet().stream().filter(causeEntry -> causeEntry.getKey().isAssignableFrom(causeClass)).findFirst()
                .map(Entry::getValue).orElse(this.defaultReason);
        }

        return new CertPathValidatorException(msg, cause, null, -1, reason);
    }

    @Override
    public boolean isForwardCheckingSupported() {
        return false;
    }

    @Nullable
    @Override
    public Set<String> getSupportedExtensions() {
        return null;
    }
}
