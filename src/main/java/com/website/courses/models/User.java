package com.website.courses.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "user_seq",allocationSize = 1)
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @ManyToOne
    private Role role;
    @OneToMany(mappedBy = "student")
    private Set<CourseOrder> courseOrders = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof User user)
        {
            return id.equals(user.getId());
        }
        return false;
    }
}
