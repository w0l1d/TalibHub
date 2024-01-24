package org.ilisi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Student;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ReviewDto {
    private String id;
    private Student student;
    private Institut institut;
    private String review;
    private int rating;
}
