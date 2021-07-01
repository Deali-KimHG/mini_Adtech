package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.AdPoolService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dsp/v1/ADpool")
public class AdPoolController {
    private final AdPoolService adPoolService;

    @PostMapping("/")
    public void insertAdPool() {
        adPoolService.insertAdPool();
    }

    @DeleteMapping("/")
    public void deleteAdPool() {
        adPoolService.deleteAdPool();
    }
}
