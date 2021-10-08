# Using SSM framework

We are not using the Spark + Velocity + SQLite framework. Instead, we are using Spring + SpringMVC + Mybatis framework. We encountered difficulties when connecting to the database.

However, we implemented the core of the user story-- we will just put the sql queries we wrote in the corresponding DAO files, and write some html websites that allows user to interact
with the database. We are considering switching to Spark + Velocity +SQLite framework, because working with this Spring + SpringMVC +Mybatis framework is really diffcult. Also
there are some communication problems within, a few group members aren't assigned any task to do which slowed down the progress.  

# Current progress

- We implemented the Account and Notes classes for the user story. 

- The database is a MySQL database created locally.

- The database has a connection problem, which prevents us to implement further development.

- The index.jsp is a dummy temp index page for testing the SSM framework, and it is working except for the database post part.

- The index_backup.jsp is our main page for the project purpose.
