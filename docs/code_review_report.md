# Code Review Report 

## Overview

We conducted an around of collaborative code review in iteration 5 and had achievements as listed in the next section. Overall, both code quality and code clearness had improved a lot. We also made progress in finalizing our documentation including in-line annotations and user instructions. Refer section [Code Review Achievements](##Code-Review-Achievements) for detailed information on each code review improvement.  

## Code Review Achievements

### Design improvements and refactoring 

* Separated big long Main.java into modules [Qifan Yu]

    Before refactoring we had a big long Main.java file that contains all of our API endpoints and related methods. After refactoring, we separated the differently functionality of Main.java into 3 packages (Controllers, Functionality, and model). This better design of separating a long script into different modules leads to a huge reduction in overall code complexity. 

* Refactored DaoConstructor and optimized Dao usage [Leyang Feng]
  
  The DaoConstructor use to have the the code smell of duplicate code -- it contains several methods of using very similar code to generate DAO object for different models. The refactoring of the DaoConstructor generalized several methods into one generic method to construct DAO object for different models using different parameters. More importantly, we modified the usage of DAO object to allow re-use of the same DAO for different API endpoints, thus improved the performance of backend services and optimized database connections to avoid unexpected large number of established connections. 

* Refactored functionality scripts [Alex]

  As one script providing one of the core functionality of our app, the script `TextFunctions.java` used to be long and contains redundant code. The refactoring of this script removes the code smell of long methods and reduce overall code complexity. 

### Testing 

* Unit-tests for core functionality [Alex] 

  Unit-tests are added to test our core app functionalities of task auto-scheduling, auto-spelling correction. These unit-tests ensured the robustness of our core functionalities. 

### Commenting and Documentation 

* Added in-line comments [Chris, Alex, Leyang, Fangyi, Qifan, Yihao, Zhenyong]

  Everyone adds more comments in his/her code if he/she wrote that code. At least, every function needs to be explained in the comment.

* Improved README.md [Zhenyong He]

 Details about how the application runs are updated and more functionalities that we implement will be explained in README.md file.

### Naming and Styling 

* Unified code styling [Yihao, Fangyi]

  The styling of the code has been adjusted to be consistent across different code files including indentation and use of new lines.  

* Unified variable naming [Leyang Feng]

  Variables renamed in APIEndpoint.java have been examined and adjusted to be meaningful and easy to follow.  

