package com.esacinc.commons.crypto.key.utils;

import com.esacinc.commons.crypto.key.KeyAlgorithms;
import java.security.Key;
import java.security.interfaces.DSAKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import javax.crypto.interfaces.DHKey;
import javax.crypto.spec.DHParameterSpec;

public final class EsacKeyUtils {
    private EsacKeyUtils() {
    }

    public static int extractSize(String algName, Key key) {
        // noinspection ConstantConditions
        switch (algName) {
            case KeyAlgorithms.DH_NAME:
                DHParameterSpec dhKeyParams = ((DHKey) key).getParams();
                int dhKeyParamLValue = dhKeyParams.getL();

                return ((dhKeyParamLValue != 0) ? dhKeyParamLValue : dhKeyParams.getP().bitLength());

            case KeyAlgorithms.DSA_NAME:
                return (((DSAKey) key).getParams().getP().bitLength() - 1);

            case KeyAlgorithms.EC_NAME:
                return ((ECKey) key).getParams().getOrder().bitLength();
            
            case KeyAlgorithms.RSA_NAME:
                return ((RSAKey) key).getModulus().bitLength();
            
            default:
                return -1;
        }
    }
}
