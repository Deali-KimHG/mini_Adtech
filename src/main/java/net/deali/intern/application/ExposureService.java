package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.Exposure;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.infrastructure.repository.ExposureRepository;
import net.deali.intern.presentation.dto.NormalizeResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    @Transactional
    public class ExposureService {
        private final ExposureRepository exposureRepository;
    private final CreativeRepository creativeRepository;
    private final MongoTemplate mongoTemplate;

    // TODO: Aggregation보단 Java 코드내에서 min, max를 가져오고 Java 알고리즘으로 정규화 진행후 선정
    public List<Exposure> expose10advert() {
        GroupOperation minmaxGroupOperation = Aggregation.group().max("price").as("maxPrice").min("price").as("minPrice")
                .max("updatedDate").as("maxUpdatedDate").min("updatedDate").as("minUpdatedDate");
        ProjectionOperation minmaxProjectionOperation = Aggregation.project().andExclude("_id");
        AggregationResults<NormalizeResponse> minmaxResult = mongoTemplate.aggregate(
                Aggregation.newAggregation(minmaxGroupOperation, minmaxProjectionOperation
                ), "ad", NormalizeResponse.class);
        NormalizeResponse minmaxResponse = minmaxResult.getUniqueMappedResult();

        AggregationExpression expr = ArithmeticOperators.valueOf(
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf("price").subtract(minmaxResponse.getMinPrice())
                        ).divideBy(
                                minmaxResponse.getMaxPrice() - minmaxResponse.getMinPrice()
                        )
                ).multiplyBy(6)
        ).add(
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf(
                                        ConvertOperators.Convert.convertValueOf("updatedDate").to("long")
                                ).subtract(
                                        ConvertOperators.Convert.convertValue(minmaxResponse.getMinUpdatedDate()).to("long")
                                )
                        ).divideBy(
                                ArithmeticOperators.valueOf(
                                        ConvertOperators.Convert.convertValue(minmaxResponse.getMaxUpdatedDate()).to("long")
                                ).subtract(
                                        ConvertOperators.Convert.convertValue(minmaxResponse.getMinUpdatedDate()).to("long")
                                )
                        )
                ).multiplyBy(4)
        );
        ProjectionOperation projectionOperation = Aggregation.project().and(expr).as("standard");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "standard");
        LimitOperation limitOperation = Aggregation.limit(10);
        AggregationResults<Exposure> exposureAggregationResults = mongoTemplate.aggregate(Aggregation.newAggregation(projectionOperation, sortOperation, limitOperation), "ad", Exposure.class);
        List<Exposure> exposureList = exposureAggregationResults.getMappedResults();

        for(Exposure exposure : exposureList) {
            Creative creative = creativeRepository.findById(exposure.getCreativeId())
                    .orElseThrow(() -> new EntityNotFoundException(String.valueOf(exposure.getCreativeId())));
            creative.countPlus();
        }
        return exposureList;
    }

    // TODO: New Service
    public void insertAdPool() {
        List<Creative> creativeList = creativeRepository.findByExposureStartDateBeforeAndExposureEndDateAfter(
                LocalDateTime.now(), LocalDateTime.now()
        );
        // creative to exposure
        for(Creative creative : creativeList) {
            Optional<Exposure> exposure = exposureRepository.findByCreativeId(creative.getId());

            if(exposure.isPresent()) {
                exposure.get().updateExposure(creative);
            } else {
                exposureRepository.save(new Exposure(creative));
            }
            creative.startAdvertise();
        }
    }

    public void deleteAdPool() {
        List<Exposure> exposureList = exposureRepository.findByExposureEndDateIs(LocalDateTime.now());
        for(Exposure exposure : exposureList) {
            exposureRepository.delete(exposure);
            Creative creative = creativeRepository.findById(exposure.getCreativeId())
                    .orElseThrow(() -> new EntityNotFoundException(String.valueOf(exposure.getCreativeId())));
            creative.stopAdvertise();
        }
    }
}
