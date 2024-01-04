package org.ilisi.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.repository.InstitutRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instituts")
@RequiredArgsConstructor
public class InstitutController {
    private final InstitutRepository institutRepository;

    @GetMapping("")
    public List<Institut> getInstituts() {
        return institutRepository.findAll();
    }
}
