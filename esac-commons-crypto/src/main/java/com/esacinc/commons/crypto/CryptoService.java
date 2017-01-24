package com.esacinc.commons.crypto;

import com.esacinc.commons.crypto.beans.AlgorithmNamedBean;
import java.security.Provider;

public interface CryptoService extends AlgorithmNamedBean {
    public void setAlgorithmName(String algName);

    public Provider getProvider();

    public void setProvider(Provider prov);
}
