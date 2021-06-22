package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;

@Entity
@Getter
@NoArgsConstructor
public class CreativeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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

    public void setCreative(Creative creative) {
        this.creative = creative;
        creative.getCreativeImages().add(this);
    }

    // TODO: 예외처리
    public void saveLocal(MultipartFile file) {
        // TODO: 상대경로로 변경
        File dest = new File("~/IdeaProject/intern/" + creative.getId() + File.separator + StringUtils.getFilenameExtension(file.getOriginalFilename()));

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
