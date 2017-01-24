package com.esacinc.commons.beans.factory.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component("embeddedExprResolver")
public class EmbeddedExpressionResolver implements BeanFactoryAware, InitializingBean {
    private ConfigurableBeanFactory beanFactory;
    private ConversionService convService;
    private BeanExpressionResolver beanExprResolver;
    private BeanExpressionContext beanExprContext;

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> T resolveExpression(String expr, Class<T> exprValueClass) {
        Object exprValueObj = this.resolveExpression(expr);

        if (exprValueObj == null) {
            return null;
        }

        Class<?> exprValueObjClass = exprValueObj.getClass();

        if (!ClassUtils.isAssignable(exprValueObjClass, exprValueClass)) {
            if (((exprValueObj = this.convService.convert(exprValueObj, exprValueClass)) != null) &&
                !ClassUtils.isAssignable((exprValueObjClass = exprValueObj.getClass()), exprValueClass)) {
                throw new ApplicationContextException(
                    String.format("Resolved embedded expression (%s) value (class=%s) is not assignable to expected class (name=%s).", expr, exprValueObjClass,
                        exprValueClass));
            }
        }

        return ((T) exprValueObj);
    }

    @Nullable
    public Object resolveExpression(String expr) {
        return ((!StringUtils.isEmpty(expr) && !StringUtils.isEmpty((expr = this.beanFactory.resolveEmbeddedValue(expr))))
            ? this.beanExprResolver.evaluate(expr, this.beanExprContext) : expr);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.convService = this.beanFactory.getConversionService();
        this.beanExprResolver = this.beanFactory.getBeanExpressionResolver();
        this.beanExprContext = new BeanExpressionContext(this.beanFactory, null);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((ConfigurableBeanFactory) beanFactory);
    }
}
