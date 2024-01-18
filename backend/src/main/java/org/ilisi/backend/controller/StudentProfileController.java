package org.ilisi.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.model.User;
import org.ilisi.backend.service.ProfileService;
import org.ilisi.backend.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Slf4j
public class StudentProfileController {

    private final ProfileService profileService;
    private final StudentService studentService;
    @GetMapping("")
    public List<Profile> getProfiles() {
        return profileService.getAll();
    }

    // get authentified user profile
    @GetMapping("/me")
    public Profile getProfile(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        String email = user.getEmail();
        log.info("get profile for user with email {}", email);
        return profileService.getProfile(email);
    }


    @PostMapping("/{profileId}/educations")
    public Profile addEducation(@PathVariable String profileId, @RequestBody @Valid EducationDto educationDto) {
        Profile profile = profileService.findById(profileId);
        if(profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.addEducation(profile, educationDto);
    }

    @PutMapping("/{profileId}/educations/{educationId}")
    public Profile updateEducation(@PathVariable String profileId, @PathVariable String educationId, @RequestBody @Valid EducationDto educationDto) {
        Profile profile = profileService.findById(profileId);
        if (profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.updateEducation(profile, educationId, educationDto);
    }

    @DeleteMapping("/{profileId}/educations/{educationId}")
    public Profile deleteEducation(@PathVariable String profileId, @PathVariable String educationId) {
        Profile profile = profileService.findById(profileId);
        if (profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.deleteEducation(profile, educationId);
    }

    @PostMapping("/{profileId}/experiences")
    public Profile addExperience(@PathVariable String profileId, @RequestBody @Valid ExperienceDto experienceDto) {
        Profile profile = profileService.findById(profileId);
        if(profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.addExperience(profile, experienceDto);
    }

    @PutMapping("/{profileId}/experiences/{experienceId}")
    public Profile updateExperience(@PathVariable String profileId, @PathVariable String experienceId, @RequestBody @Valid ExperienceDto experienceDto) {
        Profile profile = profileService.findById(profileId);
        if (profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.updateExperience(profile, experienceId, experienceDto);
    }

    @DeleteMapping("/{profileId}/experiences/{experienceId}")
    public Profile deleteExperience(@PathVariable String profileId, @PathVariable String experienceId) {
        Profile profile = profileService.findById(profileId);
        if (profile == null) {
            String errorMessage = String.format("Profile with id %s not found", profileId);
            log.error(errorMessage);
            throw new EntityNotFoundException(errorMessage, "PROFILE_NOT_FOUND");
        }
        return profileService.deleteExperience(profile, experienceId);
    }

    @GetMapping("/search")
    public List<Profile> searchProfilesByKeyword(@RequestParam("query") @Length(min = 3) String keyword) {
        return studentService.searchStudents(keyword).stream().map(Student::getProfile).toList();
    }
}
