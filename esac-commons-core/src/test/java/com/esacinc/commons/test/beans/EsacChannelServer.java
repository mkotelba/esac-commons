package com.esacinc.commons.test.beans;

import com.esacinc.commons.concurrent.impl.ThreadPoolTaskExecutorService;
import javax.annotation.Nonnegative;
import org.springframework.beans.factory.InitializingBean;

public interface EsacChannelServer extends InitializingBean, EsacServer {
    @Nonnegative
    public int getBacklog();

    public void setBacklog(@Nonnegative int backlog);

    public ThreadPoolTaskExecutorService getRequestTaskExecutorService();

    public void setRequestTaskExecutorService(ThreadPoolTaskExecutorService reqTaskExecService);

    public ThreadPoolTaskExecutorService getTaskExecutorService();

    public void setTaskExecutorService(ThreadPoolTaskExecutorService taskExecService);
}
