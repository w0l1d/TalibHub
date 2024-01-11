package org.ilisi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "student", orphanRemoval = true)
    private List<Profile> profiles;

    @Override
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_STUDENT");
    }

    @Transient
    public boolean hasGraduated() {
        return graduationYear != null && graduationYear.isBefore(Year.now());
    }

}
