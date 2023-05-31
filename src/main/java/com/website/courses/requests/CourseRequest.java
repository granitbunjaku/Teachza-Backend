package com.website.courses.requests;

import com.website.courses.models.Category;
import com.website.courses.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private Integer id;
    private String name;
    private double price;
    private Date starting_date;
    private Date ending_date;
    private int categoryNumber;
}
