package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.net.http.logging.HttpRequestLoggingEvent;
import com.esacinc.commons.net.logging.RestEventType;
import javax.annotation.Nullable;

public class HttpRequestLoggingEventImpl extends AbstractHttpLoggingEvent implements HttpRequestLoggingEvent {
    private String authType;
    private String contextPath;
    private String localName;
    private Integer localPort;
    private String method;
    private String pathInfo;
    private String protocol;
    private HttpParameterMultimap queryParams;
    private String queryString;
    private String remoteAddr;
    private String remoteHost;
    private Integer remotePort;
    private String scheme;
    private boolean secure;
    private String serverName;
    private Integer serverPort;
    private String servletPath;
    private String uri;
    private String url;
    private String userPrincipal;

    public HttpRequestLoggingEventImpl() {
        super(RestEventType.REQUEST);
    }

    @Override
    public boolean hasAuthType() {
        return (this.authType != null);
    }

    @Nullable
    @Override
    public String getAuthType() {
        return this.authType;
    }

    @Override
    public void setAuthType(@Nullable String authType) {
        this.authType = authType;
    }

    @Override
    public boolean hasContextPath() {
        return (this.contextPath != null);
    }

    @Nullable
    @Override
    public String getContextPath() {
        return this.contextPath;
    }

    @Override
    public void setContextPath(@Nullable String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public boolean hasLocalName() {
        return (this.localName != null);
    }

    @Nullable
    @Override
    public String getLocalName() {
        return this.localName;
    }

    @Override
    public void setLocalName(@Nullable String localName) {
        this.localName = localName;
    }

    @Override
    public boolean hasLocalPort() {
        return (this.localPort != null);
    }

    @Nullable
    @Override
    public Integer getLocalPort() {
        return this.localPort;
    }

    @Override
    public void setLocalPort(@Nullable Integer localPort) {
        this.localPort = localPort;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getPathInfo() {
        return this.pathInfo;
    }

    @Override
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public HttpParameterMultimap getQueryParameters() {
        return this.queryParams;
    }

    @Override
    public void setQueryParameters(HttpParameterMultimap queryParams) {
        this.queryParams = queryParams;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public boolean hasRemoteAddr() {
        return (this.remoteAddr != null);
    }

    @Nullable
    @Override
    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    @Override
    public void setRemoteAddr(@Nullable String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public String getRemoteHost() {
        return this.remoteHost;
    }

    @Override
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Override
    public Integer getRemotePort() {
        return this.remotePort;
    }

    @Override
    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean hasServerName() {
        return (this.serverName != null);
    }

    @Nullable
    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    @Override
    public boolean hasServerPort() {
        return (this.serverPort != null);
    }

    @Nullable
    @Override
    public Integer getServerPort() {
        return this.serverPort;
    }

    @Override
    public void setServerPort(@Nullable Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public boolean hasServletPath() {
        return (this.servletPath != null);
    }

    @Nullable
    @Override
    public String getServletPath() {
        return this.servletPath;
    }

    @Override
    public void setServletPath(@Nullable String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean hasUserPrincipal() {
        return (this.userPrincipal != null);
    }

    @Nullable
    @Override
    public String getUserPrincipal() {
        return this.userPrincipal;
    }

    @Override
    public void setUserPrincipal(@Nullable String userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
