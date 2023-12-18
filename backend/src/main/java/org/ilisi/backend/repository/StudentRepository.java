package org.ilisi.backend.repository;

import org.ilisi.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * StudentRepository is an interface that extends JpaRepository.
 * It provides CRUD operations for Student entities.
 */
@Service
public interface StudentRepository extends JpaRepository<Student, String> {



}
