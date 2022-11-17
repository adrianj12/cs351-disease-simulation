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
Run

`java -jar DiseaseSimulation.jar C:\Path\to\configfile.txt`

Where *configfile.txt* is is the configuration file for the simulation. 
Use quotation marks if your path has a space in it. Make sure you have JavaFx JDK17 on yout system.
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
Probability `p` that an agent will recover. *Default: 0.95*
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
### More Documentation
- [Agent States](doc/DiseaseSimulationAgentStates.pdf)
- [Object Design Diagram](doc/ObjectDesignDiagram.pdf)