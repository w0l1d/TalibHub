package org.ilisi.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "keycloak_role")
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    private String id;
    private String name;

}
