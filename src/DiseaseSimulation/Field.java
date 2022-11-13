/*************************************************************************
 * Daniel Morales-Garcia
 *
 * Field.java is used as a singular object and is the field in the which the
 * disease simulation takes place. This class takes in the path to a config
 * file int the constructor, reads simulation configurations, creates the
 * field in which simulation takes place, and creates all agents.
 *
 *************************************************************************/

package DiseaseSimulation;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class Field {

    // arrayList of all agents
    public ArrayList<Agent> allAgents;

    //height of field
    public int height;

    // width of field
    public int width;

    // row number if field is grid
    public int rows;

    //column number if field is grid
    public int columns;

    // constructor that calls takes in filepath to send to readConfigurations
    public Field(String filePath) {
        readConfigurations(filePath);
    }

    /*
     * readConfigurations takes in a String that is the path to the config file, reads the file and
     * parses the data. Then calls a function to create the agents.
     *
     * @Parameters
     * String configFile
     *      path to txt config file
     *
     * @return
     * void
     */
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
        } catch (InputMismatchException e){
            System.out.println("There was an input mismatch error.");
            System.out.println("Please refer to README to correctly run program.");
            System.exit(1);
        }

        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;

        createAgents(agentLocationType, width, height, exposureDistance, incubation, sickness, recover, rows, columns, agents, initialSick);

    }

    /*
     * createAgents creates all agents that will be displayed on field
     *
     * @Parameters
     * char agentLocationType
     *      determines play type of grid('g'), random('r'), or randomgrid('x')
     * int width
     *      width dimension of board
     * int height
     *      height dimension of board
     * int exposureDistance
     *      distance that an agent can get another agent sick
     * int incubation
     *      time that an agent takes to get sick after exposure
     * int sickess
     *      time that an agent is sick before dying or recovering
     * double recover
     *      likelihood that an agent will recover
     * int rows
     *      rows on field if grid option is chosen
     * int columns
     *      columns on field if grid option is chosen
     * int agents
     *      number of agents at play if specified
     * int initialSick
     *      number of initial sick agents
     *
     * @return
     * void
     */
    private void createAgents(char agentLocationType, int width, int height, int exposureDistance, int incubation,
                                     int sickness, double recover, int rows, int columns, int agents, int initialSick) {

        this.allAgents = new ArrayList<Agent>();

        if (agentLocationType == 'g') {
            /* calculation of how many vertexes of separation agents must be on row to be as far as possible but
               still in exposure distance */
            double singleRowHeight = ((double) height) / ((double) rows);
            int agentDistanceRows = (int)(exposureDistance / singleRowHeight);

            double singleColumnWidth = ((double) width) / ((double) columns);
            int agentDistanceColumns = (int)(exposureDistance / singleColumnWidth);

            // randomizing where sick agents will be on field
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
                    // distance of next agent on grid is met
                    if (i % agentDistanceRows == 0 && j % agentDistanceColumns == 0) {
                        Agent agent;
                        // sick agent is created
                        if(initialSick != 0 && sickAgentIndexes.contains(agentCounter)) {
                            agent = new Agent((int)(j * singleColumnWidth),(int)(height - (i * singleRowHeight)), exposureDistance, incubation, sickness, recover, true);
                        }
                        // non sick agent is created
                        else{
                            agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)), exposureDistance, incubation, sickness, recover, false);
                        }
                        allAgents.add(agent);
                        agentCounter++;
                    }
                }
            }
        //case for random selection
        } else if (agentLocationType == 'r') {
            Random rand = new Random();

            // randomizing agent locations
            for(int i = 0; i < agents; i++){
                int agentWidth = rand.nextInt(width+1);
                int agentHeight = rand.nextInt(height+1);
                Agent agent = new Agent(agentWidth, agentHeight, exposureDistance, incubation, sickness, recover, false);
                allAgents.add(agent);
            }

            //adding sick agents
            HashSet<Integer> alreadySickAgentIndexes = new HashSet<>();
            int index = 0;
            while(index < initialSick) {
                int sickAgentIndex = rand.nextInt(agents);
                if(!alreadySickAgentIndexes.contains(sickAgentIndex)){
                    alreadySickAgentIndexes.add(index);
                    allAgents.get(index).sick = true;
                    index++;
                }
            }
        // case for randomgrid selection
        } else {
            double singleRowHeight = ((double) height) / ((double) rows);
            int agentDistanceRows = (int)(exposureDistance / singleRowHeight);

            double singleColumnWidth = ((double) width) / ((double) columns);
            int agentDistanceColumns = (int)(exposureDistance / singleColumnWidth);

            ArrayList<Integer> allAgentsIndexRandomized = new ArrayList<>();
            // creating grid with all possible agent locations
            for(int i = 0; i < ((rows+1) * (columns+1)); i++) allAgentsIndexRandomized.add(0);
            // adding agents to arraylist (represented as 1's)
            for(int i = 0; i < agents; i++) allAgentsIndexRandomized.set(i, 1);
            // adding the sick agents to arraylist(represented as 2's)
            for(int i = 0; i < initialSick; i++) allAgentsIndexRandomized.set(i, 2);

            //randomizing Arraylist order
            Collections.shuffle(allAgentsIndexRandomized);

            int index = 0;
            for(int i = 0; i < rows+1; i++){
                for(int j = 0 ; j < columns+1; j++){
                    // non sick agent is added to grid position
                    if(allAgentsIndexRandomized.get(index) == 1) {
                        Agent agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)), exposureDistance, incubation, sickness, recover, false);
                        allAgents.add(agent);
                    }
                    // sick agent is added to grid position
                    else if(allAgentsIndexRandomized.get(index) == 2){
                        Agent agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)), exposureDistance, incubation, sickness, recover, true);
                        allAgents.add(agent);
                    }
                    index++;
                }
            }
        }

        findAgentsInProximity(exposureDistance);

    }

    /*
     * startAgents starts every agent thread.
     *
     * @Parameters
     * void
     *
     * @return
     * void
     */
    public void startAgents() {

        System.out.println("Starting agents...");

        for(int i = 0; i < allAgents.size(); i++){
            allAgents.get(i).start();
        }
    }

    /*
     * find AgentsInProximity finds all agents that are in exposure distance of each other
     *
     * @Parameters
     * int exposureDistance
     *      distance range that an agent can make another sick
     *
     * @return
     * void
     */
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