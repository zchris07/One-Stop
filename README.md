# Placeholder project documentation

## Dependencies
- IntelliJ Idea integrated development environment
	- The project development is done in Idea and is currently not deployed to a remote web server.
	- Java version in use is Java 8. The configuration should be automatically loaded with the project folder.

## Runing the server and start the app
- Run the file *2021-fall-group-placeholders/placeholder/src/main/java/Main.java* (change folder name or not)
- Type in localhost:7000/main to direct to the main page of the app (/main or no /main)

## Implemented app functions
- Click on the Create List button to add a list
- Click on a created list to check the items inside
- Click on the Create Item button to add an item in the highlighted created list
- Click on the profile photo for the user profile page
- Click on the delete button to delete the list or task
	- A minor issue still exists in the current code. For testing of the delete list, you shall not delete all the list. Just leave the first list without deleting when you want to delete list and add what ever task you want to any list or create list. A notification will be sent.
- Note autocorrection function (Implemented the algorithm and test cases, but has not been integrated to the main project.)
	- For a input string, we want to find all the missing punctuations, or fix the incorrect punctuations, based on the length of the sentence capital letters and context of the sentence.

## File structures
- /placeholder/: project folder (change folder name or not)
	- src/main/
		- java/
			- model/: database entities
			- Main.java: server script
		- resources/public/: css files, js scripts, and .vm (Velocity) templetes
	- JBApp.db: SQLite database (to be changed to remote server database once deploying in a remote host) (change database name or not)

## Major app pages
- Index page
	- Index page is where the main functionalities were implemented
- Login page
- User registration page
- Personal profile page
