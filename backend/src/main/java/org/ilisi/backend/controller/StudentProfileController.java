package org.ilisi.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.service.ProfileService;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/{profileId}/educations")
    public Profile addEducations(@PathVariable String profileId, @RequestBody @Valid EducationDto educationDto) {
        Profile profile = profileService.findById(profileId);
        if(profile == null) {
            log.error("Profile not found");
            throw new RuntimeException("Profile +" + profileId + " not found");
        }
        return profileService.addEducation(profile, educationDto);
    }

    @PostMapping("/{profileId}/experiences")
    public Profile addExperience(@PathVariable String profileId, @RequestBody @Valid ExperienceDto experienceDto) {
        Profile profile = profileService.findById(profileId);
        if(profile == null) {
            log.error("Profile not found");
            throw new RuntimeException("Profile +" + profileId + " not found");
        }
        return profileService.addExperience(profile, experienceDto);
    }
}
