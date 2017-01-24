package com.esacinc.commons.beans;

import javax.annotation.Nullable;

public interface OptionalMutableIdentifiedBean extends MutableIdentifiedBean, OptionalIdentifiedBean {
    @Override
    public void setId(@Nullable String id);
}
