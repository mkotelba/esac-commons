package com.esacinc.commons.beans;

import javax.annotation.Nullable;

public interface OptionalNamedBean extends NamedBean {
    public default boolean hasName() {
        return (this.getName() != null);
    }
    
    @Nullable
    @Override
    public String getName();
}
