package com.esacinc.commons.validation.constraints;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;

public interface EsacConstraintValidator<T extends Annotation, U> extends ConstraintValidator<T, U> {
}
