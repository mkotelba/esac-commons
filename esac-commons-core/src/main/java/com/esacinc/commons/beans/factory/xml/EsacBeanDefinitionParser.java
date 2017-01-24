package com.esacinc.commons.beans.factory.xml;

import java.util.Map;
import javax.annotation.Nullable;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.w3c.dom.Element;

public interface EsacBeanDefinitionParser extends BeanDefinitionParser {
    @Nullable
    public String getBeanClassName(Element elem);

    public boolean hasBeanClass(Element elem);

    @Nullable
    public Class<?> getBeanClass(Element elem);

    public Map<String, Class<?>> getElementBeanClasses();
}
