package com.example.azurespringboottutorial.todos;

import com.example.azurespringboottutorial.exceptions.TodoNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @PutMapping("/todos/{id}")
    public Todo updateTodo(@PathVariable Long id, @Valid @RequestBody Todo updatedTodo) {
        Todo todo = todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        return todoService.update(todo, updatedTodo);
    }

    @GetMapping("/todos")
    public Iterable<Todo> getTodos() {
        return todoService.findAll();
    }

    @GetMapping("/todos/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    @DeleteMapping("/todos/{id}")
    public void deleteTodoById(@PathVariable Long id) {
        Todo todo = todoService.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoService.delete(todo);
    }
}
