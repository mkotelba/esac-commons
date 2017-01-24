package com.esacinc.commons.xml;

import com.esacinc.commons.config.EsacOptions;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmValue;

public interface XmlTransformOptions<T extends XmlTransformOptions<T>> extends EsacOptions<T> {
    public T addVariable(QName qname, XdmValue value);

    public boolean hasVariables();

    public Map<QName, XdmValue> getVariables();

    public T setVariables(@Nullable Map<QName, XdmValue> vars);
}
