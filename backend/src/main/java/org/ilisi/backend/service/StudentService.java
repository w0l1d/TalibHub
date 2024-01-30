package org.ilisi.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.ilisi.backend.email.EmailService;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.repository.ProfileRepository;
import org.ilisi.backend.repository.StudentRepository;
import org.ilisi.backend.specs.StudentSpecifications;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class StudentService {

    private StudentRepository studentRepository;
    private ProfileRepository profileRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public List<Student> createStudents(@RequestBody List<Student> students) {
        List<Profile> profiles = new ArrayList<>();
        String password = generateRandomPassword();
        students.forEach(student -> student.setPassword(passwordEncoder.encode(password)));

        students.forEach(student -> student.setProfile(new Profile()));

        studentRepository.saveAll(students);
        profileRepository.saveAll(profiles);

        // Send email to students with their passwords
        students.forEach(student ->
            emailService.sendSimpleMessage(
                    student.getEmail(),
                    "Welcome to ILISI",
                    """
                            <h1>Welcome to ILISI</h1>
                            <p>Your password is: """ + password
            )
        );

        return students;
    }

    public DataTablesOutput<Student> getDataTableStudents(DataTablesInput input) {
        return studentRepository.findAll(input);
    }


    private String generateRandomPassword() {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());
            sb.append(alphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public List<Student> searchStudents(@Length(min = 3) String query) {
        return studentRepository.findAll(StudentSpecifications.hasKeyword(query));
    }
}
