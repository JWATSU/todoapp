package com.example.todos.todos;

import com.example.todos.exceptions.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/todos/secure")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(
            JwtAuthenticationToken jwtAuthenticationToken,
            @Valid @RequestBody Todo todo) {
        String userEmail = jwtAuthenticationToken.getToken().getSubject();
        todo.setUserEmail(userEmail);
        return todoService.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(
            JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable Long id, @Valid @RequestBody Todo updatedTodo) {
        Todo todo = todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        return todoService.update(todo, updatedTodo);
    }

    @GetMapping("")
    public Iterable<Todo> getTodos(JwtAuthenticationToken jwtAuthenticationToken) {
        String userEmail = jwtAuthenticationToken.getToken().getSubject();

        return todoService.findAll(userEmail);
    }

    @GetMapping("/{id}")
    public Todo getTodoById(
            JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable Long id) {
        return todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deleteTodoById(
            JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable Long id) {
        Todo todo = todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoService.delete(todo);
    }
}
