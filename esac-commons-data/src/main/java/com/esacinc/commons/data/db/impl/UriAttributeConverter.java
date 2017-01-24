package com.esacinc.commons.data.db.impl;

import java.net.URI;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvUri")
@Converter(autoApply = true)
public class UriAttributeConverter implements AttributeConverter<URI, String> {
    @Nullable
    @Override
    public URI convertToEntityAttribute(@Nullable String dbValue) {
        return ((dbValue != null) ? URI.create(dbValue) : null);
    }

    @Nullable
    @Override
    public String convertToDatabaseColumn(@Nullable URI entityValue) {
        return Objects.toString(entityValue, null);
    }
}
