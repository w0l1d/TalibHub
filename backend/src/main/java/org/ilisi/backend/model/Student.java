package org.ilisi.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String cne;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Year enrollmentYear;

    @Column
    private Year graduationYear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    @ToString.Exclude
    private Profile profile;

    @Override
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_STUDENT");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Transient
    public boolean hasGraduated() {
        return graduationYear != null && graduationYear.isBefore(Year.now());
    }

}
