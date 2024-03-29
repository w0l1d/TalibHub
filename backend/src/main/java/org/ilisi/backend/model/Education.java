package org.ilisi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

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
    private YearMonth startAt;
    private YearMonth endAt;
    private String location;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Institut institut;
}
