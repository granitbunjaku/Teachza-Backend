package com.website.courses.responses;

import com.website.courses.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
