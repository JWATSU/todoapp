import React, { useState } from "react";

export const EditTodoForm = ({ editDescription, todo }) => {
  const [value, setValue] = useState(todo.description);

  const handleSubmit = (event) => {
    event.preventDefault();
    if (value) {
      editDescription(value, todo);
    }
  };

  return (
    <div className={"todo card bg-white mt-2"}>
      <div className="card-body d-flex justify-content-between align-items-center">
        <form id="myForm" onSubmit={handleSubmit}>
          <div className="form-group d-flex">
            <input
              type="text"
              className="form-control"
              placeholder="Update Todo"
              onChange={(event) => setValue(event.target.value)}
              value={value}
            />
            <button type="submit" form="myForm" className="btn">
              <i className="fa-solid fa-square-check fa-lg"></i>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
