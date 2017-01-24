package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.crypto.ssl.logging.SslTrustLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.SslTrustLoggingEventHandler;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.net.logging.RestEndpointType;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class SslTrustLoggingEventHandlerImpl extends AbstractSslLoggingEventHandler<SslTrustLoggingEvent> implements SslTrustLoggingEventHandler {
    public final static String BEAN_NAME = SSL_BEAN_NAME_PREFIX + "Trust";

    public final static String SSL_CLIENT_TRUST_FIELD_NAME = ElasticsearchFieldNames.SSL_CLIENT_PREFIX + ElasticsearchFieldNames.TRUST_SUFFIX;
    public final static String SSL_SERVER_TRUST_FIELD_NAME = ElasticsearchFieldNames.SSL_SERVER_PREFIX + ElasticsearchFieldNames.TRUST_SUFFIX;

    private final static String CERT_MSG_DELIM = ", ";

    private final static String MSG_FORMAT_PREFIX = SSL_MSG_FORMAT_PREFIX + " is%s trusted";

    private final static String SHORT_MSG_FORMAT = MSG_FORMAT_PREFIX + EsacStringUtils.PERIOD;

    private final static String CERT_FULL_MSG_FORMAT = "{version=%d, issuerDn={%s}, subjDn={%s}, serialNum=%s, interval={start=%d, end=%d}}";
    private final static String FULL_MSG_FORMAT =
        MSG_FORMAT_PREFIX + " (authType=%s, cert(s)=[%s], pathCert(s)=[%s], trustAnchorCert=%s" + SSL_FULL_MSG_FORMAT_SUFFIX;

    private final static String NOT_TRUSTED_MSG_FORMAT_ARG = " not";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(SSL_CLIENT_TRUST_FIELD_NAME, ElasticsearchDatatype.OBJECT, SslTrustLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(SSL_SERVER_TRUST_FIELD_NAME, ElasticsearchDatatype.OBJECT, SslTrustLoggingEvent.class));

    public SslTrustLoggingEventHandlerImpl() {
        super(SslTrustLoggingEvent.class);
    }

    @Override
    public String buildMessage(SslTrustLoggingEvent event, boolean full) {
        String eventEndpointTypeName = event.getEndpointType().getName(), eventTrusted = (event.isTrusted() ? StringUtils.EMPTY : NOT_TRUSTED_MSG_FORMAT_ARG);

        // noinspection ConstantConditions
        return (full
            ? String.format(FULL_MSG_FORMAT, eventEndpointTypeName, eventTrusted, event.getAuthType(), (event.hasCertificates()
                ? Stream.of(event.getCertificates()).map(SslTrustLoggingEventHandlerImpl::buildCertificateMessage).collect(Collectors.joining(CERT_MSG_DELIM))
                : StringUtils.EMPTY),
                (event.hasPathCertificates()
                    ? Stream.of(event.getPathCertificates()).map(SslTrustLoggingEventHandlerImpl::buildCertificateMessage)
                        .collect(Collectors.joining(CERT_MSG_DELIM))
                    : StringUtils.EMPTY),
                (event.hasTrustAnchorCertificate() ? buildCertificateMessage(event.getTrustAnchorCertificate()) : StringUtils.EMPTY))
            : String.format(SHORT_MSG_FORMAT, eventEndpointTypeName, eventTrusted));
    }

    @Override
    public String buildFieldName(SslTrustLoggingEvent event) {
        return ((event.getEndpointType() == RestEndpointType.CLIENT) ? SSL_CLIENT_TRUST_FIELD_NAME : SSL_SERVER_TRUST_FIELD_NAME);
    }

    private static String buildCertificateMessage(X509Certificate cert) {
        return String.format(CERT_FULL_MSG_FORMAT, cert.getVersion(), cert.getIssuerX500Principal().toString(), cert.getSubjectX500Principal().toString(),
            cert.getSerialNumber(), cert.getNotBefore().getTime(), cert.getNotAfter().getTime());
    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
