package com.website.courses.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements GrantedAuthority {
    @Id
    @SequenceGenerator(name = "role_seq",allocationSize = 1)
    @GeneratedValue(generator = "role_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}

//public enum Role {
//    TEACHER,
//    STUDENT
//}
