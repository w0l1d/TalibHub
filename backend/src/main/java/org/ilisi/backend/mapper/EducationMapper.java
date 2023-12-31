package org.ilisi.backend.mapper;


import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.EducationDto;
import org.ilisi.backend.model.Education;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EducationMapper {

    public EducationDto educationToEducationDto(Education education) {
        log.info("educationToEducationDto");
        return EducationDto.builder()
                .id(education.getId())
                .title(education.getTitle())
                .studyField(education.getStudyField())
                .description(education.getDescription())
                .startAt(education.getStartAt())
                .endAt(education.getEndAt())
                .location(education.getLocation())
                .institut(education.getInstitut())
                .build();
    }

    public Education educationDtoToEducation(EducationDto educationDto) {
        return Education.builder()
                .id(educationDto.getId())
                .title(educationDto.getTitle())
                .studyField(educationDto.getStudyField())
                .description(educationDto.getDescription())
                .startAt(educationDto.getStartAt())
                .endAt(educationDto.getEndAt())
                .location(educationDto.getLocation())
                .institut(educationDto.getInstitut())
                .build();
    }
}
