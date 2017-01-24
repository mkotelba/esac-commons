package com.esacinc.commons.test.beans;

import com.esacinc.commons.beans.LifecycleBean;
import java.net.InetAddress;
import javax.annotation.Nonnegative;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public interface EsacServer extends InitializingBean, LifecycleBean {
    public InetAddress getHostAddress();

    public void setHostAddress(InetAddress hostAddr);

    @Nonnegative
    public int getPort();

    public void setPort(@Nonnegative int port);

    public CustomizableThreadFactory getShutdownThreadFactory();

    public void setShutdownThreadFactory(CustomizableThreadFactory shutdownThreadFactory);
}
