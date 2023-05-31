package com.website.courses.requests;

import com.website.courses.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private Optional<String> firstname;
    private Optional<String> lastname;
    private Optional<String> email;
    private Optional<Role> role;
}
