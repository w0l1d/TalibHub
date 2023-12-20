package org.ilisi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.mapper.EducationMapper;
import org.ilisi.backend.mapper.ExperienceMapper;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.repository.EducationRepository;
import org.ilisi.backend.repository.ExperienceRepository;
import org.ilisi.backend.repository.InstitutRepository;
import org.ilisi.backend.repository.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ProfileServiceTests {
    @Mock
    private ProfileRepository profileRepository;;
    @Mock
    private InstitutRepository institutRepository;
    @Mock
    private EducationRepository educationRepository;
    @Mock
    private ExperienceRepository experienceRepository;
    @Mock
    private EducationMapper educationMapper;
    @Mock
    private ExperienceMapper experienceMapper;
    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getAllReturnsProfiles() {

        //arrange
        List<Profile> profiles = List.of(Profile.builder().build(), Profile.builder().build());
        Mockito.when(profileRepository.findAll()).thenReturn(profiles);
        //act
        List<Profile> result = profileService.getAll();
        //assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void findByIdReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().id(UUID.randomUUID().toString()).build();

        //act
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        Profile result = profileService.findById(profile.getId());

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(profile.getId(), result.getId());
    }

    @Test
    public void addEducationWithExistingInstitutReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startDate("2019-01-01")
                .endDate("2020-01-01")
                .institut(Institut.builder().name("institut").build()).build();
        Education education = Education.builder()
                .title(educationDto.getTitle())
                .studyField(educationDto.getStudyField())
                .startDate(educationDto.getStartDate())
                .endDate(educationDto.getEndDate())
                .institut(educationDto.getInstitut()).build();
        //act
        Mockito.when(institutRepository.findById(educationDto.getInstitut().getId())).thenReturn(Optional.of(education.getInstitut()));
        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenReturn(education);
        Mockito.when(educationRepository.save(education)).thenReturn(education);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addEducation(profile, educationDto);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(1, result.getEducations().size());
    }

    @Test
    public void addExperienceWithExistingInstitutReturnsProfile(){
        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startDate("2019-01-01")
                .endDate("2020-01-01")
                .institut(Institut.builder().name("institut").build()).build();

        Experience experience = Experience.builder()
                .title(experienceDto.getTitle())
                .description(experienceDto.getDescription())
                .startDate(experienceDto.getStartDate())
                .endDate(experienceDto.getEndDate())
                .institut(experienceDto.getInstitut()).build();

        //act
        Mockito.when(institutRepository.findById(experienceDto.getInstitut().getId())).thenReturn(Optional.of(experience.getInstitut()));
        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenReturn(experience);
        Mockito.when(experienceRepository.save(experience)).thenReturn(experience);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addExperience(profile, experienceDto);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getExperiences().isEmpty());
        Assertions.assertEquals(1, result.getExperiences().size());
    }
}
