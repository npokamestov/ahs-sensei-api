package com.adhocsensei.ahssenseiapi.controller;

import com.adhocsensei.ahssenseiapi.dao.CourseRepository;
import com.adhocsensei.ahssenseiapi.model.Course;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository mockRepo;

    private ObjectMapper mapper = new ObjectMapper();

    private Course mockCourse;
    private String mockCourseJson;
    private List<Course> allMockCourses = new ArrayList<>();
    private String allMockCoursesJson;

    @Before
    public void setUp() throws Exception {
        mockCourse = new Course(
                1l,
                "Course 1",
                "Software",
                "07/24/2021",
                "This is a short description",
                "Florida",
                60,
                20,
                "This is a long description"
                );

        mockCourseJson = mapper.writeValueAsString(mockCourse);

        Course mockCourse1 = new Course(
                2l,
                "Course 2",
                "Engineering",
                "07/25/2021",
                "This is a short description",
                "Florida",
                120,
                25,
                "This is a long description"
        );

        Course mockCourse2 = new Course(
                3l,
                "Course 3",
                "Advertising",
                "07/26/2021",
                "This is a short description",
                "Florida",
                30,
                40,
                "This is a long description"
        );

        allMockCourses.add(mockCourse);
        allMockCourses.add(mockCourse1);
        allMockCourses.add(mockCourse2);

        allMockCoursesJson = mapper.writeValueAsString(allMockCourses);

    }

    @Test
    public void shouldReturnListOfAllCourses() throws Exception {
        given(mockRepo.findAll()).willReturn(allMockCourses);

        mockMvc.perform(
                get("/course"))
                .andExpect(status().isOk())
                .andExpect(content().json(allMockCoursesJson));
    }

    @Test
    public void shouldReturnAllCoursesByTitle() throws Exception {
        given(mockRepo.findByTitle("Course 1")).willReturn(allMockCourses);

        mockMvc.perform(
                get("/course?title=Course 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allMockCoursesJson));
    }

    @Test
    public void shouldReturnAllCoursesByCategory() throws Exception {
        given(mockRepo.findByCategory("Software")).willReturn(allMockCourses);

        mockMvc.perform(
                get("/course?category=Software"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allMockCoursesJson));
    }

    @Test
    public void shouldReturnAllCoursesByLocation() throws Exception {
        given(mockRepo.findByLocation("Florida")).willReturn(allMockCourses);

        mockMvc.perform(
                get("/course?location=Florida"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allMockCoursesJson));
    }

    @Test
    public void shouldReturnAllCoursesByDate() throws Exception {
        given(mockRepo.findByDate("07/24/2021")).willReturn(allMockCourses);

        mockMvc.perform(
                get("/course?date=07/24/2021"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allMockCoursesJson));
    }

    @Test
    public void shouldReturnCourseById() throws Exception {
        Optional<Course> optionalCourse = Optional.of(mockCourse);
        given(mockRepo.findById(1L)).willReturn(optionalCourse);

        mockMvc.perform(
                get("/course/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mockCourseJson));
    }

    @Test
    public void shouldCreateNewCourseOnPost() throws Exception {
        Course input = new Course(
                3l,
                "Course 4",
                "Marketing",
                "07/27/2021",
                "This is a short description",
                "Florida",
                45,
                35,
                "This is a long description"
        );

        String inputJson = mapper.writeValueAsString(input);

        given(mockRepo.save(input)).willReturn(mockCourse);

        mockMvc.perform(
                post("/course")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mockCourseJson));

    }

    @Test
    public void shouldUpdateCourseByIdAndReturn204Status() throws Exception {
        Optional<Course> optionalCourse = Optional.of(mockCourse);
        given(mockRepo.findById(1L)).willReturn(optionalCourse);

        Course input = optionalCourse.get();
        input.setDuration(100);
        String inputJson = mapper.writeValueAsString(input);

        given(mockRepo.save(input)).willReturn(mockCourse);

        mockMvc.perform(
                put("/course/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(content().json(inputJson));
    }

    @Test
    public void shouldDeleteCourseByIdAndReturn204Status() throws Exception {
        mockMvc.perform(
                delete("/course/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}