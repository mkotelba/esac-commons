package com.esacinc.commons.crypto.ssl.logging.impl;

import ch.qos.logback.classic.Level;
import com.esacinc.commons.crypto.ssl.logging.SslHelloLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.SslHelloLoggingEventProcessor;
import com.esacinc.commons.net.logging.RestEndpointType;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class SslHelloLoggingEventProcessorImpl extends AbstractSslLoggingEventProcessor<SslHelloLoggingEvent> implements SslHelloLoggingEventProcessor {
    private final static String HANDSHAKE_MSG_CLASS_NAME_PREFIX = SslDebugPrintStream.SUN_SEC_SSL_PKG_NAME_PREFIX + "HandshakeMessage$";

    private final static String HELLO_CLASS_NAME_SUFFIX = "Hello";

    private final static Map<String, RestEndpointType> CALLER_CLASS_ENDPOINT_TYPES = Stream.of(RestEndpointType.values()).collect(Collectors.toMap(
        endpointType -> (HANDSHAKE_MSG_CLASS_NAME_PREFIX + StringUtils.capitalize(endpointType.getName()) + HELLO_CLASS_NAME_SUFFIX), Function.identity()));

    private final static String CIPHER_SUITE_DELIM = ", ";

    private final static String CIPHER_SUITES_PATTERN = "([\\w" + CIPHER_SUITE_DELIM + "]+)";

    private final static String PROTOCOL_LINE_PATTERN_PREFIX = "^";
    private final static String CIPHER_SUITES_LINE_PATTERN_PREFIX = "^Cipher Suite";

    private final static String PROTOCOL_LINE_PATTERN_SUFFIX = HELLO_CLASS_NAME_SUFFIX + ", ([^$]+)$";
    private final static String CLIENT_CIPHER_SUITES_LINE_PATTERN_SUFFIX = "s: \\[" + CIPHER_SUITES_PATTERN + "\\]$";
    private final static String SERVER_CIPHER_SUITES_LINE_PATTERN_SUFFIX = ": " + CIPHER_SUITES_PATTERN + "$";

    private final static Map<RestEndpointType, Pattern> PROTOCOL_LINE_PATTERN_MAP =
        Stream.of(RestEndpointType.values()).collect(Collectors.toMap(Function.identity(),
            endpointType -> Pattern.compile((PROTOCOL_LINE_PATTERN_PREFIX + StringUtils.capitalize(endpointType.getName()) + PROTOCOL_LINE_PATTERN_SUFFIX))));

    private final static Map<RestEndpointType, Pattern> CIPHER_SUITES_LINE_PATTERN_MAP =
        Stream.of(RestEndpointType.values()).collect(Collectors.toMap(Function.identity(), endpointType -> Pattern.compile((CIPHER_SUITES_LINE_PATTERN_PREFIX +
            ((endpointType == RestEndpointType.CLIENT) ? CLIENT_CIPHER_SUITES_LINE_PATTERN_SUFFIX : SERVER_CIPHER_SUITES_LINE_PATTERN_SUFFIX)))));

    public SslHelloLoggingEventProcessorImpl() {
        super(SslHelloLoggingEventImpl::new, "handshake");
    }

    @Override
    public synchronized void processEvent(StackTraceElement[] frames, String msg) {
        String callerClassName = frames[0].getClassName();
        RestEndpointType endpointType =
            CALLER_CLASS_ENDPOINT_TYPES.keySet().stream().filter(callerClassName::startsWith).findFirst().map(CALLER_CLASS_ENDPOINT_TYPES::get).orElse(null);
        SslHelloLoggingEvent event = this.threadEvent.get();

        if (event.getEndpointType() == null) {
            Matcher protocolLineMatcher = PROTOCOL_LINE_PATTERN_MAP.get(endpointType).matcher(msg);

            if (!protocolLineMatcher.matches()) {
                this.threadEvent.remove();

                return;
            }

            event.setEndpointType(endpointType);
            event.setProtocol(protocolLineMatcher.group(1));
        } else {
            Matcher cipherSuitesMatcher = CIPHER_SUITES_LINE_PATTERN_MAP.get(endpointType).matcher(msg);

            if (!cipherSuitesMatcher.matches()) {
                return;
            }

            String[] cipherSuites = StringUtils.splitByWholeSeparator(cipherSuitesMatcher.group(1), CIPHER_SUITE_DELIM);
            event.setCipherSuites(cipherSuites);

            this.dispatchEvent(frames, Level.DEBUG, event);
        }
    }

    @Override
    public boolean canProcessEvent(StackTraceElement[] frames) {
        if (!super.canProcessEvent(frames)) {
            return false;
        }

        String callerClassName = frames[0].getClassName();

        return CALLER_CLASS_ENDPOINT_TYPES.keySet().stream().anyMatch(callerClassName::startsWith);
    }
}
