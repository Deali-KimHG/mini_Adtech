package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import net.deali.intern.domain.Exposure;
import net.deali.intern.domain.ExposureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExposureService {
    private final ExposureRepository exposureRepository;

    public List<Exposure> expose10advert() {
        List<Exposure> exposureList = exposureRepository.findAll();
        List<Exposure> resultList = new ArrayList<>();

        return resultList;
    }
}
