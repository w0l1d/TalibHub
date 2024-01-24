package org.ilisi.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Institut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String name;
    private String website;

    @OneToMany(mappedBy = "institut", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
