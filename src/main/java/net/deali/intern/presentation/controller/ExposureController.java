package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.ExposureService;
import net.deali.intern.domain.Exposure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dsp/v1")
public class ExposureController {
    private final ExposureService exposureService;

    @GetMapping("/")
    public List<Exposure> expose10advert() {
        return exposureService.expose10advert();
    }
}
