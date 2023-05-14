package com.example.todos.todos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class TodoSeeder implements CommandLineRunner {

    private final TodoRepository todoRepository;

    public TodoSeeder(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (todoRepository.count() == 0) {
            String[] descriptions = {"Buy milk", "Clean the house", "Go for a walk", "Call a friend", "Read a book", "Walk the cat", "Paint the house"};
            Random random = new Random();
            List<Todo> todos = new ArrayList<>();

            IntStream.rangeClosed(1, 10)
                    .forEach(i -> {
                        int index = random.nextInt(descriptions.length);
                        boolean done = random.nextBoolean();
                        Todo todo = new Todo();
                        todo.setUpdatedAt(LocalDateTime.now());
                        todo.setDescription(descriptions[index]);
                        todo.setDone(done);
                        todo.setUserEmail("admin@outlook.com");
                        todos.add(todo);
                    });

            todoRepository.saveAll(todos);
        }
    }
}