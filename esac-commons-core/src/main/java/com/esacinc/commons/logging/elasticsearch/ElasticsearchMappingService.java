package com.esacinc.commons.logging.elasticsearch;

import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;

public interface ElasticsearchMappingService extends InitializingBean {
    public byte[] encodeIndexMapping() throws IOException;

    public ElasticsearchIndexMapping getIndexMapping();

    public void setIndexMapping(ElasticsearchIndexMapping indexMapping);
}
