package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.CreativeService;
import net.deali.intern.domain.Creative;
import net.deali.intern.presentation.dto.CreativeRequest;
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
    public ResponseEntity<Void> createCreative(@Valid CreativeRequest creativeRequest) {
        creativeService.createCreative(creativeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateCreative(@PathVariable Long id, @Valid CreativeRequest creativeRequest) {
        creativeService.updateCreative(id, creativeRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCreative(@PathVariable Long id) {
        creativeService.deleteCreative(id);
    }
}
