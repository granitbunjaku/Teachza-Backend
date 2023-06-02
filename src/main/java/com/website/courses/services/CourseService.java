package com.website.courses.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.website.courses.models.*;
import com.website.courses.requests.CourseRequest;
import com.website.courses.requests.UpdateCourseRequest;
import com.website.courses.responses.CourseResponse;
import com.website.courses.responses.UserResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> create(CourseRequest courseRequest, Principal principal)
    {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());
        Optional<Category> categoryOptional = categoryRepository.findById(courseRequest.getCategoryNumber());

            if(categoryOptional.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category doesn't exist!");
            }

            User user = userOptional.get();
            Category category = categoryOptional.get();

            Course course = Course.builder()
                    .name(courseRequest.getName())
                    .price(courseRequest.getPrice())
                    .starting_date(courseRequest.getEnding_date())
                    .ending_date(courseRequest.getEnding_date())
                    .category(category)
                    .owner(user)
                    .build();

            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();

            courseRepository.save(course);

            CourseResponse courseResponse = CourseResponse.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .price(course.getPrice())
                    .starting_date(course.getStarting_date())
                    .ending_date(course.getEnding_date())
                    .members(orderRepository.countByCourseId(course.getId()))
                    .category(category)
                    .owner(userResponse)
                    .build();

            return ResponseEntity.ok().body(courseResponse);
    }

    public ResponseEntity<List<Course>> getAllCourses()
    {
        return ResponseEntity.ok().body(courseRepository.findAll());
    }

    public ResponseEntity<?> getCourse(int id)
    {
        Optional<Course> courseOptional = courseRepository.findById(id);

        if(courseOptional.isPresent())
        {
            Course course = courseOptional.get();

            UserResponse userResponse = UserResponse.builder()
                    .id(course.getOwner().getId())
                    .firstname(course.getOwner().getFirstname())
                    .lastname(course.getOwner().getLastname())
                    .email(course.getOwner().getEmail())
                    .role(course.getOwner().getRole())
                    .build();

            CourseResponse courseResponse = CourseResponse.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .price(course.getPrice())
                    .starting_date(course.getStarting_date())
                    .ending_date(course.getEnding_date())
                    .members(orderRepository.countByCourseId(course.getId()))
                    .category(course.getCategory())
                    .owner(userResponse)
                    .build();

            return ResponseEntity.ok().body(courseResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course doesn't exist!");
    }

    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> update(int id, UpdateCourseRequest request) {
        Optional<Course> courseOptional = courseRepository.findById(id);

        if(courseOptional.isPresent())
        {
            Course course = courseOptional.get();

            if(request.getName() != null)
                course.setName(request.getName());

            if(request.getPrice() != null)
                course.setPrice(request.getPrice());

            if(request.getStarting_date() != null)
                course.setStarting_date(request.getStarting_date());

            if(request.getEnding_date() != null)
                course.setEnding_date(request.getEnding_date());

            courseRepository.save(course);
            return ResponseEntity.ok().body("Updated!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course doesn't exist!");
    }

    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> delete(int id, Principal principal)
    {
        Optional<User> user = userRepository.findByEmail(principal.getName());
        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent())
        {
            if (course.get().getOwner().getId() == user.get().getId())
            {
                courseRepository.deleteById(id);
                return ResponseEntity.ok().body("Successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be the owner of the course to delete it!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with id: " + id + " doesn't exist");
        }
    }

    public ResponseEntity<Set<Course>> coursesByCategory(int categoryId)
    {
        return ResponseEntity.ok(courseRepository.findByCategory_Id(categoryId));
    }

    public ResponseEntity<Set<CourseResponse>> coursesByOwner(int userId)
    {
        ModelMapper modelMapper = new ModelMapper();
        Set<Course> courses = courseRepository.findByOwner_Id(userId);
        Set<CourseResponse> responses = courses.stream()
                .map(course -> {
                    CourseResponse courseResponses =  modelMapper.map(course, CourseResponse.class);
                    courseResponses.setMembers(orderRepository.countByCourseId(course.getId()));
                    return courseResponses;
                })
                .collect(Collectors.toSet());
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<Set<CourseResponse>> coursesByUser(int id)
    {
        ModelMapper modelMapper = new ModelMapper();
        Set<CourseOrder> courseOrders = orderRepository.findCourseOrderByStudentId(id);
        Set<Course> courses = new HashSet<>();

        for(CourseOrder c : courseOrders)
        {
            courses.add(c.getCourse());
        }

        Set<CourseResponse> responses = courses.stream()
                .map(c -> {
                    CourseResponse courseResponse =  modelMapper.map(c, CourseResponse.class);
                    courseResponse.setMembers(orderRepository.countByCourseId(c.getId()));
                    return courseResponse;
                })
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(responses);

    }

    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> joinCourse(int courseId, Principal principal)
    {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(userOptional.isPresent() && courseOptional.isPresent())
        {
            User user = userOptional.get();
            Course course = courseOptional.get();

            CourseOrder courseOrder = CourseOrder.builder()
                    .course(course)
                    .student(user)
                    .price(course.getPrice())
                    .dateJoined(new Date())
                    .build();

            orderRepository.save(courseOrder);
            return ResponseEntity.ok().body("Successfully joined!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
    }

}
