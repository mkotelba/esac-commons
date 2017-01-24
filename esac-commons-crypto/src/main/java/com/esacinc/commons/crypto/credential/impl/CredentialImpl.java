package com.esacinc.commons.crypto.credential.impl;

import com.esacinc.commons.beans.impl.AbstractDescriptorBean;
import com.esacinc.commons.crypto.credential.Credential;
import com.esacinc.commons.crypto.credential.CredentialConfig;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import javax.annotation.Nullable;

public class CredentialImpl extends AbstractDescriptorBean implements Credential {
    private CredentialConfig config;
    private CredentialInfo info;
    private Credential issuerCred;

    public CredentialImpl(@Nullable String id, @Nullable String name, CredentialConfig config) {
        super(id, name);

        this.config = config;
    }

    @Override
    public CredentialConfig getConfig() {
        return this.config;
    }

    @Override
    public boolean hasInfo() {
        return (this.info != null);
    }

    @Nullable
    @Override
    public CredentialInfo getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(@Nullable CredentialInfo info) {
        this.info = info;
    }

    @Override
    public boolean hasIssuerCredential() {
        return (this.issuerCred != null);
    }

    @Nullable
    @Override
    public Credential getIssuerCredential() {
        return this.issuerCred;
    }

    @Override
    public void setIssuerCredential(@Nullable Credential issuerCred) {
        this.issuerCred = issuerCred;
    }
}
