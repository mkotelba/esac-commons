package com.esacinc.commons.xml.saxon.impl;

import net.sf.saxon.om.DocumentURI;
import org.apache.commons.lang3.StringUtils;

public class EsacDocumentUri extends DocumentURI {
    private String uri;

    public EsacDocumentUri(String uri) {
        super(StringUtils.EMPTY);

        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof EsacDocumentUri) && this.uri.equals(((EsacDocumentUri) obj).getUri()));
    }

    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
