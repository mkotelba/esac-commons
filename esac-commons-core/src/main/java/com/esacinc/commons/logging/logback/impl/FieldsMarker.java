package com.esacinc.commons.logging.logback.impl;

import com.esacinc.commons.config.property.impl.PropertyNameComparator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FieldsMarker extends AbstractEsacMarker {
    private final static long serialVersionUID = 0L;

    private Map<String, Object> fields = new TreeMap<>(PropertyNameComparator.INSTANCE);

    public FieldsMarker() {
        super("FIELDS");
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof FieldsMarker) && super.equals(obj));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.fields.hashCode()).toHashCode();
    }

    public boolean hasFields() {
        return !this.fields.isEmpty();
    }

    public Map<String, Object> getFields() {
        return this.fields;
    }
}
