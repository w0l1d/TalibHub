package org.ilisi.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String content;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @ManyToOne
    @JsonIgnore
    private Poste poste;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //List of replies as comments
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private List<Comment> replies;

    public void addReply(Comment comment) {
        this.replies.add(comment);
    }

    public void updateComment(String replyId, Comment replyToUpdate) {
        Comment reply = this.replies.stream().filter(r -> r.getId().equals(replyId)).findFirst().orElseThrow(() -> new RuntimeException("Reply not found"));
        reply.setContent(replyToUpdate.getContent());
    }
}
