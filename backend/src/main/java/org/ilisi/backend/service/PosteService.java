package org.ilisi.backend.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.CommentDto;
import org.ilisi.backend.dto.PosteDto;
import org.ilisi.backend.exception.EntityNotFoundException;
import org.ilisi.backend.mapper.CommentMapper;
import org.ilisi.backend.mapper.PosteMapper;
import org.ilisi.backend.model.Comment;
import org.ilisi.backend.model.Poste;
import org.ilisi.backend.model.User;
import org.ilisi.backend.repository.CommentRepository;
import org.ilisi.backend.repository.PosteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PosteService {

    private final PosteRepository posteRepository;
    private final CommentRepository commentRepository;
    private final PosteMapper posteMapper;
    private final CommentMapper commentMapper;


    public List<Poste> getAllPostes() {
        return posteRepository.findAll();
    }

    public Poste findPosteById(String id) {
        return posteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Poste with id %s not found", id), "POSTE_NOT_FOUND"));
    }

    public Poste addNewPoste(PosteDto posteDto) {
        Poste poste = posteMapper.posteDtoToPoste(posteDto);
        return posteRepository.save(poste);
    }

    public Poste updatePoste(String posteId, PosteDto posteDto) {
        Poste posteToUpdate = findPosteById(posteId);
        Poste poste = posteMapper.posteDtoToPoste(posteDto);
        poste.setId(posteId);
        poste.setComments(posteToUpdate.getComments());
        return posteRepository.save(poste);
    }

    public void deletePoste(String posteId) {
        posteRepository.deleteById(posteId);
    }

    public List<Comment> getPosteComments(String posteId) {
        Poste poste = findPosteById(posteId);
        return poste.getComments();
    }

    public Comment findCommentById(String posteId, String commentId) {
        Poste poste = findPosteById(posteId);
        return poste.getComments().stream().filter(comment -> comment.getId().equals(commentId)).findFirst().orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id %s not found", commentId), "COMMENT_NOT_FOUND"));
    }


    public Poste addComment(String posteId, CommentDto comment, User user) {
        Poste poste = findPosteById(posteId);
        comment.setUser(user);
        Comment commentToAdd = commentMapper.commentDtoToComment(comment);
        poste.addComment(commentToAdd);
        return posteRepository.save(poste);
    }

    public Poste updateComment(String posteId, String commentId, CommentDto comment, User user) {
        Poste poste = findPosteById(posteId);
        comment.setUser(user);
        findCommentById(posteId, commentId);
        Comment commentToUpdate = commentMapper.commentDtoToComment(comment);
        commentToUpdate.setId(commentId);
        poste.updateComment(commentId, commentToUpdate);
        return posteRepository.save(poste);
    }

    public void deleteComment(String posteId, String commentId) {
        findPosteById(posteId);
        commentRepository.deleteById(commentId);
    }

    public Comment addReply(String posteId, String commentId, CommentDto comment, User user) {
        findPosteById(posteId);
        Comment commentToReply = findCommentById(posteId, commentId);
        comment.setUser(user);
        commentToReply.addReply(commentMapper.commentDtoToComment(comment));
        return commentRepository.save(commentToReply);
    }

    public Comment updateReply(String posteId, String commentId, String replyId, CommentDto comment, User user) {
        findPosteById(posteId);
        Comment commentToReply = findCommentById(posteId, commentId);
        comment.setUser(user);
        findCommentById(posteId, commentId);
        Comment replyToUpdate = commentMapper.commentDtoToComment(comment);
        replyToUpdate.setId(replyId);
        commentToReply.updateComment(replyId, replyToUpdate);
        return commentRepository.save(commentToReply);
    }

    public void deleteReply(String posteId, String commentId, String replyId) {
        findPosteById(posteId);
        findCommentById(posteId, commentId);
        commentRepository.deleteById(replyId);
    }
}
