# Placeholder project documentation

## Dependencies
- IntelliJ Idea integrated development environment
	- The project development is done in Idea and is currently not deployed to a remote web server.
	- Java version in use is Java 8. The configuration should be automatically loaded with the project folder.

## Runing the server and start the app
- Run the file *2021-fall-group-placeholders/project-connect-task/src/main/java/Main.java* (**To be edited** change folder name or not)
- Type in localhost:7000/main to direct to the main page of the app (**To be edited** /main or no /main)

## Implemented app functions
- Click on the Create List button to add a list
- Click on a created list to check the items inside
- Click on the Create Item button to add an item in the highlighted created list
- Click on the profile photo for the user profile page

## File structures
- /project-connect-task/: project folder (**To be edited** change folder name or not)
	- src/main/
		- java/
			- model/: database entities
			- Main.java: server script
		- resources/public/: css files, js scripts, and .vm (Velocity) templetes
	- JBApp.db: SQLite database (to be changed to remote server database once deploying in a remote host) (**To be edited** change database name or not)

## Major app pages
- Index page
	- Index page is where the main functionalities were implemented
- Login page
- User registration page
- Personal profile page
