import React, { useState } from "react";

export const Todoform = ({ addTodo }) => {
  const [description, setDescription] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();
    if (description.trim()) {
      addTodo(description);
      setDescription("");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-group todo-form">
        <input
          type="text"
          className="form-control"
          placeholder="What needs doing?"
          onChange={(event) => setDescription(event.target.value)}
          value={description}
        />
        <button type="submit" className="btn btn-primary text-nowrap">
          Add todo
        </button>
      </div>
    </form>
  );
};
