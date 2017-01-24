package com.esacinc.commons.crypto.key;

import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.Comparator;
import java.util.stream.Stream;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

public final class KeyAlgorithms {
    public final static String DH_NAME = "DH";
    public final static AlgorithmIdentifier DH_ID = new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement);

    public final static String DSA_NAME = "DSA";
    public final static AlgorithmIdentifier DSA_ID = new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa);

    public final static String EC_NAME = "EC";
    public final static AlgorithmIdentifier EC_ID = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey);

    public final static String RSA_NAME = "RSA";
    public final static AlgorithmIdentifier RSA_ID = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption);

    public final static SortedBidiMap<String, AlgorithmIdentifier> NAME_IDS = UnmodifiableSortedBidiMap.unmodifiableSortedBidiMap(Stream
        .of(new ImmutablePair<>(DH_NAME, DH_ID), new ImmutablePair<>(DSA_NAME, DSA_ID), new ImmutablePair<>(EC_NAME, EC_ID),
            new ImmutablePair<>(RSA_NAME, RSA_ID))
        .collect(
            EsacStreamUtils.toMap(() -> new DualTreeBidiMap<>(String.CASE_INSENSITIVE_ORDER, Comparator.comparing(algId -> algId.getAlgorithm().getId())))));

    private KeyAlgorithms() {
    }
}
