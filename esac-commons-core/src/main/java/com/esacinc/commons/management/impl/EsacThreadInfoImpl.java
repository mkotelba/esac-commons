package com.esacinc.commons.management.impl;

import com.esacinc.commons.management.EsacThreadInfo;
import java.lang.Thread.State;

public class EsacThreadInfoImpl implements EsacThreadInfo {
    private long id;
    private String name;
    private boolean _native;
    private boolean monitorDeadlocked;
    private boolean synchronizerDeadlocked;
    private State state;

    public EsacThreadInfoImpl(long id, String name, boolean _native, boolean monitorDeadlocked, boolean synchronizerDeadlocked, State state) {
        this.id = id;
        this.name = name;
        this._native = _native;
        this.monitorDeadlocked = monitorDeadlocked;
        this.synchronizerDeadlocked = synchronizerDeadlocked;
        this.state = state;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isMonitorDeadlocked() {
        return this.monitorDeadlocked;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isNative() {
        return this._native;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public boolean isSynchronizerDeadlocked() {
        return this.synchronizerDeadlocked;
    }
}
