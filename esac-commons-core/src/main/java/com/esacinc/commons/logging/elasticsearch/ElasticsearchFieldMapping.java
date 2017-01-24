package com.esacinc.commons.logging.elasticsearch;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.beans.TypedBean;
import javax.annotation.Nullable;

public interface ElasticsearchFieldMapping extends NamedBean, TypedBean {
    public ElasticsearchDatatype getDatatype();

    public boolean hasFormat();

    @Nullable
    public String getFormat();

    public boolean isIndexed();

    public boolean hasType();

    @Nullable
    @Override
    public Class<?> getType();
}
