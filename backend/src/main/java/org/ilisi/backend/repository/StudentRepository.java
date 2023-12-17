package org.ilisi.backend.repository;

import org.ilisi.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
