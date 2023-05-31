package com.website.courses.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<CourseOrder, Integer> {
    Optional<CourseOrder> findByStudentIdAndCourseId(int student_id, int course_id);
}
