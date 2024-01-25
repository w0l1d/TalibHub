package org.ilisi.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.ilisi.backend.model.Poste;
import org.ilisi.backend.model.User;

@Data
@AllArgsConstructor
@ToString
@Builder
public class CommentDto {

    private String id;
    private String content;
    private Poste poste;
    private User user;

}
