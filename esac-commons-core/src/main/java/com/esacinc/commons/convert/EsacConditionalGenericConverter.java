package com.esacinc.commons.convert;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public interface EsacConditionalGenericConverter extends ConditionalGenericConverter {
    @Override
    public default boolean matches(TypeDescriptor srcType, TypeDescriptor targetType) {
        return this.getConvertibleTypes().stream().anyMatch(convType -> (srcType.isAssignableTo(TypeDescriptor.valueOf(convType.getSourceType()))
            && targetType.isAssignableTo(TypeDescriptor.valueOf(convType.getTargetType()))));
    }
}
