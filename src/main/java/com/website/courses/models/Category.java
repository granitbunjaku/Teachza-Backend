package com.website.courses.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @SequenceGenerator(name = "category_seq",allocationSize = 1)
    @GeneratedValue(generator = "category_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
}
