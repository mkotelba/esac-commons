package com.esacinc.commons.transform;

import com.esacinc.commons.transform.impl.ResourceSource;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.context.ResourceLoaderAware;

public interface ResourceSourceResolver extends ResourceLoaderAware {
    public ResourceSource[] resolveAll(String ... locs) throws IOException;

    @Nullable
    public ResourceSource resolve(String loc) throws IOException;
}
