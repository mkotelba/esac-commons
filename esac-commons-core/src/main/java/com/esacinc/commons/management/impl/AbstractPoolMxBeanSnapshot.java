package com.esacinc.commons.management.impl;

import com.esacinc.commons.management.PoolMxBeanSnapshot;
import javax.management.AttributeList;
import javax.management.ObjectName;

public abstract class AbstractPoolMxBeanSnapshot extends AbstractMxBeanSnapshot implements PoolMxBeanSnapshot {
    protected String name;
    protected ObjectName objName;

    protected AbstractPoolMxBeanSnapshot(String name, ObjectName objName, AttributeList attrs) {
        super(attrs);

        this.name = name;
        this.objName = objName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ObjectName getObjectName() {
        return this.objName;
    }
}
