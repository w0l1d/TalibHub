package org.ilisi.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.service.InstitutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instituts")
@RequiredArgsConstructor
public class InstitutController {
    private final InstitutRepository institutRepository;
    private final InstitutService institutService;

    @GetMapping("")
    public List<Institut> getInstituts() {
        return institutRepository.findAll();
    }

    @GetMapping("/{id}")
    public Institut getInstitutById(@PathVariable String id) {
        return institutService.findById(id);
    }
}
