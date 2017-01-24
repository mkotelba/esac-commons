package com.esacinc.commons.crypto.sign;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.crypto.sign.utils.EsacSignatureUtils.EsacSignatureAlgorithmIdentifierFinder;
import java.util.Comparator;
import java.util.stream.Stream;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public final class SignatureAlgorithms {
    public final static String SHA_1_WITH_RSA_ENCRYPTION_NAME = "sha1WithRSAEncryption";
    public final static AlgorithmIdentifier SHA_1_WITH_RSA_ENCRYPTION_ID = EsacSignatureAlgorithmIdentifierFinder.INSTANCE.find(SHA_1_WITH_RSA_ENCRYPTION_NAME);

    public final static String SHA_256_WITH_RSA_ENCRYPTION_NAME = "sha256WithRSAEncryption";
    public final static AlgorithmIdentifier SHA_256_WITH_RSA_ENCRYPTION_ID =
        EsacSignatureAlgorithmIdentifierFinder.INSTANCE.find(SHA_256_WITH_RSA_ENCRYPTION_NAME);

    public final static String SHA_384_WITH_RSA_ENCRYPTION_NAME = "sha384WithRSAEncryption";
    public final static AlgorithmIdentifier SHA_384_WITH_RSA_ENCRYPTION_ID =
        EsacSignatureAlgorithmIdentifierFinder.INSTANCE.find(SHA_384_WITH_RSA_ENCRYPTION_NAME);

    public final static String SHA_512_WITH_RSA_ENCRYPTION_NAME = "sha512WithRSAEncryption";
    public final static AlgorithmIdentifier SHA_512_WITH_RSA_ENCRYPTION_ID =
        EsacSignatureAlgorithmIdentifierFinder.INSTANCE.find(SHA_512_WITH_RSA_ENCRYPTION_NAME);

    public final static SortedBidiMap<String, AlgorithmIdentifier> NAME_IDS = UnmodifiableSortedBidiMap.unmodifiableSortedBidiMap(Stream
        .of(new ImmutablePair<>(SHA_1_WITH_RSA_ENCRYPTION_NAME, SHA_1_WITH_RSA_ENCRYPTION_ID),
            new ImmutablePair<>(SHA_256_WITH_RSA_ENCRYPTION_NAME, SHA_256_WITH_RSA_ENCRYPTION_ID),
            new ImmutablePair<>(SHA_384_WITH_RSA_ENCRYPTION_NAME, SHA_384_WITH_RSA_ENCRYPTION_ID),
            new ImmutablePair<>(SHA_512_WITH_RSA_ENCRYPTION_NAME, SHA_512_WITH_RSA_ENCRYPTION_ID))
        .collect(
            EsacStreamUtils.toMap(() -> new DualTreeBidiMap<>(String.CASE_INSENSITIVE_ORDER, Comparator.comparing(algId -> algId.getAlgorithm().getId())))));

    private SignatureAlgorithms() {
    }
}
