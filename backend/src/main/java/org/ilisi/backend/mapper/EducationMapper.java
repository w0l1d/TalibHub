package org.ilisi.backend.mapper;


import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.model.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public EducationDto educationToEducationDto(Education education) {
        return EducationDto.builder()
                .id(education.getId())
                .title(education.getTitle())
                .studyField(education.getStudyField())
                .description(education.getDescription())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .location(education.getLocation())
                .build();
    }

    public Education educationDtoToEducation(EducationDto educationDto) {
        return Education.builder()
                .id(educationDto.getId())
                .title(educationDto.getTitle())
                .studyField(educationDto.getStudyField())
                .description(educationDto.getDescription())
                .startDate(educationDto.getStartDate())
                .endDate(educationDto.getEndDate())
                .location(educationDto.getLocation())
                .build();
    }
}
