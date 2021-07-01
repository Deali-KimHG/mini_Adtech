package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.AdvertisementService;
import net.deali.intern.presentation.dto.AdvertisementResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dsp/v1/advertisement")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping("/")
    public List<AdvertisementResponse> expose10advert() {
        return advertisementService.expose10advert();
    }
}
