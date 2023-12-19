package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ProfileDto;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.repository.EducationRepository;
import org.ilisi.backend.repository.ExperienceRepository;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ProfileService {

    private ProfileRepository profileRepository;
    private EducationRepository educationRepository;
    private InstitutRepository institutRepository;
    private ExperienceRepository experienceRepository;

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }


    public Profile addEducations(ProfileDto profileDto) {
        Profile profile = profileRepository.findById(profileDto.getId()).get();
        profileDto.getEducations().forEach(education -> {
            if(education.getInstitut().getId() != null)
                education.setInstitut(institutRepository.findById(education.getInstitut().getId()).get());
            else
                education.setInstitut(institutRepository.save(education.getInstitut()));
        });

        profile.setEducations(profileDto.getEducations());
        educationRepository.saveAll(profileDto.getEducations());
        return profileRepository.save(profile);
    }

    public Profile addExperiences(ProfileDto profileDto) {
        Profile profile = profileRepository.findById(profileDto.getId()).get();
        profileDto.getExperiences().forEach(experience -> {
            if(experience.getInstitut().getId() != null)
                experience.setInstitut(institutRepository.findById(experience.getInstitut().getId()).get());
            else
                experience.setInstitut(institutRepository.save(experience.getInstitut()));
        });

        profile.setExperiences(profileDto.getExperiences());
        experienceRepository.saveAll(profileDto.getExperiences());
        return profileRepository.save(profile);
    }
}
