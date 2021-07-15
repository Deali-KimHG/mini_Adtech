package net.deali.intern.presentation.interceptor.annotation;

import net.deali.intern.presentation.interceptor.validator.ImageNotEmptyValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageNotEmptyValidator.class)
public @interface ImageNotEmpty {
    String message() default "이미지를 등록해야 합니다";
    Class[] groups() default {};
    Class[] payload() default {};
}
