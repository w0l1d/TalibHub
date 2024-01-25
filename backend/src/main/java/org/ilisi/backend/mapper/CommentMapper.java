package org.ilisi.backend.mapper;


import org.ilisi.backend.dto.CommentDto;
import org.ilisi.backend.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .poste(comment.getPoste())
                .user(comment.getUser())
                .build();
    }

    public Comment commentDtoToComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .content(commentDto.getContent())
                .poste(commentDto.getPoste())
                .user(commentDto.getUser())
                .build();
    }
}
