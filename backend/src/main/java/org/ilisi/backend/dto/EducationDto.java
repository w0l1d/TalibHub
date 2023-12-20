package org.ilisi.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ilisi.backend.model.Institut;

@Data
@AllArgsConstructor @NoArgsConstructor
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
    private String startDate;
    @NotNull
    private String endDate;
    private String location;
    @NotNull
    private Institut institut;
}
