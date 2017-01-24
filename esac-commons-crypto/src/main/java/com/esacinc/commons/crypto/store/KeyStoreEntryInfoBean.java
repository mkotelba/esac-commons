package com.esacinc.commons.crypto.store;

import com.esacinc.commons.beans.InfoBean;
import java.security.KeyStore.Entry;
import java.util.Date;
import javax.annotation.Nullable;

public interface KeyStoreEntryInfoBean<T extends Entry> extends InfoBean, KeyStoreEntryDescriptorBean {
    public boolean hasCreationDate();

    @Nullable
    public Date getCreationDate();

    public T getEntry();
}
