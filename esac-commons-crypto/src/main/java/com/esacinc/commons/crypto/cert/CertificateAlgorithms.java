package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.Comparator;
import java.util.stream.Stream;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public final class CertificateAlgorithms {
    public final static String X509_NAME = "X.509";
    public final static AlgorithmIdentifier X509_ID = new AlgorithmIdentifier(PKCSObjectIdentifiers.x509Certificate);

    public final static SortedBidiMap<String, AlgorithmIdentifier> NAME_IDS =
        UnmodifiableSortedBidiMap.unmodifiableSortedBidiMap(Stream.of(new ImmutablePair<>(X509_NAME, X509_ID)).collect(
            EsacStreamUtils.toMap(() -> new DualTreeBidiMap<>(String.CASE_INSENSITIVE_ORDER, Comparator.comparing(algId -> algId.getAlgorithm().getId())))));

    private CertificateAlgorithms() {
    }
}
