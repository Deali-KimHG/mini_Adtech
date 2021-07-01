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
        Map<String, Object> map = getMinMaxData(advertisementList);

        return advertisementList.stream()
                .map(advertisement -> {
                    long price = advertisement.getPrice();
                    LocalDateTime date = advertisement.getUpdatedDate();
                    double score = getScoreFromNormalizedData(price, date, map);
                    return new AdvertisementResponse(advertisement, score);
                })
                .sorted((o1, o2) -> o2.getScore().compareTo(o1.getScore()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public double getScoreFromNormalizedData(long price, LocalDateTime date, Map<String, Object> map) {
        double normalizedPrice = (double) (price - (long) map.get("minPrice")) / ((long) map.get("maxPrice") - (long) map.get("minPrice"));
        double normalizedDate = (double) (Duration.between((LocalDateTime) map.get("minDate"), date).toMinutes())
                / (Duration.between((LocalDateTime) map.get("minDate"), (LocalDateTime) map.get("maxDate")).toMinutes());
        return normalizedPrice * 6 + normalizedDate * 4;
    }

    public Map<String, Object> getMinMaxData(List<Advertisement> advertisementList) {
        Map<String, Object> map = new HashMap<>();

        long maxPrice = Integer.MIN_VALUE;
        long minPrice = Integer.MAX_VALUE;
        LocalDateTime maxDate = LocalDateTime.MIN;
        LocalDateTime minDate = LocalDateTime.MAX;

        for(Advertisement advertisement : advertisementList) {
            maxPrice = Math.max(maxPrice, advertisement.getPrice());
            minPrice = Math.min(minPrice, advertisement.getPrice());
            maxDate = maxDate.isBefore(advertisement.getUpdatedDate()) ? advertisement.getUpdatedDate() : maxDate;
            minDate = minDate.isAfter(advertisement.getUpdatedDate()) ? advertisement.getUpdatedDate() : minDate;
        }

        map.put("maxPrice", maxPrice);
        map.put("minPrice", minPrice);
        map.put("maxDate", maxDate);
        map.put("minDate", minDate);

        return map;
    }
}
