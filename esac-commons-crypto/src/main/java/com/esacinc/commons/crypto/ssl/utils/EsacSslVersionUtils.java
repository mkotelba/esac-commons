package com.esacinc.commons.crypto.ssl.utils;

import javax.annotation.Nonnegative;

public final class EsacSslVersionUtils {
    private EsacSslVersionUtils() {
    }

    @Nonnegative
    public static int extractMajor(int version) {
        return ((version >>> 8) & 0xff);
    }

    @Nonnegative
    public static int extractMinor(int version) {
        return (version & 0xff);
    }

    @Nonnegative
    public static int buildVersion(@Nonnegative int major, @Nonnegative int minor) {
        return (((major & 0xff) << 8) | (minor & 0xff));
    }
}
