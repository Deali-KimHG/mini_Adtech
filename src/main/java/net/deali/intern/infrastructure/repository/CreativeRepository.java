package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeRepository extends JpaRepository<Creative, Long> {
}
