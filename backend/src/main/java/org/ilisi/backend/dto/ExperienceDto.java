package org.ilisi.backend.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.Institut;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ExperienceDto {

    private String id;

    @NotNull
    private String title;
    private String description;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private String location;
    @NotNull
    private Institut institut;
}
