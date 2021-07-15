package net.deali.intern.presentation.interceptor.annotation;

import net.deali.intern.presentation.interceptor.validator.FutureOrPresentValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureOrPresentValidator.class)
public @interface FutureOrPresent {
    String message() default "광고 시작일시가 과거의 시간이면 안됩니다";
    Class[] groups() default {};
    Class[] payload() default {};
}
