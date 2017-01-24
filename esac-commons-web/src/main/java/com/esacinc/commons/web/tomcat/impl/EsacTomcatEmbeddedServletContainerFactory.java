package com.esacinc.commons.web.tomcat.impl;

import com.esacinc.commons.net.EsacSchemes;
import com.esacinc.commons.tx.impl.TxIdGenerator;
import com.esacinc.commons.web.tomcat.crypto.impl.EsacJsseImplementation;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.servlet.SessionTrackingMode;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.net.NioChannel;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class EsacTomcatEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {
    public static class EsacHttp11NioProtocol extends AbstractHttp11JsseProtocol<NioChannel> {
        private final static String NAME_PREFIX = (EsacSchemes.HTTPS + "-nio");

        private final static Log LOG = LogFactory.getLog(EsacHttp11NioProtocol.class);

        public EsacHttp11NioProtocol() {
            super(new EsacNioEndpoint());
        }

        @Override
        public EsacNioEndpoint getEndpoint() {
            return ((EsacNioEndpoint) super.getEndpoint());
        }

        @Override
        protected Log getLog() {
            return LOG;
        }

        @Override
        protected String getNamePrefix() {
            return NAME_PREFIX;
        }
    }

    public static class EsacConnector extends Connector {
        public EsacConnector() {
            super(EsacHttp11NioProtocol.class.getName());

            this.protocolHandlerClassName = Http11NioProtocol.class.getName();
        }
    }

    private File baseDir;
    private String connEndpointName;
    private int connKeepAliveTimeout;
    private int connTimeout;
    private LoginConfig loginConfig;
    private int maxConns;
    private int maxConnThreads;
    private int minConnThreads;
    private EsacSessionConfig sessionConfig;
    private EsacJsseImplementation sslImpl;
    private TxIdGenerator txIdGen;
    private File workDir;
    private Tomcat tomcat;
    private EsacConnector conn;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer ... servletContextInits) {
        (this.tomcat = new Tomcat()).setBaseDir(this.baseDir.getAbsolutePath());

        Service service = this.tomcat.getService();
        service.addConnector((this.conn = new EsacConnector()));

        this.customizeConnector(this.conn);
        this.tomcat.setConnector(this.conn);

        Host host = this.tomcat.getHost();
        host.setAutoDeploy(false);

        this.tomcat.getEngine().setBackgroundProcessorDelay(-1);

        this.prepareContext(host, servletContextInits);

        return this.getTomcatEmbeddedServletContainer(this.tomcat);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void customizeConnector(Connector conn) {
        super.customizeConnector(conn);

        EsacHttp11NioProtocol connProtocol = ((EsacHttp11NioProtocol) conn.getProtocolHandler());
        connProtocol.setConnectionTimeout(this.connTimeout);
        connProtocol.setKeepAliveTimeout(this.connKeepAliveTimeout);
        connProtocol.setMaxConnections(this.maxConns);
        connProtocol.setMaxThreads(this.maxConnThreads);
        connProtocol.setMinSpareThreads(this.minConnThreads);
        connProtocol.setSSLEnabled(true);

        EsacNioEndpoint connEndpoint = connProtocol.getEndpoint();
        connEndpoint.setName(this.connEndpointName);
        connEndpoint.setSslImplementation(this.sslImpl);
        connEndpoint.setTxIdGenerator(this.txIdGen);

        conn.setSecure(true);
        conn.setScheme(EsacSchemes.HTTPS);
    }

    @Override
    protected void configureContext(Context context, ServletContextInitializer[] servletContextInits) {
        context.setDistributable(true);
        context.setIgnoreAnnotations(true);
        context.setTldValidation(true);
        context.setXmlNamespaceAware(true);
        context.setXmlValidation(true);

        context.setLoginConfig(this.loginConfig);

        ((StandardContext) context).setWorkDir(this.workDir.getAbsolutePath());

        super.configureContext(context, ArrayUtils.add(servletContextInits, 0, servletContext -> {
            BeanUtils.copyProperties(this.sessionConfig, servletContext.getSessionCookieConfig());

            Set<SessionTrackingMode> effectiveSessionTrackingModes = servletContext.getEffectiveSessionTrackingModes();
            effectiveSessionTrackingModes.clear();
            effectiveSessionTrackingModes.addAll(this.sessionConfig.getTrackingModes());
        }));
    }

    public File getBaseDirectory() {
        return this.baseDir;
    }

    @Override
    public void setBaseDirectory(File baseDir) {
        super.setBaseDirectory((this.baseDir = baseDir));
    }

    public String getConnectionEndpointName() {
        return this.connEndpointName;
    }

    public void setConnectionEndpointName(String connEndpointName) {
        this.connEndpointName = connEndpointName;
    }

    @Nonnegative
    public int getConnectionKeepAliveTimeout() {
        return this.connKeepAliveTimeout;
    }

    public void setConnectionKeepAliveTimeout(@Nonnegative int connKeepAliveTimeout) {
        this.connKeepAliveTimeout = connKeepAliveTimeout;
    }

    public int getConnectionTimeout() {
        return this.connTimeout;
    }

    public void setConnectionTimeout(@Nonnegative int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public EsacConnector getConnector() {
        return this.conn;
    }

    public LoginConfig getLoginConfig() {
        return this.loginConfig;
    }

    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public int getMaxConnections() {
        return this.maxConns;
    }

    public void setMaxConnections(@Nonnegative int maxConns) {
        this.maxConns = maxConns;
    }

    public int getMaxConnectionThreads() {
        return this.maxConnThreads;
    }

    public void setMaxConnectionThreads(@Nonnegative int maxConnThreads) {
        this.maxConnThreads = maxConnThreads;
    }

    @Nonnegative
    public int getMinConnectionThreads() {
        return this.minConnThreads;
    }

    public void setMinConnectionThreads(@Nonnegative int minConnThreads) {
        this.minConnThreads = minConnThreads;
    }

    public EsacSessionConfig getSessionConfig() {
        return this.sessionConfig;
    }

    public void setSessionConfig(EsacSessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
    }

    public EsacJsseImplementation getSslImplementation() {
        return this.sslImpl;
    }

    public void setSslImplementation(EsacJsseImplementation sslImpl) {
        this.sslImpl = sslImpl;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }

    public TxIdGenerator getTxIdGenerator() {
        return this.txIdGen;
    }

    public void setTxIdGenerator(TxIdGenerator txIdGen) {
        this.txIdGen = txIdGen;
    }

    public void setValves(Valve ... valves) {
        this.setContextValves(Stream.of(valves).sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList()));
    }

    public File getWorkDirectory() {
        return this.workDir;
    }

    public void setWorkDirectory(File workDir) {
        this.workDir = workDir;
    }
}
