package org.ilisi.backend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
@Slf4j
public class StudentController {

    private StudentRepository studentRepository;
    private ProfileRepository profileRepository;

    @PostMapping("/saveAll")
    public List<Student> createStudents(@RequestBody List<Student> students) {
        List<Profile> profiles = new ArrayList<>();

        students.forEach(student -> {
            Profile profile = Profile.builder().student(student).build();
            profiles.add(profile);
        });

        studentRepository.saveAll(students);
        profileRepository.saveAll(profiles);

        return students;

    }


}
