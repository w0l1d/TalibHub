package org.ilisi.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ilisi.backend.model.Institut;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EducationDto {
    private String id;
    @NotNull
    private String title;
    @NotNull
    private String studyField;
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private YearMonth startAt;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private YearMonth endAt;
    private String location;
    @NotNull
    private Institut institut;
}
