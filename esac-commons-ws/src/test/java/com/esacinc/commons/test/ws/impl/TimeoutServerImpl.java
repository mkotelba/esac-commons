package com.esacinc.commons.test.ws.impl;

import com.esacinc.commons.test.beans.impl.AbstractEsacHttpServer;
import com.esacinc.commons.test.ws.TimeoutServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeoutServerImpl extends AbstractEsacHttpServer implements TimeoutServer {
    private class TimeoutServerRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private final static long SLEEP_INTERVAL = 1000;

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpRequest reqMsg) throws Exception {
            while (TimeoutServerImpl.this.channel.isOpen()) {
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeoutServerImpl.class);

    private SSLEngine sslEngine;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sslEngine.setUseClientMode(false);

        super.afterPropertiesSet();
    }

    @Override
    protected void stopInternal() {
        super.stopInternal();

        LOGGER.info(String.format("Stopped timeout server (hostAddr={%s}, port=%d).", this.hostAddr, this.port));
    }

    @Override
    protected void startInternal() {
        super.startInternal();

        LOGGER.info(String.format("Started timeout server (hostAddr={%s}, port=%d).", this.hostAddr, this.port));
    }

    @Override
    protected void initializeRequestChannel(NioSocketChannel reqChannel, ChannelPipeline reqChannelPipeline) {
        reqChannelPipeline.addLast(new SslHandler(this.sslEngine));

        super.initializeRequestChannel(reqChannel, reqChannelPipeline);

        reqChannelPipeline.addLast(new TimeoutServerRequestHandler());
    }

    @Override
    public SSLEngine getSslEngine() {
        return this.sslEngine;
    }

    @Override
    public void setSslEngine(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }
}
