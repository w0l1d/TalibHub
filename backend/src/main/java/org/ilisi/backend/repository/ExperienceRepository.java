package org.ilisi.backend.repository;

import org.ilisi.backend.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ExperienceRepository extends JpaRepository<Experience, String> {
}
