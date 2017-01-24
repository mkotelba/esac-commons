package com.esacinc.commons.test.context.impl;

import com.esacinc.commons.context.EsacApplicationConfiguration;
import com.esacinc.commons.io.EsacDirectories;
import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.io.EsacFiles;
import com.esacinc.commons.io.EsacResourceLocations;
import com.esacinc.commons.utils.EsacStringUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

@EsacApplicationConfiguration(applicationClass = EsacCommonsApplication.class, groupName = EsacCommonsApplication.GROUP_NAME,
    propertyNamePrefix = EsacCommonsApplication.PROP_NAME_PREFIX,
    propertySourceLocations = { (ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + EsacResourceLocations.HIBERNATE_VALIDATOR_MSGS_PROP_FILE),
        EsacCommonsApplicationConfiguration.PROP_SRC_LOC },
    sourceLocations = { EsacCommonsApplicationConfiguration.SRC_LOC })
public class EsacCommonsApplicationConfiguration {
    public final static String PROP_SRC_LOC =
        ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + EsacStringUtils.SLASH +
            EsacCommonsApplication.GROUP_NAME + EsacStringUtils.SLASH + EsacCommonsApplication.GROUP_NAME + "*." + EsacFileNameExtensions.PROPERTIES;

    public final static String SRC_LOC = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME +
        EsacStringUtils.SLASH + EsacCommonsApplication.GROUP_NAME + EsacStringUtils.SLASH + EsacDirectories.SPRING_NAME + EsacStringUtils.SLASH +
        EsacFiles.SPRING_NAME_PREFIX + EsacCommonsApplication.GROUP_NAME + "*." + EsacFileNameExtensions.XML;
}
