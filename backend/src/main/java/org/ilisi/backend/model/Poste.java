package org.ilisi.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @NotBlank(message = "Titre is mandatory")
    private String titre;

    @NotBlank(message = "Description is mandatory")
    private String description;

    private String image;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "poste_id", referencedColumnName = "id")
    private List<Comment> comments;

    @ManyToOne
    private User user;

    public void addComment(Comment commentToAdd) {
        this.comments.add(commentToAdd);
    }

    public void updateComment(String commentId, Comment commentToUpdate) {
        Comment comment = this.comments.stream().filter(c -> c.getId().equals(commentId)).findFirst().orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(commentToUpdate.getContent());
    }
}
