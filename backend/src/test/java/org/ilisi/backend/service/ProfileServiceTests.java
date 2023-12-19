package org.ilisi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ProfileDto;
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
    public void addEducationsReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().id(UUID.randomUUID().toString()).build();
        List<Education> educations = List.of(
                Education.builder().institut(Institut.builder().name("institut1").build()).build(),
                Education.builder().institut(Institut.builder().name("institut2").build()).build()
                );
        Mockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        Mockito.when(institutRepository.findByName("institut1")).thenReturn(null);
        Mockito.when(institutRepository.findByName("institut2")).thenReturn(Optional.of(Institut.builder().name("institut2").build()));
        Mockito.when(institutRepository.save(Institut.builder().name("institut1").build())).thenReturn(Institut.builder().name("institut1").build());

        //act
        Profile result = profileService.addEducations(ProfileDto.builder().id(profile.getId()).educations(educations).build());
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(2, result.getEducations().size());

    }

}
