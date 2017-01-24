package com.esacinc.commons.xml.saxon.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import javax.annotation.Nonnegative;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.expr.parser.ExplicitLocation;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import org.apache.commons.lang3.StringUtils;

public class PrettyXmlReceiver extends ProxyReceiver {
    private static class PrettyXmlElement {
        private boolean childElems;

        public boolean getChildElements() {
            return this.childElems;
        }

        public void setChildElements(boolean childElems) {
            this.childElems = childElems;
        }
    }

    private boolean omitDecl;
    private int indentLen;
    private String indentStr;
    private int depth;
    private Deque<PrettyXmlElement> elems = new LinkedList<>();
    private boolean decl;

    public PrettyXmlReceiver(Receiver nextReceiver, boolean omitDecl, @Nonnegative int indentLen) {
        super(nextReceiver);

        this.omitDecl = omitDecl;

        char[] indentChars = new char[(this.indentLen = indentLen)];
        Arrays.fill(indentChars, EsacStringUtils.SPACE_CHAR);

        this.indentStr = new String(indentChars, 0, this.indentLen);
    }

    @Override
    public void characters(CharSequence chars, Location loc, int props) throws XPathException {
        String str = chars.toString().trim();

        if (!str.isEmpty()) {
            super.characters(str, loc, props);
        }
    }

    @Override
    public void endElement() throws XPathException {
        PrettyXmlElement elem = this.elems.pop();

        this.depth--;

        if (elem.getChildElements()) {
            this.writeNewline();
            this.writeIndent();
        }

        super.endElement();
    }

    @Override
    public void startElement(NodeName elemName, SchemaType schemaType, Location loc, int props) throws XPathException {
        boolean elemAvailable = !this.elems.isEmpty();

        if (elemAvailable) {
            this.elems.peek().setChildElements(true);
        }

        if (elemAvailable || this.decl) {
            this.writeNewline();
        }

        this.writeIndent();

        this.depth++;

        super.startElement(elemName, schemaType, loc, props);

        this.elems.push(new PrettyXmlElement());
    }

    @Override
    public void processingInstruction(String name, CharSequence value, Location loc, int props) throws XPathException {
        super.processingInstruction(name, value, loc, props);

        this.writeNewline();
    }

    @Override
    public void startDocument(int props) throws XPathException {
        super.startDocument(props);

        this.decl = !this.omitDecl;
    }

    private void writeNewline() throws XPathException {
        super.characters(StringUtils.LF, ExplicitLocation.UNKNOWN_LOCATION, 0);
    }

    private void writeIndent() throws XPathException {
        for (int a = 0; a < this.depth; a++) {
            super.characters(this.indentStr, ExplicitLocation.UNKNOWN_LOCATION, 0);
        }
    }
}
