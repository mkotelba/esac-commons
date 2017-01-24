package com.esacinc.commons.context.impl;

import com.esacinc.commons.beans.factory.impl.BeanFactoryStringValueResolver;
import com.esacinc.commons.beans.factory.impl.EmbeddedExpressionResolver;
import com.esacinc.commons.beans.utils.EsacBeanPropertyUtils;
import com.esacinc.commons.context.EsacApplicationValue;
import com.esacinc.commons.context.impl.AbstractEsacApplication.EsacWebApplicationValidationGroup;
import com.esacinc.commons.convert.impl.CaseInsensitiveStringToEnumConverterFactory;
import com.esacinc.commons.convert.impl.StringTokenMapConverter;
import com.esacinc.commons.io.EsacCharsets;
import com.esacinc.commons.validation.impl.EsacValidatorFactoryBean;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.beans.PropertyDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

@Order(Ordered.HIGHEST_PRECEDENCE)
public abstract class AbstractBindingApplicationRunListener<T extends AbstractEsacApplication> extends AbstractEsacApplicationRunListener<T> {
    protected static class ApplicationPropertyValues extends MutablePropertyValues {
        protected EmbeddedExpressionResolver embeddedExprResolver;

        private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationPropertyValues.class);

        private final static long serialVersionUID = 0L;

