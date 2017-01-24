package com.esacinc.commons.beans;

import javax.annotation.Nullable;

public interface OptionalIdentifiedBean extends IdentifiedBean {
    public default boolean hasId() {
        return (this.getId() != null);
    }
    
    @Nullable
    @Override
    public String getId();
}
