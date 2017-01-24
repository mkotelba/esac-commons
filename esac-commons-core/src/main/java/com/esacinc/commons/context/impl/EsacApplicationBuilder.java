package com.esacinc.commons.context.impl;

import com.esacinc.commons.context.EsacApplicationConfiguration;
import com.esacinc.commons.context.EsacApplicationCustomizer;
import com.esacinc.commons.io.utils.EsacResourceUtils;
import com.esacinc.commons.io.utils.EsacSpringFactoriesUtils;
import com.esacinc.commons.utils.EsacStringUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.Banner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.BackgroundPreinitializer;
import org.springframework.boot.autoconfigure.logging.AutoConfigurationReportLoggingInitializer;
import org.springframework.boot.builder.ParentContextCloserApplicationListener;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer;
import org.springframework.boot.context.ContextIdApplicationContextInitializer;
import org.springframework.boot.context.FileEncodingApplicationListener;
import org.springframework.boot.context.config.AnsiOutputApplicationListener;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.config.DelegatingApplicationContextInitializer;
import org.springframework.boot.context.config.DelegatingApplicationListener;
import org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener;
import org.springframework.boot.logging.ClasspathLoggingApplicationListener;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.MultiValueMap;

public class EsacApplicationBuilder<T extends AbstractEsacApplication> extends SpringApplicationBuilder {
    @SuppressWarnings({ CompilerWarnings.DEPRECATION })
    protected final static Set<String> DEFAULT_FILTERED_APP_CONTEXT_INITIALIZER_CLASS_NAMES = Stream.concat(
        ClassUtils
            .convertClassesToClassNames(Stream.of(AutoConfigurationReportLoggingInitializer.class, ConfigurationWarningsApplicationContextInitializer.class,
                ContextIdApplicationContextInitializer.class, DelegatingApplicationContextInitializer.class,
                org.springframework.boot.context.web.ServerPortInfoApplicationContextInitializer.class).collect(Collectors.toList()))
            .stream(),
        Stream.of("org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer")).collect(Collectors.toSet());

    protected final static Set<String> DEFAULT_FILTERED_APP_LISTENER_CLASS_NAMES = Stream
        .concat(ClassUtils.convertClassesToClassNames(Stream
            .of(BackgroundPreinitializer.class, ParentContextCloserApplicationListener.class, FileEncodingApplicationListener.class,
                AnsiOutputApplicationListener.class, ConfigFileApplicationListener.class, DelegatingApplicationListener.class,
                LiquibaseServiceLocatorApplicationListener.class, ClasspathLoggingApplicationListener.class, LoggingApplicationListener.class)
            .collect(Collectors.toList())).stream(), Stream.of("org.springframework.boot.ClearCachesApplicationListener"))
        .collect(Collectors.toSet());

