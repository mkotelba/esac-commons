package com.esacinc.commons.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import java.util.Map;

public class EsacXmlOutputFactory extends WstxOutputFactory {
    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
