package com.esacinc.commons.net.http;

import com.google.common.collect.ListMultimap;
import java.util.Map;

public interface HttpParameterMultimap extends ListMultimap<String, Object> {
    public boolean putAll(Map<String, ? extends Iterable<?>> map);
    
    public boolean putAll(String key, Object ... values);
}
