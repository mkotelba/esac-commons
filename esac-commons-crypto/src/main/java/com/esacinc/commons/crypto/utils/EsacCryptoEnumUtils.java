package com.esacinc.commons.crypto.utils;

import com.esacinc.commons.crypto.beans.AlgorithmIdentifiedBean;
import com.esacinc.commons.crypto.beans.AlgorithmNamedBean;
import com.esacinc.commons.crypto.beans.ObjectIdentifiedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import com.esacinc.commons.utils.EsacEnumUtils;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public final class EsacCryptoEnumUtils {
    private EsacCryptoEnumUtils() {
    }

    @Nullable
    public static <T extends Enum<T> & AlgorithmIdentifiedBean> T findByAlgorithmId(Class<T> enumClass, AlgorithmIdentifier algId) {
        ASN1ObjectIdentifier algOid = algId.getAlgorithm();

        return EsacEnumUtils.findByPredicate(enumClass,
            enumItem -> Objects.equals(Optional.ofNullable(enumItem.getAlgorithmId()).map(AlgorithmIdentifier::getAlgorithm).orElse(null), algOid));
    }

    @Nullable
    public static <T extends Enum<T> & AlgorithmIdentifiedBean> T findByAlgorithmId(Stream<T> enumItems, AlgorithmIdentifier algId) {
        ASN1ObjectIdentifier algOid = algId.getAlgorithm();

        return EsacEnumUtils.findByPredicate(enumItems,
            enumItem -> Objects.equals(Optional.ofNullable(enumItem.getAlgorithmId()).map(AlgorithmIdentifier::getAlgorithm).orElse(null), algOid));
    }

    @Nullable
    public static <T extends Enum<T> & AlgorithmNamedBean> T findByAlgorithmName(Class<T> enumClass, String algName) {
        return EsacEnumUtils.findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getAlgorithmName(), algName));
    }

    @Nullable
    public static <T extends Enum<T> & AlgorithmNamedBean> T findByAlgorithmName(Stream<T> enumItems, String algName) {
        return EsacEnumUtils.findByPredicate(enumItems, enumItem -> Objects.equals(enumItem.getAlgorithmName(), algName));
    }

    @Nullable
    public static <T extends Enum<T> & ObjectIdentifiedBean> T findByOid(Class<T> enumClass, ASN1ObjectIdentifier oid) {
        return EsacEnumUtils.findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getOid(), oid));
    }

    @Nullable
    public static <T extends Enum<T> & ObjectIdentifiedBean> T findByOid(Stream<T> enumItems, ASN1ObjectIdentifier oid) {
        return EsacEnumUtils.findByPredicate(enumItems, enumItem -> Objects.equals(enumItem.getOid(), oid));
    }

    @Nullable
    public static <T extends Enum<T> & TaggedBean> T findByTag(Class<T> enumClass, int tag) {
        return EsacEnumUtils.findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getTag(), tag));
    }

    @Nullable
    public static <T extends Enum<T> & TaggedBean> T findByTag(Stream<T> enumItems, int tag) {
        return EsacEnumUtils.findByPredicate(enumItems, enumItem -> Objects.equals(enumItem.getTag(), tag));
    }
}
