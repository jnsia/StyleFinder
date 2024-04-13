package com.d111.backend.repository.mongo;

import com.d111.backend.entity.coordi.Coordi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoCoordiRepository extends MongoRepository<Coordi, String> {
    Optional<Coordi> findById(String id);

    Coordi findBy_id(String id);

}
