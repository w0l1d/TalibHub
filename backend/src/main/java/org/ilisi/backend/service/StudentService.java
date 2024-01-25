package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ilisi.backend.email.EmailService;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private ProfileRepository profileRepository;
    private EmailService emailService;

    @Transactional
    public List<Student> createStudents(@RequestBody List<Student> students) {
        List<Profile> profiles = new ArrayList<>();
        students.forEach(student -> student.setPassword(GenerateRandomPassword()));

        students.forEach(student -> {
            Profile profile = Profile.builder().student(student).build();
            profiles.add(profile);
        });

        studentRepository.saveAll(students);
        profileRepository.saveAll(profiles);

        // Send email to students with their passwords
        students.forEach(student -> {
            emailService.sendSimpleMessage(
                    student.getEmail(),
                    "Welcome to ILISI",
                    """
                            <h1>Welcome to ILISI</h1>
                            <p>Your password is: """ + student.getPassword()
            );
        });

        return students;
    }

    public DataTablesOutput<Student> getDataTableStudents(DataTablesInput input) {
        return studentRepository.findAll(input);
    }


    private String GenerateRandomPassword() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
