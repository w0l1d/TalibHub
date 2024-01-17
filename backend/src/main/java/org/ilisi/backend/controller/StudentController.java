package org.ilisi.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.service.StudentService;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
@Slf4j
@Validated
public class StudentController {

    private StudentService studentService;

    @PostMapping("/saveAll")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Student> createStudents(@RequestBody List<Student> students) {
        List<Student> savedStudents = studentService.createStudents(students);
        log.info("Saved {} students", savedStudents.size());
        log.info("Saved students: {}", savedStudents);
        return savedStudents;
    }

    @PostMapping("/data")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public DataTablesOutput<Student> getStudents(@Valid @RequestBody DataTablesInput input) {
        return studentService.getDataTableStudents(input);
    }

    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam @Length(min = 3) String query) {

        return studentService.searchStudents(query);
    }


}
