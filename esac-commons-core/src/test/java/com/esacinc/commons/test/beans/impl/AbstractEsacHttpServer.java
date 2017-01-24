package com.esacinc.commons.test.beans.impl;

import com.esacinc.commons.test.beans.EsacHttpServer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import javax.annotation.Nonnegative;

public abstract class AbstractEsacHttpServer extends AbstractEsacChannelServer implements EsacHttpServer {
    protected int maxContentLen;

    @Override
    protected void initializeRequestChannel(NioSocketChannel reqChannel, ChannelPipeline reqChannelPipeline) {
        reqChannelPipeline.addLast(new HttpRequestDecoder());
        reqChannelPipeline.addLast(new HttpResponseEncoder());
        reqChannelPipeline.addLast(new HttpObjectAggregator(this.maxContentLen));
    }

    @Nonnegative
    @Override
    public int getMaxContentLength() {
        return this.maxContentLen;
    }

    @Override
    public void setMaxContentLength(@Nonnegative int maxContentLen) {
        this.maxContentLen = maxContentLen;
    }
}
