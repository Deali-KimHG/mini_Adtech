package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Document("exposure")
@Getter
@NoArgsConstructor
public class Exposure {
    @Id
    private String id;

    private String title;
    private String image;
    private Long price;
    private Long creativeId;
    private LocalDateTime exposureStartDate;
    private LocalDateTime exposureEndDate;

    @Builder
    public Exposure(String title, String image, Long price, Long creativeId, LocalDateTime exposureStartDate, LocalDateTime exposureEndDate) {
        this.title = title;
        this.image = image;
        this.price = price;
        this.creativeId = creativeId;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
    }

    public Exposure(Creative creative) {
        this.title = creative.getTitle();
        this.image = creative.getCreativeImages().get(0).getName();
        this.price = creative.getPrice();
        this.creativeId = creative.getId();
        this.exposureStartDate = creative.getExposureStartDate();
        this.exposureEndDate = creative.getExposureEndDate();
    }

    public void updateExposure(Creative creative) {
        this.title = creative.getTitle();
        this.image = creative.getCreativeImages().get(0).getName();
        this.price = creative.getPrice();
        this.exposureStartDate = creative.getExposureStartDate();
        this.exposureEndDate = creative.getExposureEndDate();
    }
}
