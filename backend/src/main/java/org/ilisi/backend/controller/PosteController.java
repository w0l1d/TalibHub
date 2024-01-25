package org.ilisi.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilisi.backend.dto.CommentDto;
import org.ilisi.backend.dto.PosteDto;
import org.ilisi.backend.model.Comment;
import org.ilisi.backend.model.Poste;
import org.ilisi.backend.model.User;
import org.ilisi.backend.service.PosteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/postes")
@RequiredArgsConstructor
@Slf4j
public class PosteController {

    private final PosteService posteService;


    @GetMapping("")
    public List<Poste> getAllPostes() {
        log.info("getAllPostes");
        return posteService.getAllPostes();
    }

    @PostMapping("")
    public Poste addNewPoste(@RequestBody PosteDto posteDto, Principal principal) {
        log.info("addNewPoste");
        User user = (User) ((Authentication) principal).getPrincipal();
        posteDto.setUser(user);
        return posteService.addNewPoste(posteDto);
    }

    @PutMapping("/{posteId}")
    public Poste updatePoste(@PathVariable String posteId, @RequestBody PosteDto posteDto, Principal principal) {
        log.info("updatePoste");
        User user = (User) ((Authentication) principal).getPrincipal();
        posteDto.setUser(user);
        return posteService.updatePoste(posteId, posteDto);
    }

    @DeleteMapping("/{posteId}")
    public void deletePoste(@PathVariable String posteId) {
        log.info("deletePoste");
        posteService.deletePoste(posteId);
    }

    @GetMapping("/{posteId}/comments")
    public List<Comment> getPosteComments(@PathVariable String posteId) {
        log.info("getPosteComments");
        return posteService.getPosteComments(posteId);
    }

    @GetMapping("/{posteId}/comments/{commentId}")
    public Comment getComment(@PathVariable String posteId, @PathVariable String commentId) {
        log.info("getComment");
        return posteService.findCommentById(posteId, commentId);
    }

    @PostMapping("/{posteId}/comments")
    public Poste addComment(@PathVariable String posteId, @RequestBody CommentDto comment, Principal principal) {
        log.info("addComment");
        User user = (User) ((Authentication) principal).getPrincipal();
        return posteService.addComment(posteId, comment, user);
    }

    @PutMapping("/{posteId}/comments/{commentId}")
    public Poste updateComment(@PathVariable String posteId, @PathVariable String commentId, @RequestBody CommentDto comment, Principal principal) {
        log.info("updateComment");
        User user = (User) ((Authentication) principal).getPrincipal();
        return posteService.updateComment(posteId, commentId, comment, user);
    }

    @DeleteMapping("/{posteId}/comments/{commentId}")
    public void deleteComment(@PathVariable String posteId, @PathVariable String commentId) {
        log.info("deleteComment");
        posteService.deleteComment(posteId, commentId);
    }

    @PostMapping("/{posteId}/comments/{commentId}/replies")
    public Comment addReply(@PathVariable String posteId, @PathVariable String commentId, @RequestBody CommentDto comment, Principal principal) {
        log.info("addReply");
        User user = (User) ((Authentication) principal).getPrincipal();
        return posteService.addReply(posteId, commentId, comment, user);
    }

    @PutMapping("/{posteId}/comments/{commentId}/replies/{replyId}")
    public Comment updateReply(@PathVariable String posteId, @PathVariable String commentId, @PathVariable String replyId, @RequestBody CommentDto comment, Principal principal) {
        log.info("updateReply");
        User user = (User) ((Authentication) principal).getPrincipal();
        return posteService.updateReply(posteId, commentId, replyId, comment, user);
    }

    @DeleteMapping("/{posteId}/comments/{commentId}/replies/{replyId}")
    public void deleteReply(@PathVariable String posteId, @PathVariable String commentId, @PathVariable String replyId) {
        log.info("deleteReply");
        posteService.deleteReply(posteId, commentId, replyId);
    }

}
