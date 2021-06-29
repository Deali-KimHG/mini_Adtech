package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeCount;
import net.deali.intern.domain.CreativeImage;
import net.deali.intern.domain.CreativeStatus;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
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
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    public void createCreative(CreativeRequest creativeRequest) {
        CreativeCount creativeCount = CreativeCount.builder().count(0L).build();
        Creative creative = Creative.builder()
                .title(creativeRequest.getTitle())
                .price(creativeRequest.getPrice())
                .exposureStartDate(creativeRequest.getExposureStartDate())
                .exposureEndDate(creativeRequest.getExposureEndDate())
                .creativeCounts(creativeCount)
                .build();
        CreativeImage image = CreativeImage.builder()
                .name(StringUtils.getFilename(creativeRequest.getImages().getOriginalFilename()))
                .extension(StringUtils.getFilenameExtension(creativeRequest.getImages().getOriginalFilename()))
                .size(creativeRequest.getImages().getSize())
                .build();
        // Association mapping between image and creative
        creative.mapAssociation(image);
        // Image save to local
        try {
            creative = creativeRepository.save(creative);
            creative.saveImageToLocal(creativeRequest.getImages());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCreative(Long id, CreativeRequest creativeRequest) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        try {
            creative.updateCreative(creativeRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCreative(Long id) {
        Creative creative = creativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        creative.deleteCreative();
    }
}
