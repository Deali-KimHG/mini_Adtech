package net.deali.intern.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CreativeCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    private Creative creative;
    private Long count;

    @Builder
    public CreativeCount(Long count) {
        this.count = count;
    }

    public void mapAssociation(Creative creative) {
        this.creative = creative;
        creative.getCreativeCounts().add(this);
    }
}
