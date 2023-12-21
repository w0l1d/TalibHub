package org.ilisi.backend.repository;

import org.ilisi.backend.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProfileRepository extends JpaRepository<Profile, String> {
}
