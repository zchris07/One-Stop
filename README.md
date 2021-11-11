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
- Run the file *2021-fall-group-placeholders/placeholder/src/main/java/Main.java* 
- Type in localhost:7000/ in your browser to direct to app page 

## Usage
The default end-point will re-direct to login page, if no cache was previously stored. 

### Login/Signup Page
- Click on the signup button on the login page if you has not created an account. Or, type in localhost:7000/main in your browser to use it as a local app.



- Login with an account and password if having an existing account, or sign up for a new account otherwise. The password will be encrypted using SHA-256.
- After login/signup, user's task home page would be displayed.
![alt text](./docs/images/loginpage.png)

### Home Page
- If logged in, the displayed lists belong to the lists owner or the logged in user is a collaborator for those lists. If using it as a local app, the lists that are visible are the ones that do not have an owen or a collaborator.
![alt text](./docs/images/mainpage.png)
- Click on the Create List button to add a list.
![alt text](./docs/images/addlist1.png)
- Add collaborators by inputing their user emails. Separate by semicolon.
![alt text](./docs/images/addlist2.png)
- The lists on the main page will be those that belong to the logged-in user or the user is a collaborator.
- Click on the Delete List button to delete a list.
- Click on a created list to check the items inside.
- Click on the Add Task button to add a task in the highlighted created list.
- Click on the Delete Task button to delete a task from the chosen list.
- Click on the Task Detail button to view the notes, like special instruction, of a task.
- Click on the profile photo for the user profile page.
- Navigate to Home Page, Profile Page, and Schedule Page through NavBar.

### User Profile page

- Once logged in, click the icon at the upper right cornor to enter user profile page. 
- Newly registered user should only have his/her email displayed on the page. 
![alt text](./docs/images/profile_1.png)
- Update the user information and user summary using buttons on the page. 
![alt text](./docs/images/profile_2.png)
- Once updated, the page should look like this: 
![alt text](./docs/images/profile_3.png)

### Task Detail Page
- The defualt note of a task would be empty. Click on the Edit button in order to edit task notes.
- Once the Edit button is clicked, it will prompt user to a dialog box, where user can input the notes and select the following functionalities:
  1. Fix fullstop -- by selecting this functionality, the missing fullstops will be inserted, or the unnecessary capital letters will become 
     lower case letters. The program will decide which of the two things will it do, base on if the problematic word is a noun or not.
  2. Fix Spelling -- by selecting this functionality, the program will check if the input string is free of spelling mistake. (However, it can't
     correct words that can be found in dictionary-- for example, if a user misspell "Catt" for "Cat", the auto correction does not apply because
     "Catt" can be found in dictionary.) Once the program found a misspelled word, program generate a phrase that combines the word before the 
     misspelled word(if any) and at most 5 possible replacements of the misspelled word, and find the phrase that has the highest frequency on
     the Google NGram Viewer. Then it corrects user's string into that string.
  3. Fix Capital -- by selecting this functionality, the program finds the words that have capital letters not positioned at the front of the string.
     Then those capital letters would be changed to lower case letter.
  4. Fix Long Running-- by selecting this functionality, if the input string is too long, the program finds a conjunction in the sentence and split the
     sentence into half, by putting a comma in front of the conjunction. 

The fix spelling has consistency issue- for some input strings, the Fix Spelling function doesn't work consistently. It will still generate a string that is free of spelling mistakes, but everytime it outputs different string that are similar. However, when we step into debugger hoping to discover where the inconsistency arises, the returning string suddenly becomes consistent. This is a issue that needs to be solved.
### Schedule Page
- A visualization of all tasks added would be displayed in the Monthly Calendar.
- A weekly calendar would be added in the future so that a auto-schedule algorithm could be displayed more specifically.
![alt text](./docs/images/schedule_1.png)

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
- Yihao Liu - yliu333@jhu.edu
- Leyang Feng - lfeng13@jhu.edu
- Alex Zhang - azhang41@jhu.edu
- Chris Zou - czou8@jhu.edu

## Acknowledgement
- https://bootstrap4.com
- https://bootswatch.com
