package com.esacinc.commons.io.utils;

import com.esacinc.commons.io.EsacDirectories;
import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.io.EsacFiles;
import com.esacinc.commons.net.EsacUris;
import com.esacinc.commons.test.context.impl.EsacCommonsApplication;
import com.esacinc.commons.test.impl.AbstractEsacCommonsUnitTests;
import com.esacinc.commons.utils.EsacStringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.Test;

@Test(groups = { "esac-commons.test.unit.io", "esac-commons.test.unit.io.utils", "esac-commons.test.unit.io.utils.resource" })
public class EsacResourceUtilsUnitTests extends AbstractEsacCommonsUnitTests {
    private final static String TEST_FILE_BASE_NAME_PREFIX = EsacFiles.SPRING_NAME_PREFIX + EsacCommonsApplication.GROUP_NAME;

    private final static String TEST_CLASSES_DIR_NAME = "classes";

    private final static String TEST_JAR_FILE_NAME = EsacCommonsApplication.GROUP_NAME + "-core." + EsacFileNameExtensions.JAR;

    private final static String TEST_FILE_PATH_1 =
        EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + EsacStringUtils.SLASH + EsacCommonsApplication.GROUP_NAME + EsacStringUtils.SLASH +
            EsacDirectories.SPRING_NAME + EsacStringUtils.SLASH + TEST_FILE_BASE_NAME_PREFIX + EsacStringUtils.PERIOD + EsacFileNameExtensions.XML;
    private final static String TEST_FILE_RESOURCE_LOC_1 = ResourceUtils.FILE_URL_PREFIX + TEST_CLASSES_DIR_NAME + TEST_FILE_PATH_1;
    private final static String TEST_JAR_RESOURCE_LOC_1 = EsacUris.JAR_FILE_URL_PREFIX + TEST_JAR_FILE_NAME + EsacStringUtils.APOS + TEST_FILE_PATH_1;

    private final static String TEST_FILE_PATH_2 = EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + EsacStringUtils.SLASH +
        EsacCommonsApplication.GROUP_NAME + EsacStringUtils.SLASH + EsacDirectories.SPRING_NAME + EsacStringUtils.SLASH + TEST_FILE_BASE_NAME_PREFIX +
        EsacFiles.TEST_NAME_SUFFIX + EsacStringUtils.PERIOD + EsacFileNameExtensions.XML;
    private final static String TEST_FILE_RESOURCE_LOC_2 = ResourceUtils.FILE_URL_PREFIX + TEST_CLASSES_DIR_NAME + TEST_FILE_PATH_2;
    private final static String TEST_JAR_RESOURCE_LOC_2 = EsacUris.JAR_FILE_URL_PREFIX + TEST_JAR_FILE_NAME + EsacStringUtils.APOS + TEST_FILE_PATH_2;

    private final static String TEST_FILE_PATH_3 =
        EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + EsacStringUtils.SLASH + EsacCommonsApplication.GROUP_NAME + EsacStringUtils.SLASH +
            EsacDirectories.SPRING_NAME + EsacStringUtils.SLASH + TEST_FILE_BASE_NAME_PREFIX + "-data" + EsacStringUtils.PERIOD + EsacFileNameExtensions.XML;
    private final static String TEST_FILE_RESOURCE_LOC_3 = ResourceUtils.FILE_URL_PREFIX + TEST_CLASSES_DIR_NAME + TEST_FILE_PATH_3;
    private final static String TEST_JAR_RESOURCE_LOC_3 = EsacUris.JAR_FILE_URL_PREFIX + TEST_JAR_FILE_NAME + EsacStringUtils.APOS + TEST_FILE_PATH_3;

    @Value(TEST_FILE_RESOURCE_LOC_1)
    private Resource testFileResource1;

    @Value(TEST_JAR_RESOURCE_LOC_1)
    private Resource testJarResource1;

    @Value(TEST_FILE_RESOURCE_LOC_2)
    private Resource testFileResource2;

    @Value(TEST_JAR_RESOURCE_LOC_2)
    private Resource testJarResource2;

    @Value(TEST_FILE_RESOURCE_LOC_3)
    private Resource testFileResource3;

    @Value(TEST_JAR_RESOURCE_LOC_3)
    private Resource testJarResource3;

    @Test(dependsOnMethods = { "testExtractFilePath" })
    public void testSortByLocation() throws Exception {
        Assertions.assertThat(ArrayUtils.toArray(this.testJarResource1, this.testFileResource1, this.testJarResource2, this.testFileResource2,
            this.testJarResource3, this.testFileResource3)).isSortedAccordingTo(EsacResourceUtils.LOC_COMPARATOR);
    }

    @Test(dependsOnMethods = { "testExtractMetaInfPath" })
    public void testExtractFilePath() throws Exception {
        Assertions.assertThat(EsacResourceUtils.extractFilePath(EsacResourceUtils.extractPath(this.testJarResource1))).isEqualTo(TEST_FILE_PATH_1);
    }

    @Test
    public void testExtractMetaInfPath() throws Exception {
        Assertions.assertThat(EsacResourceUtils.extractPath(this.testFileResource1, true))
            .isEqualTo(EsacResourceUtils.extractPath(this.testJarResource1, true));
    }
}
