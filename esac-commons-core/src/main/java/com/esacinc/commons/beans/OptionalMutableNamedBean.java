package com.esacinc.commons.beans;

import javax.annotation.Nullable;

public interface OptionalMutableNamedBean extends MutableNamedBean, OptionalNamedBean {
    @Override
    public void setName(@Nullable String name);
}
