package com.esacinc.commons.xml.xpath.saxon.impl;

import com.esacinc.commons.transform.saxon.impl.EsacSaxonController;
import com.esacinc.commons.transform.saxon.impl.EsacSaxonProcessor;
import com.esacinc.commons.utils.EsacIteratorUtils;
import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.xml.xpath.DynamicXpathOptions;
import com.esacinc.commons.xml.xpath.StaticXpathOptions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.sxpath.IndependentContext;
import net.sf.saxon.sxpath.XPathDynamicContext;
import net.sf.saxon.sxpath.XPathExpression;
import net.sf.saxon.sxpath.XPathVariable;
import net.sf.saxon.trans.XPathException;

public class EsacXpathExecutable extends XPathExecutable {
    private EsacSaxonProcessor proc;
    private StaticXpathOptions staticOpts;

    public EsacXpathExecutable(EsacSaxonProcessor proc, @Nullable StaticXpathOptions staticOpts, IndependentContext staticContext, XPathExpression expr) {
        super(expr, proc, staticContext);

        this.proc = proc;
        this.staticOpts = staticOpts;
    }

    @Override
    public EsacXpathSelector load() {
        return this.load(null);
    }

    public EsacXpathSelector load(@Nullable DynamicXpathOptions dynamicOpts) {
        XPathExpression expr = this.getUnderlyingExpression();
        IndependentContext staticContext = this.getUnderlyingStaticContext();
        Map<StructuredQName, XPathVariable> declaredVars = EsacIteratorUtils.stream(staticContext.iterateExternalVariables())
            .collect(EsacStreamUtils.toMap(XPathVariable::getVariableQName, Function.identity(), LinkedHashMap::new));
        boolean staticOptsAvailable = (this.staticOpts != null), dynamicOptsAvailable = (dynamicOpts != null);
        EsacSaxonController controller = new EsacSaxonController(this.proc.getUnderlyingConfiguration());

        if (dynamicOptsAvailable && dynamicOpts.hasContextData()) {
            controller.getContextData().putAll(dynamicOpts.getContextData());
        }

        try {
            // noinspection ConstantConditions
            XPathDynamicContext dynamicContext =
                expr.createDynamicContext(controller, ((dynamicOptsAvailable && dynamicOpts.hasContextItem()) ? dynamicOpts.getContextItem() : null));
            EsacXpathSelector selector = new EsacXpathSelector(expr, this.staticOpts, dynamicOpts, dynamicContext, declaredVars);

            Map<QName, XdmValue> vars = new LinkedHashMap<>();

            if (staticOptsAvailable && this.staticOpts.hasVariables()) {
                vars.putAll(this.staticOpts.getVariables());
            }

            if (dynamicOptsAvailable && dynamicOpts.hasVariables()) {
                vars.putAll(dynamicOpts.getVariables());
            }

            for (QName varQname : vars.keySet()) {
                selector.setVariable(varQname, vars.get(varQname));
            }

            return selector;
        } catch (SaxonApiException | XPathException e) {
            throw new SaxonApiUncheckedException(e);
        }
    }

    public StaticXpathOptions getStaticOptions() {
        return this.staticOpts;
    }

    @Override
    public IndependentContext getUnderlyingStaticContext() {
        return ((IndependentContext) super.getUnderlyingStaticContext());
    }
}
