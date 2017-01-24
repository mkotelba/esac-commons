package com.esacinc.commons.management;

import com.esacinc.commons.EsacException;
import javax.annotation.Nullable;

public class EsacManagementException extends EsacException {
    private final static long serialVersionUID = 0L;

    public EsacManagementException() {
        this(((String) null));
    }

    public EsacManagementException(@Nullable String msg) {
        this(msg, null);
    }

    public EsacManagementException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public EsacManagementException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
