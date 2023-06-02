package com.website.courses.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Course {
    @Id
    @SequenceGenerator(name = "course_seq",allocationSize = 1)
    @GeneratedValue(generator = "course_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    private Double price;
    private Date starting_date;
    private Date ending_date;
    @ManyToOne()
    @JoinColumn(name="category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseOrder> courseOrders = new HashSet<>();

}
