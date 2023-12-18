package org.ilisi.backend.repository;

import org.ilisi.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}