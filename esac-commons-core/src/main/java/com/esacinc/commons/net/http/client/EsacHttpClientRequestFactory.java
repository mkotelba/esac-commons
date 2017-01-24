package com.esacinc.commons.net.http.client;

import com.esacinc.commons.concurrent.impl.ThreadPoolTaskExecutorService;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.JdkSslContext;
import java.util.concurrent.ThreadFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;

public class EsacHttpClientRequestFactory extends Netty4ClientHttpRequestFactory {
    private NioEventLoopGroup eventLoopGroup;

    public EsacHttpClientRequestFactory(ThreadPoolTaskExecutorService reqTaskExecService, SSLContext sslContext) {
        this(new NioEventLoopGroup(reqTaskExecService.getMaxPoolSize(), ((ThreadFactory) reqTaskExecService)), sslContext);
    }

    private EsacHttpClientRequestFactory(NioEventLoopGroup eventLoopGroup, SSLContext sslContext) {
        super(eventLoopGroup);

        this.eventLoopGroup = eventLoopGroup;

        SSLParameters sslParams = sslContext.getDefaultSSLParameters();

        this.setSslContext(new JdkSslContext(sslContext, true,
            (sslParams.getNeedClientAuth() ? ClientAuth.REQUIRE : (sslParams.getWantClientAuth() ? ClientAuth.OPTIONAL : ClientAuth.NONE))));
    }

    @Override
    public void destroy() throws InterruptedException {
        this.eventLoopGroup.shutdownGracefully();

        super.destroy();
    }
}
