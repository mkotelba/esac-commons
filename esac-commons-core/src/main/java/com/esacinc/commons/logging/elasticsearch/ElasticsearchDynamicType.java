package com.esacinc.commons.logging.elasticsearch;

import com.esacinc.commons.beans.NamedBean;
import javax.annotation.Nullable;

public enum ElasticsearchDynamicType implements NamedBean {
    TRUE(Boolean.TRUE), FALSE(Boolean.FALSE), STRICT(null);

    private Boolean value;
    private String name;

    private ElasticsearchDynamicType(@Nullable Boolean value) {
        this.value = value;
        this.name = this.name().toLowerCase();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public boolean hasValue() {
        return (this.value != null);
    }

    @Nullable
    public Boolean getValue() {
        return this.value;
    }
}
