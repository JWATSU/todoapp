package com.example.todos;

import com.example.todos.todos.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void shouldGetTodoItem() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure/1")
                        .with(jwt().jwt(jwt -> jwt.subject("admin@outlook.com"))))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        Todo todo = objectMapper.readValue(responseBody, Todo.class);

        assertNotNull(todo.getDescription());
        assertNotNull(todo.getId());
        assertNotNull(todo.getCreatedAt());
        assertNotNull(todo.getUpdatedAt());
        assertNotNull(todo.getUserEmail());
        assertEquals("admin@outlook.com", todo.getUserEmail());
    }

    @Test
    void shouldReturnErrorMessageForTodoNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/todos/secure/20")
                        .with(jwt().jwt(jwt -> jwt.subject("admin@outlook.com"))))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("Todo with id 20 not found."));
    }
    @Test
    void shouldCreateTodo() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/todos/secure")
                        .with(jwt().jwt(jwt -> jwt.subject("adam@outlook.com")))
                        .content("""
                                {
                                    "description": "testing!",
                                    "done": false
                                }""")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Todo todo = objectMapper.readValue(responseBody, Todo.class);

        assertEquals("adam@outlook.com", todo.getUserEmail());
        assertFalse(todo.isDone());
        assertEquals("testing!", todo.getDescription());

    }
}


