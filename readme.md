[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2122/gr2122/)
# Group gr2122 repository

## Genereal information:
- This git repository consists of an application developed in the NTNU course IT1901

- The project is built with Maven and configured for Gitpod

- The Maven plugin Jacoco is used for showing test-coverage of source code

- The project use a 'modular' approach for organising files

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

In order to start the application use mvn javafx:run in messeption/ui
