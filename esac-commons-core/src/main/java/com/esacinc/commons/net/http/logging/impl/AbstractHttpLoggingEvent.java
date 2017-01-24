package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.net.http.HttpParameterMultimap;
import com.esacinc.commons.net.http.logging.HttpLoggingEvent;
import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.net.logging.impl.AbstractRestLoggingEvent;
import java.util.Locale;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class AbstractHttpLoggingEvent extends AbstractRestLoggingEvent implements HttpLoggingEvent {
    protected String charEnc;
    protected Long contentLen;
    protected String contentType;
    protected HttpParameterMultimap headers;
    protected Locale locale;
    protected long timestamp;

    protected AbstractHttpLoggingEvent(RestEventType eventType) {
        super(eventType);
    }

    @Override
    public String getCharacterEncoding() {
        return this.charEnc;
    }

    @Override
    public void setCharacterEncoding(String charEnc) {
        this.charEnc = charEnc;
    }

    @Override
    public boolean hasContentLength() {
        return (this.contentLen != null);
    }

    @Nullable
    public Long getContentLength() {
        return this.contentLen;
    }

    public void setContentLength(@Nullable Long contentLen) {
        this.contentLen = contentLen;
    }

    @Override
    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @Nullable
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }

    @Override
    public HttpParameterMultimap getHeaders() {
        return this.headers;
    }

    @Override
    public void setHeaders(HttpParameterMultimap headers) {
        this.headers = headers;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Nonnegative
    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(@Nonnegative long timestamp) {
        this.timestamp = timestamp;
    }
}
