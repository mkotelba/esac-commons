package com.esacinc.commons.logging.elasticsearch.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class ElasticsearchPropertyNamingStrategy extends PropertyNamingStrategyBase {
    private final static long serialVersionUID = 0L;

    @Nullable
    @Override
    public String translate(String propName) {
        return (StringUtils.isEmpty(propName)
            ? propName
            : Stream.of(StringUtils.split(propName, EsacStringUtils.PERIOD_CHAR))
                .map(propNamePart -> Stream.of(EsacStringUtils.splitCamelCase(propNamePart, EsacStringUtils.PERIOD)).map(String::toLowerCase)
                    .collect(Collectors.joining(EsacStringUtils.PERIOD)))
                .collect(Collectors.joining(EsacStringUtils.PERIOD)));
    }
}
