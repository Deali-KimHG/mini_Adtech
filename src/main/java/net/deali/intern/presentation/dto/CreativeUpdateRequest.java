package net.deali.intern.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.deali.intern.presentation.interceptor.annotation.FutureOrPresent;
import net.deali.intern.presentation.interceptor.annotation.Image;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreativeUpdateRequest {
    @Length(max = 255, message = "제목이 255자를 넘으면 안됩니다")
    private String title;

    @Min(value = 1, message = "낙찰가가 1원 이하면 안됩니다")
    @Max(value = 10, message = "낙찰가가 10원 이상이면 안됩니다")
    private Long price;

    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseStartDate;

    @FutureOrPresent(message = "광고 종료일시가 과거의 시간이면 안됩니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime advertiseEndDate;

    @Image
    private MultipartFile images;
}
