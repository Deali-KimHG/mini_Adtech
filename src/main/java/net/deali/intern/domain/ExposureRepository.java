package net.deali.intern.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExposureRepository extends MongoRepository<Exposure, String> {
}
