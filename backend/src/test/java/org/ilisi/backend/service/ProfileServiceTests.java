package org.ilisi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.mapper.EducationMapper;
import org.ilisi.backend.mapper.ExperienceMapper;
import org.ilisi.backend.model.Education;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class ProfileServiceTests {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private EducationRepository educationRepository;
    @Mock
    private InstitutRepository institutRepository;
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

    /*@Test
    public void addEducationsReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().id(UUID.randomUUID().toString()).build();
        Education education1 = Education.builder().institut(Institut.builder().id(null).name("institut1").build()).build();
        Education education2 = Education.builder().institut(Institut.builder().id(UUID.randomUUID().toString()).build()).build();
        List<Education> educations = List.of(education1, education2);


        //act
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        Mockito.when(institutRepository.findById(education2.getInstitut().getId())).thenReturn(Optional.of(education2.getInstitut()));
        Mockito.when(institutRepository.save(education1.getInstitut())).thenReturn(education1.getInstitut());
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addEducations(ProfileDto.builder().id(profile.getId()).educations(educations).build());

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(2, result.getEducations().size());

    }*/

    /*@Test
    public void addExperiencesReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().id(UUID.randomUUID().toString()).build();
        Experience experience1 = Experience.builder().institut(Institut.builder().id(null).name("institut1").build()).build();
        Experience experience2 = Experience.builder().institut(Institut.builder().id(UUID.randomUUID().toString()).build()).build();
        List<Experience> experiences = List.of(experience1, experience2);

        //act
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        Mockito.when(institutRepository.findById(experience2.getInstitut().getId())).thenReturn(Optional.of(experience2.getInstitut()));
        Mockito.when(institutRepository.save(experience1.getInstitut())).thenReturn(experience1.getInstitut());
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addExperiences(ProfileDto.builder().id(profile.getId()).experiences(experiences).build());

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getExperiences().isEmpty());
        Assertions.assertEquals(2, result.getExperiences().size());
    }*/

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
        Profile profile = Profile.builder().id(UUID.randomUUID().toString()).build();
        Education education = Education.builder().institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut1").build()).build();
        //act
        Mockito.when(institutRepository.findById(education.getInstitut().getId())).thenReturn(Optional.of(education.getInstitut()));
        Profile result = profileService.addEducation(profile, educationMapper.educationToEducationDto(education));
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(1, result.getEducations().size());
    }
}
