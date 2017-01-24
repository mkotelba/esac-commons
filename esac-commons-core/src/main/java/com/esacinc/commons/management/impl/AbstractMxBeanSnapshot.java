package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.management.MxBeanSnapshot;
import java.util.HashMap;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;

public abstract class AbstractMxBeanSnapshot implements MxBeanSnapshot {
    public final static String MEM_ATTR_NAME_PREFIX = "Memory";
    public final static String TOTAL_ATTR_NAME_PREFIX = "Total";

    public final static String TIME_ATTR_NAME_SUFFIX = "Time";

    public final static String COUNT_ATTR_NAME = "Count";
    public final static String NAME_ATTR_NAME = "Name";
    public final static String USAGE_ATTR_NAME = "Usage";
    public final static String VERSION_ATTR_NAME = "Version";

    protected AbstractMxBeanSnapshot(AttributeList attrs) {
        this.buildAttributes(
            attrs.asList().stream().collect(EsacStreamUtils.toMap(Attribute::getName, Attribute::getValue, () -> new HashMap<>(attrs.size()))));
    }

    protected abstract void buildAttributes(Map<String, Object> attrs);
}
