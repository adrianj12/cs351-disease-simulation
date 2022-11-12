package DiseaseSimulation;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Field {

    public ArrayList<Agent> allAgents;

    public Field(String filePath) {
        readConfigurations(filePath);
    }


    private void readConfigurations(String configFile) {
        Random rand = new Random();

        // initializing and setting default configuration values
        int width = 200;
        int height = 200;
        int exposureDistance = 20;
        int incubation = 5;
        int sickness = 10;
        double recover = 0.95;
        int rows = 0;
        int columns = 0;
        int agents = 100;
        int initialSick = 1;
        // determines play type of grid('g'), random('r'), or randomgrid('x'). default is random with 100 agents.
        char agentLocationType = 'r';


        try {
            File file = new File(configFile);
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String option = sc.next();

                // making sure word read in has no leading symbols. ("zero-width no-break space" was giving me issues)
                StringBuilder sb = new StringBuilder(option);
                if (!Character.isLetter(sb.charAt(0))) sb.deleteCharAt(0);
                option = sb.toString();

                switch (option) {
                    case ("dimensions"):
                        width = sc.nextInt();
                        height = sc.nextInt();
                        break;
                    case ("exposuredistance"):
                        exposureDistance = sc.nextInt();
                        break;
                    case ("incubation"):
                        incubation = sc.nextInt();
                        break;
                    case ("sickness"):
                        sickness = sc.nextInt();
                        break;
                    case ("recover"):
                        recover = sc.nextDouble();
                        break;
                    case ("grid"):
                        agentLocationType = 'g';
                        rows = sc.nextInt();
                        columns = sc.nextInt();
                        break;
                    case ("random"):
                        agentLocationType = 'r';
                        agents = sc.nextInt();
                        break;
                    case ("randomgrid"):
                        agentLocationType = 'x';
                        rows = sc.nextInt();
                        columns = sc.nextInt();
                        agents = sc.nextInt();
                        break;
                    case ("initialsick"):
                        initialSick = sc.nextInt();
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println("Please refer to README to correctly run program.");
            System.exit(1);
        }

        createAgents(agentLocationType, width, height, exposureDistance, incubation, sickness, recover, rows, columns, agents, initialSick);

    }

    private void createAgents(char agentLocationType, int width, int height, int exposureDistance, int incubation,
                                     int sickness, double recover, int rows, int columns, int agents, int initialSick) {

        this.allAgents = new ArrayList<Agent>();

        if (agentLocationType == 'g') {
            /* calculation of how many vertexes of separation agents must be on row to be as far as possible but
               still in exposure distance */
            double singleRowHeight = ((double) height) / ((double) rows);
            int agentDistanceRows = (int) (exposureDistance / singleRowHeight);

            double singleColumnHeight = ((double) width) / ((double) columns);
            int agentDistanceColumns = (int) (exposureDistance / singleColumnHeight);

            ArrayList<Integer> allAgentsIndexRandomized = new ArrayList<>();
            for (int i = 0; i < rows + 1; i++) {
                for (int j = 0; j < columns + 1; j++) {
                    if (i % agentDistanceRows == 0 && (j % agentDistanceColumns == 0)) {
                        allAgentsIndexRandomized.add(allAgentsIndexRandomized.size());
                    }
                }
            }
            Collections.shuffle(allAgentsIndexRandomized);
            HashSet<Integer> sickAgentIndexes = new HashSet<>();
            for(int i = 0; i < initialSick; i++) sickAgentIndexes.add(allAgentsIndexRandomized.get(i));

            int agentCounter = 0;
            for (int i = 0; i < rows + 1; i++) {
                for (int j = 0; j < columns + 1; j++) {
                    if (i % agentDistanceRows == 0 && (j % agentDistanceColumns == 0)) {
                        if(initialSick != 0 && sickAgentIndexes.contains(agentCounter)) {
                            Agent agent = new Agent((int) (j * singleColumnHeight), (int) (i * singleRowHeight), exposureDistance, incubation, sickness, recover, true);
                            allAgents.add(agent);
                        }
                        else{
                            Agent agent = new Agent((int) (j * singleColumnHeight), (int) (i * singleRowHeight), exposureDistance, incubation, sickness, recover, false);
                            allAgents.add(agent);
                        }
                        agentCounter++;
                    }
                }
            }
        } else if (agentLocationType == 'r') {
            // USE DISTANCE FORMULA TO CALCULATE THE AGENTS IN PROXIMITY
        } else {

        }

        findAgentsInProximity(exposureDistance);


        for(int i = 0; i < allAgents.get(3).agentsInExposureDistance.size(); i++) {
            System.out.print(allAgents.get(3).agentsInExposureDistance.get(i));
            System.out.print("  ");
        }
    }

    private void findAgentsInProximity(int exposureDistance){
        for(int i = 0; i < allAgents.size() - 1; i++){
            for(int j = i + 1; j < allAgents.size(); j++){
                // using distance formula to calculate distance between two agents
                double distanceXAxis = allAgents.get(j).positionX - allAgents.get(i).positionX;
                double distanceYAxis = allAgents.get(j).positionY - allAgents.get(i).positionY;
                double totalDistance = Math.sqrt(Math.pow(distanceXAxis, 2) + Math.pow(distanceYAxis, 2));

                if(totalDistance <= exposureDistance){
                    allAgents.get(i).agentsInExposureDistance.add(allAgents.get(j));
                    allAgents.get(j).agentsInExposureDistance.add(allAgents.get(i));
                }
            }
        }
    }
}