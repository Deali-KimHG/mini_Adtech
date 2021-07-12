package net.deali.intern.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class CreativeImage {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @ColumnDefault("random_uuid()")
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Creative creative;

    private String name;
    private String extension;
    private Long size;

    @Builder
    public CreativeImage(String name, String extension, Long size) {
        this.name = name;
        this.extension = extension;
        this.size = size;
    }

    public void mapAssociation(Creative creative) {
        this.creative = creative;
        creative.getCreativeImages().add(this);
    }

    public void updateImage(MultipartFile file) {
        this.name = StringUtils.getFilename(file.getOriginalFilename());
        this.extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        this.size = file.getSize();
    }
}
