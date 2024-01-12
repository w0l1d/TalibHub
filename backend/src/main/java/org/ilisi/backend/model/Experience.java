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
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String title;
    private String description;
    private YearMonth startAt;
    private YearMonth endAt;
    private String location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institu_id", nullable = false)
    private Institut institut;
}
