package com.esacinc.commons.logging.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LogbackException;
import com.esacinc.commons.logging.LoggingMessages;
import com.esacinc.commons.logging.logback.impl.AbstractEsacAppender;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.net.http.impl.HttpParameterMultimapImpl;
import com.esacinc.commons.net.http.logging.HttpRequestLoggingEvent;
import com.esacinc.commons.net.http.logging.impl.HttpRequestLoggingEventImpl;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.test.impl.AbstractEsacCommonsUnitTests;
import com.esacinc.commons.utils.EsacMultimapUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.assertj.core.api.Assertions;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "esac-commons.test.unit.io.utils", "esac-commons.test.unit.config" },
    groups = { "esac-commons.test.unit.logging", "esac-commons.test.unit.logging.appender", "esac-commons.test.unit.logging.appender.event" })
public class EventAppenderUnitTests extends AbstractEsacCommonsUnitTests {
    private static class RecordingAppender extends AbstractEsacAppender {
        private LoggingEvent event;

        public RecordingAppender(LoggerContext loggerContext) {
            super("recording");

            this.setContext(loggerContext);

            this.start();
        }

        @Override
        protected void doAppendInternal(LoggingEvent event) throws LogbackException {
            this.event = event;
        }

        public LoggingEvent getEvent() {
            return this.event;
        }
    }

    private final static String HEADER_NAME_1 = HttpHeaderNames.CONTENT_TYPE.toString();
    private final static String HEADER_NAME_2 = "X-Header";
    private final static String HEADER_NAME_3 = "x-heaDer";

    private final static String HEADER_VALUE_1 = MediaType.TEXT_HTML_VALUE;
    private final static String HEADER_VALUE_2 = "headerValue2";
    private final static String HEADER_VALUE_3 = "headerValue3";

    private final static String QUERY_PARAM_NAME_1 = "queryParam";
    private final static String QUERY_PARAM_NAME_2 = "qUeryparaM";

    private final static String QUERY_PARAM_VALUE_1 = "queryParamValue1";
    private final static String QUERY_PARAM_VALUE_2 = "queryParamValue2";

    private final static HttpParameterMultimap HEADERS_EVENT_FIELD_VALUE = Stream
        .of(new ImmutablePair<>(HEADER_NAME_1, HEADER_VALUE_1), new ImmutablePair<>(HEADER_NAME_2, HEADER_VALUE_2),
            new ImmutablePair<>(HEADER_NAME_2, HEADER_VALUE_2), new ImmutablePair<>(HEADER_NAME_3, HEADER_VALUE_3))
        .collect(EsacMultimapUtils.toListMultimap(HttpParameterMultimapImpl::new));
    private final static String LOCAL_NAME_EVENT_FIELD_VALUE = "localhost";
    private final static int LOCAL_PORT_EVENT_FIELD_VALUE = 80;
    private final static String METHOD_EVENT_FIELD_VALUE = HttpMethod.GET.toString();
    private final static HttpParameterMultimap QUERY_PARAMS_EVENT_FIELD_VALUE =
        Stream.of(new ImmutablePair<>(QUERY_PARAM_NAME_1, QUERY_PARAM_VALUE_1), new ImmutablePair<>(QUERY_PARAM_NAME_2, QUERY_PARAM_VALUE_2))
            .collect(EsacMultimapUtils.toListMultimap(HttpParameterMultimapImpl::new));
    private final static String REMOTE_HOST_EVENT_FIELD_VALUE = "remotehost";
    private final static int REMOTE_PORT_EVENT_FIELD_VALUE = 0xffff;

    private final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(EventAppenderUnitTests.class));

    private RecordingAppender recordingAppender;

    @Test
    public void testLogEvent() throws Exception {
        HttpRequestLoggingEvent loggingEvent = new HttpRequestLoggingEventImpl();
        loggingEvent.setEndpointType(RestEndpointType.SERVER);
        loggingEvent.setHeaders(HEADERS_EVENT_FIELD_VALUE);
        loggingEvent.setLocalName(LOCAL_NAME_EVENT_FIELD_VALUE);
        loggingEvent.setLocalPort(LOCAL_PORT_EVENT_FIELD_VALUE);
        loggingEvent.setMethod(METHOD_EVENT_FIELD_VALUE);
        loggingEvent.setQueryParameters(QUERY_PARAMS_EVENT_FIELD_VALUE);
        loggingEvent.setRemoteHost(REMOTE_HOST_EVENT_FIELD_VALUE);
        loggingEvent.setRemotePort(REMOTE_PORT_EVENT_FIELD_VALUE);

        LOGGER.info(new MarkerBuilder().setEvent(loggingEvent).build(), null);

        LoggingEvent event = this.recordingAppender.getEvent();
        LoggingMessages eventMsgs = EsacMarkerUtils.getMessages(event);

        Assertions.assertThat(eventMsgs).isNotNull();
    }

    @BeforeClass
    public void initializeLogger() {
        LOGGER.addAppender((this.recordingAppender = new RecordingAppender(LOGGER.getLoggerContext())));
    }
}
