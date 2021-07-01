package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.AdvertisementResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CreativeRepository creativeRepository;

    public List<AdvertisementResponse> expose10advert() {
        List<Advertisement> advertisementList = advertisementRepository.findAll();

        List<AdvertisementResponse> responseList = advertisementRepository.select10Advertisement(advertisementList);

        for(AdvertisementResponse response : responseList) {
            Creative creative = creativeRepository.findById(response.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            creative.countPlus();
        }
        return responseList;
    }
}
