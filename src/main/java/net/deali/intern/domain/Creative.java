package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.deali.intern.infrastructure.util.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Creative extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long price;
    private String status;
    private LocalDateTime exposureStartDate;
    private LocalDateTime exposureEndDate;

    @OneToMany(mappedBy = "creative")
    private List<CreativeImage> creativeImages;

    @OneToMany(mappedBy = "creative")
    private List<CreativeCount> creativeCounts;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public Creative(String title, Long price, LocalDateTime exposureStartDate, LocalDateTime exposureEndDate) {
        this.title = title;
        this.price = price;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
    }
}
