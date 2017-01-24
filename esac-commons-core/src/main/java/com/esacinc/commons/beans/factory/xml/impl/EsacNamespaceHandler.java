package com.esacinc.commons.beans.factory.xml.impl;

import com.esacinc.commons.beans.factory.xml.EsacBeanDefinitionParser;
import com.esacinc.commons.io.utils.EsacSpringFactoriesUtils;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.ClassUtils;

public class EsacNamespaceHandler extends NamespaceHandlerSupport {
    private Map<String, EsacBeanDefinitionParser> beanDefParsers = new TreeMap<>();

    @Override
    public void init() {
        for (EsacBeanDefinitionParser beanDefParser : EsacSpringFactoriesUtils.loadFactories(EsacBeanDefinitionParser.class, ClassUtils.getDefaultClassLoader(),
            this)) {
            for (String beanDefParserElemLocalName : beanDefParser.getElementBeanClasses().keySet()) {
                this.beanDefParsers.put(beanDefParserElemLocalName, beanDefParser);

                this.registerBeanDefinitionParser(beanDefParserElemLocalName, beanDefParser);
            }
        }
    }

    public Map<String, EsacBeanDefinitionParser> getBeanDefinitionParsers() {
        return this.beanDefParsers;
    }
}
