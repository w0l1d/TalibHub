package org.ilisi.backend.repository;

import org.ilisi.backend.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface EducationRepository extends JpaRepository<Education, String> {
}
