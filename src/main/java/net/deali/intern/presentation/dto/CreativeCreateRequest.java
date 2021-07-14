package net.deali.intern.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.deali.intern.presentation.interceptor.annotation.FutureOrPresent;
import net.deali.intern.presentation.interceptor.annotation.Image;
import net.deali.intern.presentation.interceptor.annotation.ImageNotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreativeCreateRequest {
    @NotEmpty
    @Length(max = 255, message = "255자를 넘으면 안됩니다")
    private String title;

    @NotNull
    @Min(value = 1, message = "1원 이하면 안됩니다")
    @Max(value = 10, message = "10원 이상이면 안됩니다")
    private Long price;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseStartDate;

    @NotNull
    @Future(message = "현재 시간이나 과거의 시간이면 안됩니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseEndDate;

    @NotNull
    @ImageNotEmpty
    @Image
    private MultipartFile images;
}
