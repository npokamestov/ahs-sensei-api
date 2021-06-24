package com.adhocsensei.ahssenseiapi.controller;

import com.adhocsensei.ahssenseiapi.dao.UserRepository;
import com.adhocsensei.ahssenseiapi.model.User;
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
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository mockRepo;

    private ObjectMapper mapper = new ObjectMapper();

    private User mockUser;
    private String mockUserJson;
    private List<User> allMockUsers = new ArrayList<>();
    private String allMockUsersJson;

    @Before
    public void setUp() throws Exception {
        mockUser = new User(
                1L,
                "Sensei",
                "Nikita",
                "Pokamestov",
                "email@email.com",
                "password",
                "A bio",
                true
                );

        mockUserJson = mapper.writeValueAsString(mockUser);

        User mockUser1 = new User(
                2L,
                "Sensei",
                "Yashsa",
                "A",
                "email@email.com",
                "password",
                "A bio",
                true
        );

        User mockUser2 = new User(
                3L,
                "Sensei",
                "Dale",
                "K",
                "email@email.com",
                "password",
                "A bio",
                true
        );

        allMockUsers.add(mockUser);
        allMockUsers.add(mockUser1);
        allMockUsers.add(mockUser2);

        allMockUsersJson = mapper.writeValueAsString(allMockUsers);

    }

    @Test
    public void shouldReturnListOfAllUsers() throws Exception {
        given(mockRepo.findAll()).willReturn(allMockUsers);

        mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(allMockUsersJson));
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        Optional<User> optionalUser = Optional.of(mockUser);
        given(mockRepo.findById(1L)).willReturn(optionalUser);

        mockMvc.perform(
                get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mockUserJson));
    }

    @Test
    public void shouldCreateNewUserOnPost() throws Exception {
        User input = new User(
                3L,
                "Sensei",
                "Dan",
                "M",
                "email@email.com",
                "password",
                "A bio",
                true
        );

        String inputJson = mapper.writeValueAsString(input);

        given(mockRepo.save(input)).willReturn(mockUser);

        mockMvc.perform(
                post("/user")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mockUserJson));
    }

    @Test
    public void shouldUpdateUserByIdAndReturn204Status() throws Exception {
        Optional<User> optionalUser = Optional.of(mockUser);
        given(mockRepo.findById(1L)).willReturn(optionalUser);

        User input = optionalUser.get();
        input.setBio("This a great bio!");
        String inputJson = mapper.writeValueAsString(input);

        given(mockRepo.save(input)).willReturn(mockUser);

        mockMvc.perform(
                put("/user/1")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(content().json(inputJson));
    }

    @Test
    public void shouldDeleteUserByIdAndReturn204Status() throws Exception {
        mockMvc.perform(
                delete("/user/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}