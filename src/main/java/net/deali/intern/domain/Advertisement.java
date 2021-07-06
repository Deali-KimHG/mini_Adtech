package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document("advertisement")
@Getter
@NoArgsConstructor
public class Advertisement {
    @Id
    private String id;

    private String title;
    private String image;
    private Long price;
    private Long creativeId;
    private LocalDateTime advertiseStartDate;
    private LocalDateTime advertiseEndDate;
    private LocalDateTime updatedDate;

    @Builder
    public Advertisement(String title, String image, Long price, Long creativeId, LocalDateTime advertiseStartDate, LocalDateTime advertiseEndDate, LocalDateTime updatedDate) {
        this.title = title;
        this.image = image;
        this.price = price;
        this.creativeId = creativeId;
        this.advertiseStartDate = advertiseStartDate;
        this.advertiseEndDate = advertiseEndDate;
        this.updatedDate = updatedDate;
    }

    public Advertisement(Creative creative) {
        this.title = creative.getTitle();
        this.image = creative.getCreativeImages().get(0).getName();
        this.price = creative.getPrice();
        this.creativeId = creative.getId();
        this.advertiseStartDate = creative.getAdvertiseStartDate();
        this.advertiseEndDate = creative.getAdvertiseEndDate();
        this.updatedDate = creative.getUpdatedDate();
    }

    public void updateAdvertisement(Creative creative) {
        this.title = creative.getTitle();
        this.image = creative.getCreativeImages().get(0).getName();
        this.price = creative.getPrice();
        this.advertiseStartDate = creative.getAdvertiseStartDate();
        this.advertiseEndDate = creative.getAdvertiseEndDate();
        this.updatedDate = creative.getUpdatedDate();
    }
}
