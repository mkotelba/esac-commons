package com.esacinc.commons.xml.saxon.convert.impl;

import com.esacinc.commons.convert.EsacConditionalGenericConverter;
import com.esacinc.commons.xml.saxon.impl.EsacDocumentUri;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.om.DocumentURI;
import org.springframework.core.convert.TypeDescriptor;

public class EsacDocumentUriConverter implements EsacConditionalGenericConverter {
    private final static Set<ConvertiblePair> CONV_TYPES =
        Stream.of(new ConvertiblePair(String.class, DocumentURI.class), new ConvertiblePair(String.class, EsacDocumentUri.class)).collect(Collectors.toSet());

    @Nullable
    @Override
    public Object convert(Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        if (!this.matches(srcType, targetType)) {
            return null;
        }

        return new EsacDocumentUri(((String) src));
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONV_TYPES;
    }
}
