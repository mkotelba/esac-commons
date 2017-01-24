package com.esacinc.commons.xml.saxon.impl;

import com.esacinc.commons.config.EsacOptionValues;
import com.esacinc.commons.config.utils.EsacOptionUtils;
import com.esacinc.commons.transform.impl.ByteArrayResult;
import com.esacinc.commons.transform.saxon.impl.EsacSaxonConfiguration;
import com.esacinc.commons.transform.saxon.impl.EsacPipelineConfiguration;
import com.esacinc.commons.transform.saxon.impl.EsacSaxonProcessor;
import com.esacinc.commons.transform.saxon.impl.EsacPullEventSource;
import com.esacinc.commons.transform.utils.EsacTransformUtils;
import com.esacinc.commons.xml.impl.EsacXmlInputFactory;
import com.esacinc.commons.xml.impl.EsacXmlOutputFactory;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.Sender;
import net.sf.saxon.event.StreamWriterToReceiver;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacSerializer extends Serializer {
    private final static String INDENT_OUT_PROP_KEY = Property.INDENT.getQName().getClarkName();
    private final static String INDENT_SPACES_OUT_PROP_KEY = Property.SAXON_INDENT_SPACES.getQName().getClarkName();
    private final static String OMIT_XML_DECL_OUT_PROP_KEY = Property.OMIT_XML_DECLARATION.getQName().getClarkName();

    @Autowired
    private EsacXmlOutputFactory xmlOutFactory;

    @Autowired
    private EsacXmlInputFactory xmlInFactory;

    public EsacSerializer(EsacSaxonProcessor processor) {
        super(processor);

        this.setDefaultOutputProperties(processor.getUnderlyingConfiguration().getDefaultSerializationProperties());
    }

    public byte[] serialize(Source src, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        return this.serialize(src, new ByteArrayResult(), parseOpts, outProps).getBytes();
    }

    public <T extends Result> T serialize(Source src, T result, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        EsacSaxonConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        EsacPipelineConfiguration pipelineConfig = config.makePipelineConfiguration();

        if (src instanceof StreamSource) {
            StaxToEventBridge srcEventIterator = new StaxToEventBridge();
            srcEventIterator.setPipelineConfiguration(pipelineConfig);

            try {
                srcEventIterator.setXMLStreamReader(this.xmlInFactory.createXMLStreamReader(src));

                src = new EsacPullEventSource(EsacTransformUtils.getPublicId(src), src.getSystemId(), srcEventIterator);
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        Receiver receiver = this.getReceiver(config, pipelineConfig, result, parseOpts, outProps);

        try {
            Sender.send(src, receiver, null);

            receiver.close();

            return result;
        } catch (XPathException e) {
            throw new SaxonApiException(String.format("Unable to serialize source (class=%s, sysId=%s) to result (class=%s, sysId=%s).",
                src.getClass().getName(), src.getSystemId(), result.getClass().getName(), result.getSystemId()), e);
        }
    }

    @Override
    public StreamWriterToReceiver getXMLStreamWriter() throws SaxonApiException {
        EsacSaxonConfiguration config = this.getProcessor().getUnderlyingConfiguration();

        return this.getXMLStreamWriter(config, config.makePipelineConfiguration(), this.getResult(), null, null);
    }

    public StreamWriterToReceiver getXMLStreamWriter(EsacSaxonConfiguration config, EsacPipelineConfiguration pipelineConfig, Result result,
        @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        return new StreamWriterToReceiver(this.getReceiver(config, pipelineConfig, result, parseOpts, outProps));
    }

    @Override
    public Receiver getReceiver(Configuration config) throws SaxonApiException {
        return this.getReceiver(config.makePipelineConfiguration());
    }

    @Override
    public Receiver getReceiver(PipelineConfiguration pipelineConfig) throws SaxonApiException {
        return this.getReceiver(((EsacSaxonConfiguration) pipelineConfig.getConfiguration()), ((EsacPipelineConfiguration) pipelineConfig), this.getResult(), null,
            null);
    }

    public Receiver getReceiver(EsacSaxonConfiguration config, EsacPipelineConfiguration pipelineConfig, Result result, @Nullable ParseOptions parseOpts,
        @Nullable Properties outProps) throws SaxonApiException {
        boolean pretty = EsacOptionUtils.getBooleanValue((outProps = this.mergeOutputProperties(outProps)), INDENT_OUT_PROP_KEY, false);
        int indentLen = EsacOptionUtils.getIntegerValue(outProps, INDENT_SPACES_OUT_PROP_KEY, -1);

        outProps.setProperty(INDENT_OUT_PROP_KEY, EsacOptionValues.NO);

        if (result instanceof StreamResult) {
            try {
                result = new StAXResult(this.xmlOutFactory.createXMLStreamWriter(result));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        Receiver receiver;

        try {
            receiver = config.getSerializerFactory().getReceiver(result, pipelineConfig, outProps);
        } catch (XPathException e) {
            throw new SaxonApiException(
                String.format("Unable to build Saxon serializer receiver for result (class=%s, sysId=%s).", result.getClass().getName(), result.getSystemId()),
                e);
        }

        if (receiver.getSystemId() == null) {
            receiver.setSystemId(result.getSystemId());
        }

        if (pretty) {
            (receiver = new PrettyXmlReceiver(receiver, EsacOptionUtils.getBooleanValue(outProps, OMIT_XML_DECL_OUT_PROP_KEY, false), indentLen))
                .setPipelineConfiguration(pipelineConfig);
        }

        ParseOptions pipelineParseOpts = pipelineConfig.getParseOptions();

        if (parseOpts != null) {
            pipelineParseOpts.merge(parseOpts);
        }

        List<FilterFactory> parseFilters = pipelineParseOpts.getFilters();

        if (!CollectionUtils.isEmpty(parseFilters)) {
            for (int a = (parseFilters.size() - 1); a >= 0; a--) {
                receiver = parseFilters.get(a).makeFilter(receiver);
            }

            parseFilters.clear();
        }

        return receiver;
    }

    private Properties mergeOutputProperties(@Nullable Properties outProps) {
        Properties mergedOutProps = super.getCombinedOutputProperties();

        if (outProps != null) {
            mergedOutProps.putAll(EsacTransformUtils.buildOutputProperties(outProps));
        }

        return mergedOutProps;
    }

    @Override
    public Properties getLocalOutputProperties() {
        return super.getLocalOutputProperties();
    }

    public void setLocalOutputProperties(Properties localOutProps) {
        EsacTransformUtils.buildOutputProperties(localOutProps).forEach(
            (localOutPropKey, localOutPropValue) -> this.setOutputProperty(new QName(((StructuredQName) localOutPropKey)), ((String) localOutPropValue)));
    }

    @Override
    public EsacSaxonProcessor getProcessor() {
        return ((EsacSaxonProcessor) super.getProcessor());
    }
}
