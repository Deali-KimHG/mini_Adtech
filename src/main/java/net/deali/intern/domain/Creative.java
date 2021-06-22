package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.deali.intern.infrastructure.util.BaseTimeEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
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

    public void saveImageToLocal(MultipartFile file) {
        // TODO: 상대경로로 변경
        File dest = new File("~/IdeaProject/intern/" + this.id + File.separator + file.getOriginalFilename());

        // TODO: 좀 더 자세한 예외처리
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
