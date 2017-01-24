package com.esacinc.commons.transform.convert.impl;

import com.esacinc.commons.convert.EsacConditionalGenericConverter;
import com.esacinc.commons.transform.ResourceSourceResolver;
import com.esacinc.commons.transform.impl.ResourceSource;
import com.esacinc.commons.utils.EsacStringUtils;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

public class ResourceSourceConverter implements EsacConditionalGenericConverter {
    private final static Set<ConvertiblePair> CONV_TYPES =
        Stream.of(new ConvertiblePair(String.class, Source.class), new ConvertiblePair(String.class, ResourceSource.class),
            new ConvertiblePair(String.class, Source[].class), new ConvertiblePair(String.class, ResourceSource[].class),
            new ConvertiblePair(String[].class, Source.class), new ConvertiblePair(String[].class, ResourceSource.class),
            new ConvertiblePair(String[].class, Source[].class), new ConvertiblePair(String[].class, ResourceSource[].class)).collect(Collectors.toSet());

    @Autowired
    private ResourceSourceResolver resourceSrcResolver;

    @Nullable
    @Override
    public Object convert(Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        if (!this.matches(srcType, targetType)) {
            return null;
        }

        boolean srcArr = srcType.isArray(), resolveAll = (srcArr || targetType.isArray());
        String strSrc = (!srcArr ? ((String) src) : null);
        String[] strSrcs = (srcArr ? EsacStringUtils.splitTokens(((String[]) src)) : EsacStringUtils.splitTokens(strSrc));

        if (!resolveAll && (strSrcs.length > 1)) {
            resolveAll = true;
        }

        try {
            return (resolveAll ? this.resourceSrcResolver.resolveAll(strSrcs) : this.resourceSrcResolver.resolve(strSrc));
        } catch (IOException e) {
            throw new ConversionFailedException(srcType, targetType, src, e);
        }
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONV_TYPES;
    }
}
