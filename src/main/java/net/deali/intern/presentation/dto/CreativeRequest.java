package net.deali.intern.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreativeRequest {
    private String title;
    private Long price;
    private LocalDateTime exposureStartDate;
    private LocalDateTime exposureEndDate;
    private MultipartFile images;
}
