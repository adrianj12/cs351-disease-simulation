/*************************************************************************
 * Daniel Morales-Garcia
 *
 * Agent.java are objects that represent the agents in the disease simulation.
 * each of these objects run on their own thread.
 *
 *************************************************************************/

package DiseaseSimulation;

import java.util.ArrayList;
import java.util.Random;

public class Agent extends Thread {

    public int agentNum;

    // position of agent on X axis
    public int positionX;

    //position of agent on Y axis
    public int positionY;

    // distance that an agent can get another agent sick
    public int exposureDistance;

    // time between exposure and becoming sick
    public int incubation;

    // time that an agent is sick
    public int sickness;

    // whether agent will recover from sickness or die
    public boolean recover;

    // agent is exposed but not sick yet
    public boolean vulnerable = false;

    // agent is sick but asymptomatic
    public boolean asymptomatic = false;

    // agent is sick
    public boolean sick;

    // agent has become immune to the disease
    public boolean immune = false;

    // agent has died
    public boolean dead = false;

    // whether agent is being run
    public boolean activeAgent = false;

    // whether agent will become asymptomatic
    public boolean becomeAsymptomatic;


    // arrayList containing all agents in exposure distance to this one.
    ArrayList<Agent> agentsInExposureDistance = new ArrayList<>();

    public Agent(int positionX, int positionY, int exposureDistance, int incubation, int sickness, double recover,
                 boolean sick, int agentNum, double asymptomatic){
        this.positionX = positionX;
        this.positionY = positionY;
        this.exposureDistance = exposureDistance;
        this.incubation = incubation;
        this.sickness = sickness;
        this.recover = determineFromPercentage(recover);
        this.sick = sick;
        this.agentNum = agentNum;
        this.becomeAsymptomatic = determineFromPercentage(asymptomatic);
    }

    /**
     * run starts a thread
     *
     * @Parameters
     * String[] args
     *     receives full path of txt file to reach and store dictionary in trie
     *
     * @return
     * void
     */
    @Override
    public void run(){
        activeAgent = true;
        try {
            if(sick) {
                System.out.println("Agent " + agentNum + " is sick on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
                exposeAgents();
                sleep(1000L * sickness);
            }
            else if(asymptomatic){
                System.out.println("Agent " + agentNum + " is asymptomatic on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
                exposeAgents();
                sleep(1000L * sickness);
            }
            else {
                vulnerable = true;
                sleep(1000L * incubation);
                vulnerable = false;
                if(becomeAsymptomatic){
                    asymptomatic = true;
                    System.out.println("Agent " + agentNum + " is asymptomatic on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
                }
                else{
                    sick = true;
                    System.out.println("Agent " + agentNum + " is sick on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
                }
                exposeAgents();
                sleep(1000L * sickness);
            }
        }
        catch(Exception e) {System.out.println("Thread Error");}

        if(recover || asymptomatic) immune = true;
        else dead = true;

        if(dead) System.out.println("Agent " + agentNum + " is dead on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
        if(immune) System.out.println("Agent " + agentNum + " is Immune on day " + ((System.currentTimeMillis() - main.startTime) / 1000 ));
    }

    /**
     * exposeAgents makes agents around this sick/asymptomatic agent sick/asymptomatic by starting their thread.
     *
     * @Parameters
     * void
     *
     * @return
     * void
     */
    public void exposeAgents(){
        for(int i = 0; i < agentsInExposureDistance.size(); i++){
            if(!agentsInExposureDistance.get(i).activeAgent){
                agentsInExposureDistance.get(i).activeAgent = true;
                agentsInExposureDistance.get(i).start();
            }
        }
    }



    /**
    * determineFromPercentage takes in a percentage likelihood of situation occurring, randomizes result and returns
    * true or false.
    *
    * @Parameters
    * double recover
    *      double ranging from 0.0 - 1.0. Represents the percentage likelihood
    *
    * @return
    * boolean
    *      whether occurs from likelihood
    */
    private boolean determineFromPercentage(double likelihood){
        Random rand = new Random();

        double percent = 100 * likelihood;
        if((rand.nextInt(100) + 1)  > percent){
            return false;
        }
        else return true;
    }
}