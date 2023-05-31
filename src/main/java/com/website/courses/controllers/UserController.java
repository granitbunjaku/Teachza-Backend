package com.website.courses.controllers;

import com.website.courses.requests.SecretCodeRequest;
import com.website.courses.requests.UpdatePasswordRequest;
import com.website.courses.requests.UpdateUserRequest;
import com.website.courses.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping ("update/password")
    public ResponseEntity<String> changePassword(@RequestBody UpdatePasswordRequest request, Principal principal)
    {
        if(request.getPassword() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password field should not be null");
        }

        return userService.updatePassword(request.getPassword(), principal);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, Principal principal)
    {
        return userService.updateUser(request, principal);
    }

    @PostMapping("send/secret")
    public ResponseEntity<?> updateUser(Principal principal)
    {
        return userService.updatePassword(principal);
    }

    @PostMapping("check/secret")
    public ResponseEntity<?> passwordSecurityCheck(@RequestBody SecretCodeRequest request, Principal principal)
    {
        return userService.passwordSecurityCheck(request.getSecret(),   principal);
    }


    @GetMapping
    public ResponseEntity<?> getMyUser(Principal principal)
    {
        return userService.getMyUser(principal);
    }

}
