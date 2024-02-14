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
public class Institut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String name;
    private String website;
    private String imageUri;

    @OneToMany(mappedBy = "institut", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
