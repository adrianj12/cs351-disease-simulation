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

    // arrayList containing all agents in exposure distance to this one.
    ArrayList<Agent> agentsInExposureDistance = new ArrayList<>();

    public Agent(int positionX, int positionY, int exposureDistance, int incubation, int sickness, double recover, boolean sick){
        this.positionX = positionX;
        this.positionY = positionY;
        this.exposureDistance = exposureDistance;
        this.incubation = incubation;
        this.sickness = sickness;
        this.recover = determineIfRecovers(recover);
        this.sick = sick;
    }

    /*
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
        try{
            while(!dead && !immune){
                if(sick){
                    Thread.sleep(1000L * sickness);
                    sick = false;
                    if(recover) immune = true;
                    else {
                        dead = true;
                    }
                }
                else{
                    boolean infected = checkExposed();
                    if(infected){
                        vulnerable = true;
                        sleep(1000L * incubation);
                        vulnerable = false;
                        sick = true;
                    }
                }
                // used to avoid constant loop, minimise CPU usage a bit.
                sleep(3);
            }

            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * determineIfRecovers takes in the likelihood that an agent will die after being sick
    *
    * @Parameters
    * double recover
    *      double ranging from 0.0 - 1.0. Represents the percentage likelihood that agent will survive disease
    *
    * @return
    * boolean
    *      if agent will recover from disease.
    */
    private boolean determineIfRecovers(double recover){
        Random rand = new Random();

        double recoverPercentage = 100 * recover;
        if((rand.nextInt(100) + 1)  > recoverPercentage){
            return false;
        }
        else return true;
    }

    /*
     * checkExposed checks if any agents in an agents exposure distance is sick.
     *
     * @Parameters
     * void
     *
     * @return
     * boolean
     *      whether agent is in contact with a sick agent
     */
    public boolean checkExposed(){
        for(int i = 0; i < agentsInExposureDistance.size();){
            if(agentsInExposureDistance.get(i).sick){
                return true;
            }
        }
        return false;
    }

}