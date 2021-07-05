package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.AdvertiseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertiseLogRepository extends JpaRepository<AdvertiseLog, Long> {
    AdvertiseLog findByCreativeId(Long id);
}
