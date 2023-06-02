package com.website.courses.requests;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UpdateCourseRequest {
    private String name;
    private Double price;
    private Date starting_date;
    private Date ending_date;
}
