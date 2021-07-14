package net.deali.intern.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deali.intern.domain.Creative;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PageService {
    private final CreativeService creativeService;

    public Creative detail(Long id) {
        return creativeService.findById(id);
    }
}
