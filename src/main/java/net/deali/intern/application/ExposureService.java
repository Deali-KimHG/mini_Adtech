package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Creative;
import net.deali.intern.domain.Exposure;
import net.deali.intern.infrastructure.repository.CreativeRepository;
import net.deali.intern.infrastructure.repository.ExposureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExposureService {
    private final ExposureRepository exposureRepository;
    private final CreativeRepository creativeRepository;

    public List<Exposure> expose10advert() {
        List<Exposure> exposureList = exposureRepository.findAll();
        List<Exposure> resultList = new ArrayList<>();

        for(Exposure exposure : exposureList) {

        }

        return resultList;
    }

    public void insertAdPool() {
        List<Creative> creativeList = creativeRepository.findByExposureStartDateAfterAndExposureEndDateBefore(
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
        }
    }

    public void deleteAdPool() {
        List<Exposure> exposureList = exposureRepository.findByExposureEndDateIs(LocalDateTime.now());
        for(Exposure exposure : exposureList) {
            exposureRepository.delete(exposure);
        }
    }
}
