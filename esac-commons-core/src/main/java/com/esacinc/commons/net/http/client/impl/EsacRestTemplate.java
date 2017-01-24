package com.esacinc.commons.net.http.client.impl;

import com.esacinc.commons.net.http.client.EsacHttpClientRequestFactory;
import org.springframework.web.client.RestTemplate;

public class EsacRestTemplate extends RestTemplate {
    public EsacRestTemplate(EsacHttpClientRequestFactory reqFactory) {
        super(reqFactory);
    }
}
