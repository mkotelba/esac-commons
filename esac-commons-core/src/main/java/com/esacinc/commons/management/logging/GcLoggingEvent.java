package com.esacinc.commons.management.logging;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnegative;

public interface GcLoggingEvent extends EsacLoggingEvent, NamedBean {
    @JsonProperty
    public String getAction();

    @JsonProperty
    public String getCause();

    @JsonProperty
    @Nonnegative
    public long getDuration();

    @ElasticsearchField(datatype = ElasticsearchDatatype.DATE)
    @JsonProperty
    @Nonnegative
    public long getEndTimestamp();

    @ElasticsearchField(datatype = ElasticsearchDatatype.DATE)
    @JsonProperty
    @Nonnegative
    public long getStartTimestamp();
}
