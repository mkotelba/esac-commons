package com.esacinc.commons.logging.elasticsearch;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.beans.TypedBean;
import java.net.InetAddress;
import javax.annotation.Nullable;

public enum ElasticsearchDatatype implements NamedBean, TypedBean {
    DEFAULT(null, null, false), OBJECT(null, null, false), BINARY(null, String.class, true), BOOLEAN(null, Boolean.class, true), BYTE(null, Byte.class, true),
    DATE("long", Long.class, true), DOUBLE(null, Double.class, true), FLOAT(null, Float.class, true), GEO_POINT(null, null, true), GEO_SHAPE(null, null, true),
    HALF_FLOAT(null, Float.class, true), INTEGER(null, Integer.class, true), IP(null, InetAddress.class, true), KEYWORD(null, String.class, true),
    LONG(null, Long.class, true), NESTED(null, null, true), SHORT(null, Short.class, true), TEXT(null, String.class, true), TOKEN_COUNT(null, Long.class, true);

    private final String name;
    private final Class<?> type;
    private final boolean explicit;

    private ElasticsearchDatatype(@Nullable String name, @Nullable Class<?> type, boolean explicit) {
        this.name = ((name != null) ? name : this.name().toLowerCase());
        this.type = type;
        this.explicit = explicit;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    public boolean hasName() {
        return (this.name != null);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    public boolean hasType() {
        return (this.type != null);
    }

    @Nullable
    @Override
    public Class<?> getType() {
        return this.type;
    }
}
