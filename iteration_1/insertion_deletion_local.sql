INSERT INTO userlocal
VALUES (1,"Tom","2021-10-5","1999-10-5"),
(2,"Tim","2021-10-5","1995-10-7"),
(3,"John","2021-10-5","2000-10-8");

INSERT INTO reserved_days
VALUES (1,"2005-10-5",5),
(1,"2006-05-25",5),
(2,"2010-04-12",3),
(2,"2010-04-22",4);


DELETE FROM userlocal 
WHERE id = 3;

DELETE FROM reserved_days
WHERE id = 2 AND setdate = "2010-04-22";

