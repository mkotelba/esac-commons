package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.crypto.ssl.logging.SslHelloLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.SslHelloLoggingEventHandler;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.utils.EsacStringUtils;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;

public class SslHelloLoggingEventHandlerImpl extends AbstractSslLoggingEventHandler<SslHelloLoggingEvent> implements SslHelloLoggingEventHandler {
    public final static String BEAN_NAME = SSL_BEAN_NAME_PREFIX + "Hello";

    public final static String SSL_CLIENT_HELLO_FIELD_NAME = ElasticsearchFieldNames.SSL_CLIENT_PREFIX + ElasticsearchFieldNames.HELLO_SUFFIX;
    public final static String SSL_SERVER_HELLO_FIELD_NAME = ElasticsearchFieldNames.SSL_SERVER_PREFIX + ElasticsearchFieldNames.HELLO_SUFFIX;

    private final static String MSG_FORMAT_PREFIX = SSL_MSG_FORMAT_PREFIX + " HELLO";

    private final static String SHORT_MSG_FORMAT = MSG_FORMAT_PREFIX + EsacStringUtils.PERIOD;
    private final static String FULL_MSG_FORMAT = MSG_FORMAT_PREFIX + " (protocol=%s, cipherSuite(s)=[%s]" + SSL_FULL_MSG_FORMAT_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(SSL_CLIENT_HELLO_FIELD_NAME, ElasticsearchDatatype.OBJECT, SslHelloLoggingEvent.class),
            new ElasticsearchFieldMappingImpl(SSL_SERVER_HELLO_FIELD_NAME, ElasticsearchDatatype.OBJECT, SslHelloLoggingEvent.class));

    public SslHelloLoggingEventHandlerImpl() {
        super(SslHelloLoggingEvent.class);
    }

    @Override
    public String buildMessage(SslHelloLoggingEvent event, boolean full) {
        String eventEndpointTypeName = event.getEndpointType().getName();

        return (full
            ? String.format(FULL_MSG_FORMAT, eventEndpointTypeName, event.getProtocol(),
                Stream.of(event.getCipherSuites()).collect(Collectors.joining(EsacStringUtils.ITEM_DELIM)))
            : String.format(SHORT_MSG_FORMAT, eventEndpointTypeName));
    }

    @Override
    public String buildFieldName(SslHelloLoggingEvent event) {
        return ((event.getEndpointType() == RestEndpointType.CLIENT) ? SSL_CLIENT_HELLO_FIELD_NAME : SSL_SERVER_HELLO_FIELD_NAME);
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