        public ApplicationPropertyValues(EmbeddedExpressionResolver embeddedExprResolver) {
            this.embeddedExprResolver = embeddedExprResolver;
        }

        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        public <T> void addResolvedProperty(String propName, String propValueStr, Class<T> propValueClass) {
            T propValue = null;

            if (!StringUtils.isEmpty(propValueStr)) {
                propValue = this.embeddedExprResolver.resolveExpression(propValueStr, propValueClass);
            }

            this.addPropertyValue(propName, propValue);

            LOGGER.trace(String.format("Added resolved application property (name=%s, value=%s, resolvedValue=%s).", propName, propValueStr, propValue));
        }
    }

    protected final static String PROP_SRCS_PLACEHOLDER_CONFIGURER_BEAN_NAME = "propSrcsPlaceholderConfigurerAppPropsBinding";

    protected AbstractBindingApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
        ConfigurableListableBeanFactory beanFactory = appContext.getBeanFactory();
        beanFactory.registerSingleton(AbstractEsacApplication.BEAN_NAME, this.app);
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
        MutablePropertySources propSrcs = env.getPropertySources();
        propSrcs.addLast(this.app.getPropertySource());

        this.bindApplicationProperties(env);
    }

    protected void bindApplicationProperties(ConfigurableEnvironment env) {
        ClassLoader beanClassLoader = this.app.getClassLoader();
        MessageSource msgSrc = this.buildApplicationPropertiesMessageSource(env);
        ConfigurableListableBeanFactory beanFactory = this.buildApplicationPropertiesBeanFactory(beanClassLoader, env, msgSrc);
        FormattingConversionService convService = this.buildApplicationPropertiesConversionService(beanFactory);
        EmbeddedExpressionResolver embeddedExprResolver = this.buildApplicationPropertiesEmbeddedExpressionResolver(beanFactory);

        this.buildApplicationPropertiesPropertySourcesPlaceholderConfigurer(env, beanFactory);

        ApplicationPropertyValues appPropValues = this.buildApplicationPropertyValues(embeddedExprResolver);
        RelaxedDataBinder appPropsDataBinder = this.buildApplicationPropertiesDataBinder(convService, msgSrc);
        BeanWrapperImpl appPropsWrapper = new BeanWrapperImpl(this.app);
        String appPropsPropName;
        EsacApplicationValue appPropsPropValueAnno;

        for (PropertyDescriptor appPropsPropDesc : EsacBeanPropertyUtils.findAll(appPropsWrapper, null, true, null, null)) {
            appPropsPropName = appPropsPropDesc.getName();

            if (((appPropsPropValueAnno = AnnotationUtils.findAnnotation(appPropsPropDesc.getReadMethod(), EsacApplicationValue.class)) == null)) {
                continue;
            }

            appPropValues.addResolvedProperty(appPropsPropName, appPropsPropValueAnno.value(), appPropsPropDesc.getPropertyType());
        }

        appPropsDataBinder.bind(appPropValues);
        appPropsDataBinder.validate();

        BindingResult appPropsBindingResult = appPropsDataBinder.getBindingResult();

        if (this.app.isWebEnvironment()) {
            appPropsDataBinder.validate(EsacWebApplicationValidationGroup.class);

            appPropsBindingResult.addAllErrors(appPropsDataBinder.getBindingResult());
        }

        if (appPropsBindingResult.hasErrors()) {
            throw new ApplicationContextException("Unable to bind application properties.", new BindException(appPropsBindingResult));
        }
    }

    protected ApplicationPropertyValues buildApplicationPropertyValues(EmbeddedExpressionResolver embeddedExprResolver) {
        return new ApplicationPropertyValues(embeddedExprResolver);
    }

    protected RelaxedDataBinder buildApplicationPropertiesDataBinder(FormattingConversionService convService, MessageSource msgSrc) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(this.app);
        dataBinder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
        dataBinder.setConversionService(convService);
        dataBinder.setIgnoreUnknownFields(false);
        dataBinder.setValidator(this.buildApplicationPropertiesValidator(msgSrc));

        return dataBinder;
    }

    protected EsacValidatorFactoryBean buildApplicationPropertiesValidator(MessageSource msgSrc) {
        EsacValidatorFactoryBean validator = new EsacValidatorFactoryBean();
        validator.setMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(msgSrc)));

        try {
            validator.afterPropertiesSet();
        } catch (Exception e) {
            throw new ApplicationContextException("Unable to initialize application property binding validator.", e);
        }

        return validator;
    }

    protected PropertySourcesPlaceholderConfigurer buildApplicationPropertiesPropertySourcesPlaceholderConfigurer(ConfigurableEnvironment env,
        ConfigurableListableBeanFactory beanFactory) {
        PropertySourcesPlaceholderConfigurer propSrcsPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propSrcsPlaceholderConfigurer.setBeanFactory(beanFactory);
        propSrcsPlaceholderConfigurer.setEnvironment(env);
        propSrcsPlaceholderConfigurer.setFileEncoding(EsacCharsets.UTF_8_NAME);
        beanFactory.registerSingleton(PROP_SRCS_PLACEHOLDER_CONFIGURER_BEAN_NAME, propSrcsPlaceholderConfigurer);

        propSrcsPlaceholderConfigurer.postProcessBeanFactory(beanFactory);

        return propSrcsPlaceholderConfigurer;
    }

    protected EmbeddedExpressionResolver buildApplicationPropertiesEmbeddedExpressionResolver(ConfigurableListableBeanFactory beanFactory) {
        EmbeddedExpressionResolver embeddedExprResolver = new EmbeddedExpressionResolver();
        embeddedExprResolver.setBeanFactory(beanFactory);

        try {
            embeddedExprResolver.afterPropertiesSet();
        } catch (Exception e) {
            throw new ApplicationContextException("Unable to initialize application property binding embedded expression resolver.", e);
        }

        return embeddedExprResolver;
    }

    protected FormattingConversionService buildApplicationPropertiesConversionService(ConfigurableListableBeanFactory beanFactory) {
        FormattingConversionService convService = new DefaultFormattingConversionService(new BeanFactoryStringValueResolver(beanFactory), true);
        convService.addConverter(new StringTokenMapConverter());
        convService.addConverterFactory(new CaseInsensitiveStringToEnumConverterFactory());
        beanFactory.setConversionService(convService);

        return convService;
    }

    protected ConfigurableListableBeanFactory buildApplicationPropertiesBeanFactory(ClassLoader beanClassLoader, ConfigurableEnvironment env,
        MessageSource msgSrc) {
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setBeanClassLoader(beanClassLoader);
        beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanClassLoader));
        beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this.app.getResourceLoader(), env));
        beanFactory.registerSingleton(AbstractEsacApplication.BEAN_NAME, this.app);
        beanFactory.registerSingleton(ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME, env);
        beanFactory.registerSingleton(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, msgSrc);

        return beanFactory;
    }

    protected MessageSource buildApplicationPropertiesMessageSource(ConfigurableEnvironment env) {
        return new PropertyResolvingMessageSource(env);
    }
}
