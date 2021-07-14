package net.deali.intern.presentation.interceptor.validator;

import net.deali.intern.presentation.interceptor.annotation.Image;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        if(value.isEmpty())
            return true;
        return Objects.equals(StringUtils.getFilenameExtension(value.getOriginalFilename()), "jpg")
                || Objects.equals(StringUtils.getFilenameExtension(value.getOriginalFilename()), "jpeg")
                || Objects.equals(StringUtils.getFilenameExtension(value.getOriginalFilename()), "png");
    }
}
