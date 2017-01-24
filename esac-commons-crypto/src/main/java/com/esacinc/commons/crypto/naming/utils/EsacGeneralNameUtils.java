package com.esacinc.commons.crypto.naming.utils;

import com.esacinc.commons.crypto.utils.EsacCryptoEnumUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.naming.GeneralNameType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public final class EsacGeneralNameUtils {
    private EsacGeneralNameUtils() {
    }

    public static GeneralNames buildNames(ListMultimap<GeneralNameType, ASN1Encodable> names) {
        if (names.isEmpty()) {
            return new GeneralNames(new GeneralName[0]);
        }

        int numNames = names.size(), nameTypeTag;
        List<GeneralName> nameItems = new ArrayList<>(numNames);

        for (GeneralNameType nameType : names.keySet()) {
            nameTypeTag = nameType.getTag();

            for (ASN1Encodable nameEncodableValue : names.get(nameType)) {
                nameItems.add(new GeneralName(nameTypeTag, nameEncodableValue));
            }
        }

        return new GeneralNames(nameItems.toArray(new GeneralName[numNames]));
    }

    public static ListMultimap<GeneralNameType, ASN1Encodable> mapNames(@Nullable GeneralNames names) {
        return ((names != null) ? mapNames(names.getNames()) : ArrayListMultimap.create());
    }

    public static ListMultimap<GeneralNameType, ASN1Encodable> mapNames(@Nullable GeneralName ... nameItems) {
        if (nameItems == null) {
            return ArrayListMultimap.create();
        }
        
        ListMultimap<GeneralNameType, ASN1Encodable> names = ArrayListMultimap.create(nameItems.length, 1);

        Stream.of(nameItems).forEach(nameItem -> names.put(EsacCryptoEnumUtils.findByTag(GeneralNameType.class, nameItem.getTagNo()), nameItem.getName()));

        return names;
    }
}
