package com.website.courses.controllers;

import com.website.courses.models.Role;
import com.website.courses.models.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping()
    public ResponseEntity<String> create(@RequestBody Role role)
    {
        if(role.getName() != null)
        {
            roleRepository.save(role);
            return ResponseEntity.ok().body("Role Created");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid body");
    }
}
