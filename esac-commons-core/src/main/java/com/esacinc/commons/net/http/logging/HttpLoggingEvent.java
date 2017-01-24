package com.esacinc.commons.net.http.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.net.logging.RestLoggingEvent;
import java.util.Locale;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface HttpLoggingEvent extends RestLoggingEvent {
    @JsonProperty
    public String getCharacterEncoding();

    public void setCharacterEncoding(String charEnc);

    public boolean hasContentLength();

    @JsonProperty
    @Nullable
    public Long getContentLength();

    public void setContentLength(@Nullable Long contentLen);

    public boolean hasContentType();

    @JsonProperty
    @Nullable
    public String getContentType();

    public void setContentType(@Nullable String contentType);

    @ElasticsearchField(datatype = ElasticsearchDatatype.NESTED)
    @JsonProperty("header")
    public HttpParameterMultimap getHeaders();

    public void setHeaders(HttpParameterMultimap headers);

    @ElasticsearchField(datatype = ElasticsearchDatatype.TEXT)
    @JsonProperty
    public Locale getLocale();

    public void setLocale(Locale locale);

    @ElasticsearchField(datatype = ElasticsearchDatatype.DATE)
    @JsonProperty
    @Nonnegative
    public long getTimestamp();

    public void setTimestamp(@Nonnegative long timestamp);
}
