INSERT INTO notes_storage
VALUES (1,'2021-10-5',1, 'chemistry note','is 1>2'),
(1,'2021-10-5',2,'physicals note','im hungry'),
(2,'2021-10-6',1,'math notes','hungry');

INSERT INTO todolist
VALUES (1,1,'oose',1,'10-6-2021'),
(1,2,'algebra',5,'10-7-2021'),
(2,3,'idk',3,'10-8-2021');

INSERT INTO othermembers
VALUES (1,2),
(1,3);

DELETE FROM notes_storage
WHERE id = 1 AND notes_id = 1;