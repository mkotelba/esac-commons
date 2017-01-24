package com.esacinc.commons.web.servlet.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

public class UriPatternMappingResourceTransformer implements ResourceTransformer {
    private Map<Pattern, Resource> mappedUriPatternResources;
    private Map<Pattern, byte[]> mappedUriPatternResourceContents;

    public UriPatternMappingResourceTransformer(Map<Pattern, Resource> mappedUriPatternResources) throws IOException {
        this.mappedUriPatternResourceContents = new HashMap<>((this.mappedUriPatternResources = mappedUriPatternResources).size());

        for (Pattern mappedUriPattern : this.mappedUriPatternResources.keySet()) {
            try (InputStream mappedUriPatternResourceInStream = this.mappedUriPatternResources.get(mappedUriPattern).getInputStream()) {
                this.mappedUriPatternResourceContents.put(mappedUriPattern, IOUtils.toByteArray(mappedUriPatternResourceInStream));
            }
        }
    }

    @Override
    public Resource transform(HttpServletRequest servletReq, Resource resource, ResourceTransformerChain resourceTransformerChain) throws IOException {
        String reqUri = servletReq.getRequestURI();

        for (Pattern mappedUriPattern : this.mappedUriPatternResources.keySet()) {
            if (mappedUriPattern.matcher(reqUri).matches()) {
                return new TransformedResource(this.mappedUriPatternResources.get(mappedUriPattern),
                    this.mappedUriPatternResourceContents.get(mappedUriPattern));
            }
        }

        return resource;
    }
}
