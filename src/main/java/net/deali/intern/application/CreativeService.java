package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeCount;
import net.deali.intern.domain.CreativeImage;
import net.deali.intern.domain.CreativeStatus;
import net.deali.intern.infrastructure.exception.CreativeControlException;
import net.deali.intern.infrastructure.exception.ErrorCode;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreativeService {
    private final CreativeRepository creativeRepository;

    public List<Creative> findAll() {
        return creativeRepository.findAllByStatusIsNot(CreativeStatus.DELETED);
    }

    public Creative findById(Long id) {
        return creativeRepository.findById(id)
                .orElseThrow(() -> new CreativeControlException(ErrorCode.FIND_CREATIVE_FAIL));
    }

    public void createCreative(CreativeRequest creativeRequest) {
        CreativeCount creativeCount = CreativeCount.builder().count(0L).build();
        Creative creative = Creative.builder()
                .title(creativeRequest.getTitle())
                .price(creativeRequest.getPrice())
                .exposureStartDate(creativeRequest.getExposureStartDate())
                .exposureEndDate(creativeRequest.getExposureEndDate())
                .creativeCount(creativeCount)
                .build();
        CreativeImage image = CreativeImage.builder()
                .name(StringUtils.getFilename(creativeRequest.getImages().getOriginalFilename()))
                .extension(StringUtils.getFilenameExtension(creativeRequest.getImages().getOriginalFilename()))
                .size(creativeRequest.getImages().getSize())
                .build();
        // Association mapping between image and creative
        creative.mapAssociation(image);
        // Image save to local
        creative = creativeRepository.save(creative);
        creative.saveImageToLocal(creativeRequest.getImages());
    }

    public void updateCreative(Long id, CreativeRequest creativeRequest) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new CreativeControlException(ErrorCode.FIND_CREATIVE_FAIL));

        creative.update(creativeRequest);
    }

    public void deleteCreative(Long id) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new CreativeControlException(ErrorCode.FIND_CREATIVE_FAIL));
        creative.delete();
    }
}
