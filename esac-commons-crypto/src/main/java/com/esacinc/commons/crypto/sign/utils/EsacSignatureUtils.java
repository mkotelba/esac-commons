package com.esacinc.commons.crypto.sign.utils;

import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

public final class EsacSignatureUtils {
    public static class EsacSignatureAlgorithmIdentifierFinder extends DefaultSignatureAlgorithmIdentifierFinder {
        public final static EsacSignatureAlgorithmIdentifierFinder INSTANCE = new EsacSignatureAlgorithmIdentifierFinder();

        @Nullable
        public ASN1ObjectIdentifier findOid(String sigAlgName) {
            return this.find(sigAlgName).getAlgorithm();
        }
    }

    private EsacSignatureUtils() {
    }
}
