package com.esacinc.commons.crypto.naming;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.utils.EsacStringUtils;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.GeneralName;

public enum GeneralNameType implements NamedBean, TaggedBean {
    OTHER_NAME(GeneralName.otherName, null), RFC822_NAME(GeneralName.rfc822Name, null), DNS_NAME(GeneralName.dNSName, "dNSName"),
    X400_ADDRESS(GeneralName.x400Address, null), DIRECTORY_NAME(GeneralName.directoryName, null), EDI_PARTY_NAME(GeneralName.ediPartyName, null),
    UNIFORM_RESOURCE_IDENTIFIER(GeneralName.uniformResourceIdentifier, null), IP_ADDRESS(GeneralName.iPAddress, "iPAddress"),
    REGISTERED_ID(GeneralName.registeredID, "registeredID");

    private final int tag;
    private final String name;

    private GeneralNameType(int tag, @Nullable String name) {
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
