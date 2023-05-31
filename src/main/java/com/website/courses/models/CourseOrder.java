package com.website.courses.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@ToString(exclude = {"student", "course"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourseOrder {
    @Id
    @SequenceGenerator(name = "order_seq",allocationSize = 1)
    @GeneratedValue(generator = "order_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    private double price;
    private Date dateJoined;
}
