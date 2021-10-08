-- Find all user names and their reserved dates and importance userlocal given the id  --

SELECT username,setdate,importance
FROM userlocal NATURAL JOIN reserved_days 
WHERE id = '1';

-- Find all user name and their birthday that have a reserved day given the date and importance inputted ---

SELECT username
FROM userlocal NATURAL JOIN reserved_days
WHERE setdate = '2006-10-06' AND importance = 3

