package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.AdvertiseLog;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.AdvertiseLogRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.AdvertisementResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CreativeRepository creativeRepository;
    private final AdvertiseLogRepository advertiseLogRepository;

    public List<AdvertisementResponse> expose10advert() {
        List<Advertisement> advertisementList = advertisementRepository.findAll();

        List<AdvertisementResponse> responseList = advertisementRepository.select10Advertisement(advertisementList);

        for(AdvertisementResponse response : responseList) {
            Creative creative = creativeRepository.findById(response.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            // TODO: 노출횟수 카운트하는 부분은 Redis로 변경예정
            creative.countPlus();

            AdvertiseLog advertiseLog = AdvertiseLog.builder()
                    .creative(creative)
                    .advertiseDate(LocalDateTime.now())
                    .score(response.getScore())
                    .build();
            advertiseLogRepository.save(advertiseLog);
        }
        return responseList;
    }
}
