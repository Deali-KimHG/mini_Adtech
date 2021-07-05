package net.deali.intern.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AdvertiseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Creative creative;
    private LocalDateTime advertiseDate;
    private Double score;

    @Builder
    public AdvertiseLog(Creative creative, LocalDateTime advertiseDate, Double score) {
        this.creative = creative;
        this.advertiseDate = advertiseDate;
        this.score = score;
    }
}
