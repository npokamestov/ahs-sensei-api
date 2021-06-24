package com.adhocsensei.ahssenseiapi.dao;

import com.adhocsensei.ahssenseiapi.model.Course;
import com.netflix.discovery.converters.Auto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    CourseRepository repo;

    private Course course1;
    private Course course2;

    @Before
    public void setUp() {
        repo.deleteAll();

        course1 = new Course();
        course1.setTitle("Course 1");
        course1.setCategory("Software");
        course1.setDate("07/24/2021");
        course1.setShortDescription("This is a short description");
        course1.setLocation("Texas");
        course1.setDuration(60);
        course1.setCapacity(20);
        course1.setLongDescription("This is a long description");

        repo.save(course1);

        course2 = new Course();
        course2.setTitle("Course 2");
        course2.setCategory("Engineering");
        course2.setDate("07/25/2021");
        course2.setShortDescription("This is a short description");
        course2.setLocation("Georgia");
        course2.setDuration(120);
        course2.setCapacity(25);
        course2.setLongDescription("This is a long description");

        repo.save(course2);
    }

    @Test
    public void shouldAddAndGetCourseFromDatabase() {

        Course fromRepo = repo.findById(course1.getCourseId()).get();
        assertEquals(course1, fromRepo);
    }

    @Test
    public void shouldUpdateCourseInDatabase() {
        course1.setCapacity(1);
        repo.save(course1);

        Course fromRepo = repo.findById(course1.getCourseId()).get();
        assertEquals(course1, fromRepo);
    }

    @Test
    public void shouldDeleteCourseFromDatabase() {
        repo.deleteById(course1.getCourseId());

        Optional<Course> fromRepo = repo.findById(course1.getCourseId());

        assertFalse(fromRepo.isPresent());
    }

    @Test
    public void shouldFindCourseByTitle() {
        List<Course> result = repo.findByTitle("Course 1");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));

        result = repo.findByTitle("Course 2");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));

        result = repo.findByTitle("Not a course");

        assertEquals(0,result.size());
    }

    @Test
    public void shouldFindCourseByCategory() {
        List<Course> result = repo.findByCategory("Software");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));

        result = repo.findByCategory("Engineering");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));

        result = repo.findByCategory("Not a category");

        assertEquals(0,result.size());
    }

    @Test
    public void shouldFindCourseByLocation() {
        List<Course> result = repo.findByLocation("Texas");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));

        result = repo.findByLocation("Georgia");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));

        result = repo.findByLocation("Not a course");

        assertEquals(0,result.size());
    }

    @Test
    public void shouldFindCourseByDate() {
        List<Course> result = repo.findByDate("07/24/2021");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));

        result = repo.findByDate("07/25/2021");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));

        result = repo.findByDate("07/35/2021");

        assertEquals(0,result.size());
    }

}