package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchMappingService;
import com.esacinc.commons.test.context.impl.EsacCommonsApplication;
import com.esacinc.commons.test.impl.AbstractEsacCommonsUnitTests;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "esac-commons.test.unit.io.utils", "esac-commons.test.unit.config" }, groups = { "esac-commons.test.unit.logging",
    "esac-commons.test.unit.logging.elasticsearch", "esac-commons.test.unit.logging.elasticsearch.mapping.service" })
public class ElasticsearchMappingServiceUnitTests extends AbstractEsacCommonsUnitTests {
    private final static String TEST_INDEX_MAPPING_OUTPUT_FILE_NAME_SUFFIX = "-logging-elasticsearch_index-mapping." + EsacFileNameExtensions.JSON;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private ElasticsearchMappingService mappingService;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private EsacCommonsApplication app;

    @Test
    public void testEncodeIndexMapping() throws Exception {
        String testIndexMappingContent = new String(this.mappingService.encodeIndexMapping(), StandardCharsets.UTF_8);

        Assertions.assertThat(testIndexMappingContent).isNotEmpty();

        FileUtils.writeStringToFile(new File(this.app.getLogDirectory(), (this.app.getGroupName() + TEST_INDEX_MAPPING_OUTPUT_FILE_NAME_SUFFIX)),
            testIndexMappingContent, StandardCharsets.UTF_8);
    }
}
