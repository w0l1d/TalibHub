package org.ilisi.backend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
@Slf4j
public class StudentController {

    private StudentRepository studentRepository;
    private ProfileRepository profileRepository;

    @PostMapping("/createStudents")
    public String createStudents(@RequestBody List<Student> students) {
        List<Profile> profiles = new ArrayList<>();

        students.forEach(student -> {
            Profile profile = Profile.builder().student(student).build();
            profiles.add(profile);
        });

        studentRepository.saveAll(students);
        profileRepository.saveAll(profiles);

        return "Students are created";

    }

}
