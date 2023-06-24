package com.example.todos.todos;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Todo todo, Todo updatedTodo) {
        todo.setDone(updatedTodo.isDone());
        todo.setDescription(updatedTodo.getDescription());
        todo.setUpdatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Iterable<Todo> findAll(String email) {
        return todoRepository.findTodosByUserEmail(email);
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public void delete(Todo todo) {
        todoRepository.delete(todo);
    }
}
