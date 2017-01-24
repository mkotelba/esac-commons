package com.esacinc.commons.net.mime;

import com.esacinc.commons.utils.EsacStringUtils;
import org.springframework.http.MediaType;

public final class EsacMediaTypes {
    public final static String APP_TYPE = "application";
    public final static String IMAGE_TYPE = "image";
    public final static String TEXT_TYPE = "text";

    public final static String HTML_SUBTYPE = "html";
    public final static String JSON_SUBTYPE = "json";
    public final static String X_ICON_SUBTYPE = "x-icon";
    public final static String XML_SUBTYPE = "xml";
    public final static String WILDCARD_XML_SUBTYPE = EsacStringUtils.ASTERISK + EsacStringUtils.PLUS + XML_SUBTYPE;

    public final static String WILDCARD_XML_VALUE = EsacStringUtils.ASTERISK + EsacStringUtils.SLASH + XML_SUBTYPE;
    public final static MediaType WILDCARD_XML = new MediaType(EsacStringUtils.ASTERISK, XML_SUBTYPE);

    public final static String WILDCARD_WILDCARD_XML_VALUE = EsacStringUtils.ASTERISK + EsacStringUtils.SLASH + WILDCARD_XML_SUBTYPE;
    public final static MediaType WILDCARD_WILDCARD_XML = new MediaType(EsacStringUtils.ASTERISK, WILDCARD_XML_SUBTYPE);

    public final static String IMAGE_X_ICON_VALUE = IMAGE_TYPE + EsacStringUtils.SLASH + X_ICON_SUBTYPE;
    public final static MediaType IMAGE_X_ICON = new MediaType(IMAGE_TYPE, X_ICON_SUBTYPE);

    private EsacMediaTypes() {
    }
}
