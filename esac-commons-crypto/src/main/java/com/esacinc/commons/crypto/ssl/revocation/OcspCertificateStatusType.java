package com.esacinc.commons.crypto.ssl.revocation;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.beans.TypedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.UnknownStatus;
import org.springframework.core.PriorityOrdered;

public enum OcspCertificateStatusType implements NamedBean, PriorityOrdered, TaggedBean, TypedBean {
    GOOD(CertificateStatus.class), REVOKED(RevokedStatus.class), UNKNOWN(UnknownStatus.class);

    private final int tag;
    private final Class<?> type;
    private final String name;
    private final int order;

    private OcspCertificateStatusType(Class<?> type) {
        this.tag = this.ordinal();
        this.type = type;
        this.name = this.name().toLowerCase();
        this.order = (this.tag * -1);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
