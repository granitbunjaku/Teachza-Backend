package com.website.courses.controllers;

import com.website.courses.models.*;
import com.website.courses.requests.CourseRequest;
import com.website.courses.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("by/user/{id}")
    public ResponseEntity<?> getCoursesByUser(@PathVariable("id") int id)
    {
        return courseService.coursesByOwner(id);
    }

    @PostMapping("join/{id}")
    public ResponseEntity<String> joinCourse(@PathVariable("id") int id, Principal principal)
    {
        return courseService.joinCourse(id, principal);
    }
}
