package net.deali.intern.infrastructure.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NowDateTimeJobParameter {
    private LocalDateTime nowDate;

    @Value("#{jobParameters[nowDate]}")
    public void setNowDate(String nowDate) {
        this.nowDate = LocalDateTime.parse(nowDate);
    }
}
