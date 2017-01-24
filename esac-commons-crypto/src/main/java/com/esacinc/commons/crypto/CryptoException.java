package com.esacinc.commons.crypto;

import com.esacinc.commons.EsacException;
import javax.annotation.Nullable;

public class CryptoException extends EsacException {
    private final static long serialVersionUID = 0L;

    public CryptoException(@Nullable String msg) {
        this(msg, null);
    }

    public CryptoException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public CryptoException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
