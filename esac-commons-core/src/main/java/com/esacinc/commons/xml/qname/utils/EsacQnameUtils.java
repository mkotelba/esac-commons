package com.esacinc.commons.xml.qname.utils;

import com.esacinc.commons.utils.EsacStringUtils;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class EsacQnameUtils {
    private EsacQnameUtils() {
    }

    public static String buildQualifiedName(QName qname) {
        String prefix = qname.getPrefix(), localPart = qname.getLocalPart();

        return (StringUtils.isEmpty(prefix) ? localPart : (prefix + EsacStringUtils.COLON + localPart));
    }

    public static QName build(@Nullable String nsPrefix, @Nullable String nsUri, String localPart) {
        return new QName(ObjectUtils.defaultIfNull(nsPrefix, XMLConstants.DEFAULT_NS_PREFIX), ObjectUtils.defaultIfNull(nsUri, XMLConstants.NULL_NS_URI),
            localPart);
    }
}
