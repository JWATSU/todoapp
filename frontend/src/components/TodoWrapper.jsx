import React, { useEffect, useState } from "react";
import { Todoform } from "./Todoform";
import { Todo } from "./Todo";
import { EditTodoForm } from "./EditTodoForm";
import { useOktaAuth } from "@okta/okta-react";
export const TodoWrapper = () => {
  const [todos, setTodos] = useState([]);

  const { oktaAuth, authState } = useOktaAuth();

  useEffect(() => {
    const fetchTodos = async () => {
      if (authState?.isAuthenticated) {
        console.log(authState);
        const requestOptions = {
          headers: {
            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
          },
        };
        const response = await fetch("/api/v1/todos/secure", requestOptions);
        const todosResponse = await response.json();

        /*
        Sort ascending. In the future, this should be sorted by creation date
        since we cannot rely on the format of the id. In the future,
        ids could be UUIDs.
        */
        todosResponse.sort((a, b) => a.id - b.id);

        const loadedTodos = todosResponse.map((todo) => ({
          id: todo.id,
          description: todo.description,
          completed: todo.done,
          isEditing: false,
        }));

        setTodos(loadedTodos);
      }
    };

    fetchTodos().catch((error) => {
      console.log(error);
    });
  }, [authState?.isAuthenticated]);

  const addTodo = async (description) => {
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
      },
      body: JSON.stringify({ description, done: false }),
    };
    const response = await fetch("/api/v1/todos/secure", requestOptions);

    const todosResponse = await response.json();

    setTodos([
      ...todos,
      {
        id: todosResponse.id,
        description: description,
        completed: false,
        isEditing: false,
      },
    ]);
  };

  const toggleComplete = async (todoToToggle) => {
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
      },
      body: JSON.stringify({
        description: todoToToggle.description,
        done: !todoToToggle.completed,
      }),
    };

    await fetch(`/api/v1/todos/secure/${todoToToggle.id}`, requestOptions);

    setTodos(
      todos.map((todo) =>
        todo.id === todoToToggle.id
          ? { ...todo, completed: !todo.completed }
          : todo
      )
    );
  };

  const deleteTodo = async (id) => {
    await fetch(`/api/v1/todos/secure/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
      },
    });
    setTodos(todos.filter((todo) => todo.id !== id));
  };

  const setTodoToEditMode = (id) => {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, isEditing: !todo.isEditing } : todo
      )
    );
  };

  const editDescription = async (description, todoToUpdate) => {
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
      },
      body: JSON.stringify({
        description: description,
        done: todoToUpdate.completed,
      }),
    };

    await fetch(`/api/v1/todos/secure/${todoToUpdate.id}`, requestOptions);

    setTodos(
      todos.map((todo) =>
        todo.id === todoToUpdate.id
          ? { ...todo, description, isEditing: !todo.isEditing }
          : todo
      )
    );
  };

  return (
    <div className="wrapper">
      <h1>Todos</h1>
      <Todoform addTodo={addTodo} />
      {todos.map((todo) =>
        todo.isEditing ? (
          <EditTodoForm
            key={todo.id}
            editDescription={editDescription}
            todo={todo}
          />
        ) : (
          <Todo
            todo={todo}
            key={todo.id}
            toggleComplete={toggleComplete}
            deleteTodo={deleteTodo}
            setTodoToEditMode={setTodoToEditMode}
          />
        )
      )}
    </div>
  );
};
