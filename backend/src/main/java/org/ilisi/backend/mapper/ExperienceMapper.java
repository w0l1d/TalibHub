package org.ilisi.backend.mapper;

import org.ilisi.backend.dto.ExperienceDto;
import org.ilisi.backend.model.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public ExperienceDto experienceToExperienceDto(Experience experience) {
        return ExperienceDto.builder()
                .id(experience.getId())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .location(experience.getLocation())
                .institut(experience.getInstitut())
                .build();
    }

    public Experience experienceDtoToExperience(ExperienceDto experienceDto) {
        return Experience.builder()
                .id(experienceDto.getId())
                .title(experienceDto.getTitle())
                .description(experienceDto.getDescription())
                .startDate(experienceDto.getStartDate())
                .endDate(experienceDto.getEndDate())
                .location(experienceDto.getLocation())
                .institut(experienceDto.getInstitut())
                .build();
    }
}