package net.deali.intern.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.deali.intern.domain.Advertisement;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdvertisementResponse {
    private String title;
    private String image;
    private Long price;
    private Long creativeId;
    private LocalDateTime advertiseStartDate;
    private LocalDateTime advertiseEndDate;
    private LocalDateTime updatedDate;
    private Double score;

    public AdvertisementResponse(Advertisement advertisement, double score) {
        this.title = advertisement.getTitle();
        this.image = advertisement.getImage();
        this.price = advertisement.getPrice();
        this.creativeId = advertisement.getCreativeId();
        this.advertiseStartDate = advertisement.getAdvertiseStartDate();
        this.advertiseEndDate = advertisement.getAdvertiseEndDate();
        this.updatedDate = advertisement.getUpdatedDate();
        this.score = score;
    }
}
