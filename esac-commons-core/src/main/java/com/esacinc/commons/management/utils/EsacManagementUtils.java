package com.esacinc.commons.management.utils;

import com.esacinc.commons.management.EsacManagementException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public final class EsacManagementUtils {
    public final static String NAME_OBJ_NAME_PROP_NAME = "name";

    private EsacManagementUtils() {
    }

    public static Map<String, Object> mapData(CompositeData data) {
        Set<String> keySet = data.getCompositeType().keySet();
        int numKeys = keySet.size();
        String[] keys = keySet.toArray(new String[numKeys]);
        Object[] values = data.getAll(keys);
        Map<String, Object> dataMap = new LinkedHashMap<>(numKeys);

        for (int a = 0; a < numKeys; a++) {
            dataMap.put(keys[a], values[a]);
        }

        return dataMap;
    }

    public static ObjectName buildObjectName(String objName, String namePropValue) {
        try {
            return new ObjectName(objName, NAME_OBJ_NAME_PROP_NAME, namePropValue);
        } catch (MalformedObjectNameException e) {
            throw new EsacManagementException(String.format("Malformed management bean object name (nameProp=%s): %s", namePropValue, objName), e);
        }
    }

    public static ObjectName buildObjectName(String objName) {
        try {
            return new ObjectName(objName);
        } catch (MalformedObjectNameException e) {
            throw new EsacManagementException(String.format("Malformed management bean object name: %s", objName), e);
        }
    }
}
