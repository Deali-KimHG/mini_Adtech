package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Exposure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExposureRepository extends MongoRepository<Exposure, String> {
    List<Exposure> findByExposureEndDateIs(LocalDateTime date);
    Optional<Exposure> findByCreativeId(Long creativeId);
}
