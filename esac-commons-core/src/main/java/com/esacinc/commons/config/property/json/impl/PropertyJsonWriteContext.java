package com.esacinc.commons.config.property.json.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public class PropertyJsonWriteContext extends JsonWriteContext {
    private String propName;
    private boolean propNameAvailable;
    private String propNamePrefix;
    private String currentPropName;
    private List<Object> propValues;

    public PropertyJsonWriteContext() {
        this(TYPE_ROOT, null, null);
    }

    public PropertyJsonWriteContext(@Nonnegative int type, @Nullable PropertyJsonWriteContext parent, @Nullable String propName) {
        super(type, parent, null);

        this.initialize(propName);
    }

    @Override
    public int writeFieldName(String name) throws JsonProcessingException {
        int status = super.writeFieldName(name);

        if (status != STATUS_EXPECT_VALUE) {
            this.currentPropName = (this.propNameAvailable ? (this.propNamePrefix + this._currentName) : this._currentName);
        }

        return status;
    }

    @Nullable
    @Override
    public PropertyJsonWriteContext clearAndGetParent() {
        return ((PropertyJsonWriteContext) super.clearAndGetParent());
    }

    @Override
    public PropertyJsonWriteContext createChildArrayContext() {
        return this.createChildContext(TYPE_ARRAY, this.currentPropName);
    }

    @Override
    public PropertyJsonWriteContext createChildObjectContext() {
        return this.createChildContext(TYPE_OBJECT, this.currentPropName);
    }

    @Override
    public PropertyJsonWriteContext reset(@Nonnegative int type) {
        super.reset(type);

        this.initialize(propName);

        return this;
    }

    @Override
    public void appendDesc(StringBuilder builder) {
        if (!this.inRoot()) {
            ((PropertyJsonWriteContext) this._parent).appendDesc(builder);

            builder.append(EsacStringUtils.SLASH_CHAR);
        }

        if (this.inArray()) {
            builder.append(this.getCurrentIndex());
        } else if (this.inObject() && this.hasCurrentName()) {
            builder.append(this._currentName);
        }
    }

    private PropertyJsonWriteContext createChildContext(@Nonnegative int type, String propName) {
        PropertyJsonWriteContext child;

        if (this._child == null) {
            this._child = child = new PropertyJsonWriteContext(type, this, propName);
        } else {
            (child = ((PropertyJsonWriteContext) this._child)).reset(type);
        }

        return child;
    }

    private void initialize(String propName) {
        this.propNamePrefix = (((this.propNameAvailable = ((this.propName = propName) != null)) && (this._type == TYPE_OBJECT))
            ? (this.propName + EsacStringUtils.PERIOD) : null);
        this.currentPropName = null;

        this.propValues = ((this._type == TYPE_ARRAY) ? new ArrayList<>() : null);
    }

    public boolean hasCurrentName() {
        return (this._currentName != null);
    }

    public boolean hasCurrentPropertyName() {
        return (this.currentPropName != null);
    }

    @Nullable
    public String getCurrentPropertyName() {
        return this.currentPropName;
    }

    public boolean hasPropertyName() {
        return this.propNameAvailable;
    }

    @Nullable
    public String getPropertyName() {
        return this.propName;
    }

    @Nullable
    public String getPropertyNamePrefix() {
        return this.propNamePrefix;
    }

    @Nullable
    public List<Object> getPropertyValues() {
        return this.propValues;
    }
}
