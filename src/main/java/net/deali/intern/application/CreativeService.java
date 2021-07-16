package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deali.intern.domain.*;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.CreativeCreateRequest;
import net.deali.intern.presentation.dto.CreativeUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CreativeService {
    private final CreativeRepository creativeRepository;
    private final AdvertisementRepository advertisementRepository;

    public List<Creative> findAll() {
        return creativeRepository.findAllByStatusIsNot(CreativeStatus.DELETED);
    }

    public Creative findById(Long id) {
        return creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
    }

    public void createCreative(CreativeCreateRequest creativeCreateRequest) {
        CreativeCount creativeCount = CreativeCount.builder().count(0L).build();
        Creative creative = Creative.builder()
                .title(creativeCreateRequest.getTitle())
                .price(creativeCreateRequest.getPrice())
                .advertiseStartDate(creativeCreateRequest.getAdvertiseStartDate())
                .advertiseEndDate(creativeCreateRequest.getAdvertiseEndDate())
                .creativeCount(creativeCount)
                .build();
        CreativeImage image = CreativeImage.builder()
                .name(StringUtils.getFilename(creativeCreateRequest.getImages().getOriginalFilename()))
                .extension(StringUtils.getFilenameExtension(creativeCreateRequest.getImages().getOriginalFilename()))
                .size(creativeCreateRequest.getImages().getSize())
                .build();

        creative.mapAssociation(image);

        creative = creativeRepository.save(creative);
        creative.saveImageToLocal(creativeCreateRequest.getImages());

        if(creative.updateDateToAdvertising()) {
            advertisementRepository.save(new Advertisement(creative));
            creative.startAdvertise();
        }
    }

    public void updateCreative(Long id, CreativeUpdateRequest creativeUpdateRequest) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));

        creative.update(creativeUpdateRequest);

        switch (creative.getStatus()) {
            case WAITING:
                if(creative.updateDateToAdvertising()) {
                    advertisementRepository.save(new Advertisement(creative));
                    creative.startAdvertise();
                }
                if(creative.updateDateToExpiration()) {
                    creative.stopAdvertise();
                }
                break;
            case ADVERTISING:
                if(creative.updateDateToExpiration()) {
                    deleteAdvertisement(creative.getId());
                    creative.stopAdvertise();
                } else if(creative.updateDateToWaiting()) {
                    deleteAdvertisement(creative.getId());
                    creative.waitAdvertise();
                } else {
                    updateAdvertisement(creative);
                }
                break;
            case EXPIRATION:
                if(creative.updateDateToAdvertising()) {
                    advertisementRepository.save(new Advertisement(creative));
                    creative.startAdvertise();
                }
                if(creative.updateDateToWaiting()) {
                    creative.waitAdvertise();
                }
            case PAUSE:
                if(creative.updateDateToWaiting()) {
                    creative.waitAdvertise();
                }
                if(creative.updateDateToAdvertising()) {
                    advertisementRepository.save(new Advertisement(creative));
                    creative.startAdvertise();
                }
                if(creative.updateDateToExpiration()) {
                    creative.stopAdvertise();
                }
                break;
            case DELETED:
                throw new EntityControlException(ErrorCode.DELETED_CREATIVE);
        }
    }

    public void updateAdvertisement(Creative creative) {
        Advertisement advertisement = advertisementRepository.findByCreativeId(creative.getId())
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        advertisement.updateAdvertisement(creative);
        advertisementRepository.save(advertisement);
    }

    public void deleteAdvertisement(Long id) {
        Advertisement advertisement = advertisementRepository.findByCreativeId(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
        advertisementRepository.delete(advertisement);
    }

    public void deleteCreative(Long id) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));

        if(creative.getStatus() == CreativeStatus.ADVERTISING)
            deleteAdvertisement(id);

        creative.delete();
    }

    public void pauseCreative(Long id) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        switch (creative.getStatus()) {
            case WAITING:
                creative.pauseAdvertise();
                break;
            case ADVERTISING:
                Advertisement advertisement = advertisementRepository.findByCreativeId(id)
                        .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
                advertisementRepository.delete(advertisement);
                creative.pauseAdvertise();
                break;
            case EXPIRATION:
                throw new EntityControlException(ErrorCode.EXPIRED_CREATIVE);
            case DELETED:
                throw new EntityControlException(ErrorCode.DELETED_CREATIVE);
            case PAUSE:
                throw new EntityControlException(ErrorCode.PAUSED_CREATIVE);
        }
    }
}
