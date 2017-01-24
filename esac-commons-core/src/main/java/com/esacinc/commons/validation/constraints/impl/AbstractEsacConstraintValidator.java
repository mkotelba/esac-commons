package com.esacinc.commons.validation.constraints.impl;

import com.esacinc.commons.validation.constraints.EsacConstraintValidator;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractEsacConstraintValidator<T extends Annotation, U> implements EsacConstraintValidator<T, U> {
    protected T constraintAnno;

    @Override
    public boolean isValid(U value, ConstraintValidatorContext context) {
        return true;
    }

    @Override
    public void initialize(T constraintAnno) {
        this.constraintAnno = constraintAnno;
    }
}
