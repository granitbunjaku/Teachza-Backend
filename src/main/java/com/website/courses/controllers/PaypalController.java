package com.website.courses.controllers;

import com.website.courses.exceptions.NotFoundException;
import com.website.courses.models.*;
import com.website.courses.services.PaypalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("paypal")
public class PaypalController {

    private final PaypalService paypalService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @PostMapping("create/payment/{id}")
    public ResponseEntity<?> createPayment(@PathVariable("id") int id, Principal principal) throws NotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(id);
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if(courseOptional.isPresent())
        {
            Course course = courseOptional.get();
            User user = userOptional.get();

            if(orderRepository.findByStudentIdAndCourseId(user.getId(), course.getId()).isPresent())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are already a member");
            }

            return ResponseEntity.ok().body(paypalService.createPayment(String.valueOf(course.getPrice())));
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }

    @PostMapping("complete/payment/{id}")
    public Map<String, Object> completePayment(HttpServletRequest request, @PathVariable("id") int id, Principal principal)
    {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if(courseOptional.isPresent()) {
            Course course = courseOptional.get();
            return paypalService.completePayment(request, course, principal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }

}
