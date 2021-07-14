package net.deali.intern.presentation.interceptor.validator;

import net.deali.intern.presentation.interceptor.annotation.FutureOrPresent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FutureOrPresentValidator implements ConstraintValidator<FutureOrPresent, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        return !value.isBefore(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))));
    }
}
