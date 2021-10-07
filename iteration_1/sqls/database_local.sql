CREATE TABLE userlocal (
        id INT,
        username VARCHAR(255) NOT NULL,
        registerDate VARCHAR(255) NOT NULL,
        birthday VARCHAR(255),
        PRIMARY KEY(id)
);



CREATE TABLE reserved_days (
        id INT REFERENCES userlocal(id) ON DELETE CASCADE ON UPDATE CASCADE,
        setdate VARCHAR (255) NOT NULL,
        importance INT,
        PRIMARY KEY(id, setdate)
);




        