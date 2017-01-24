package com.esacinc.commons.test.context;

import com.esacinc.commons.test.context.impl.EsacApplicationTestContextBootstrapper;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@BootstrapWith(EsacApplicationTestContextBootstrapper.class)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@Target({ ElementType.TYPE })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class }, inheritListeners = false)
public @interface EsacApplicationTest {
    public final static String CLASSES_ATTR_NAME = "classes";
    public final static String WEB_ENV_ATTR_NAME = "webEnvironment";

    @AliasFor(annotation = SpringBootTest.class, attribute = CLASSES_ATTR_NAME)
    Class<?>[] classes() default {};

    @AliasFor(annotation = SpringBootTest.class, attribute = WEB_ENV_ATTR_NAME)
    WebEnvironment webEnvironment() default WebEnvironment.NONE;
}
