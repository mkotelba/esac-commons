package com.esacinc.commons.crypto.ssl;

import com.esacinc.commons.crypto.beans.AlgorithmNamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.crypto.ssl.utils.EsacSslVersionUtils;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public enum SslVersion implements AlgorithmNamedBean, TaggedBean {
    SSL_20(0x02, 0, 2, 0, "SSLv2", "SSLv2Hello"), SSL_30(0x03, 0, 3, 0, "SSLv3", null), TLS_10(0x03, 0x01, 1, 0, "TLSv1", null),
    TLS_11(0x03, 0x02, 1, 1, "TLSv1.1", null), TLS_12(0x03, 0x03, 1, 2, "TLSv1.2", null);

    private final int minor;
    private final int major;
    private final int tag;
    private final int minorDisplay;
    private final int majorDisplay;
    private final String algName;
    private final String helloAlgName;

    private SslVersion(@Nonnegative int minor, @Nonnegative int major, @Nonnegative int minorDisplay, @Nonnegative int majorDisplay, String algName,
        @Nullable String helloAlgName) {
        this.tag = EsacSslVersionUtils.buildVersion((this.major = major), (this.minor = minor));
        this.minorDisplay = minorDisplay;
        this.majorDisplay = majorDisplay;
        this.algName = algName;
        this.helloAlgName = helloAlgName;
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }

    public boolean hasHelloAlgorithmName() {
        return (this.helloAlgName != null);
    }

    @Nullable
    public String getHelloAlgorithmName() {
        return this.helloAlgName;
    }

    @Nonnegative
    public int getMajor() {
        return this.major;
    }

    @Nonnegative
    public int getMajorDisplay() {
        return this.majorDisplay;
    }

    @Nonnegative
    public int getMinor() {
        return this.minor;
    }

    @Nonnegative
    public int getMinorDisplay() {
        return this.minorDisplay;
    }

    @Nonnegative
    @Override
    public int getTag() {
        return this.tag;
    }
}
