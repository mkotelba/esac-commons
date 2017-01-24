package com.esacinc.commons.context;

import com.esacinc.commons.context.impl.AbstractEsacApplication;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationUtils;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@SpringBootConfiguration
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface EsacApplicationConfiguration {
    public final static String APP_CLASS_ATTR_NAME = "applicationClass";
    public final static String BASE_PKGS_ATTR_NAME = "basePackages";
    public final static String GROUP_NAME_ATTR_NAME = "groupName";
    public final static String PROP_NAME_PREFIX_ATTR_NAME = "propertyNamePrefix";
    public final static String PROP_SRC_LOCS_ATTR_NAME = "propertySourceLocations";
    public final static String SRC_LOCS_ATTR_NAME = "sourceLocations";

    Class<? extends AbstractEsacApplication> applicationClass();

    String groupName();

    String propertyNamePrefix() default StringUtils.EMPTY;

    String[] propertySourceLocations();

    String[] sourceLocations();

    @AliasFor(annotation = Configuration.class, attribute = AnnotationUtils.VALUE)
    String value() default "appConfiguration";
}
