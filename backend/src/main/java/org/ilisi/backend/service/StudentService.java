package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private ProfileRepository profileRepository;

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