    protected EsacApplicationBuilder(T app) {
        super(ArrayUtils.<Object> toArray(app));

        this.addCommandLineProperties(false);
        this.bannerMode(Mode.OFF);
        this.headless(true);

        this.filterInitializers(
            appContextInitializer -> !DEFAULT_FILTERED_APP_CONTEXT_INITIALIZER_CLASS_NAMES.contains(appContextInitializer.getClass().getName()));

        this.filterListeners(appListener -> !DEFAULT_FILTERED_APP_LISTENER_CLASS_NAMES.contains(appListener.getClass().getName()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T extends AbstractEsacApplication> EsacApplicationBuilder<T> forConfigurationClass(Class<?> appConfigClass) {
        String appConfigClassName = appConfigClass.getName(), appPropNamePrefix;
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        MetadataReader appConfigClassMetadataReader;

        try {
            appConfigClassMetadataReader = new SimpleMetadataReaderFactory(resourceLoader).getMetadataReader(appConfigClassName);
        } catch (IOException e) {
            throw new ApplicationContextException(
                String.format("Unable to build application configuration class (name=%s) metadata reader.", appConfigClassName), e);
        }

        MultiValueMap<String, Object> appConfigAnnoAttrs =
            appConfigClassMetadataReader.getAnnotationMetadata().getAllAnnotationAttributes(EsacApplicationConfiguration.class.getName());

        if (appConfigAnnoAttrs == null) {
            throw new ApplicationContextException(
                String.format("Application configuration class (name=%s) is not annotated with %s.", appConfigClassName, EsacApplicationConfiguration.class));
        }

        String appGroupName = ((String) appConfigAnnoAttrs.getFirst(EsacApplicationConfiguration.GROUP_NAME_ATTR_NAME));
        Class<? extends AbstractEsacApplication> appClass =
            ((Class<? extends AbstractEsacApplication>) appConfigAnnoAttrs.getFirst(EsacApplicationConfiguration.APP_CLASS_ATTR_NAME));

        if (StringUtils.isBlank(appGroupName)) {
            throw new ApplicationContextException(
                String.format("Invalid application (class=%s, configClass=%s) group name: %s", appClass, appConfigClassName, appGroupName));
        }

        if ((appPropNamePrefix = ((String) appConfigAnnoAttrs.getFirst(EsacApplicationConfiguration.PROP_NAME_PREFIX_ATTR_NAME))).isEmpty()) {
            appPropNamePrefix = (appGroupName + EsacStringUtils.PERIOD);
        }

        T app;

        try {
            app = ((T) ConstructorUtils.invokeConstructor(appClass, appGroupName, appPropNamePrefix));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new ApplicationContextException(
                String.format("Unable to instantiate application (class=%s, configClass=%s, groupName=%s).", appClass, appConfigClassName, appGroupName), e);
        }

        EsacApplicationBuilder<T> appBuilder = new EsacApplicationBuilder<>(app);
        appBuilder.resourceLoader(resourceLoader);

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        appBuilder.resourcePatternResolver(resourcePatternResolver);

        appBuilder.sourceLocations(((String[]) appConfigAnnoAttrs.getFirst(EsacApplicationConfiguration.SRC_LOCS_ATTR_NAME)));

        appBuilder.propertySourceLocations(((String[]) appConfigAnnoAttrs.getFirst(EsacApplicationConfiguration.PROP_SRC_LOCS_ATTR_NAME)));

        for (EsacApplicationCustomizer<?> appCustomizer : EsacSpringFactoriesUtils.loadFactories(EsacApplicationCustomizer.class,
            resourceLoader.getClassLoader())) {
            ((EsacApplicationCustomizer<T>) appCustomizer).customizeApplication(appBuilder);
        }

        return appBuilder;
    }

    @Override
    public T build() {
        return this.build(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    @Override
    public T build(String ... args) {
        super.build(args);

        return this.application();
    }

    @Override
    public EsacApplicationBuilder<T> child(Object ... srcs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EsacApplicationBuilder<T> sibling(Object ... srcs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EsacApplicationBuilder<T> sibling(Object[] srcs, String ... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EsacApplicationBuilder<T> parent(Object ... srcs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EsacApplicationBuilder<T> parent(ConfigurableApplicationContext parentAppContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EsacApplicationBuilder<T> addCommandLineProperties(boolean addCommandLineProps) {
        super.addCommandLineProperties(addCommandLineProps);

        return this;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T application() {
        return ((T) super.application());
    }

    @Override
    public EsacApplicationBuilder<T> banner(Banner banner) {
        super.banner(banner);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> bannerMode(Mode bannerMode) {
        super.bannerMode(bannerMode);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> beanNameGenerator(BeanNameGenerator beanNameGen) {
        super.beanNameGenerator(beanNameGen);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> contextClass(Class<? extends ConfigurableApplicationContext> appContextClass) {
        super.contextClass(appContextClass);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> environment(ConfigurableEnvironment env) {
        super.environment(env);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> headless(boolean headless) {
        super.headless(headless);

        return this;
    }

    public EsacApplicationBuilder<T> filterInitializers(Predicate<? super ApplicationContextInitializer<?>> appContextInitializerPredicate) {
        this.application().setInitializers(this.application().getInitializers().stream().filter(appContextInitializerPredicate).collect(Collectors.toList()));

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> initializers(ApplicationContextInitializer<?> ... appContextInitializers) {
        super.initializers(appContextInitializers);

        return this;
    }

    public EsacApplicationBuilder<T> filterListeners(Predicate<? super ApplicationListener<?>> appListenerPredicate) {
        this.application().setListeners(this.application().getListeners().stream().filter(appListenerPredicate).collect(Collectors.toList()));

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> listeners(ApplicationListener<?> ... appListeners) {
        super.listeners(appListeners);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> logStartupInfo(boolean logStartupInfo) {
        super.logStartupInfo(logStartupInfo);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> main(Class<?> mainAppClass) {
        super.main(mainAppClass);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> profiles(String ... profiles) {
        super.profiles(profiles);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> properties(String ... defaultProps) {
        super.properties(defaultProps);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> properties(Properties defaultProps) {
        super.properties(defaultProps);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> properties(Map<String, Object> defaultProps) {
        super.properties(defaultProps);

        return this;
    }

    public EsacApplicationBuilder<T> propertySourceLocations(String ... propSrcLocs) {
        if (propSrcLocs.length == 0) {
            return this;
        }

        T app = this.application();
        ResourcePatternResolver resourcePatternResolver = app.getResourcePatternResolver();
        SortedSetMultimap<String, Resource> propSrcResources = TreeMultimap.create(Comparator.naturalOrder(), EsacResourceUtils.LOC_COMPARATOR);

        for (String propSrcLoc : propSrcLocs) {
            try {
                Stream.of(resourcePatternResolver.getResources(propSrcLoc)).forEach(propSrcResource -> propSrcResources.put(propSrcLoc, propSrcResource));
            } catch (IOException e) {
                throw new ApplicationContextException(String.format("Unable to resolve application source (loc=%s) resource(s).", propSrcLoc), e);
            }
        }

        CompositePropertySource propSrc = new CompositePropertySource(AbstractEsacApplication.PROP_SRC_NAME);
        Resource propSrcResource;

        for (Entry<String, Resource> propSrcResourceEntry : propSrcResources.entries()) {
            propSrcResource = propSrcResourceEntry.getValue();

            try {
                propSrc.addFirstPropertySource(
                    new PropertiesPropertySource(propSrcResource.getURI().toString(), PropertiesLoaderUtils.loadProperties(propSrcResource)));
            } catch (IOException e) {
                throw new ApplicationContextException(String.format("Unable to load application property source (loc=%s) resource (fileName=%s, desc=%s).",
                    propSrcResourceEntry.getKey(), propSrcResource.getFilename(), propSrcResource.getDescription()), e);
            }
        }

        app.setPropertySource(propSrc);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> registerShutdownHook(boolean registerShutdownHook) {
        super.registerShutdownHook(registerShutdownHook);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> resourceLoader(ResourceLoader resourceLoader) {
        super.resourceLoader(resourceLoader);

        return this;
    }

    public EsacApplicationBuilder<T> resourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.application().setResourcePatternResolver(resourcePatternResolver);

        return this;
    }

    public EsacApplicationBuilder<T> sourceLocations(String ... srcLocs) {
        if (srcLocs.length == 0) {
            return this;
        }

        T app = this.application();
        ResourcePatternResolver resourcePatternResolver = app.getResourcePatternResolver();
        List<Resource> srcResources = new ArrayList<>();

        for (String srcLoc : srcLocs) {
            try {
                Stream.of(resourcePatternResolver.getResources(srcLoc)).forEach(srcResources::add);
            } catch (IOException e) {
                throw new ApplicationContextException(String.format("Unable to resolve application source (loc=%s) resource(s).", srcLoc), e);
            }
        }

        srcResources.sort(EsacResourceUtils.LOC_COMPARATOR);

        app.getSources().addAll(srcResources);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> sources(Class<?> ... srcs) {
        super.sources(srcs);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> sources(Object ... srcs) {
        super.sources(srcs);

        return this;
    }

    @Override
    public EsacApplicationBuilder<T> web(boolean webEnv) {
        super.web(webEnv);

        return this;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected T createSpringApplication(Object ... srcs) {
        return ((T) srcs[0]);
    }
}
