project structure:

libs contains the libraries used in the project:
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