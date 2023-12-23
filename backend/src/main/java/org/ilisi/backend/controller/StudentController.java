package org.ilisi.backend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
@Slf4j
public class StudentController {

    private StudentService studentService;



    @PostMapping("/saveAll")
    public List<Student> createStudents(@RequestBody List<Student> students) {
        return studentService.createStudents(students);
    }
}
