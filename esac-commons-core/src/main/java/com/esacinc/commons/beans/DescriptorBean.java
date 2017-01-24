package com.esacinc.commons.beans;

import javax.annotation.Nullable;

public interface DescriptorBean extends IdentifiedBean, NamedBean {
    public void initialize() throws Exception;

    public void reset();

    public boolean hasId();

    @Nullable
    @Override
    public String getId();

    public void setId(@Nullable String id);

    public boolean hasName();

    @Nullable
    @Override
    public String getName();

    public void setName(@Nullable String name);
}
