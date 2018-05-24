project structure:

two external libraries used in the project:
commons-csv-1.5      Output the csv file.
gson-2.6.2           Read the configure file.

src contains the source code the model.


Configure.json contains the basic configuration of the model.
    "initialCopDensity":0.04,    the initial cop density
    "initialAgentDensity":0.70,  the initial agent density
    "vision":7,                  the vision
    "govermentLegitimacy":0.62,  the governement legitimacy
    "maxJailTerm":30,            the max jail term
    "movement":true,             whether the agent can move or not
    "extention1":false,          whether swith the first extension
    "tickTime":1000              the tick time the model will run

The main method is in Controller.java file.

In windows environment:

    Use the following command in the terminal to build the project:
    javac -cp gson-2.6.2.jar;commons-csv-1.5.jar *.java
    use the following command in the terminal to run the project.
    java -cp gson-2.6.2.jar;commons-csv-1.5.jar;. Controller

In Linux/Unix environment:

    Use the following command in the terminal to build the project:
    javac -cp gson-2.6.2.jar:commons-csv-1.5.jar *.java
    use the following command in the terminal to run the project.
    java -cp gson-2.6.2.jar:commons-csv-1.5.jar:. Controller

A data.csv file will generate. This file constains number of agents (
quiet, active, jailed) 