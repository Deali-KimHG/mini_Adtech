package net.deali.intern.presentation.interceptor.validator;

import net.deali.intern.presentation.interceptor.annotation.ImageNotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNotEmptyValidator implements ConstraintValidator<ImageNotEmpty, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        return !value.isEmpty();
    }
}
