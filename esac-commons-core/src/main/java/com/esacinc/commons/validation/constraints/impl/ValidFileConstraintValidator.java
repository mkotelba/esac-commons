package com.esacinc.commons.validation.constraints.impl;

import com.esacinc.commons.validation.constraints.ValidFile;
import java.io.File;
import javax.validation.ConstraintValidatorContext;

public class ValidFileConstraintValidator extends AbstractEsacConstraintValidator<ValidFile, File> {
    private boolean constraintDir;

    @Override
    public boolean isValid(File value, ConstraintValidatorContext constraintValidatorContext) {
        if (this.constraintAnno.existing() && !value.exists()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(this.constraintAnno.existingMessage());

            return false;
        }

        boolean valueDir = value.isDirectory();

        if (this.constraintDir) {
            if (!valueDir) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(this.constraintAnno.directoryMessage());

                return false;
            }
        } else {
            if (valueDir) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(this.constraintAnno.fileMessage());

                return false;
            }
        }

        return true;
    }

    @Override
    public void initialize(ValidFile constraintAnno) {
        super.initialize(constraintAnno);
        
        this.constraintDir = this.constraintAnno.directory();
    }
}
