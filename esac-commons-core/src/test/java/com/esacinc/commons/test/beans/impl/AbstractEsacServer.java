package com.esacinc.commons.test.beans.impl;

import com.esacinc.commons.beans.impl.AbstractLifecycleBean;
import com.esacinc.commons.test.beans.EsacServer;
import java.net.InetAddress;
import javax.annotation.Nonnegative;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public abstract class AbstractEsacServer extends AbstractLifecycleBean implements EsacServer {
    protected InetAddress hostAddr;
    protected int port;
    protected CustomizableThreadFactory shutdownThreadFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        Runtime.getRuntime().addShutdownHook(this.shutdownThreadFactory.newThread(this::stop));

        this.start();
    }

    @Override
    public InetAddress getHostAddress() {
        return this.hostAddr;
    }

    @Override
    public void setHostAddress(InetAddress hostAddr) {
        this.hostAddr = hostAddr;
    }

    @Nonnegative
    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(@Nonnegative int port) {
        this.port = port;
    }

    @Override
    public CustomizableThreadFactory getShutdownThreadFactory() {
        return this.shutdownThreadFactory;
    }

    @Override
    public void setShutdownThreadFactory(CustomizableThreadFactory shutdownThreadFactory) {
        this.shutdownThreadFactory = shutdownThreadFactory;
    }
}
