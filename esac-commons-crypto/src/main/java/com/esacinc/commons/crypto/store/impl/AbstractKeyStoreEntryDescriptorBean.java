package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.beans.impl.AbstractDescriptorBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryDescriptorBean;
import com.esacinc.commons.crypto.store.KeyStoreEntryType;

public abstract class AbstractKeyStoreEntryDescriptorBean extends AbstractDescriptorBean implements KeyStoreEntryDescriptorBean {
    protected KeyStoreEntryType type;
    protected String alias;

    protected AbstractKeyStoreEntryDescriptorBean(KeyStoreEntryType type, String alias) {
        super(null, null);

        this.type = type;
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public KeyStoreEntryType getType() {
        return this.type;
    }
}
