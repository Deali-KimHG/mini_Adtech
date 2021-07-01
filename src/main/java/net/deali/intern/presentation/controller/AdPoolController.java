package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.AdPoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dsp/v1/adpool")
public class AdPoolController {
    private final AdPoolService adPoolService;

    @PostMapping("/")
    public ResponseEntity<Void> insertAdPool() {
        adPoolService.insertAdPool();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/")
    public void deleteAdPool() {
        adPoolService.deleteAdPool();
    }
}
