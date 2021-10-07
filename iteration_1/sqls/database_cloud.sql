CREATE TABLE notes_storage (
		id INT REFERENCES userlocal(id) ON DELETE CASCADE ON UPDATE CASCADE,
        date_taken VARCHAR(255) NOT NULL,
        notes_id INT NOT NULL,
        notes TEXT,
        extension TEXT,
        PRIMARY KEY(id,notes_id)
);

CREATE TABLE todolist (
		user_id INT REFERENCES userlocal(id) ON DELETE CASCADE ON UPDATE CASCADE,
        task_id INT NOT NULL,
        task VARCHAR(255),
        importance INT,
        duetime VARCHAR(255),
        PRIMARY KEY(task_id)
        );

CREATE TABLE othermembers (
		task_id INT REFERENCES todolist(task_other_memebrs),
        collab_id INT
        )
        