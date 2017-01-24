package com.esacinc.commons.crypto.ssl.revocation;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.utils.EsacStringUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.CRLReason;
import org.springframework.core.PriorityOrdered;

public enum OcspRevokeReasonType implements NamedBean, PriorityOrdered, TaggedBean {
    UNSPECIFIED(CRLReason.unspecified, null), KEY_COMPROMISE(CRLReason.keyCompromise, null), CA_COMPROMISE(CRLReason.cACompromise, "cACompromise"),
    AFFILIATION_CHANGED(CRLReason.affiliationChanged, null), SUPERSEDED(CRLReason.superseded, null),
    CESSATION_OF_OPERATION(CRLReason.cessationOfOperation, null), CERTIFICATE_HOLD(CRLReason.certificateHold, null),
    REMOVE_FROM_CRL(CRLReason.removeFromCRL, "removeFromCRL"), PRIVILEGE_WITHDRAWN(CRLReason.privilegeWithdrawn, null),
    AA_COMPROMISE(CRLReason.aACompromise, "aACompromise");

    private final int tag;
    private final String name;
    private final java.security.cert.CRLReason reason;

    private OcspRevokeReasonType(int tag, @Nullable String name) {
        this.tag = tag;
        this.name = ((name != null) ? name : EsacStringUtils.joinCamelCase(StringUtils.split(this.name(), EsacStringUtils.UNDERSCORE)));
        this.reason = java.security.cert.CRLReason.valueOf(this.name());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getOrder() {
        return this.tag;
    }

    public java.security.cert.CRLReason getReason() {
        return this.reason;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
