package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.mapper.EducationMapper;
import org.ilisi.backend.mapper.ExperienceMapper;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.repository.EducationRepository;
import org.ilisi.backend.repository.ExperienceRepository;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ProfileService {

    private ProfileRepository profileRepository;
    private InstitutRepository institutRepository;
    private EducationMapper educationMapper;
    private ExperienceMapper experienceMapper;
    private EducationRepository educationRepository;
    private ExperienceRepository experienceRepository;

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }


    public Profile addEducation(Profile profile, EducationDto educationDto) {
        if(educationDto.getInstitut().getId() != null)
            educationDto.setInstitut(institutRepository.findById(educationDto.getInstitut().getId()).get());
        else
            educationDto.setInstitut(institutRepository.save(educationDto.getInstitut()));

        Education education = educationRepository.save(educationMapper.educationDtoToEducation(educationDto));
        profile.getEducations().add(education);
        return profileRepository.save(profile);
    }

    public Profile addExperience(Profile profile, ExperienceDto experienceDto) {
        if(experienceDto.getInstitut().getId() != null)
            experienceDto.setInstitut(institutRepository.findById(experienceDto.getInstitut().getId()).get());
        else
            experienceDto.setInstitut(institutRepository.save(experienceDto.getInstitut()));

        Experience experience = experienceRepository.save(experienceMapper.experienceDtoToExperience(experienceDto));
        profile.getExperiences().add(experience);
        return profileRepository.save(profile);

    }

    public Profile findById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.orElse(null);
    }
}
