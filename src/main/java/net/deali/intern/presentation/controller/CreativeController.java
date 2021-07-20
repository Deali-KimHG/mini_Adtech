package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.CreativeService;
import net.deali.intern.domain.Creative;
import net.deali.intern.presentation.dto.CreativeCreateRequest;
import net.deali.intern.presentation.dto.CreativeUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core/v1/creative")
public class CreativeController {
    private final CreativeService creativeService;

    @GetMapping("/")
    public List<Creative> findAll() {
        return creativeService.findAll();
    }

    @GetMapping("/{id}")
    public Creative findById(@PathVariable Long id) {
        return creativeService.findById(id);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCreative(@Valid CreativeCreateRequest creativeCreateRequest) {
        creativeService.createCreative(creativeCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateCreative(@PathVariable Long id, @Valid CreativeUpdateRequest creativeUpdateRequest) {
        creativeService.updateCreative(id, creativeUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCreative(@PathVariable Long id) {
        creativeService.deleteCreative(id);
    }

    @GetMapping("/pause/{id}")
    public void pauseCreative(@PathVariable Long id) {
        creativeService.pauseCreative(id);
    }

    @GetMapping("/restart/{id}")
    public void restartCreative(@PathVariable Long id) {
        creativeService.restartCreative(id);
    }
}
