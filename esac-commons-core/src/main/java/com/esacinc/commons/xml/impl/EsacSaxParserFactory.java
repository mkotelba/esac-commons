package com.esacinc.commons.xml.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import com.ctc.wstx.sax.WstxSAXParserFactory;

public class EsacSaxParserFactory extends WstxSAXParserFactory {
    public EsacSaxParserFactory(EsacXmlInputFactory xmlInFactory) {
        super(xmlInFactory);
    }

    @Override
    public WstxSAXParser newSAXParser() {
        return ((WstxSAXParser) super.newSAXParser());
    }
}
