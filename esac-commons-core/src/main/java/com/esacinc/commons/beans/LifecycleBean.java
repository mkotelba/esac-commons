package com.esacinc.commons.beans;

import org.springframework.context.SmartLifecycle;

public interface LifecycleBean extends SmartLifecycle {
    public boolean canStop();

    public boolean canStart();

    public void setAutoStartup(boolean autoStartup);

    public void setPhase(int phase);
}
