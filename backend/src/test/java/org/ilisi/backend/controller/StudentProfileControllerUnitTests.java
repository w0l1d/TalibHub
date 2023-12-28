package org.ilisi.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
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
    void addExperienceReturnsProfileWhenProfileExists() {
        //arrange
        Profile profile = Profile.builder()
                .id(UUID.randomUUID().toString())
                .experiences(new ArrayList<>())
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

}
