package com.esacinc.commons.xml.saxon.impl;

import com.esacinc.commons.transform.saxon.impl.EsacSaxonController;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;

public abstract class AbstractEsacExtensionFunction extends ExtensionFunctionDefinition {
    protected class EsacExtensionFunctionCall extends ExtensionFunctionCall {
        @Override
        public Sequence call(XPathContext context, Sequence[] args) throws XPathException {
            try {
                return AbstractEsacExtensionFunction.this.callInternal(context, ((EsacSaxonController) context.getController()).getContextData(), args);
            } catch (Exception e) {
                throw new XPathException(String.format("Unable to call extension function (qname=%s, resultType=%s, argTypes=[%s]): args=[%s]",
                    AbstractEsacExtensionFunction.this.qname, AbstractEsacExtensionFunction.this.resultType,
                    Stream.of(AbstractEsacExtensionFunction.this.argTypes).map(Object::toString).collect(Collectors.joining(", ")),
                    Stream.of(args).map(Object::toString).collect(Collectors.joining(", "))), e);
            }
        }
    }

    protected StructuredQName qname;
    protected SequenceType resultType;
    protected SequenceType[] argTypes;

    protected AbstractEsacExtensionFunction(StructuredQName qname, SequenceType resultType, SequenceType ... argTypes) {
        this.qname = qname;
        this.resultType = resultType;
        this.argTypes = argTypes;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new EsacExtensionFunctionCall();
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgTypes) {
        return this.resultType;
    }

    protected abstract Sequence callInternal(XPathContext context, Map<Object, Object> contextData, Sequence ... args) throws Exception;

    @Override
    public SequenceType[] getArgumentTypes() {
        return this.argTypes;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return this.qname;
    }

    @Nonnegative
    @Override
    public int getMaximumNumberOfArguments() {
        return this.argTypes.length;
    }

    @Nonnegative
    @Override
    public int getMinimumNumberOfArguments() {
        return this.argTypes.length;
    }
}
