package org.ilisi.backend.controller;


import lombok.RequiredArgsConstructor;
import org.ilisi.backend.entity.Student;
import org.ilisi.backend.entity.User;
import org.ilisi.backend.repository.StudentRepository;
import org.ilisi.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    @PostMapping("/create")
    public Student createStudent(@RequestBody Student student) {
        Optional<User> user = Optional.ofNullable(userRepository.
                findByEmail(student.getEmail()).
                orElseThrow(() -> new RuntimeException("User not found")));

        student.setUser(user.get());
        return studentRepository.save(student);
    }
}
