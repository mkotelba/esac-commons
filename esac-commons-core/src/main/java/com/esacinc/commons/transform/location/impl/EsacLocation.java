package com.esacinc.commons.transform.location.impl;

import com.esacinc.commons.beans.LocationBean;
import com.fasterxml.jackson.core.JsonLocation;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.transform.SourceLocator;
import net.sf.saxon.expr.parser.ExplicitLocation;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.tree.AttributeLocation;
import net.sf.saxon.type.Type;
import org.codehaus.stax2.XMLStreamLocation2;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class EsacLocation extends ExplicitLocation implements Location, LocationBean, XMLStreamLocation2 {
    private final static String TO_STR_FORMAT = "publicId=%s, sysId=%s, lineNum=%d, colNum=%d, charOffset=%d, context=%s, elemQname=%s, attrQname=%s";

    private String publicId;
    private String sysId;
    private int lineNum;
    private int colNum;
    private int charOffset;
    private XMLStreamLocation2 context;
    private QName attrQname;
    private QName elemQname;

    public EsacLocation(@Nullable JsonLocation jsonLoc) {
        this();

        this.initializeJsonLocation(jsonLoc);
    }

    public EsacLocation(@Nullable NodeInfo nodeInfo) {
        this(((net.sf.saxon.expr.parser.Location) nodeInfo));

        this.initializeNodeInfo(nodeInfo);
    }

    public EsacLocation(@Nullable net.sf.saxon.expr.parser.Location loc) {
        this(((SourceLocator) loc));
    }

    public EsacLocation(@Nullable SourceLocator srcLocator) {
        this();

        this.initializeSourceLocator(srcLocator);
        this.initializeAttributeLocation(srcLocator);
    }

    public EsacLocation(@Nullable DOMLocator domLocator) {
        this();

        if (domLocator != null) {
            this.initializeNode(domLocator.getRelatedNode());
        }
    }

    public EsacLocation(@Nullable SAXParseException cause) {
        this();

        this.initializeSaxParseException(cause);
    }

    public EsacLocation(@Nullable Locator locator) {
        this();

        this.initializeLocator(locator);
        this.initializeAttributeLocation(locator);
    }

    public EsacLocation(@Nullable Location loc) {
        this();

        this.initializeLocation(loc);
    }

    public EsacLocation() {
        this(null, -1, -1);
    }

    public EsacLocation(@Nullable String sysId, int lineNum, int colNum) {
        this(null, sysId, lineNum, colNum);
    }

    public EsacLocation(@Nullable String publicId, @Nullable String sysId, int lineNum, int colNum) {
        this(publicId, sysId, lineNum, colNum, -1);
    }

    public EsacLocation(@Nullable String publicId, @Nullable String sysId, int lineNum, int colNum, int charOffset) {
        super(null, -1, -1);

        this.publicId = publicId;
        this.sysId = sysId;
        this.lineNum = lineNum;
        this.colNum = colNum;
        this.charOffset = charOffset;
    }

    @Override
    public EsacLocation saveLocation() {
        return this;
    }

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, this.publicId, this.sysId, this.lineNum, this.colNum, this.charOffset, this.context, this.elemQname,
            this.attrQname);
    }

    private void initializeJsonLocation(@Nullable JsonLocation jsonLoc) {
        if (jsonLoc == null) {
            return;
        }

        this.charOffset = ((int) jsonLoc.getCharOffset());
        this.colNum = jsonLoc.getColumnNr();
        this.lineNum = jsonLoc.getLineNr();
    }

    private void initializeNodeInfo(@Nullable NodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        int nodeKind = nodeInfo.getNodeKind();
        NodeInfo elemInfo = null;

        if (nodeKind == Type.ATTRIBUTE) {
            this.attrQname = new QName(nodeInfo.getPrefix(), nodeInfo.getURI(), nodeInfo.getLocalPart());

            elemInfo = nodeInfo.getParent();
        } else if (nodeKind == Type.ELEMENT) {
            elemInfo = nodeInfo;
        }

        if (elemInfo == null) {
            return;
        }

        this.elemQname = new QName(elemInfo.getPrefix(), elemInfo.getURI(), elemInfo.getLocalPart());
    }

    private void initializeSourceLocator(@Nullable SourceLocator srcLocator) {
        if (srcLocator == null) {
            return;
        }

        this.colNum = srcLocator.getColumnNumber();
        this.lineNum = srcLocator.getLineNumber();
        this.publicId = srcLocator.getPublicId();
        this.sysId = srcLocator.getSystemId();

        if (srcLocator instanceof javax.xml.transform.dom.DOMLocator) {
            this.initializeNode(((javax.xml.transform.dom.DOMLocator) srcLocator).getOriginatingNode());
        }
    }

    private void initializeNode(@Nullable Node node) {
        if (node == null) {
            return;
        }

        Element elem = null;

        if (node instanceof Attr) {
            Attr attr = ((Attr) node);

            this.attrQname = new QName(attr.getPrefix(), null, attr.getLocalName());

            elem = attr.getOwnerElement();
        } else if (node instanceof Element) {
            elem = ((Element) node);
        }

        if (elem == null) {
            return;
        }

        this.elemQname = new QName(elem.getPrefix(), null, elem.getLocalName());
    }

    private void initializeSaxParseException(@Nullable SAXParseException cause) {
        if (cause == null) {
            return;
        }

        this.colNum = cause.getColumnNumber();
        this.lineNum = cause.getLineNumber();
        this.publicId = cause.getPublicId();
        this.sysId = cause.getSystemId();
    }

    private void initializeLocator(@Nullable Locator locator) {
        if (locator == null) {
            return;
        }

        this.colNum = locator.getColumnNumber();
        this.lineNum = locator.getLineNumber();
        this.publicId = locator.getPublicId();
        this.sysId = locator.getSystemId();
    }

    private void initializeAttributeLocation(@Nullable Object loc) {
        if ((loc == null) || !(loc instanceof AttributeLocation)) {
            return;
        }

        AttributeLocation attrLoc = ((AttributeLocation) loc);
        StructuredQName nodeQname = attrLoc.getAttributeName();

        if (nodeQname != null) {
            this.attrQname = new QName(nodeQname);
        }

        if ((nodeQname = attrLoc.getElementName()) != null) {
            this.elemQname = new QName(nodeQname);
        }
    }

    private void initializeLocation(@Nullable Location loc) {
        if (loc == null) {
            return;
        }

        this.charOffset = loc.getCharacterOffset();
        this.colNum = loc.getColumnNumber();
        this.lineNum = loc.getLineNumber();
        this.publicId = loc.getPublicId();
        this.sysId = loc.getSystemId();

        if (loc instanceof XMLStreamLocation2) {
            this.context = ((XMLStreamLocation2) loc).getContext();
        }
    }

    public boolean hasAttributeQname() {
        return (this.attrQname != null);
    }

    @Nullable
    public QName getAttributeQname() {
        return this.attrQname;
    }

    public void setAttributeQname(@Nullable QName attrQname) {
        this.attrQname = attrQname;
    }

    public boolean hasCharacterOffset() {
        return (this.charOffset > 0);
    }

    @Override
    public int getCharacterOffset() {
        return this.charOffset;
    }

    public void setCharacterOffset(int charOffset) {
        this.charOffset = charOffset;
    }

    public boolean hasColumnNumber() {
        return (this.colNum > 0);
    }

    @Override
    public int getColumnNumber() {
        return this.colNum;
    }

    public void setColumnNumber(int colNum) {
        this.colNum = colNum;
    }

    public boolean hasContext() {
        return (this.context != null);
    }

    @Nullable
    @Override
    public XMLStreamLocation2 getContext() {
        return this.context;
    }

    public void setContext(@Nullable XMLStreamLocation2 context) {
        this.context = context;
    }

    public boolean hasElementQname() {
        return (this.elemQname != null);
    }

    @Nullable
    public QName getElementQname() {
        return this.elemQname;
    }

    public void setElementQname(@Nullable QName elemQname) {
        this.elemQname = elemQname;
    }

    public boolean hasLineNumber() {
        return (this.lineNum > 0);
    }

    @Override
    public int getLineNumber() {
        return this.lineNum;
    }

    public void setLineNumber(int lineNum) {
        this.lineNum = lineNum;
    }

    public boolean hasPublicId() {
        return (this.publicId != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(@Nullable String publicId) {
        this.publicId = publicId;
    }

    public boolean hasSystemId() {
        return (this.sysId != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        return this.sysId;
    }

    public void setSystemId(@Nullable String sysId) {
        this.sysId = sysId;
    }
}
