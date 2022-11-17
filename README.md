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
Use quotation marks if your path has a space in it. Make sure you have JavaFx JDK17 on yout system.
<br />
#### GUI
There is three buttons on the simulation GUI. Start, Stop, and Reset. After stopping the simulation, you cannot continue,
your only option is to restart a new simulation.
<br /><br />

### Configuration file:
One configuration setting per line.
Each line generally follows an `optionname [option1] [option2]...` format.

    dimensions width height
Width and height of simulation field in pixels. *Default: 200*
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

    asymptomatic n
Number `n` is the percentage likelihood that an agent will be asymptomatic (0.00 - 1.00). Meaning that an agent can pass on the disease 
without being sick themselves. Asymptomatic agent will always become immune.  *Default: 0.05*
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
Generates `n` agents and places places them randomly throughout the simulation 
field area.

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

    black
an agent killed by the disease.
<br /><br />


### More Documentation
- [Agent States](doc/DiseaseSimulationAgentStates.pdf)
- [Object Design Diagram](doc/ObjectDesignDiagram.pdf)
- [Test Files](Test%20Files)