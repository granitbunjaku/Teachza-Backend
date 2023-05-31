package com.website.courses.services;
import com.website.courses.models.*;
import com.website.courses.requests.CourseRequest;
import com.website.courses.responses.CourseResponse;
import com.website.courses.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.*;
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
                    .category(course.getCategory())
                    .owner(userResponse)
                    .build();

            return ResponseEntity.ok().body(courseResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course doesn't exist!");
    }


//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<String> update(int id)
//    {
//        Optional<Course> courseOptional = courseRepository.findById(id);
//
//        if(courseOptional.isPresent())
//        {
//            Course course = courseOptional.get();
//        }
//    }

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

    public ResponseEntity<Set<Course>> coursesByOwner(int userId)
    {
        return ResponseEntity.ok(courseRepository.findByOwner_Id(userId));
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
