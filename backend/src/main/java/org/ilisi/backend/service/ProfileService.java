package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.mapper.EducationMapper;
import org.ilisi.backend.mapper.ExperienceMapper;
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
    private EducationRepository educationRepository;
    private InstitutRepository institutRepository;
    private ExperienceRepository experienceRepository;
    private EducationMapper educationMapper;
    private ExperienceMapper experienceMapper;

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }


    public Profile addEducation(Profile profile, EducationDto educationDto) {
        if(educationDto.getInstitut().getId() != null)
            educationDto.setInstitut(institutRepository.findById(educationDto.getInstitut().getId()).get());
        else
            educationDto.setInstitut(institutRepository.save(educationDto.getInstitut()));

        profile.getEducations().add(educationMapper.educationDtoToEducation(educationDto));
        return profileRepository.save(profile);
    }

    public Profile addExperience(Profile profile, ExperienceDto experienceDto) {
        if(experienceDto.getInstitut().getId() != null)
            experienceDto.setInstitut(institutRepository.findById(experienceDto.getInstitut().getId()).get());
        else
            experienceDto.setInstitut(institutRepository.save(experienceDto.getInstitut()));

        profile.getExperiences().add(experienceMapper.experienceDtoToExperience(experienceDto));
        return profileRepository.save(profile);

    }

    public Profile findById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.orElse(null);
    }
}
