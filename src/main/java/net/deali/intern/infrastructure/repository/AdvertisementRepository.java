package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Advertisement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends MongoRepository<Advertisement, String>, AdvertisementRepositoryCustom {
    Optional<Advertisement> findByCreativeId(Long creativeId);
}
