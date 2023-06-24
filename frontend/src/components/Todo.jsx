import React from "react";
import {faPenToSquare, faTrash} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const Todo = ({
  todo,
  toggleComplete,
  deleteTodo,
  setTodoToEditMode,
}) => {
  return (
    <div
      className={`${
        todo.completed
          ? "todo card bg-light mt-2 completed"
          : "todo card bg-white mt-2"
      }`}
      onClick={() => toggleComplete(todo)}
    >
      <div className="card-body d-flex justify-content-between align-items-center">
        <p className="card-text mb-0">{todo.description}</p>
        <div>
          {!todo.completed && (
            <FontAwesomeIcon
              icon={faPenToSquare}
              onClick={(e) => {
                e.stopPropagation();
                setTodoToEditMode(todo.id);
              }}
            />
          )}

          <FontAwesomeIcon
            icon={faTrash}
            onClick={(e) => {
              e.stopPropagation();
              deleteTodo(todo.id);
            }}
          />
        </div>
      </div>
    </div>
  );
};
