package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.utils.EsacStringUtils;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.KeyUsage;

public enum KeyUsageType implements NamedBean, TaggedBean {
    DIGITAL_SIGNATURE(KeyUsage.digitalSignature, null), NON_REPUDIATION(KeyUsage.nonRepudiation, null), KEY_ENCIPHERMENT(KeyUsage.keyEncipherment, null),
    DATA_ENCIPHERMENT(KeyUsage.dataEncipherment, null), KEY_AGREEMENT(KeyUsage.keyAgreement, null), KEY_CERT_SIGN(KeyUsage.keyCertSign, null),
    CRL_SIGN(KeyUsage.cRLSign, "cRLSign"), ENCIPHER_ONLY(KeyUsage.encipherOnly, null), DECIPHER_ONLY(KeyUsage.decipherOnly, null);

    private final int tag;
    private final String name;

    private KeyUsageType(int tag, @Nullable String name) {
        this.tag = tag;
        this.name = ((name != null) ? name : EsacStringUtils.joinCamelCase(EsacStringUtils.splitCamelCase(this.name(), EsacStringUtils.UNDERSCORE)));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
