package com.esacinc.commons.convert.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

public class StringTokenMapConverter implements Converter<String, Map<String, String>> {
    @Override
    public Map<String, String> convert(String src) {
        return EsacStringUtils.mapTokens(src);
    }
}
