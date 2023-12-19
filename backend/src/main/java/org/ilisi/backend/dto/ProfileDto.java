package org.ilisi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Student;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ProfileDto {
    private String id;
    private String about;
    private List<Education> educations;
    private List<Experience> experiences;
    private Student student;
}
