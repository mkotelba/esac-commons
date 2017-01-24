package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.crypto.factory.CryptoServiceFactory;
import java.security.cert.X509Certificate;

public interface CertificateFactory
    extends CryptoServiceFactory<CertificateConfig, CertificateFactoryRequest, X509Certificate, CertificateInfo, CertificateFactoryResponse> {
}
