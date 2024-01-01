package org.ilisi.backend.repository;

import org.ilisi.backend.model.Student;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

/**
 * StudentRepository is an interface that extends JpaRepository.
 * It provides CRUD operations for Student entities.
 */
@Repository
public interface StudentRepository extends DataTablesRepository<Student, String> {
}
