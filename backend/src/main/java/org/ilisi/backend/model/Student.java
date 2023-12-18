package org.ilisi.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String cne;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Year enrollmentYear;

    @Column
    private Year graduationYear;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_STUDENT");
    }

    @Transient
    public boolean hasGraduated() {
        return graduationYear != null && graduationYear.isBefore(Year.now());
    }
}
