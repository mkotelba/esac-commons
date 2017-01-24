package com.esacinc.commons.xml.impl;

import com.ctc.wstx.stax.WstxInputFactory;
import java.util.Map;

public class EsacXmlInputFactory extends WstxInputFactory {
    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
