package org.ilisi.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ProfileDto;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.repository.EducationRepository;
import org.ilisi.backend.repository.ExperienceRepository;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Slf4j
public class StudentProfileController {

    private final ProfileRepository profileRepository;
    private final EducationRepository educationRepository;
    private final InstitutRepository institutRepository;
    private final ExperienceRepository experienceRepository;

    @GetMapping("")
    public List<Profile> getProfiles() {
        return profileRepository.findAll();
    }


    @PostMapping("/addEducations")
    public Profile addEducations(@RequestBody ProfileDto profileDto) {
        Profile profile = profileRepository.findById(profileDto.getId()).get();
        profileDto.getEducations().forEach(education -> {
            if(education.getInstitut() != null) {
                Institut institut = institutRepository.
                        findByName(education.getInstitut().getName())
                        .orElse(null);

                if (institut == null) {
                    institut = institutRepository.save(education.getInstitut());
                }

                education.setInstitut(institut);
            }
        });

        profile.setEducations(profileDto.getEducations());
        educationRepository.saveAll(profileDto.getEducations());
        return profileRepository.save(profile);
    }

    @PostMapping("/addExperiences")
    public Profile addExperiences(@RequestBody ProfileDto profileDto) {
        Profile profile = profileRepository.findById(profileDto.getId()).get();
        profileDto.getExperiences().forEach(experience -> {
            if(experience.getInstitut() != null) {
                Institut institut = institutRepository.
                        findByName(experience.getInstitut().getName())
                        .orElse(null);

                if (institut == null) {
                    institut = institutRepository.save(experience.getInstitut());
                }

                experience.setInstitut(institut);
            }
        });

        profile.setExperiences(profileDto.getExperiences());
        experienceRepository.saveAll(profileDto.getExperiences());
        return profileRepository.save(profile);
    }
}
