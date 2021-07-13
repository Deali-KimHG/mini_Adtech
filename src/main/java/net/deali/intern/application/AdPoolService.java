package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Advertisement;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeStatus;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdPoolService {
    private final AdvertisementRepository advertisementRepository;
    private final CreativeRepository creativeRepository;


    public void insertAdPool() {
        List<Creative> creativeList = creativeRepository.findByAdvertiseStartDateIsBeforeAndAdvertiseEndDateAfterAndStatusIsNot(
                LocalDateTime.now(), LocalDateTime.now(), CreativeStatus.DELETED
        );

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

    public void deleteAdPool() {
        List<Advertisement> advertisementList = advertisementRepository.findByAdvertiseStartDateAfterOrAdvertiseEndDateIsBefore(
                LocalDateTime.now(), LocalDateTime.now());

        for(Advertisement advertisement : advertisementList) {
            advertisementRepository.delete(advertisement);
            Creative creative = creativeRepository.findById(advertisement.getCreativeId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
            creative.stopAdvertise();
        }
    }
}
