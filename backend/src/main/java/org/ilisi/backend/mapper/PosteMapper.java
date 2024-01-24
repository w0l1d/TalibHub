package org.ilisi.backend.mapper;


import org.ilisi.backend.dto.PosteDto;
import org.ilisi.backend.model.Poste;
import org.springframework.stereotype.Component;

@Component
public class PosteMapper {

    public PosteDto posteToPosteDto(Poste poste) {
        return PosteDto.builder()
                .id(poste.getId())
                .titre(poste.getTitre())
                .description(poste.getDescription())
                .image(poste.getImage())
                .user(poste.getUser())
                .build();
    }

    public Poste posteDtoToPoste(PosteDto posteDto) {
        return Poste.builder()
                .id(posteDto.getId())
                .titre(posteDto.getTitre())
                .description(posteDto.getDescription())
                .image(posteDto.getImage())
                .user(posteDto.getUser())
                .build();
    }
}
