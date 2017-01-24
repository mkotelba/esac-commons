package com.esacinc.commons.crypto.digest.utils;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.crypto.CryptoException;
import com.esacinc.commons.crypto.CryptoProviders;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.bc.BCObjectIdentifiers;
import org.bouncycastle.asn1.bsi.BSIObjectIdentifiers;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

public final class EsacDigestUtils {
    /**
     * Extension of the default digest algorithm identifier finder in order to handle the missing signature algorithm mappings (see
     * <a href="http://www.bouncycastle.org/jira/browse/BJA-640">BouncyCastle JIRA issue BJA-640</a>).
     */
    public static class EsacDigestAlgorithmIdentifierFinder extends DefaultDigestAlgorithmIdentifierFinder {
        public final static EsacDigestAlgorithmIdentifierFinder INSTANCE = new EsacDigestAlgorithmIdentifierFinder();

        private final static Map<ASN1ObjectIdentifier, ASN1ObjectIdentifier> SIG_DIGEST_ALG_OIDS =
            Stream.of(new ImmutablePair<>(BSIObjectIdentifiers.ecdsa_plain_RIPEMD160, TeleTrusTObjectIdentifiers.ripemd160),
                new ImmutablePair<>(EACObjectIdentifiers.id_TA_ECDSA_SHA_1, OIWObjectIdentifiers.idSHA1),
                new ImmutablePair<>(BCObjectIdentifiers.sphincs256_with_SHA3_512, NISTObjectIdentifiers.id_sha3_512),
                new ImmutablePair<>(EACObjectIdentifiers.id_TA_ECDSA_SHA_224, NISTObjectIdentifiers.id_sha224),
                new ImmutablePair<>(EACObjectIdentifiers.id_TA_ECDSA_SHA_256, NISTObjectIdentifiers.id_sha256),
                new ImmutablePair<>(EACObjectIdentifiers.id_TA_ECDSA_SHA_512, NISTObjectIdentifiers.id_sha512),
                new ImmutablePair<>(BCObjectIdentifiers.sphincs256_with_SHA512, NISTObjectIdentifiers.id_sha512)).collect(EsacStreamUtils.toMap());

        private final static Map<ASN1ObjectIdentifier, AlgorithmIdentifier> SIG_DIGEST_ALG_IDS = SIG_DIGEST_ALG_OIDS.entrySet().stream()
            .collect(EsacStreamUtils.toMap(Entry::getKey, sigDigestAlgOidEntry -> new AlgorithmIdentifier(sigDigestAlgOidEntry.getValue()), HashMap::new));

        @Nullable
        public ASN1ObjectIdentifier findOid(AlgorithmIdentifier sigAlgId) {
            ASN1ObjectIdentifier sigAlgOid = sigAlgId.getAlgorithm();

            return (SIG_DIGEST_ALG_OIDS.containsKey(sigAlgOid) ? SIG_DIGEST_ALG_OIDS.get(sigAlgOid) : super.find(sigAlgId).getAlgorithm());
        }

        @Override
        public AlgorithmIdentifier find(AlgorithmIdentifier sigAlgId) {
            ASN1ObjectIdentifier sigAlgOid = sigAlgId.getAlgorithm();

            return (SIG_DIGEST_ALG_IDS.containsKey(sigAlgOid) ? SIG_DIGEST_ALG_IDS.get(sigAlgOid) : super.find(sigAlgId));
        }
    }

    public static class EsacDigestCalculator implements DigestCalculator {
        private DigestCalculator delegate;

        public EsacDigestCalculator(DigestCalculator delegate) {
            this.delegate = delegate;
        }

        public byte[] digest(byte ... data) throws CryptoException {
            try {
                OutputStream outStream = this.getOutputStream();
                outStream.write(data);
                outStream.flush();
                outStream.close();

                return this.getDigest();
            } catch (IOException e) {
                throw new CryptoException(String.format("Unable to digest (algOid=%s) data (size=%d).", this.getAlgorithmIdentifier(), data.length), e);
            }
        }

        @Override
        public byte[] getDigest() {
            return this.delegate.getDigest();
        }

        @Override
        public OutputStream getOutputStream() {
            return this.delegate.getOutputStream();
        }

        @Override
        public AlgorithmIdentifier getAlgorithmIdentifier() {
            return this.delegate.getAlgorithmIdentifier();
        }
    }

    public static class EsacDigestCalculatorProvider implements DigestCalculatorProvider {
        private DigestCalculatorProvider delegate;

        public EsacDigestCalculatorProvider(Provider prov) {
            try {
                this.delegate = new JcaDigestCalculatorProviderBuilder().setProvider(prov).build();
            } catch (OperatorCreationException e) {
                throw new CryptoException(
                    String.format("Unable to build digest calculator provider using provider (class=%s, name=%s).", prov.getClass().getName(), prov.getName()),
                    e);
            }
        }

        public byte[] digest(AlgorithmIdentifier algId, byte ... data) throws CryptoException {
            try {
                return this.get(algId).digest(data);
            } catch (OperatorCreationException e) {
                throw new CryptoException(String.format("Unable to build digest (algOid=%s) calculator using provider wrapper (class=%s).",
                    algId.getAlgorithm(), this.delegate.getClass().getName()), e);
            }
        }

        @Override
        public EsacDigestCalculator get(AlgorithmIdentifier algId) throws OperatorCreationException {
            return new EsacDigestCalculator(this.delegate.get(algId));
        }
    }

    public final static EsacDigestCalculatorProvider CALC_PROV = new EsacDigestCalculatorProvider(CryptoProviders.SUN);

    private EsacDigestUtils() {
    }
}
