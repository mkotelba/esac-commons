package com.esacinc.commons.validation.constraints;

import com.esacinc.commons.validation.constraints.impl.ValidFileConstraintValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = { ValidFileConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@NotNull
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface ValidFile {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
    public @interface List {
        ValidFile[] value();
    }

    boolean directory() default false;

    String directoryMessage() default "{esac-commons.msg.validation.file.valid.dir}";

    boolean existing() default true;

    String existingMessage() default "{esac-commons.msg.validation.file.valid.existing}";

    String fileMessage() default "{esac-commons.msg.validation.file.valid.file}";

    String message() default "{esac-commons.msg.validation.file.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
