package com.website.courses.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Set<Course> findByCategory_Id(int category_id);

    Set<Course> findByOwner_Id(int user_id);

}
