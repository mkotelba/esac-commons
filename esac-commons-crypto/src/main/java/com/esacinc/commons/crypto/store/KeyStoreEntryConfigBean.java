package com.esacinc.commons.crypto.store;

import com.esacinc.commons.beans.ConfigBean;
import com.esacinc.commons.crypto.credential.Credential;
import javax.annotation.Nullable;

public interface KeyStoreEntryConfigBean extends ConfigBean, KeyStoreEntryDescriptorBean {
    public boolean hasCredential();

    @Nullable
    public Credential getCredential();

    public void setCredential(@Nullable Credential cred);
}
