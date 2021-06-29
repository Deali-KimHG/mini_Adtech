package net.deali.intern.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.deali.intern.infrastructure.util.BaseTimeEntity;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "creative", fetch = FetchType.LAZY)
    private List<CreativeImage> creativeImages = new ArrayList<>();

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    private CreativeCount creativeCounts;

    @Builder
    public Creative(String title, Long price, LocalDateTime exposureStartDate, LocalDateTime exposureEndDate, CreativeCount creativeCounts) {
        this.title = title;
        this.price = price;
        this.exposureStartDate = exposureStartDate;
        this.exposureEndDate = exposureEndDate;
        this.creativeCounts = creativeCounts;
    }

    public void mapAssociation(CreativeImage image) {
        image.mapAssociation(this);
    }

    public void saveImageToLocal(MultipartFile file) throws IOException {
        Path directory = Paths.get(System.getProperty("user.dir") + "/images/" + this.id + File.separator).toAbsolutePath().normalize();
        Files.createDirectories(directory);

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetPath = directory.resolve(filename).normalize();

        file.transferTo(targetPath);
    }

    public void updateCreative(CreativeRequest creativeRequest) throws IOException {
        CreativeImage image = this.creativeImages.get(0);
        if(!sameImage(creativeRequest.getImages().getOriginalFilename())) {
            // Delete image file in local
            File oldFile = new File(System.getProperty("user.dir") + "/images/" + this.id + File.separator + image.getName());
            if(oldFile.exists()) {
                if(oldFile.delete()) {
                    // Create new image file to local
                    saveImageToLocal(creativeRequest.getImages());

                    // Change CreativeImage's name, extension, size
                    image.updateImage(creativeRequest.getImages());
                }
            } else {
                throw new FileNotFoundException("File not found");
            }
        }

        this.title = creativeRequest.getTitle();
        this.price = creativeRequest.getPrice();
        this.exposureStartDate = creativeRequest.getExposureStartDate();
        this.exposureEndDate = creativeRequest.getExposureEndDate();
    }

    public boolean sameImage(String filename) {
        CreativeImage image = this.creativeImages.get(0);
        return image.getName().equals(StringUtils.getFilename(filename)) &&
                image.getExtension().equals(StringUtils.getFilenameExtension(filename));
    }

    public void deleteCreative() {
        this.status = CreativeStatus.DELETED;
    }
    public void startAdvertise() {
        this.status = CreativeStatus.ADVERTISING;
    }
    public void stopAdvertise() {
        this.status = CreativeStatus.EXPIRATION;
    }
    public void countPlus() {
        this.creativeCounts.countPlus();
    }
}
