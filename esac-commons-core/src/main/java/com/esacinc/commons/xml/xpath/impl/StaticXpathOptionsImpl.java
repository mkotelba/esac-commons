package com.esacinc.commons.xml.xpath.impl;

import com.esacinc.commons.xml.impl.AbstractStaticXmlTransformOptions;
import com.esacinc.commons.xml.xpath.StaticXpathOptions;

public class StaticXpathOptionsImpl extends AbstractStaticXmlTransformOptions<StaticXpathOptions> implements StaticXpathOptions {
    private final static long serialVersionUID = 0L;

    public StaticXpathOptionsImpl() {
        super();
    }

    @Override
    protected StaticXpathOptions cloneInternal() {
        return new StaticXpathOptionsImpl();
    }
}
