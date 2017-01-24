package com.esacinc.commons.transform.impl;

import com.esacinc.commons.io.utils.EsacResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

public class ResourceSource extends ByteArraySource {
    private Resource resource;

    public ResourceSource(Resource resource) throws IOException {
        this(resource, null);
    }

    public ResourceSource(Resource resource, @Nullable String sysId) throws IOException {
        this(resource, null, sysId);
    }

    public ResourceSource(Resource resource, @Nullable String publicId, @Nullable String sysId) throws IOException {
        super(publicId, ((sysId != null) ? sysId : Objects.toString(EsacResourceUtils.extractUri(resource), null)));

        try (InputStream resourceInStream = (this.resource = resource).getInputStream()) {
            this.bytes = IOUtils.toByteArray(resourceInStream);
        }
    }

    public Resource getResource() {
        return this.resource;
    }
}
