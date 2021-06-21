package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeRepository;
import net.deali.intern.domain.Image;
import net.deali.intern.domain.ImageRepository;
import net.deali.intern.presentation.dto.CreativeRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreativeService {
    private final CreativeRepository creativeRepository;
    private final ImageRepository imageRepository;

    public List<Creative> findAll() {
        return creativeRepository.findAll();
    }

    public Creative findById(Long id) {
        return creativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    public void registerCreative(CreativeRequest creativeRequest) {
        Creative creative = Creative.builder()
                .title(creativeRequest.getTitle())
                .price(creativeRequest.getPrice())
                .exposureStartDate(creativeRequest.getExposureStartDate())
                .exposureEndDate(creativeRequest.getExposureEndDate())
                .build();
        creativeRepository.save(creative);

        MultipartFile files = creativeRequest.getImages();
        Image image = Image.builder()
                .creative(creative)
                .size(files.getSize())
                .directory(files.getOriginalFilename())
                .build();
        imageRepository.save(image);
    }

    public void updateCreative(Long id, CreativeRequest creativeRequest) {
    }

    public void deleteCreative(Long id) {
        creativeRepository.deleteById(id);
    }
}
