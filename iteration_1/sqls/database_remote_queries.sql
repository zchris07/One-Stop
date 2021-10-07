-- Find the task numbers and the collaborators s.t. name of task that include x
-- and none of them takes note on date y
WITH team AS (
SELECT task_id,collab_id
FROM todolist NATURAL JOIN othermembers
WHERE task LIKE  '%oose%')

SELECT T.task_id,T.collab_id
FROM team AS T JOIN notes_storage AS N
WHERE T.collab_id = N.id and N.date_taken <> '2021-10-05';

-- Find the id of person that takes the most number of notes in one day

SELECT id
FROM notes_storage
GROUP BY date_taken
ORDER BY count(notes_id) DESC LIMIT 1