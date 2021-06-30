package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.AdvertisementService;
import net.deali.intern.domain.Advertisement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dsp/v1")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping("/")
    public List<Advertisement> expose10advert() {
        return advertisementService.expose10advert();
    }

    @PostMapping("/")
    public void insertAdPool() {
        advertisementService.insertAdPool();
    }

    @DeleteMapping("/")
    public void deleteAdPool() {
        advertisementService.deleteAdPool();
    }
}
