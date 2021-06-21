package net.deali.intern.domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document("exposure")
@Getter
public class Exposure {
    @Id
    private String id;

    private String title;
    private String image;
    private Long price;
    private LocalDateTime exposureStartDate;
    private LocalDateTime exposureEndDate;
}
