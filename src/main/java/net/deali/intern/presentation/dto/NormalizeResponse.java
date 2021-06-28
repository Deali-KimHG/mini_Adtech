package net.deali.intern.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormalizeResponse {
    private Long minPrice;
    private Long maxPrice;
    private LocalDateTime minUpdatedDate;
    private LocalDateTime maxUpdatedDate;
}
