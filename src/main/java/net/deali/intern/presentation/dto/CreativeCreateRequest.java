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
    @NotEmpty(message = "제목을 입력해야 합니다")
    @Length(max = 255, message = "제목이 255자를 넘으면 안됩니다")
    private String title;

    @NotNull(message = "낙찰가를 입력해야 합니다")
    @Min(value = 1, message = "낙찰가가 1원 보다 작으면 안됩니다")
    @Max(value = 10, message = "낙찰가가 10원 보다 크면 안됩니다")
    private Long price;

    @NotNull(message = "광고 시작일시를 입력해야 합니다")
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseStartDate;

    @NotNull(message = "광고 종료일시를 입력해야 합니다")
    @Future(message = "광고 종료일시가 현재 시간이나 과거의 시간이면 안됩니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseEndDate;

    @NotNull(message = "이미지를 등록해야 합니다")
    @ImageNotEmpty
    @Image
    private MultipartFile images;
}
