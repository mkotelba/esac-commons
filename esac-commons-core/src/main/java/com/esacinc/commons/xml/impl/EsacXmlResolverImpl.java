package com.esacinc.commons.xml.impl;

import com.esacinc.commons.EsacException;
import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.transform.ResourceSourceResolver;
import com.esacinc.commons.transform.impl.ByteArraySource;
import com.esacinc.commons.xml.EsacXmlResolver;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EsacXmlResolverImpl implements EsacXmlResolver {
    private final static String XML_SCHEMA_FILE_LOC = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/schemas/wsdl/xml." + EsacFileNameExtensions.XSD;

    private ResourceSourceResolver resourceSrcResolver;
    private URI defaultBaseUri;
    private boolean resolve;
    private Map<String, ByteArraySource> nsUriStaticSrcs = new HashMap<>();
    private Map<String, ByteArraySource> publicIdStaticSrcs = new HashMap<>();
    private Map<String, ByteArraySource> sysIdStaticSrcs = new HashMap<>();

    public EsacXmlResolverImpl(ResourceSourceResolver resourceSrcResolver, boolean resolve) {
        this(resourceSrcResolver, URI.create((ResourceUtils.FILE_URL_PREFIX + SystemUtils.USER_DIR)), resolve);
    }

    public EsacXmlResolverImpl(ResourceSourceResolver resourceSrcResolver, URI defaultBaseUri, boolean resolve) {
        this(resourceSrcResolver, defaultBaseUri, resolve, null, null, null);

        try {
            this.addStaticSource(XMLConstants.XML_NS_URI, XMLConstants.XML_NS_URI, null, this.resourceSrcResolver.resolve(XML_SCHEMA_FILE_LOC));
        } catch (IOException e) {
            throw new EsacException(String.format("Unable to resolve XML schema file resource (loc=%s)", XML_SCHEMA_FILE_LOC), e);
        }
    }

    private EsacXmlResolverImpl(ResourceSourceResolver resourceSrcResolver, URI defaultBaseUri, boolean resolve,
        @Nullable Map<String, ByteArraySource> nsUriStaticSrcs, @Nullable Map<String, ByteArraySource> publicIdStaticSrcs,
        @Nullable Map<String, ByteArraySource> sysIdStaticSrcs) {
        this.resourceSrcResolver = resourceSrcResolver;
        this.defaultBaseUri = defaultBaseUri;
        this.resolve = resolve;

        if (nsUriStaticSrcs != null) {
            this.nsUriStaticSrcs.putAll(nsUriStaticSrcs);
        }

        if (publicIdStaticSrcs != null) {
            this.publicIdStaticSrcs.putAll(publicIdStaticSrcs);
        }

        if (sysIdStaticSrcs != null) {
            this.sysIdStaticSrcs.putAll(sysIdStaticSrcs);
        }
    }

    @Nullable
    @Override
    public Source resolve(String href, @Nullable String baseUri) throws XPathException {
        return this.resolve(null, null, baseUri, null, href);
    }

    @Nullable
    @Override
    public LSInput resolveResource(String name, String nsUri, @Nullable String publicId, @Nullable String sysId, @Nullable String baseUri) {
        return this.resolve(nsUri, name, baseUri, publicId, sysId);
    }

    @Nullable
    @Override
    public InputSource resolveEntity(@Nullable String publicId, String sysId) throws IOException, SAXException {
        ByteArraySource src = this.resolve(null, null, null, publicId, sysId);

        return ((src != null) ? src.getInputSource() : null);
    }

    @Nullable
    @Override
    public Source resolveEntity(@Nullable String publicId, String sysId, @Nullable String baseUri, @Nullable String nsUri) {
        return this.resolve(nsUri, null, baseUri, publicId, sysId);
    }

    @Nullable
    @Override
    public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId, @Nullable String sysId) {
        return this.resolve(this.resolve, nsUri, name, baseUri, publicId, sysId);
    }

    @Override
    public void addStaticSource(@Nullable String nsUri, @Nullable String publicId, @Nullable String sysId, ByteArraySource src) {
        if (nsUri != null) {
            this.nsUriStaticSrcs.put(nsUri, src);
        }

        if (publicId != null) {
            this.publicIdStaticSrcs.put(publicId, src);
        }

        if (sysId != null) {
            this.sysIdStaticSrcs.put(sysId, src);
        }
    }

    @Nullable
    private ByteArraySource resolve(boolean resolve, @Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId,
        @Nullable String sysId) {
        boolean sysIdAvailable = (sysId != null);

        if (sysIdAvailable && this.sysIdStaticSrcs.containsKey(sysId)) {
            return this.sysIdStaticSrcs.get(sysId);
        } else if ((publicId != null) && this.publicIdStaticSrcs.containsKey(publicId)) {
            return this.publicIdStaticSrcs.get(publicId);
        } else if ((nsUri != null) && this.nsUriStaticSrcs.containsKey(nsUri)) {
            return this.nsUriStaticSrcs.get(nsUri);
        } else if (sysIdAvailable && resolve) {
            try {
                return this.resourceSrcResolver.resolve(((baseUri != null) ? URI.create(baseUri) : this.defaultBaseUri).resolve(sysId).toString());
            } catch (IOException e) {
                throw new EsacException(
                    String.format("Unable to resolve source (nsUri=%s, name=%s, baseUri=%s, publicId=%s, sysId=%s).", nsUri, name, baseUri, publicId, sysId),
                    e);
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public EsacXmlResolver clone() {
        return new EsacXmlResolverImpl(this.resourceSrcResolver, this.defaultBaseUri, this.resolve, this.nsUriStaticSrcs, this.publicIdStaticSrcs,
            this.sysIdStaticSrcs);
    }
}
