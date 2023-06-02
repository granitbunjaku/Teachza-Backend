package com.website.courses.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<CourseOrder, Integer> {
    Optional<CourseOrder> findByStudentIdAndCourseId(int student_id, int course_id);

    int countByCourseId(int id);

    Set<CourseOrder> findCourseOrderByStudentId(int id);
}
