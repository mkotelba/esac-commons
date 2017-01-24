package com.esacinc.commons;

import javax.annotation.Nullable;

public class EsacException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    public EsacException(@Nullable String msg) {
        this(msg, null);
    }

    public EsacException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public EsacException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
