package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

@Service
@Transactional
@AllArgsConstructor
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

    public Profile addExperiences(ProfileDto profileDto) {
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
