package net.deali.intern.presentation.interceptor.annotation;

import net.deali.intern.presentation.interceptor.validator.ImageValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {
    String message() default "이미지가 다른 확장자 파일이면 안됩니다\n\t(.jpg, .jpeg, .png만 가능합니다)";
    Class[] groups() default {};
    Class[] payload() default {};
}
