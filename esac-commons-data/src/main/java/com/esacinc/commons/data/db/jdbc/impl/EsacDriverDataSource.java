package com.esacinc.commons.data.db.jdbc.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nonnegative;
import javax.sql.DataSource;

public class EsacDriverDataSource implements DataSource {
    private Driver driver;
    private String url;
    private Properties props;
    private boolean singleConn;
    private final ReentrantLock connLock;
    private Connection conn;

    public EsacDriverDataSource(Driver driver, String url, Properties props, boolean singleConn) {
        this.driver = driver;
        this.url = url;
        this.props = props;
        this.connLock = ((this.singleConn = singleConn) ? new ReentrantLock() : null);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.singleConn) {
            this.connLock.lock();

            try {
                if (this.conn == null) {
                    this.conn = this.driver.connect(this.url, this.props);
                }

                return this.conn;
            } finally {
                this.connLock.unlock();
            }
        } else {
            return this.driver.connect(this.url, this.props);
        }
    }

    @Override
    public Connection getConnection(String user, String pass) throws SQLException {
        return this.getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return false;
    }

    @Nonnegative
    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLoginTimeout(@Nonnegative int loginTimeout) throws SQLException {
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
}
