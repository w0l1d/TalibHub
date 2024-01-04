package org.ilisi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
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

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class ProfileServiceTests {
    @Mock
    private ProfileRepository profileRepository;
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
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAllReturnsProfiles() {

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
    void findByIdReturnsProfile() {

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
    void addEducationWithExistingInstitutReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();


        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenCallRealMethod();

        Education education = educationMapper.educationDtoToEducation(educationDto);

        //act
        Mockito.when(institutRepository.findById(educationDto.getInstitut().getId())).thenReturn(Optional.of(education.getInstitut()));
        Mockito.when(educationRepository.save(education)).thenReturn(education);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addEducation(profile, educationDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(1, result.getEducations().size());
    }

    @Test
    void addExperienceWithExistingInstitutReturnsProfile() {
        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenCallRealMethod();

        Experience experience = experienceMapper.experienceDtoToExperience(experienceDto);

        //act
        Mockito.when(institutRepository.findById(experienceDto.getInstitut().getId())).thenReturn(Optional.of(experience.getInstitut()));
        Mockito.when(experienceRepository.save(experience)).thenReturn(experience);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addExperience(profile, experienceDto);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getExperiences().isEmpty());
        Assertions.assertEquals(1, result.getExperiences().size());
    }


    @Test
    void testYearMonthParsedFromString() {
        //arrange
        String yearMonth = "2020-01";
        //act
        YearMonth result = YearMonth.parse(yearMonth);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2020, result.getYear());
        Assertions.assertEquals(1, result.getMonthValue());
    }

    @Test
    void addEducationThrowsExceptionWhenInstituteNotFound() {
        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        //act
        Mockito.when(institutRepository.findById(educationDto.getInstitut().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.addEducation(profile, educationDto)
        );

        //assert
        assertEquals(String.format("Institute with id %s not found", educationDto.getInstitut().getId()), exception.getMessage());
    }

    @Test
    void addExperienceThrowsExceptionWhenInstituteNotFound() {
        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        //act
        Mockito.when(institutRepository.findById(experienceDto.getInstitut().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.addExperience(profile, experienceDto)
        );

        //assert
        assertEquals(String.format("Institute with id %s not found", experienceDto.getInstitut().getId()), exception.getMessage());
    }

    @Test
    void addEducationWithNullOrBlankInstituteIdReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();


        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().name("institut").build()).build();

        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenCallRealMethod();

        Education education = educationMapper.educationDtoToEducation(educationDto);

        //act
        Mockito.when(institutRepository.save(educationDto.getInstitut())).thenReturn(education.getInstitut());
        Mockito.when(educationRepository.save(education)).thenReturn(education);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addEducation(profile, educationDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(1, result.getEducations().size());
    }

    @Test
    void addExperienceWithNullOrBlankInstituteIdReturnsProfile() {
        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().name("institut").build()).build();

        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenCallRealMethod();

        Experience experience = experienceMapper.experienceDtoToExperience(experienceDto);

        //act
        Mockito.when(institutRepository.save(experienceDto.getInstitut())).thenReturn(experience.getInstitut());
        Mockito.when(experienceRepository.save(experience)).thenReturn(experience);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addExperience(profile, experienceDto);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getExperiences().isEmpty());
        Assertions.assertEquals(1, result.getExperiences().size());
    }

    @Test
    void updateEducationWithExistingEducationAndInstituteReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        Education education = Education.builder()
                .id(UUID.randomUUID().toString())
                .title("title1")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getEducations().add(education);

        EducationDto educationDto = EducationDto.builder()
                .title("title2")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenCallRealMethod();

        Education educationUpdated = educationMapper.educationDtoToEducation(educationDto);
        educationUpdated.setId(education.getId());

        //act
        Mockito.when(educationRepository.findById(education.getId())).thenReturn(Optional.of(education));
        Mockito.when(institutRepository.findById(educationDto.getInstitut().getId())).thenReturn(Optional.of(educationUpdated.getInstitut()));
        Mockito.when(educationRepository.save(educationUpdated)).thenReturn(educationUpdated);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.updateEducation(profile, education.getId(), educationDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getEducations().isEmpty());
        Assertions.assertEquals(1, result.getEducations().size());

    }

    @Test
    void updateExperienceWithExistingExperienceAndInstituteReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getExperiences().add(experience);

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenCallRealMethod();

        Experience experienceUpdated = experienceMapper.experienceDtoToExperience(experienceDto);
        experienceUpdated.setId(experience.getId());

        //act
        Mockito.when(experienceRepository.findById(experience.getId())).thenReturn(Optional.of(experience));
        Mockito.when(institutRepository.findById(experienceDto.getInstitut().getId())).thenReturn(Optional.of(experienceUpdated.getInstitut()));
        Mockito.when(experienceRepository.save(experienceUpdated)).thenReturn(experienceUpdated);
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.updateExperience(profile, experience.getId(), experienceDto);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getExperiences().isEmpty());
        Assertions.assertEquals(1, result.getExperiences().size());

    }
    
    @Test
    void updateEducationWithInexistingEducationThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        Education education = Education.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getEducations().add(education);

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenCallRealMethod();

        Education educationUpdated = educationMapper.educationDtoToEducation(educationDto);
        educationUpdated.setId(education.getId());

        //act
        Mockito.when(educationRepository.findById(education.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateEducation(profile, education.getId(), educationDto)
        );

        //assert
        assertEquals(String.format("Education with id %s not found", education.getId()), exception.getMessage());

    }

    @Test
    void updateExperienceWithInexistingExperienceThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getExperiences().add(experience);

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenCallRealMethod();

        Experience experienceUpdated = experienceMapper.experienceDtoToExperience(experienceDto);
        experienceUpdated.setId(experience.getId());

        //act
        Mockito.when(experienceRepository.findById(experience.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateExperience(profile, experience.getId(), experienceDto)
        );

        //assert
        assertEquals(String.format("Experience with id %s not found", experience.getId()), exception.getMessage());

    }

    @Test
    void updateEducationWithInexistingInstituteThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        Education education = Education.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getEducations().add(education);

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(educationMapper.educationDtoToEducation(educationDto)).thenCallRealMethod();

        Education educationUpdated = educationMapper.educationDtoToEducation(educationDto);
        educationUpdated.setId(education.getId());

        //act
        Mockito.when(educationRepository.findById(education.getId())).thenReturn(Optional.of(education));
        Mockito.when(institutRepository.findById(educationDto.getInstitut().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateEducation(profile, education.getId(), educationDto)
        );

        //assert
        assertEquals(String.format("Institute with id %s not found", educationDto.getInstitut().getId()), exception.getMessage());

    }

    @Test
    void updateExperienceWithInexistingInstituteThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        profile.getExperiences().add(experience);

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        Mockito.when(experienceMapper.experienceDtoToExperience(experienceDto)).thenCallRealMethod();

        Experience experienceUpdated = experienceMapper.experienceDtoToExperience(experienceDto);
        experienceUpdated.setId(experience.getId());

        //act
        Mockito.when(experienceRepository.findById(experience.getId())).thenReturn(Optional.of(experience));
        Mockito.when(institutRepository.findById(experienceDto.getInstitut().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.updateExperience(profile, experience.getId(), experienceDto)
        );

        //assert
        assertEquals(String.format("Institute with id %s not found", experienceDto.getInstitut().getId()), exception.getMessage());

    }

    @Test
    void deleteEducationWithExistingEducationReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        Education education = Education.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .build();
        profile.getEducations().add(education);

        //act
        Mockito.when(educationRepository.findById(education.getId())).thenReturn(Optional.of(education));
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.deleteEducation(profile, education.getId());

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getEducations().isEmpty());

    }

    @Test
    void deleteExperienceWithExistingExperienceReturnsProfile() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .build();
        profile.getExperiences().add(experience);

        //act
        Mockito.when(experienceRepository.findById(experience.getId())).thenReturn(Optional.of(experience));
        Mockito.when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.deleteExperience(profile, experience.getId());

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getExperiences().isEmpty());

    }

    @Test
    void deleteEducationWithInexistingEducationThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        Education education = Education.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .build();
        profile.getEducations().add(education);

        //act
        Mockito.when(educationRepository.findById(education.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.deleteEducation(profile, education.getId())
        );

        //assert
        assertEquals(String.format("Education with id %s not found", education.getId()), exception.getMessage());

    }

    @Test
    void deleteExperienceWithInexistingExperienceThrowsException() {

        //arrange
        Profile profile = Profile.builder().
                id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .build();
        profile.getExperiences().add(experience);

        //act
        Mockito.when(experienceRepository.findById(experience.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> profileService.deleteExperience(profile, experience.getId())
        );

        //assert
        assertEquals(String.format("Experience with id %s not found", experience.getId()), exception.getMessage());

    }

}
