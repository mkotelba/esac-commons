package com.esacinc.commons.convert.impl;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class CaseInsensitiveStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    public static class CaseInsensitiveStringToEnumConverter<T extends Enum<?>> implements Converter<String, T> {
        private Class<T> targetType;

        public CaseInsensitiveStringToEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Nullable
        @Override
        public T convert(String src) {
            return (!StringUtils.isBlank(src)
                ? Stream.of(this.targetType.getEnumConstants()).filter(enumItem -> enumItem.name().equalsIgnoreCase(src)).findFirst().orElse(null) : null);
        }
    }

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new CaseInsensitiveStringToEnumConverter<T>(targetType);
    }
}
