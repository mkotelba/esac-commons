package com.esacinc.commons.transform.saxon.impl;

import com.esacinc.commons.xml.saxon.impl.EsacDocumentBuilder;
import com.esacinc.commons.xml.saxon.impl.EsacSerializer;
import com.esacinc.commons.xml.xpath.saxon.impl.EsacXpathCompiler;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import net.sf.saxon.s9api.Processor;

public class EsacSaxonProcessor extends Processor {
    public EsacSaxonProcessor(EsacSaxonConfiguration config) {
        super(config);
    }

    @Override
    public EsacDocumentBuilder newDocumentBuilder() {
        return new EsacDocumentBuilder(this.getUnderlyingConfiguration());
    }

    @Override
    public EsacSerializer newSerializer(File outFile) {
        EsacSerializer serializer = this.newSerializer();
        serializer.setOutputFile(outFile);

        return serializer;
    }

    @Override
    public EsacSerializer newSerializer(Writer outWriter) {
        EsacSerializer serializer = this.newSerializer();
        serializer.setOutputWriter(outWriter);

        return serializer;
    }

    @Override
    public EsacSerializer newSerializer(OutputStream outStream) {
        EsacSerializer serializer = this.newSerializer();
        serializer.setOutputStream(outStream);

        return serializer;
    }

    @Override
    public EsacSerializer newSerializer() {
        return new EsacSerializer(this);
    }

    @Override
    public EsacXpathCompiler newXPathCompiler() {
        return new EsacXpathCompiler(this);
    }

    @Override
    public EsacSaxonConfiguration getUnderlyingConfiguration() {
        return ((EsacSaxonConfiguration) super.getUnderlyingConfiguration());
    }
}
