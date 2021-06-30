package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeStatus;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.AdvertisementResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CreativeRepository creativeRepository;
    private final MongoTemplate mongoTemplate;

    // TODO: Aggregation보단 Java 코드내에서 min, max를 가져오고 Java 알고리즘으로 정규화 진행후 선정
    public List<Advertisement> expose10advert() {
        List<Advertisement> advertisementList = advertisementRepository.findAll();

        List<AdvertisementResponse> responseList = advertisementRepository.select10Advertisement(advertisementList);

        for(AdvertisementResponse response : responseList) {
            Creative creative = creativeRepository.findById(response.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            creative.countPlus();
        }
        return advertisementList;
    }

    // TODO: New Service
    public void insertAdPool() {
        // TODO: Creative를 가져올 때 Status가 DELETED인 것을 애초에 제외
        List<Creative> creativeList = creativeRepository.findByAdvertiseStartDateBeforeAndAdvertiseEndDateAfterAndStatusIsNot(
                LocalDateTime.now(), LocalDateTime.now(), CreativeStatus.DELETED
        );
        // creative to advertisement
        for(Creative creative : creativeList) {
            Optional<Advertisement> advertisement = advertisementRepository.findByCreativeId(creative.getId());

            if(advertisement.isPresent()) {
                advertisement.get().updateAdvertisement(creative);
            } else {
                advertisementRepository.save(new Advertisement(creative));
            }
            creative.startAdvertise();
        }
    }


    // TODO: 삭제 대상 변경, 해당 날짜와 동일한 데이터들이 아니라 해당 날짜에 포함되지 않는으로
    public void deleteAdPool() {
        List<Advertisement> advertisementList = advertisementRepository.findByAdvertiseEndDateIs(LocalDateTime.now());
        for(Advertisement advertisement : advertisementList) {
            advertisementRepository.delete(advertisement);
            Creative creative = creativeRepository.findById(advertisement.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            creative.stopAdvertise();
        }
    }
}
