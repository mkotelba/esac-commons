package com.esacinc.commons.crypto.net.mime;

import com.esacinc.commons.net.mime.EsacMediaTypes;
import com.esacinc.commons.utils.EsacStringUtils;
import org.springframework.http.MediaType;

public final class EsacCryptoMediaTypes {
    public final static String OCSP_SUBTYPE_PREFIX = "ocsp-";

    public final static String OCSP_REQ_SUBTYPE = OCSP_SUBTYPE_PREFIX + "request";
    public final static String OCSP_RESP_SUBTYPE = OCSP_SUBTYPE_PREFIX + "response";

    public final static String OCSP_RESP_VALUE = EsacMediaTypes.APP_TYPE + EsacStringUtils.SLASH + OCSP_RESP_SUBTYPE;
    public final static MediaType OCSP_RESP = new MediaType(EsacMediaTypes.APP_TYPE, OCSP_RESP_SUBTYPE);

    public final static String OCSP_REQ_VALUE = EsacMediaTypes.APP_TYPE + EsacStringUtils.SLASH + OCSP_REQ_SUBTYPE;
    public final static MediaType OCSP_REQ = new MediaType(EsacMediaTypes.APP_TYPE, OCSP_REQ_SUBTYPE);

    private EsacCryptoMediaTypes() {
    }
}
