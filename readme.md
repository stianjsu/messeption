[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2122/gr2122/-/tree/main/)
# Group gr2122 repository

## Genereal information:
- This git repository consists of an application developed in the NTNU course IT1901

- The project is built with Maven and configured for Gitpod

- The Maven plugin Jacoco is used for showing test-coverage of source code

- The project use a 'modular' approach for organising files

**In order to start the application in gitpod use mvn javafx:run in messeption/ui**
## Repository structure:
- In the 'Messeption' folder you can find the project

- In the 'docs' folder you can find release-documentations for the applications releases

- The project contains two README files, one on root-level and one inside of the project folder which describes the application

### File structure:
- The project is divided into modules, namely 'core' and 'ui'
- In both 'core' and 'ui' we have a 'src' folder. There the code is divided into 'main' and 'test' folders, for their respective purposes
- The 'main' folders split into 'java' and 'resources' folders
- 'java' folders contain java-files for code logic and module-info files for declaring dependencies in the project
- 'resources' folders contain JSON files for text saving and java-fxml files for ui representation



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
- 

