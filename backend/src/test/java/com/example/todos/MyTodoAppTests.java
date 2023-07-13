package com.example.todos;

import com.example.todos.todos.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MyTodoAppTests {

    @Autowired
    private MockMvc mvc;
    @Test
    void contextLoads() {

    }

    @Test
    void shouldNotAllowAccessWhenNoJWTIsProvided() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldNotAllowAccessToRequestWithInvalidJWT() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure/1")
                        .header("Authorization", "Bearer " + "FAKE_JWT_TOKEN"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetAllTodosBelongingToUser() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure")
                        .with(jwt().jwt(jwt -> jwt.subject("admin@outlook.com"))))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Todo> todos = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertEquals(10, todos.size());
    }

    @Test
    void shouldGetNoTodosForUserWithoutTodos() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure")
                        .with(jwt().jwt(jwt -> jwt.subject("random@outlook.com"))))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Todo> todos = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertEquals(0, todos.size());
    }

    @Test
    void shouldGetNoTodosWhenEmailIsNotProvidedInJWT() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Todo> todos = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertEquals(0, todos.size());
    }
}


