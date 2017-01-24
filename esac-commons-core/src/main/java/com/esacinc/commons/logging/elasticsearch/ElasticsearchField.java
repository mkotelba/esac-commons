package com.esacinc.commons.logging.elasticsearch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.lang3.StringUtils;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ElasticsearchField {
    ElasticsearchDatatype datatype() default ElasticsearchDatatype.DEFAULT;

    String format() default StringUtils.EMPTY;

    boolean indexed() default true;
}
