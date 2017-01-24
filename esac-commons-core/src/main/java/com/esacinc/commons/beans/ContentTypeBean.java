package com.esacinc.commons.beans;

import java.util.Set;
import org.springframework.http.MediaType;

public interface ContentTypeBean {
    public Set<MediaType> getCompatibleMediaTypes();

    public MediaType getMediaType();
}
