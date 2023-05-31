package com.website.courses.auth;

import com.website.courses.config.JwtService;
import com.website.courses.models.Role;
import com.website.courses.models.RoleRepository;
import com.website.courses.models.User;
import com.website.courses.models.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findById(request.getRole()).get())
                .build();

        userRepository.save(user);
        var token = jwtService.generateJwt(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var token = jwtService.generateJwt(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
