package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.repository.InstitutRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class InstitutService {
    private InstitutRepository institutRepository;

    public Institut findById(String id) {
        return institutRepository.findById(id).orElse(null);
    }
}
