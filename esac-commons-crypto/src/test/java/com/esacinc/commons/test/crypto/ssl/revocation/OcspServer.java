package com.esacinc.commons.test.crypto.ssl.revocation;

import com.esacinc.commons.test.beans.EsacHttpServer;
import java.security.SecureRandom;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface OcspServer extends EsacHttpServer {
    public SecureRandom getSecureRandom();

    public void setSecureRandom(SecureRandom secureRandom);

    public AlgorithmIdentifier getSignatureAlgorithmId();

    public void setSignatureAlgorithmId(AlgorithmIdentifier sigAlgId);
}
