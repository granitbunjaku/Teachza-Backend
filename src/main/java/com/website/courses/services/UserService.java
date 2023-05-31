package com.website.courses.services;

import com.website.courses.models.User;
import com.website.courses.models.UserRepository;
import com.website.courses.requests.UpdateUserRequest;
import com.website.courses.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final RedisService redisService;

    public ResponseEntity<?> getMyUser(Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            UserResponse userResponse = new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(userResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
    }

    public ResponseEntity<String> updatePassword(String newPassword ,Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            String password = passwordEncoder.encode(newPassword);
            user.setPassword(password);
            userRepository.save(user);
            return ResponseEntity.ok().body("Password successfully changed!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist!");
    }

    public ResponseEntity<String> updatePassword(Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            String secretNumber = String.valueOf((int) (Math.random() * 9000) + 1000);
            emailSenderService.sendEmail(user.getEmail(), "Code to change password", secretNumber);
            redisService.saveCode(user.getId() + "_secret", secretNumber);
            return ResponseEntity.ok().body("Code sent!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist!");
    }
    public ResponseEntity<String> passwordSecurityCheck(String secret, Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            if(secret.equals(redisService.getCode(user.getId() + "_secret")))
            {
                return ResponseEntity.ok().body("Take to the next page!");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist!");
    }

    public ResponseEntity<?> updateUser(UpdateUserRequest request, Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();

            if(request.getFirstname() != null)
            {
                user.setFirstname(request.getFirstname().get());
            }

            if(request.getLastname() != null)
            {
                user.setLastname(request.getLastname().get());
            }

            if(request.getEmail() != null)
            {
                user.setEmail(request.getEmail().get());
            }

            if(request.getRole() != null)
            {
                user.setRole(request.getRole().get());
            }

            userRepository.save(user);
            return ResponseEntity.ok("Updated");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
    }
}
