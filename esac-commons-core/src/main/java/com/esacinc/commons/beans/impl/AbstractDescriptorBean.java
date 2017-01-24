package com.esacinc.commons.beans.impl;

import com.esacinc.commons.beans.DescriptorBean;
import javax.annotation.Nullable;

public abstract class AbstractDescriptorBean implements DescriptorBean {
    protected String id;
    protected String name;

    protected AbstractDescriptorBean(@Nullable String id, @Nullable String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void initialize() throws Exception {
        this.reset();

        try {
            this.initializeInternal();
        } catch (Exception e) {
            this.reset();

            throw e;
        }
    }

    @Override
    public void reset() {
    }

    protected void initializeInternal() throws Exception {
    }

    @Override
    public boolean hasId() {
        return (this.id != null);
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Override
    public boolean hasName() {
        return (this.name != null);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }
}
