package com.esacinc.commons.crypto;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.utils.EsacStringUtils;
import org.apache.commons.lang3.StringUtils;

public enum PemType implements NamedBean {
    PRIVATE_KEY, CERTIFICATE;

    private final String name;

    private PemType() {
        this.name = StringUtils.replaceChars(this.name(), EsacStringUtils.UNDERSCORE_CHAR, EsacStringUtils.SPACE_CHAR);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
