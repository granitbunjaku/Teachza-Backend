package com.website.courses.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class UpdatePasswordRequest {
    private String password;
}
