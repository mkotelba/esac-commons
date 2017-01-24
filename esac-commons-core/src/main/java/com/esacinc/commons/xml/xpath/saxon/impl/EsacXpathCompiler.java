package com.esacinc.commons.xml.xpath.saxon.impl;

import com.esacinc.commons.transform.saxon.impl.EsacSaxonProcessor;
import com.esacinc.commons.xml.xpath.StaticXpathOptions;
import com.esacinc.commons.xml.xpath.impl.DynamicXpathOptionsImpl;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.IntegratedFunctionLibrary;
import net.sf.saxon.om.Item;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.sxpath.IndependentContext;
import net.sf.saxon.sxpath.XPathEvaluator;
import net.sf.saxon.trans.XPathException;

public class EsacXpathCompiler extends XPathCompiler {
    public final static String BEAN_NAME = "xpathCompiler";

    public final static String COMPILE_METHOD_NAME = "compile";

    public EsacXpathCompiler(EsacSaxonProcessor proc) {
        super(proc);
    }

    @Override
    public XdmValue evaluate(String exprStr, @Nullable XdmItem contextItem) throws SaxonApiException {
        // noinspection ConstantConditions
        return this.compile(exprStr).load(new DynamicXpathOptionsImpl().setContextItem(((Item) contextItem.getUnderlyingValue()))).evaluate();
    }

    @Nullable
    @Override
    public XdmItem evaluateSingle(String exprStr, @Nullable XdmItem contextItem) throws SaxonApiException {
        // noinspection ConstantConditions
        return this.compile(exprStr).load(new DynamicXpathOptionsImpl().setContextItem(((Item) contextItem.getUnderlyingValue()))).evaluateSingle();
    }

    @Override
    public EsacXpathExecutable compile(String exprStr) throws SaxonApiException {
        return this.compile(exprStr, null);
    }

    public EsacXpathExecutable compile(String exprStr, @Nullable StaticXpathOptions staticOpts) throws SaxonApiException {
        EsacSaxonProcessor proc = this.getProcessor();

        IndependentContext staticContext = new IndependentContext(this.getUnderlyingStaticContext());
        staticContext.setAllowUndeclaredVariables(true);

        if (staticOpts != null) {
            if (staticOpts.hasNamespaces()) {
                staticOpts.getNamespaces().forEach(staticContext::declareNamespace);
            }

            if (staticOpts.hasFunctions()) {
                IntegratedFunctionLibrary staticFuncLib = new IntegratedFunctionLibrary();

                staticOpts.getFunctions().forEach(staticFuncLib::registerFunction);

                ((FunctionLibraryList) staticContext.getFunctionLibrary()).addFunctionLibrary(staticFuncLib);
            }

            if (staticOpts.hasVariables()) {
                Map<QName, XdmValue> staticVars = staticOpts.getVariables();

                for (QName staticVarQname : staticVars.keySet()) {
                    staticContext.declareVariable(staticVarQname.getStructuredQName());
                }
            }
        }

        XPathEvaluator evaluator = new XPathEvaluator(proc.getUnderlyingConfiguration());
        evaluator.setStaticContext(staticContext);

        try {
            return new EsacXpathExecutable(proc, staticOpts, staticContext, evaluator.createExpression(exprStr));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public EsacSaxonProcessor getProcessor() {
        return ((EsacSaxonProcessor) super.getProcessor());
    }

    @Override
    public IndependentContext getUnderlyingStaticContext() {
        return ((IndependentContext) super.getUnderlyingStaticContext());
    }
}
