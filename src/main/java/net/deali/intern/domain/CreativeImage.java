package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String directory;

    @Builder
    public CreativeImage(String name, String extension, Long size, String directory) {
        this.name = name;
        this.extension = extension;
        this.size = size;
        this.directory = directory;
    }

    public void setCreative(Creative creative) {
        this.creative = creative;
        creative.getCreativeImages().add(this);
    }
}
