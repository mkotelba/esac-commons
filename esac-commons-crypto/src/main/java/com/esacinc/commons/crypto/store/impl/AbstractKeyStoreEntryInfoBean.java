package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.store.KeyStoreEntryInfoBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import java.security.KeyStore.Entry;
import java.util.Date;
import javax.annotation.Nullable;

public abstract class AbstractKeyStoreEntryInfoBean<T extends Entry> extends AbstractKeyStoreEntryDescriptorBean implements KeyStoreEntryInfoBean<T> {
    protected Date creationDate;
    protected T entry;

    protected AbstractKeyStoreEntryInfoBean(KeyStoreEntryType type, String alias, @Nullable Date creationDate, T entry) {
        super(type, alias);

        this.creationDate = creationDate;
        this.entry = entry;
    }

    @Override
    public boolean hasCreationDate() {
        return (this.creationDate != null);
    }

    @Nullable
    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public T getEntry() {
        return this.entry;
    }
}
