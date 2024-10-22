## Disease Simulation
*by Daniel Morales-Garcia and Adrian Abeyta*
___
### Description:
This is a simple disease simulation application intended to model the 
spread of a contagious disease. Exposure is calculated based on distance 
in a radius between agents, which defaults to **20 centimeters**. 
Specific starting conditions are read from a text file as outlined below. 
The simulation field is also customizable in size and layout. 
<br /><br />
### Usage:
#### Run

`java -jar DiseaseSimulation.jar "C:\Path\to\configfile.txt"`

Where *configfile.txt* is is the configuration file for the simulation. 
Use quotation marks if your path has a space in it. Make sure you have JavaFX JDK17 on your system.
<br />
#### GUI
There is three buttons on the simulation GUI. Start, Stop, and Reset. After stopping the simulation, you cannot continue,
your only option is to restart a new simulation.
<br /><br />

### Configuration file:
One configuration setting per line.
Each line generally follows an `optionname [option1] [option2]...` format.

    dimensions n m
Width and height of simulation field in pixels (100 < n < 770). *Default: 200 for both*
<br /><br />

    exposuredistance n
Radius of distance `n` cm of contagiousness between agents. *Default: 20*
<br /><br />

    incubation n
Incubation period `n` measured in days. However, this is scaled down to 
seconds when running the simulation. *Default: 5*
<br /><br />

    sickness n
Length of time in `n` days that an agent either recovers or dies. *Default: 10*
<br /><br />

    recover p
Probability `p` that an agent will recover and become immune, as opposed to die. *Default: 0.95*
<br /><br />

    initialsick n
Number `n` agents  that start sick at the beginning of the simulation. 
*Default: 1*
<br /><br />

    initialimmune n
Number `n` agents that are naturally immune to the disease at the beginning 
of the simulation. *Default: 0*
<br /><br />

    agent n
Number `n` of agents for "random" play option. (must be less than 120) *Default: 20*
<br /><br />

    asymptomatic p
Probability `p` that an agent will be asymptomatic (0.00 - 1.00), meaning that an agent can pass on the disease 
without being sick themselves. Asymptomatic agents will always become immune.  *Default: 0.05*
<br /><br />

    initialimmune n
Number `n` of agents that are initially immune to the disease. *Default: 2*
<br /><br />

    longterm p
Probability `p` that an agent will recover from disease with long term health side affects. (0.00 - 1.00) *Default: 0.10*
<br /><br />

#### Layouts:
    grid r c
Creates a grid `r` rows and `c` columns in size with agents filling every cell.
<br /><br />

    randomgrid r c n
Same as above, except `n` specifies a number of agents to generate randomly 
throughout the grid.
<br /><br />

    random n
Generates `n` agents and places them randomly throughout the simulation 
field area.

*Each test file can only contain 1 layout type*
<br />
*Default: Randomly place 100 agents  or `random 100`*
<br /><br />

### Activity Log
After running the program, and starting the simulation. There will be an output of the simulation activity printed out on
your terminal
<br /><br />
Ex:
<br />
```
Agent 54 is sick on day 5    
Agent 51 is asymptomatic on day 5
Agent 86 is dead on day 18
Agent 68 is Immune on day 18
```

### Agent Colors
    green
Green agents are the initial state of most agents, and means that the agent is healthy, and has not come in contact with
the disease (not immune)
<br /><br />

    red
a sick agent
<br /><br />

    yellow
yellow means that an agent has come in contact with a sick agent but is not sick just yet.
<br /><br />

    orange
Orange is a asymptomatic agent. Which means that they are carrying and passing on the disease, but are not sick themselves.
<br /><br />

    lightblue
a light blue agent is an agent that has been infected with the disease and recovered.
<br /><br />

    darker blue
a darker blue agent is an agent that has been infected with the disease and recovered, but will sustain long term health side affects from the disease.
<br /><br />

    black
an agent killed by the disease.
<br /><br />

### Assignment required additional features:
    1. rerun simulation
    2. initial immunity
    3. display history
    4. additional states (healthy, longterm health effects, asymptomatic)



### More Documentation
- [Agent States](doc/DiseaseSimulationAgentStates.pdf)
- [Object Design Diagram](doc/ObjectDesignDiagram.pdf)
- [Test Files](Test%20Files)