package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.deali.intern.infrastructure.util.BaseTimeEntity;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.util.StringUtils;
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
    private CreativeStatus status = CreativeStatus.WAITING;
    private LocalDateTime exposureStartDate;
    private LocalDateTime exposureEndDate;

    @OneToMany(mappedBy = "creative")
    private List<CreativeImage> creativeImages;

    @OneToMany(mappedBy = "creative")
    private List<CreativeCount> creativeCounts;

    @Builder
    public Creative(String title, Long price, LocalDateTime exposureStartDate, LocalDateTime exposureEndDate) {
        this.title = title;
        this.price = price;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
    }

    // TODO: 옳은 방법인지?
    public void mapAssociation(CreativeImage image) {
        image.mapAssociation(this);
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

    public Creative updateCreative(CreativeRequest creativeRequest) {
        this.title = creativeRequest.getTitle();
        this.price = creativeRequest.getPrice();
        this.exposureStartDate = creativeRequest.getExposureStartDate();
        this.exposureEndDate = creativeRequest.getExposureEndDate();

        CreativeImage image = this.creativeImages.get(0);
        if(!sameImage(creativeRequest.getImages().getOriginalFilename())) {
            // Delete image file in local
            File oldFile = new File("~/IdeaProject/intern/" + this.id + File.separator + image.getName() + "." + image.getExtension());
            if(oldFile.exists()) {
                if(oldFile.delete()) {
                    // Create new image file to local
                    saveImageToLocal(creativeRequest.getImages());

                    // Change CreativeImage's name, extension, size
                    image.updateImage(creativeRequest.getImages());
                }
                else {
                    // TODO: throw Exception?
                }
            }
        }
        return this;
    }

    public boolean sameImage(String filename) {
        CreativeImage image = this.creativeImages.get(0);
        return image.getName().equals(StringUtils.getFilename(filename)) &&
                image.getExtension().equals(StringUtils.getFilenameExtension(filename));
    }

    public void deleteCreative() {
        // status가 DELETED인 경우 다른 메소드를 동작하지 않도록 하려면? -> 모든 메소드마다 if문 throw?
        this.status = CreativeStatus.DELETED;
    }
}
