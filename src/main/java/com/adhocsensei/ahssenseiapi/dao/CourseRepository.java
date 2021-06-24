package com.adhocsensei.ahssenseiapi.dao;

import com.adhocsensei.ahssenseiapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitle(String title);
    List<Course> findByCategory(String category);
    List<Course> findByLocation(String location);
    List<Course> findByDate(String date);
}
