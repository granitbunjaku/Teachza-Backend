package com.website.courses.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.website.courses.models.*;
import com.website.courses.requests.CourseRequest;
import com.website.courses.requests.UpdateCourseRequest;
import com.website.courses.responses.CourseResponse;
import com.website.courses.services.CourseService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.*;

@RequestMapping("course")
@RequiredArgsConstructor
@RestController
public class CourseController {
    private final CourseService courseService;

    @PreAuthorize(value = "hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseRequest course, Principal principal)
    {
        return courseService.create(course, principal);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses()
    {
        return courseService.getAllCourses();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCourse(@PathVariable("id") int id)
    {
        return courseService.getCourse(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id, Principal principal)
    {
        return courseService.delete(id, principal);
    }

    @GetMapping("by/category/{id}")
    public ResponseEntity<?> getCoursesByCategory(@PathVariable("id") int id)
    {
        return courseService.coursesByCategory(id);
    }

    @GetMapping("by/owner/{id}")
    public ResponseEntity<?> getCoursesByOwner(@PathVariable("id") int id)
    {
        return courseService.coursesByOwner(id);
    }

    @GetMapping("by/user/{id}")
    public ResponseEntity<Set<CourseResponse>> getCoursesByUser(@PathVariable("id") int id)
    {
        return courseService.coursesByUser(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload( @RequestParam("file") MultipartFile file ) {

        String fileName = file.getOriginalFilename();
        try {
            file.transferTo( new File("C:\\Users\\Granit's PC\\Desktop\\courses\\src\\main\\java\\com\\website\\courses\\CourseVideos\\" + fileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @GetMapping("/test")
    public String handleFileUpload() {
        return System.getProperty("user.dir");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody UpdateCourseRequest request)
    {
        return courseService.update(id, request);
    }

    @PostMapping("join/{id}")
    public ResponseEntity<String> joinCourse(@PathVariable("id") int id, Principal principal)
    {
        return courseService.joinCourse(id, principal);
    }
}
