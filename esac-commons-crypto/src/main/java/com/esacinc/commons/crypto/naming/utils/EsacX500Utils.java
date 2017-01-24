package com.esacinc.commons.crypto.naming.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public final class EsacX500Utils {
    private EsacX500Utils() {
    }

    public static X500Name buildName(@Nullable Comparator<ASN1ObjectIdentifier> attrOidComparator, ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> attrs) {
        if (attrs.isEmpty()) {
            return new X500Name(BCStrictStyle.INSTANCE, new RDN[0]);
        }

        X500NameBuilder builder = new X500NameBuilder(BCStrictStyle.INSTANCE);
        Stream<ASN1ObjectIdentifier> attrOids = attrs.keySet().stream();

        if (attrOidComparator != null) {
            attrOids = attrOids.sorted(attrOidComparator);
        }

        attrOids.forEach(attrOid -> attrs.get(attrOid).forEach(attrValue -> builder.addRDN(attrOid, attrValue)));

        return builder.build();
    }

    public static ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(@Nullable X500Name name) {
        return ((name != null) ? mapAttributes(name.getRDNs()) : ArrayListMultimap.create());
    }

    public static ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(@Nullable RDN ... rdns) {
        if (ArrayUtils.isEmpty(rdns)) {
            return ArrayListMultimap.create();
        }

        ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> attrs = ArrayListMultimap.create();

        // noinspection ConstantConditions
        Stream.of(rdns).flatMap(rdn -> Stream.of(rdn.getTypesAndValues())).forEach(attr -> attrs.put(attr.getType(), attr.getValue()));

        return attrs;
    }

    @Nullable
    public static ASN1Encodable toEncodableAttributeValue(ASN1ObjectIdentifier attrOid, @Nullable String attrStrValue) {
        return ((attrStrValue != null) ? BCStrictStyle.INSTANCE.stringToValue(attrOid, attrStrValue) : null);
    }

    @Nullable
    public static String toStringAttributeValue(@Nullable ASN1Encodable attrEncodableValue) {
        return ((attrEncodableValue != null) ? IETFUtils.valueToString(attrEncodableValue) : null);
    }
}
