package com.esacinc.commons.xml;

import com.esacinc.commons.xml.saxon.impl.XdmDocument;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ExtensionFunctionDefinition;

public interface StaticXmlTransformOptions<T extends StaticXmlTransformOptions<T>> extends XmlTransformOptions<T> {
    public T addFunction(ExtensionFunctionDefinition funcDef);

    public boolean hasFunctions();

    public List<ExtensionFunctionDefinition> getFunctions();

    public T setFunctions(@Nullable List<ExtensionFunctionDefinition> funcs);

    public T addNamespace(String prefix, String uri);

    public boolean hasNamespaces();

    public Map<String, String> getNamespaces();

    public T setNamespaces(@Nullable Map<String, String> namespaces);

    public T addPooledDocument(XdmDocument pooledDoc);

    public boolean hasPooledDocuments();

    public List<XdmDocument> getPooledDocuments();

    public T setPooledDocuments(@Nullable List<XdmDocument> pooledDocs);
}
