package com.esacinc.commons.io;

import com.esacinc.commons.utils.EsacStringUtils;

public final class EsacResourceLocations {
    public final static String HIBERNATE_VALIDATOR_MSGS_PROP_FILE = "/org/hibernate/validator/ValidationMessages." + EsacFileNameExtensions.PROPERTIES;
    public final static String VALIDATION_MSGS_PROP_FILE =
        EsacStringUtils.SLASH + EsacDirectories.META_INF_NAME + "/esac-commons/esac-commons-messages-validation." + EsacFileNameExtensions.PROPERTIES;

    private EsacResourceLocations() {
    }
}
