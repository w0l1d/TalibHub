package org.ilisi.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
class StudentProfileControllerUnitTests {

    @Mock
    private ProfileService profileService;
    @InjectMocks
    private StudentProfileController studentProfileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEducationReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();
        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.addEducation(profile, educationDto)).thenReturn(profile);

        Profile result = studentProfileController.addEducation(profile.getId(), educationDto);
        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void addEducationThrowsExceptionWhenProfileDoesNotExist() {

        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .educations(new ArrayList<>())
                .build();

        EducationDto educationDto = EducationDto.builder()
                .title("title")
                .studyField("studyField")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        //act
        when(profileService.findById(profile.getId())).thenReturn(null);
        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.addEducation(profile.getId(), educationDto)
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());

    }

    @Test
    void updateEducationReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
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
         //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.updateEducation(profile, education.getId(), educationDto)).thenReturn(profile);

        Profile result = studentProfileController.updateEducation(profile.getId(), education.getId(), educationDto);
        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void updateThrowsExceptionWhenProfileDoesNotExist() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
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
        //act
        when(profileService.findById(profile.getId())).thenReturn(null);
        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.updateEducation(profile.getId(), education.getId(), educationDto)
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());

    }

    @Test
    void deleteEducationReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
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

        //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.deleteEducation(profile, education.getId())).thenReturn(profile);

        Profile result = studentProfileController.deleteEducation(profile.getId(), education.getId());

        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void deleteEducationThrowsExceptionWhenProfileDoesNotExist() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
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

        //act
        when(profileService.findById(profile.getId())).thenReturn(null);
        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.deleteEducation(profile.getId(), education.getId())
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());
    }

    @Test
    void addExperienceReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();
        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.addExperience(profile, experienceDto)).thenReturn(profile);

        Profile result = studentProfileController.addExperience(profile.getId(), experienceDto);
        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void addExperienceThrowsExceptionWhenProfileDoesNotExist() {

        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();
        //act
        when(profileService.findById(profile.getId())).thenReturn(null);
        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.addExperience(profile.getId(), experienceDto)
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());

    }

    @Test
    void updateExperienceReturnsProfileWhenProfileExists() {

        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();
        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build())
                .build();
        profile.getExperiences().add(experience);

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.updateExperience(profile, experience.getId(), experienceDto)).thenReturn(profile);

        Profile result = studentProfileController.updateExperience(profile.getId(), experience.getId(), experienceDto);

        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void updateExperienceThrowsExceptionWhenProfileDoesNotExist() {

        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();
        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build())
                .build();
        profile.getExperiences().add(experience);

        ExperienceDto experienceDto = ExperienceDto.builder()
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build()).build();

        //act
        when(profileService.findById(profile.getId())).thenReturn(null);

        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.updateExperience(profile.getId(), experience.getId(), experienceDto)
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());
    }

    @Test
    void deleteExperienceReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();
        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build())
                .build();
        profile.getExperiences().add(experience);

        //act
        when(profileService.findById(profile.getId())).thenReturn(profile);
        when(profileService.deleteExperience(profile, experience.getId())).thenReturn(profile);

        Profile result = studentProfileController.deleteExperience(profile.getId(), experience.getId());

        //assert
        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
    }

    @Test
    void deleteExperienceThrowsExceptionWhenProfileDoesNotExist() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
                .build();
        Experience experience = Experience.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startAt(YearMonth.of(2019, 1))
                .endAt(YearMonth.of(2020, 1))
                .institut(Institut.builder().id(UUID.randomUUID().toString()).name("institut").build())
                .build();
        profile.getExperiences().add(experience);

        //act
        when(profileService.findById(profile.getId())).thenReturn(null);
        //assert
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentProfileController.deleteExperience(profile.getId(), experience.getId())
        );
        assertEquals(String.format("Profile with id %s not found", profile.getId()), exception.getMessage());
    }

}
