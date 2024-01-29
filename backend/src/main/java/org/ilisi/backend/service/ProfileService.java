package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.mapper.EducationMapper;
import org.ilisi.backend.mapper.ExperienceMapper;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
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

    public Profile getProfile(String email) {
        return profileRepository.findByStudentEmail(email);
    }

    public Profile findById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.orElse(null);
    }

    public Profile updateProfile(String profileId, Profile profile) {
        Profile profileResult = this.findById(profileId);
        if (profileResult == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        profileResult.setId(profileId);
        profileResult.setAbout(profile.getAbout());
        Student student = profileResult.getStudent();
        student.setEmail(profile.getStudent().getEmail());
        student.setPhone(profile.getStudent().getPhone());

        if (profile.getStudent().getImageUri() != null && !profile.getStudent().getImageUri().isBlank())
            student.setImageUri(profile.getStudent().getImageUri());

        profileResult.setStudent(student);
        return profileRepository.save(profileResult);
    }

    public Profile addEducation(Profile profile, EducationDto educationDto) {

        String institute = educationDto.getInstitut().getId();

        if (institute == null || institute.isBlank())
            educationDto.setInstitut(institutRepository.save(educationDto.getInstitut()));
        else
            institutRepository.findById(institute).ifPresentOrElse(educationDto::setInstitut, () -> {
                throw new EntityNotFoundException(String.format("Institute with id %s not found", institute), "INSTITUTE_NOT_FOUND");
            });
        Education education = educationRepository.save(educationMapper.educationDtoToEducation(educationDto));
        profile.getEducations().add(education);
        return profileRepository.save(profile);
    }

    public Profile updateEducation(Profile profile, String educationId, EducationDto educationDto) {
        Education education = educationRepository.findById(educationId).orElse(null);
        if (education == null) {
            String errorMessage = String.format("Education with id %s not found", educationId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "EDUCATION_NOT_FOUND");
        }
        String instituteId = educationDto.getInstitut().getId();
        if (instituteId == null || instituteId.isBlank())
            educationDto.setInstitut(institutRepository.save(educationDto.getInstitut()));
        else
            institutRepository.findById(instituteId).ifPresentOrElse(educationDto::setInstitut, () -> {
                throw new EntityNotFoundException(String.format("Institute with id %s not found", instituteId), "INSTITUTE_NOT_FOUND");
            });

        education = educationMapper.educationDtoToEducation(educationDto);
        education.setId(educationId);
        educationRepository.save(education);
        return profileRepository.save(profile);
    }

    public Profile deleteEducation(Profile profile, String educationId) {
        Education education = educationRepository.findById(educationId).orElse(null);
        if (education == null) {
            String errorMessage = String.format("Education with id %s not found", educationId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "EDUCATION_NOT_FOUND");
        }
        profile.getEducations().remove(education);
        educationRepository.delete(education);
        return profileRepository.save(profile);
    }

    public Profile addExperience(Profile profile, ExperienceDto experienceDto) {

        String instituteId = experienceDto.getInstitut().getId();

        if (instituteId == null || instituteId.isBlank())
            experienceDto.setInstitut(institutRepository.save(experienceDto.getInstitut()));
        else
            institutRepository.findById(instituteId).ifPresentOrElse(experienceDto::setInstitut, () -> {
                throw new EntityNotFoundException(String.format("Institute with id %s not found", instituteId), "INSTITUTE_NOT_FOUND");
            });

        Experience experience = experienceRepository.save(experienceMapper.experienceDtoToExperience(experienceDto));
        profile.getExperiences().add(experience);
        return profileRepository.save(profile);

    }

    public Profile updateExperience(Profile profile, String experienceId, ExperienceDto experienceDto) {
        Experience experience = experienceRepository.findById(experienceId).orElse(null);
        if (experience == null) {
            String errorMessage = String.format("Experience with id %s not found", experienceId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "EXPERIENCE_NOT_FOUND");
        }
        String instituteId = experienceDto.getInstitut().getId();
        if (instituteId == null || instituteId.isBlank())
            experienceDto.setInstitut(institutRepository.save(experienceDto.getInstitut()));
        else
            institutRepository.findById(instituteId).ifPresentOrElse(experienceDto::setInstitut, () -> {
                throw new EntityNotFoundException(String.format("Institute with id %s not found", instituteId), "INSTITUTE_NOT_FOUND");
            });

        experience = experienceMapper.experienceDtoToExperience(experienceDto);
        experience.setId(experienceId);
        experienceRepository.save(experience);
        return profileRepository.save(profile);
    }

    public Profile deleteExperience(Profile profile, String experienceId) {
        Experience experience = experienceRepository.findById(experienceId).orElse(null);
        if (experience == null) {
            String errorMessage = String.format("Experience with id %s not found", experienceId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "EXPERIENCE_NOT_FOUND");
        }
        profile.getExperiences().remove(experience);
        experienceRepository.delete(experience);
        return profileRepository.save(profile);
    }


}
