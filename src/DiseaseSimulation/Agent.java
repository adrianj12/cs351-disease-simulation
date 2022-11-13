package DiseaseSimulation;

import java.util.ArrayList;
import java.util.Random;

public class Agent implements Runnable {

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
                        Thread.sleep(1000L * incubation);
                        vulnerable = false;
                        sick = true;
                    }
                }
            }

            Thread.sleep(3);
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
        for(int i = 0; i < agentsInExposureDistance.size();){
            if(agentsInExposureDistance.get(i).sick){
                return true;
            }
        }
        return false;
    }

}