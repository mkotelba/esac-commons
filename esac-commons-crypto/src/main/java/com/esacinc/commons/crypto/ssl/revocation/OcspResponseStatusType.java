package com.esacinc.commons.crypto.ssl.revocation;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.utils.EsacStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.springframework.core.PriorityOrdered;

public enum OcspResponseStatusType implements NamedBean, PriorityOrdered, TaggedBean {
    SUCCESSFUL(OCSPResp.SUCCESSFUL), MALFORMED_REQUEST(OCSPResp.MALFORMED_REQUEST), INTERNAL_ERROR(OCSPResp.INTERNAL_ERROR), TRY_LATER(OCSPResp.TRY_LATER),
    SIG_REQUIRED(OCSPResp.SIG_REQUIRED), UNAUTHORIZED(OCSPResp.UNAUTHORIZED);

    private final int tag;
    private final String name;

    private OcspResponseStatusType(int tag) {
        this.tag = tag;
        this.name = EsacStringUtils.joinCamelCase(StringUtils.split(this.name(), EsacStringUtils.UNDERSCORE));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getOrder() {
        return this.tag;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
