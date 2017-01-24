package com.esacinc.commons.web.tomcat.logging.impl;

import com.esacinc.commons.context.EsacPropertyNames;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.net.http.logging.HttpRequestLoggingEvent;
import com.esacinc.commons.net.http.logging.HttpResponseLoggingEvent;
import com.esacinc.commons.net.http.logging.impl.HttpRequestLoggingEventImpl;
import com.esacinc.commons.net.http.logging.impl.HttpResponseLoggingEventImpl;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.web.servlet.utils.EsacServletUtils;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingValve extends ValveBase {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingValve.class);

    @Override
    public void invoke(Request req, Response resp) throws IOException, ServletException {
        String txId = ((String) req.getAttribute(EsacPropertyNames.HTTP_SERVER_TX_ID));

        HttpRequestLoggingEvent reqEvent = new HttpRequestLoggingEventImpl();
        reqEvent.setAuthType(req.getAuthType());
        reqEvent.setCharacterEncoding(req.getCharacterEncoding());
        reqEvent.setContentLength(req.getContentLengthLong());
        reqEvent.setContentType(req.getContentType());
        reqEvent.setContextPath(req.getContextPath());
        reqEvent.setEndpointType(RestEndpointType.SERVER);
        reqEvent.setHeaders(EsacServletUtils.mapHeaders(req));
        reqEvent.setLocale(req.getLocale());
        reqEvent.setLocalName(req.getLocalName());
        reqEvent.setLocalPort(req.getLocalPort());
        reqEvent.setMethod(req.getMethod());
        reqEvent.setPathInfo(req.getPathInfo());
        reqEvent.setProtocol(req.getProtocol());
        reqEvent.setQueryParameters(EsacServletUtils.mapQueryParameters(req));
        reqEvent.setQueryString(req.getQueryString());
        reqEvent.setRemoteAddr(req.getRemoteAddr());
        reqEvent.setRemoteHost(req.getRemoteHost());
        reqEvent.setRemotePort(req.getRemotePort());
        reqEvent.setScheme(req.getScheme());
        reqEvent.setSecure(req.isSecure());
        reqEvent.setServerName(req.getServerName());
        reqEvent.setServerPort(req.getServerPort());
        reqEvent.setServletPath(req.getServletPath());
        reqEvent.setTimestamp(req.getCoyoteRequest().getStartTime());
        reqEvent.setTxId(txId);
        reqEvent.setUri(req.getRequestURI());
        reqEvent.setUrl(req.getRequestURL().toString());
        reqEvent.setUserPrincipal(Objects.toString(req.getUserPrincipal(), null));

        LOGGER.info(new MarkerBuilder().setEvent(reqEvent).build(), null);

        try {
            this.getNext().invoke(req, resp);
        } finally {
            org.apache.coyote.Response coyoteResp = resp.getCoyoteResponse();

            HttpResponseLoggingEvent respEvent = new HttpResponseLoggingEventImpl();
            respEvent.setCharacterEncoding(resp.getCharacterEncoding());
            respEvent.setContentLength(coyoteResp.getContentLengthLong());
            respEvent.setContentType(resp.getContentType());
            respEvent.setEndpointType(RestEndpointType.SERVER);
            respEvent.setHeaders(EsacServletUtils.mapHeaders(resp));
            respEvent.setLocale(resp.getLocale());
            respEvent.setStatusCode(resp.getStatus());
            respEvent.setStatusMessage(resp.getMessage());
            respEvent.setTimestamp(coyoteResp.getCommitTime());
            respEvent.setTxId(txId);

            LOGGER.info(new MarkerBuilder().setEvent(respEvent).build(), null);
        }
    }
}
