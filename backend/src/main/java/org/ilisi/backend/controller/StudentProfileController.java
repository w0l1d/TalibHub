package org.ilisi.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.ProfileDto;
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


    @PostMapping("/addEducations")
    public Profile addEducations(@RequestBody ProfileDto profileDto) {
        return profileService.addEducations(profileDto);
    }

    @PostMapping("/addExperiences")
    public Profile addExperiences(@RequestBody ProfileDto profileDto) {
        return profileService.addExperiences(profileDto);
    }
}
