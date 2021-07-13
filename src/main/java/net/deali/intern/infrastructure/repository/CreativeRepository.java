package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Creative;
import net.deali.intern.domain.CreativeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CreativeRepository extends JpaRepository<Creative, Long> {
    List<Creative> findAllByStatusIsNot(CreativeStatus status);
}
