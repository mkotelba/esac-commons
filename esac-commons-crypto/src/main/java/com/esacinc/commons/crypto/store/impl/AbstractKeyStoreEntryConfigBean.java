package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.credential.Credential;
import com.esacinc.commons.crypto.store.KeyStoreEntryConfigBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import javax.annotation.Nullable;

public abstract class AbstractKeyStoreEntryConfigBean extends AbstractKeyStoreEntryDescriptorBean implements KeyStoreEntryConfigBean {
    protected Credential cred;

    protected AbstractKeyStoreEntryConfigBean(KeyStoreEntryType type, String alias) {
        super(type, alias);
    }

    @Override
    public boolean hasCredential() {
        return (this.cred != null);
    }

    @Nullable
    @Override
    public Credential getCredential() {
        return this.cred;
    }

    @Override
    public void setCredential(@Nullable Credential cred) {
        this.cred = cred;
    }
}
