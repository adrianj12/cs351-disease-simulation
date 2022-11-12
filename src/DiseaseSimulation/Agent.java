package DiseaseSimulation;

import java.util.ArrayList;
import java.util.Random;

public class Agent extends Thread{

    public int positionX;
    public int positionY;
    public int exposureDistance;
    public int incubation;
    public int sickness;
    public boolean recover;

    public boolean vulnerable = false;
    public boolean sick;
    public boolean immune = false;
    public boolean dead = false;

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

    @Override
    public void run(){
        try{
            while(!dead && !immune){
                if(sick){
                    sleep(1000L * sickness);
                    sick = false;
                    if(recover) immune = true;
                    else {
                        dead = true;
                    }
                }
                else{
                    boolean nowSick = checkExposed();
                    if(nowSick){
                        vulnerable = true;
                        sleep(1000L * incubation);
                        vulnerable = false;
                        sick = true;
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean determineIfRecovers(double recover){
        Random rand = new Random();

        double recoverPercentage = 100 * recover;
        if((rand.nextInt(100) + 1)  > recoverPercentage){
            return false;
        }
        else return true;
    }

    public boolean checkExposed(){
        // CHECKS IF AGENTS IN PROXIMITY ARE SICK
        return false;
    }


}