package org.ilisi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String about;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;
}
