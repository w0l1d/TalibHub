package org.ilisi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;
    private String title;
    private String studyField;
    private String description;
    private String startDate;
    private String endDate;
    private String location;

    @ManyToOne
    @JoinColumn(name = "institu_id", nullable = false)
    private Institut institut;
}
