package net.deali.intern.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreativeRequest {
    private String title;
    private Long price;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime exposureStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime exposureEndDate;

    private MultipartFile images;

    @Override
    public String toString() {
        return "CreativeRequest [Title: " + title + ", Price: " + price + ", startDate: " + exposureStartDate + ", endDate: " + exposureEndDate + ", images: " + (images == null ? null : images.getOriginalFilename()) + "]";
    }
}
