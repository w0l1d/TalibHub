package org.ilisi.backend.repository;

import org.ilisi.backend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findByToken(String token);
}
