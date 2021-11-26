[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2122/gr2122/-/tree/main/)
# Group gr2122 repository

## Genereal information:
- This git repository consists of an application developed in the NTNU course IT1901

- The project is built with Maven and configured for Gitpod

- The Maven plugin Jacoco is used for showing test-coverage of source code

- The project use a 'modular' approach for organising files

<br/>

## Running the app
- You can run the app by cloning the repository to a local machine, or from gitpod by using the gitpod button above
- Due to gitpod instability some ui tests do not function properly in gitpod at all times
- The app is run from the file location gr2122/messeption with the commands specified below
### Run local (not connected to server):
```
mvn -pl ui javafx:run -Drun.local
```

### Run remotely (connected to server):
```
mvn -pl integrationtests jetty:run
```
then, in a seperate terminal instance:
```
mvn -pl ui javafx:run
```

### Note - Testing app in gitpod:
  - Due to unreliable javafx-testing of ui in gitpod, the project is configured to skip the ui-tests when the project is opened in gitpod. For running all tests in the project, it is recommended to use command `mvn clean install` in project folder locally (gr2122/messeption).  

<br/>

## Repository structure:
- In the 'Messeption' folder you can find the project

- In the 'docs' folder you can find release-documentations for the applications releases

- The project contains two README files, one on root-level (the one you are reading now) and one inside of the project folder which describes the application

<br/>

## Shippable product
- The project is configured with jlink and jpackage in order to create a shippable product. In order to ship the project the command `mvn compile javafx:jlink jpackage:jpackage` is used in the messeption/ui directory. In gitpod exclude jpackage:jpackage as wix is not installed.
- This will produce messeptionfx in messeption/ui/target that can be used for running the app locally without the use of an IDE.
The command will also produce a MesseptionFX.exe file in messeption/ui/target/dist for distribution and installing messeption as a program localy on a computer
- Shipped products require a connection to a REST server running locally on port 8080 in order to work

<br/>

## Workflow:
- During this developement process we have used a number of different strategies and techniques to improve workflow and group efficiency

- Some revolve around work speed and code verification, others target the communication within the group


#### Milestones and issue tracking:
- For each release of the project we establish a set of goals. These goals are compiled into a release milestone namely 'Sprint'
- Issues are created with spesific tasks based on the established goals, and labeled with their respective 'target area' and priority
- The board function on Gitlab is used to track and sort the issues for better overview of the developement prosess

#### Branching:
- Branching is important to structure the different code development that is taking place at the same time, and makes the prosess more efficient
- We have 'protected' the Main branch, so that we only merge with Main when the release is tested and verified
- We establish a 'dev' branch for all developement, and create branches out from the 'dev' branch for the different code updates

#### Pair programming:
- The group has used pair programming frequently, to ensure good communication and participation from all group members
- This type of developement has also made it possible to combine individuals with different strengths, to excel project developement whilst also sharing skills

#### Communication channels:
- We have used different channels for communication and information sharing, to make the developement prosess more structured
- Facebook's Messenger application has been used for quick and easy communication and information sharing
- Discord has been used for meetings and work sessions, as well as file- and document sharing
- The group has had two weekly meetings, tuesday and friday, followed by work sessions 

#### Code verification:
- The code for each release is checked and verified both manually by each group member and by tests created for the different modules
- Issues are verified after completion before they are marked as 'closed', to ensure that if a mistake has been made it is picked up as early as possible
- We have used jacoco to show how much of the code is covered by the tests. A report is put into target/site/jacoco which shows coverage in percent. Jacoco is configured for every module and compiles a report after mvn verify or equivelent is run. 
  - Note: BoardAccessRemote is not tested in ui, but in integration tests and thus isn't marked as tested int the jacoco rapport for messeption/ui
- Spotbugs has also been implemented to check the code for common mistakes. For spotbugs to function properly it must be run from gr2122/messeption and not any lower directories
- Checkstyle has been implemented to check the codes formatting and visual style so that all code is in line with the google checktyle convention

#### Commit practises:
- In Sprint 3 we started using commitizen for formating commits
- Hence our commit messages have become much more detailed and richer with information including type of change, scope, description
relation to issues in gitlab and development method (induvidual, pair, group).
- This has made the development-progress much easier to follow and document


## Account security
- User account security has not been a focus for this project. Thus all users are saved as open JSON text files and can easily be read. The server also regularly shows user credentials as part of the debug information