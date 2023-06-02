package com.website.courses.responses;

import com.website.courses.models.Category;
import com.website.courses.models.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Integer id;
    private String name;
    private double price;
    private Date starting_date;
    private Date ending_date;
    private Category category;
    private int members;
    private UserResponse owner;
}
