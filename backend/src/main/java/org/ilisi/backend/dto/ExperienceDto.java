package org.ilisi.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.Institut;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ExperienceDto {

    private String id;

    @NotBlank
    private String title;
    private String description;
    @NotNull
    private YearMonth startAt;
    @NotNull
    private YearMonth endAt;
    private String location;
    @NotNull
    private Institut institut;
}
