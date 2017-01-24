package com.esacinc.commons.crypto.credential;

import com.esacinc.commons.beans.DescriptorBean;
import javax.annotation.Nullable;

public interface Credential extends DescriptorBean {
    public CredentialConfig getConfig();

    public boolean hasInfo();

    @Nullable
    public CredentialInfo getInfo();

    public void setInfo(@Nullable CredentialInfo info);

    public boolean hasIssuerCredential();

    @Nullable
    public Credential getIssuerCredential();

    public void setIssuerCredential(@Nullable Credential issuerCred);
}
