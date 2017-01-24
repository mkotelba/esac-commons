package com.esacinc.commons.management;

import com.esacinc.commons.beans.NamedBean;
import java.lang.Thread.State;

public interface EsacThreadInfo extends NamedBean {
    public long getId();

    public boolean isMonitorDeadlocked();

    public boolean isNative();

    public State getState();

    public boolean isSynchronizerDeadlocked();
}
