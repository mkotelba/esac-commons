package com.esacinc.commons.management;

import java.lang.management.PlatformManagedObject;

public interface MxBeanSnapshot extends PlatformManagedObject {
    public String[] getAttributeNames();
}
