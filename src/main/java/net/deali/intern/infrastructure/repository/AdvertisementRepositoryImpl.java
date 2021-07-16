package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Advertisement;
import net.deali.intern.presentation.dto.AdvertisementResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {
    @Override
    public List<AdvertisementResponse> select10Advertisement(List<Advertisement> advertisementList) {
        LocalDateTime maxDate = getMinMaxData(advertisementList);

        return advertisementList.stream()
                .map(advertisement -> {
                    long price = advertisement.getPrice();
                    LocalDateTime date = advertisement.getUpdatedDate();
                    double score = getScoreFromNormalizedData(price, date, maxDate);
                    return new AdvertisementResponse(advertisement, score);
                })
                .sorted((o1, o2) -> o2.getScore().compareTo(o1.getScore()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public double getScoreFromNormalizedData(long price, LocalDateTime date, LocalDateTime maxDate) {
        double minPrice = 1;
        double maxPrice = 10;
        LocalDateTime minDate = LocalDateTime.of(2021, 6, 1, 0, 0);

        double normalizedPrice = (price - minPrice) / (maxPrice - minPrice);
        double normalizedDate = (double) (Duration.between(minDate, date).toMinutes())
                / (Duration.between(minDate, maxDate).toMinutes());
        return normalizedPrice * 6 + normalizedDate * 4;
    }

    public LocalDateTime getMinMaxData(List<Advertisement> advertisementList) {
        LocalDateTime maxDate = LocalDateTime.MIN;

        for(Advertisement advertisement : advertisementList) {
            maxDate = maxDate.isBefore(advertisement.getUpdatedDate()) ? advertisement.getUpdatedDate() : maxDate;
        }

        return maxDate;
    }
}
