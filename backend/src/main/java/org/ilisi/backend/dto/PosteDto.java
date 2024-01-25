package org.ilisi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.User;


@Data
@AllArgsConstructor
@ToString
@Builder
public class PosteDto {
    private String id;
    private String titre;
    private String description;
    private String image;
    private User user;

}
