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
                .startAt(experience.getStartAt())
                .endAt(experience.getEndAt())
                .location(experience.getLocation())
                .institut(experience.getInstitut())
                .build();
    }

    public Experience experienceDtoToExperience(ExperienceDto experienceDto) {
        return Experience.builder()
                .id(experienceDto.getId())
                .title(experienceDto.getTitle())
                .description(experienceDto.getDescription())
                .startAt(experienceDto.getStartAt())
                .endAt(experienceDto.getEndAt())
                .location(experienceDto.getLocation())
                .institut(experienceDto.getInstitut())
                .build();
    }
}