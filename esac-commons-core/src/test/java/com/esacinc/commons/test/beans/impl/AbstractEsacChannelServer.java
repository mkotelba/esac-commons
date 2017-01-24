package com.esacinc.commons.test.beans.impl;

import com.esacinc.commons.EsacException;
import com.esacinc.commons.concurrent.impl.ThreadPoolTaskExecutorService;
import com.esacinc.commons.test.beans.EsacChannelServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ThreadFactory;
import javax.annotation.Nonnegative;

public abstract class AbstractEsacChannelServer extends AbstractEsacServer implements EsacChannelServer {
    protected class ServerRequestChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel reqChannel) throws Exception {
            AbstractEsacChannelServer.this.initializeRequestChannel(reqChannel, reqChannel.pipeline());
        }
    }

    protected int backlog;
    protected ThreadPoolTaskExecutorService reqTaskExecService;
    protected ThreadPoolTaskExecutorService taskExecService;
    protected Channel channel;

    @Override
    public boolean isRunning() {
        return (this.channel != null);
    }

    @Override
    protected void stopInternal() {
        try {
            this.channel.close().sync();
        } catch (Exception e) {
            throw new EsacException(String.format("Unable to stop channel server (hostAddr={%s}, port=%d).", this.hostAddr, this.port), e);
        }
    }

    @Override
    protected void startInternal() {
        try {
            this.channel = new ServerBootstrap()
                .group(new NioEventLoopGroup(1, ((ThreadFactory) this.taskExecService)),
                    new NioEventLoopGroup(this.reqTaskExecService.getMaxPoolSize(), ((ThreadFactory) this.reqTaskExecService)))
                .channel(NioServerSocketChannel.class).option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.SO_BACKLOG, this.backlog)
                .childHandler(new ServerRequestChannelInitializer()).bind(this.hostAddr, this.port).sync().channel();
        } catch (Exception e) {
            throw new EsacException(String.format("Unable to start channel server (hostAddr={%s}, port=%d).", this.hostAddr, this.port), e);
        }
    }

    protected abstract void initializeRequestChannel(NioSocketChannel reqChannel, ChannelPipeline reqChannelPipeline);

    @Nonnegative
    @Override
    public int getBacklog() {
        return this.backlog;
    }

    @Override
    public void setBacklog(@Nonnegative int backlog) {
        this.backlog = backlog;
    }

    @Override
    public ThreadPoolTaskExecutorService getRequestTaskExecutorService() {
        return this.reqTaskExecService;
    }

    @Override
    public void setRequestTaskExecutorService(ThreadPoolTaskExecutorService reqTaskExecService) {
        this.reqTaskExecService = reqTaskExecService;
    }

    @Override
    public ThreadPoolTaskExecutorService getTaskExecutorService() {
        return this.taskExecService;
    }

    @Override
    public void setTaskExecutorService(ThreadPoolTaskExecutorService taskExecService) {
        this.taskExecService = taskExecService;
    }
}
