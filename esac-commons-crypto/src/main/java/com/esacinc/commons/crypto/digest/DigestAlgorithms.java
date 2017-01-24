package com.esacinc.commons.crypto.digest;

import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils.EsacDigestAlgorithmIdentifierFinder;
import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.Comparator;
import java.util.stream.Stream;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public final class DigestAlgorithms {
    public final static String SHA_1_NAME = DigestAlgorithms.SHA_NAME_PREFIX + 1;
    public final static AlgorithmIdentifier SHA_1_ID = EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(SHA_1_NAME);

    public final static String SHA_256_NAME = DigestAlgorithms.SHA_NAME_PREFIX + 256;
    public final static AlgorithmIdentifier SHA_256_ID = EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(SHA_256_NAME);

    public final static String SHA_384_NAME = DigestAlgorithms.SHA_NAME_PREFIX + 384;
    public final static AlgorithmIdentifier SHA_384_ID = EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(SHA_384_NAME);

    public final static String SHA_512_NAME = DigestAlgorithms.SHA_NAME_PREFIX + 512;
    public final static AlgorithmIdentifier SHA_512_ID = EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(SHA_512_NAME);

    public final static SortedBidiMap<String, AlgorithmIdentifier> NAME_IDS = UnmodifiableSortedBidiMap.unmodifiableSortedBidiMap(Stream
        .of(new ImmutablePair<>(SHA_1_NAME, SHA_1_ID), new ImmutablePair<>(SHA_256_NAME, SHA_256_ID), new ImmutablePair<>(SHA_384_NAME, SHA_384_ID),
            new ImmutablePair<>(SHA_512_NAME, SHA_512_ID))
        .collect(
            EsacStreamUtils.toMap(() -> new DualTreeBidiMap<>(String.CASE_INSENSITIVE_ORDER, Comparator.comparing(algId -> algId.getAlgorithm().getId())))));

    private final static String SHA_NAME_PREFIX = "SHA-";

    private DigestAlgorithms() {
    }
}
