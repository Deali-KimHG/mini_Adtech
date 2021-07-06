package net.deali.intern.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.exception.FileControlException;
import net.deali.intern.infrastructure.exception.InputDataNotValidException;
import net.deali.intern.infrastructure.util.BaseTimeEntity;
import net.deali.intern.presentation.dto.CreativeUpdateRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm", timezone="Asia/Seoul")
    private LocalDateTime advertiseStartDate;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm", timezone="Asia/Seoul")
    private LocalDateTime advertiseEndDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "creative", fetch = FetchType.LAZY)
    private List<CreativeImage> creativeImages = new ArrayList<>();

    @OneToOne
    private CreativeCount creativeCount;

    @Builder
    public Creative(String title, Long price, LocalDateTime advertiseStartDate, LocalDateTime advertiseEndDate, CreativeCount creativeCount) {
        this.title = title;
        this.price = price;
        this.advertiseStartDate = advertiseStartDate;
        this.advertiseEndDate = advertiseEndDate;
        this.creativeCount = creativeCount;

        if(advertiseStartDate.isAfter(advertiseEndDate))
            throw new InputDataNotValidException(ErrorCode.INVALID_INPUT_DATA);
    }

    public void mapAssociation(CreativeImage image) {
        image.mapAssociation(this);
    }

    public void saveImageToLocal(MultipartFile file) {
        if(file.getOriginalFilename() == null)
            throw new FileControlException(ErrorCode.INVALID_FILE_NAME);

        Path directory = Paths.get(System.getProperty("user.dir") + "/images/" + this.id + File.separator).toAbsolutePath().normalize();
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new FileControlException(ErrorCode.DIRECTORY_CREATION_FAIL);
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetPath = directory.resolve(filename).normalize();

        try {
            file.transferTo(targetPath);
        } catch (IOException e) {
            throw new FileControlException(ErrorCode.FILE_CREATION_FAIL);
        }
    }

    public boolean checkSameImage(MultipartFile file) {
        CreativeImage image = this.creativeImages.get(0);
        return image.getName().equals(file.getOriginalFilename())
                && image.getSize() == file.getSize();
    }

    public void update(CreativeUpdateRequest creativeUpdateRequest) {
        if(this.status == CreativeStatus.DELETED)
            throw new EntityControlException(ErrorCode.DELETED_CREATIVE);

        if(creativeUpdateRequest.getTitle() != null)
            this.title = creativeUpdateRequest.getTitle();
        if(creativeUpdateRequest.getPrice() != null)
            this.price = creativeUpdateRequest.getPrice();
        if(creativeUpdateRequest.getAdvertiseStartDate() != null)
            this.advertiseStartDate = creativeUpdateRequest.getAdvertiseStartDate();
        if(creativeUpdateRequest.getAdvertiseEndDate() != null)
            this.advertiseEndDate = creativeUpdateRequest.getAdvertiseEndDate();

        if(this.advertiseStartDate.isAfter(this.advertiseEndDate))
            throw new InputDataNotValidException(ErrorCode.INVALID_INPUT_DATA);

        if(creativeUpdateRequest.getImages() == null || creativeUpdateRequest.getImages().isEmpty())
            return ;
        CreativeImage image = this.creativeImages.get(0);
        if(checkSameImage(creativeUpdateRequest.getImages()))
            return ;

        File oldFile = new File(System.getProperty("user.dir") + "/images/" + this.id + File.separator + image.getName());

        if(!oldFile.exists())
            throw new FileControlException(ErrorCode.FILE_NOT_FOUND);
        // Delete image file in local
        if(!oldFile.delete())
            throw new FileControlException(ErrorCode.FILE_ALREADY_DELETED);
        // Create new image file to local
        saveImageToLocal(creativeUpdateRequest.getImages());
        // Change CreativeImage's name, extension, size
        image.updateImage(creativeUpdateRequest.getImages());
    }

    public boolean isWaiting() {
        return this.status == CreativeStatus.WAITING;
    }

    public boolean isAdvertising() {
        return this.status == CreativeStatus.ADVERTISING;
    }

    public boolean isExpiration() {
        return this.status == CreativeStatus.EXPIRATION;
    }

    public boolean updateAdvertiseEndDateToEnd() {
        return this.advertiseEndDate.isBefore(LocalDateTime.now()) || this.advertiseEndDate.isEqual(LocalDateTime.now());
    }

    public boolean updateAdvertiseStartDateToFuture() {
        return this.advertiseStartDate.isAfter(LocalDateTime.now());
    }

    public boolean updateAdvertiseEndDateToFuture() {
        return this.advertiseEndDate.isAfter(LocalDateTime.now());
    }

    public boolean updateAdvertiseDateToFuture() {
        return this.advertiseStartDate.isAfter(LocalDateTime.now());
    }

    public boolean updateAdvertiseStartDateToStart() {
        return (this.advertiseStartDate.isBefore(LocalDateTime.now())
                || this.advertiseStartDate.isEqual(LocalDateTime.now()))
                && this.advertiseEndDate.isAfter(LocalDateTime.now());
    }

    public void delete() {
        if(this.status == CreativeStatus.DELETED)
            throw new EntityControlException(ErrorCode.DELETED_CREATIVE);

        this.status = CreativeStatus.DELETED;
    }

    public void waitAdvertise() {
        this.status = CreativeStatus.WAITING;
    }

    public void startAdvertise() {
        this.status = CreativeStatus.ADVERTISING;
    }

    public void stopAdvertise() {
        this.status = CreativeStatus.EXPIRATION;
    }

    public void countPlus() {
        this.creativeCount.countPlus();
    }

    public Advertisement toAdvertisement() {
        return Advertisement.builder()
                .title(this.title)
                .price(this.price)
                .creativeId(this.id)
                .image(creativeImages.get(0).getName())
                .advertiseStartDate(this.advertiseStartDate)
                .advertiseEndDate(this.advertiseEndDate)
                .updatedDate(this.getUpdatedDate())
                .build();
    }
}
