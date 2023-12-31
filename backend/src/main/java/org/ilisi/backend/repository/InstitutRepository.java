package org.ilisi.backend.repository;

import org.ilisi.backend.model.Institut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutRepository extends JpaRepository<Institut, String> {
    Optional<Institut> findByName(String name);
}
