# Placeholder project documentation

## About The Project
> Placeholder Project is a Task Manager project that allow users to manage lists of tasks manually as well as automatically
> suggest the best schedule pattern to a user.

### Built With

- Spark
- Apache Velocity
- JavaScript

## Getting Started
### Prerequisite
- IntelliJ Idea integrated development environment
	- The project development is done in Idea and is currently not deployed to a remote web server.
	- Java version in use is Java 8. The configuration should be automatically loaded with the project folder.
	

### Installation / Run
- Run the file *2021-fall-group-placeholders/placeholder/src/main/java/Main.java* (change folder name or not)
- Type in localhost:7000/main to direct to the main page of the app (/main or no /main)

## Usage

### Login/Signup Page
- Login with an account and password if having an existing account, or sign up for a new account otherwise.
- After login/signup, user's task home page would be displayed.

### Home Page
- Click on the Create List button to add a list.
- Click on the Delete List button to delete a list.
- Click on a created list to check the items inside.
- Click on the Add Task button to add a task in the highlighted created list.
- Click on the Delete Task button to delete a task from the chosen list.
- Click on the Task Detail button to view the notes, like special instruction, of a task.
- Click on the profile photo for the user profile page.
- Navigate to Home Page, Profile Page, and Schedule Page through NavBar.

### Task Detail Page
- The defualt note of a task would be empty. Click on the Edit button in order to edit task notes.

### Schedule Page
- A visualization of all tasks added would be displayed in the Monthly Calendar.
- A weekly calendar would be added in the future so that a auto-schedule algorithm could be displayed more specifically.

## File structures
- /placeholder/: project folder (change folder name or not)
	- src/main/
		- java/
			- model/: database entities
			- Main.java: server script
		- resources/public/: css files, js scripts, and .vm (Velocity) templetes
	- JBApp.db: SQLite database (to be changed to remote server database once deploying in a remote host) (change database name or not)

## Contributing
> The web application could provide everyone who wants to get a more organized life with a platform that they could manage their tasks and get shceduling advice.

## Liscense
NA

## Contact
- Diana Zhang - zfy19987@gmail.com
- Qifan Yu - qyu24@jh.edu
- Zhenyong He -zhe30@jh.edu

## Acknowledgement
- https://bootstrap4.com
- https://bootswatch.com
