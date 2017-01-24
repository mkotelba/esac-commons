package com.esacinc.commons.test.data.db.impl;

import com.esacinc.commons.EsacException;
import com.esacinc.commons.data.db.jdbc.impl.EsacDataSource;
import com.esacinc.commons.data.db.jdbc.impl.EsacDataSourceConfig;
import com.esacinc.commons.data.db.jdbc.impl.EsacDriverDataSource;
import com.esacinc.commons.test.beans.impl.AbstractEsacServer;
import com.esacinc.commons.test.data.db.HsqlServer;
import com.esacinc.commons.transform.impl.ResourceSource;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;
import org.hsqldb.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringValueResolver;

public class HsqlServerImpl extends AbstractEsacServer implements HsqlServer {
    private static class HsqlNetworkServer extends Server {
        @Override
        protected boolean allowConnection(Socket socket) {
            return (super.allowConnection(socket) && ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().isLoopbackAddress());
        }

        @Override
        protected void printStackTrace(Throwable throwable) {
        }

        @Override
        protected void printError(String msg) {
            LOGGER.error(msg, this.getServerError());
        }

        @Override
        protected void printWithThread(String msg) {
            this.print(msg);
        }

        @Override
        protected void print(String msg) {
            LOGGER.debug(msg);
        }
    }

    private final static String SERVER_MAX_CONN_PROP_NAME = "server.maxconnections";

    private final static String PATH_FORMAT_STR = ResourceUtils.FILE_URL_PREFIX + "%s/%s;user=%s;password=%s";

    private final static Logger LOGGER = LoggerFactory.getLogger(HsqlServerImpl.class);

    private StringValueResolver embeddedValueResolver;

    private String dbName;
    private EsacDataSourceConfig dataSrcConfig;
    private File dir;
    private ResourceSource[] initScriptSrcs;
    private HsqlNetworkServer netServer = new HsqlNetworkServer();

    @Override
    public boolean isRunning() {
        return (this.netServer.getState() == ServerConstants.SERVER_STATE_ONLINE);
    }

    @Override
    public void initializeDataSource(EsacDataSource dataSrc) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerProperties serverProps = ServerConfiguration.newDefaultProperties(ServerConstants.SC_PROTOCOL_HSQL);
        serverProps.getProperties().clear();
        serverProps.setProperty(SERVER_MAX_CONN_PROP_NAME, this.dataSrcConfig.getMaximumPoolSize());
        this.netServer.setProperties(serverProps);

        this.netServer.setAddress(this.hostAddr.getHostAddress());
        this.netServer.setDatabaseName(0, this.dbName);
        this.netServer.setDatabasePath(0,
            String.format(PATH_FORMAT_STR, this.dir.getPath(), this.dbName, this.dataSrcConfig.getUsername(), this.dataSrcConfig.getPassword()));
        this.netServer.setDaemon(true);
        this.netServer.setPort(this.port);

        super.afterPropertiesSet();
    }

    @Override
    protected void stopInternal() {
        try {
            this.netServer.shutdown();

            LOGGER.info(String.format("Stopped database (name=%s) HyperSQL server (hostAddr={%s}, port=%d).", this.dbName, this.hostAddr, this.port));
        } catch (Exception e) {
            throw new EsacException(
                String.format("Unable to stop database (name=%s) HyperSQL server (hostAddr={%s}, port=%d).", this.dbName, this.hostAddr, this.port), e);
        }
    }

    @Override
    protected void startInternal() {
        // noinspection ConstantConditions
        boolean initDb = (!this.dir.exists() || (this.dir.list().length == 0));

        try {
            this.netServer.start();

            if (initDb) {
                this.executeInitializationScripts();
            }

            LOGGER.info(String.format("Started database (name=%s) HyperSQL server (hostAddr={%s}, port=%d).", this.dbName, this.hostAddr, this.port));
        } catch (Exception e) {
            throw new EsacException(
                String.format("Unable to start database (name=%s) HyperSQL server (hostAddr={%s}, port=%d).", this.dbName, this.hostAddr, this.port), e);
        }
    }

    private void executeInitializationScripts() {
        EsacDriverDataSource adminDataSrc = null;
        Connection adminConn = null;

        try {
            adminConn = DataSourceUtils.getConnection((adminDataSrc = this.dataSrcConfig.buildDataSource(true)));

            for (ResourceSource initScriptSrc : this.initScriptSrcs) {
                ScriptUtils.executeSqlScript(adminConn,
                    new EncodedResource(
                        new ByteArrayResource(this.embeddedValueResolver.resolveStringValue(new String(initScriptSrc.getBytes(), StandardCharsets.UTF_8))
                            .getBytes(StandardCharsets.UTF_8), initScriptSrc.getResource().getDescription()),
                        StandardCharsets.UTF_8));
            }

            adminConn.commit();
        } catch (Exception e) {
            throw new EsacException(String.format("Unable to execute database (name=%s) server (hostAddr={%s}, port=%d) initialization scripts.", this.dbName,
                this.hostAddr, this.netServer.getPort()), e);
        } finally {
            if (adminConn != null) {
                DataSourceUtils.releaseConnection(adminConn, adminDataSrc);
            }
        }
    }

    @Override
    public String getDatabaseName() {
        return this.dbName;
    }

    @Override
    public void setDatabaseName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public EsacDataSourceConfig getDataSourceConfiguration() {
        return this.dataSrcConfig;
    }

    @Override
    public void setDataSourceConfiguration(EsacDataSourceConfig dataSrcConfig) {
        this.dataSrcConfig = dataSrcConfig;
    }

    public File getDirectory() {
        return this.dir;
    }

    public void setDirectory(File dir) {
        this.dir = dir;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver) {
        this.embeddedValueResolver = embeddedValueResolver;
    }

    @Override
    public ResourceSource[] getInitializationScriptSources() {
        return this.initScriptSrcs;
    }

    @Override
    public void setInitializationScriptSources(ResourceSource ... initScriptSrcs) {
        this.initScriptSrcs = initScriptSrcs;
    }
}
