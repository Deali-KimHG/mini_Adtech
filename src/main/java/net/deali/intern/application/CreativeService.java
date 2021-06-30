package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.*;
import net.deali.intern.infrastructure.exception.EntityControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.infrastructure.repository.AdvertisementRepository;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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

    public void createCreative(CreativeRequest creativeRequest) {
        CreativeCount creativeCount = CreativeCount.builder().count(0L).build();
        Creative creative = Creative.builder()
                .title(creativeRequest.getTitle())
                .price(creativeRequest.getPrice())
                .advertiseStartDate(creativeRequest.getAdvertiseStartDate())
                .advertiseEndDate(creativeRequest.getAdvertiseEndDate())
                .creativeCount(creativeCount)
                .build();
        CreativeImage image = CreativeImage.builder()
                .name(StringUtils.getFilename(creativeRequest.getImages().getOriginalFilename()))
                .extension(StringUtils.getFilenameExtension(creativeRequest.getImages().getOriginalFilename()))
                .size(creativeRequest.getImages().getSize())
                .build();

        creative.mapAssociation(image);

        creative = creativeRepository.save(creative);
        creative.saveImageToLocal(creativeRequest.getImages());

        if(creative.updateAdvertiseStartDateToStart()) {
            advertisementRepository.save(new Advertisement(creative));
            creative.startAdvertise();
        }
    }

    public void updateCreative(Long id, CreativeRequest creativeRequest) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));

        creative.update(creativeRequest);

        // TODO: Feedback 필요(createCreative 포함), 소재 업데이트와 같이 광고풀도 업데이트 해주는 내용들
        // TODO: 다형성 등의 방법을 활용해서 if문을 줄인다.
        // if문이 너무 많아서 가독성이 떨어진다.
        // Repository는 Service에서 동작하는 것을 권장하며, Domain 객체 내에서 비즈니스 로직으로 사용하지 않는다.
        if(creative.isAdvertising()) {
            Advertisement advertisement = advertisementRepository.findByCreativeId(creative.getId())
                    .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_ADVERTISEMENT_FAIL));
            advertisementRepository.delete(advertisement);
            // if 광고를 끝내는 경우 (광고 끝 시간을 현재로 변경)
            // 광고풀에서 제거, EXPIRATION으로 변경
            if(creative.updateAdvertiseEndDateToEnd()) {
                creative.stopAdvertise();
            }
            // if 광고를 끝내는 경우 (광고 시작 시간을 미래로 변경)
            // 광고풀에서 제거, WAITING으로 변경
            if(creative.updateAdvertiseStartDateToFuture()) {
                creative.waitAdvertise();
            }
        }
        if(creative.isExpiration()) {
            // if 광고를 시작하는 경우, (만료된 광고의 끝 시간을 미래로 변경)
            // 광고풀에 삽입, ADVERTISING으로 변경
            if(creative.updateAdvertiseEndDateToFuture()) {
                advertisementRepository.save(new Advertisement(creative));
                creative.startAdvertise();
            }
            // if 광고를 준비하는 경우, (만료된 광고의 시작과 끝 시간을 미래로 변경)
            // WAITING으로 변경
            if(creative.updateAdvertiseDateToFuture()) {
                creative.waitAdvertise();
            }
        }
        // if 광고를 시작하는 경우, (준비중인 광고의 시작 시간을 현재로 변경)
        // ADVERTISING으로 변경
        if(creative.isWaiting()) {
            if(creative.updateAdvertiseStartDateToStart()) {
                advertisementRepository.save(new Advertisement(creative));
                creative.startAdvertise();
            }
        }
    }

    public void deleteCreative(Long id) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityControlException(ErrorCode.FIND_CREATIVE_FAIL));
        creative.delete();
    }
}
