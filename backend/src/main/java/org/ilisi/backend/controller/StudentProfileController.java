package org.ilisi.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.service.ProfileService;
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
    @GetMapping("")
    public List<Profile> getProfiles() {
        return profileService.getAll();
    }

    // get authentified user profile
    @GetMapping("/me")
    public Profile getProfile(Authentication authentication) {
        String email = authentication.getName();
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
}
