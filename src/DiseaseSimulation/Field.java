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

import java.io.File;
import java.io.FileNotFoundException;
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

    /**
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
        double asymptomatic = 0.05;
        int initialImmune = 2;
        double longTermHealthIssues = 0.1;

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
                    case ("initialimmune"):
                        initialImmune = sc.nextInt();
                        break;
                    case("asymptomatic"):
                        asymptomatic = sc.nextDouble();
                        break;
                    case("agents"):
                        agents = sc.nextInt();
                        break;
                    case("longterm"):
                        longTermHealthIssues = sc.nextInt();
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

        // dimensions restrictions
        if (width < 100 || width > 770 || height < 100 || height > 770){
            System.out.println("Error, dimensions must be greater than 100, and less than 770");
            System.exit(1);
        }
        if(agentLocationType == 'x' || agentLocationType == 'g'){
            if(rows < 2 || columns < 2){
                System.out.println("Error, rows and columns must be greater than 1 each");
                System.exit(1);
            }
        }

        // agent restrictions
        if (agents > 120){
            System.out.println("Error, cannot have more than 120 agents");
        }

        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;

        createAgents(agentLocationType, width, height, exposureDistance, incubation, sickness, recover, rows, columns,
                agents, initialSick, asymptomatic, initialImmune, longTermHealthIssues);

    }

    /**
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
                                     int sickness, double recover, int rows, int columns, int agents, int initialSick,
                                    double asymtomatic, int initialImmune, double longTermHealthIssues) {

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
            try {
                for (int i = 0; i < rows + 1; i++) {
                    for (int j = 0; j < columns + 1; j++) {
                        if (i % agentDistanceRows == 0 && (j % agentDistanceColumns == 0)) {
                            allAgentsIndexRandomized.add(allAgentsIndexRandomized.size());
                        }
                    }
                }
            }
            catch(ArithmeticException e){
                System.out.println("Error, your entered data has set up a simulation where starting agents cannot" +
                        " get each other sick\nTip: lower dimension sizes and increase exposure distance/grid dimensions");
                System.exit(1);
            }
            Collections.shuffle(allAgentsIndexRandomized);
            HashSet<Integer> sickAgentIndexes = new HashSet<>();
            HashSet<Integer> initialImmuneIndexes = new HashSet<>();
            for(int i = 0; i < initialSick + initialImmune; i++) {
                if(i < initialSick){
                    sickAgentIndexes.add(allAgentsIndexRandomized.get(i));
                }
                else{
                    initialImmuneIndexes.add(allAgentsIndexRandomized.get(i));
                }
            }

            int agentNum = 0;
            for (int i = 0; i < rows + 1; i++) {
                for (int j = 0; j < columns + 1; j++) {
                    // distance of next agent on grid is met
                    try {
                        if (i % agentDistanceRows == 0 && j % agentDistanceColumns == 0) {
                            Agent agent;
                            // sick agent is created
                            if (initialSick != 0 && sickAgentIndexes.contains(agentNum)) {
                                agent = new Agent((int) (j * singleColumnWidth), (int) (height - (i * singleRowHeight)),
                                        exposureDistance, incubation, sickness, recover, true, agentNum, asymtomatic,
                                        false, longTermHealthIssues);
                            }
                            // initially immune agent is added
                            else if (initialImmune != 0 && initialImmuneIndexes.contains(agentNum)) {
                                agent = new Agent((int) (j * singleColumnWidth), (int) (height - (i * singleRowHeight)),
                                        exposureDistance, incubation, sickness, recover, false, agentNum, asymtomatic,
                                        true, longTermHealthIssues);
                            }
                            // non-sick agent is created
                            else {
                                agent = new Agent((int) (j * singleColumnWidth), (int) (height - (i * singleRowHeight)),
                                        exposureDistance, incubation, sickness, recover, false, agentNum, asymtomatic,
                                        false, longTermHealthIssues);
                            }
                            allAgents.add(agent);
                            agentNum++;
                        }
                    }
                    catch(ArithmeticException e){
                        System.out.println("Error,your entered data has set up a simulation where starting agents cannot" +
                                " get eachother sick\nTip: lower dimension sizes and increase exposure distance/grid dimensions");
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
                Agent agent = new Agent(agentWidth, agentHeight, exposureDistance, incubation, sickness, recover,
                        false, i, asymtomatic, false, longTermHealthIssues);
                allAgents.add(agent);
            }

            //adding sick and initially immune agents
            HashSet<Integer> alreadySickAgentIndexes = new HashSet<>();
            HashSet<Integer> alreadyImmuneAgentIndexes = new HashSet<>();

            int index = 0;
            while(index < initialSick + initialImmune) {
                int agentIndex = rand.nextInt(agents);
                if(index < initialSick){
                    if(!alreadySickAgentIndexes.contains(agentIndex)){
                        alreadySickAgentIndexes.add(agentIndex);
                        allAgents.get(agentIndex).sick = true;
                        index++;
                    }
                }
                else {
                    if (!alreadyImmuneAgentIndexes.contains(agentIndex)) {
                        alreadyImmuneAgentIndexes.add(agentIndex);
                        allAgents.get(agentIndex).immune = true;
                        index++;
                    }
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
            for(int i = 0; i < initialSick + initialImmune; i++) {
                if(i < initialSick) {
                    allAgentsIndexRandomized.set(i, 2);
                }
                else{
                    allAgentsIndexRandomized.set(i, 3);
                }
            }

            //randomizing Arraylist order
            Collections.shuffle(allAgentsIndexRandomized);

            System.out.println(allAgentsIndexRandomized.size());
            int agentNum = 0;
            int index = 0;
            for(int i = 0; i < rows+1; i++){
                for(int j = 0 ; j < columns+1; j++){
                    // non-sick agent is added to grid position
                    if(allAgentsIndexRandomized.get(index) == 1) {
                        Agent agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)),
                                exposureDistance, incubation, sickness, recover, false, agentNum, asymtomatic,
                                false, longTermHealthIssues);
                        allAgents.add(agent);
                        agentNum++;
                    }
                    // sick agent is added to grid position
                    else if(allAgentsIndexRandomized.get(index) == 2){
                        Agent agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)),
                                exposureDistance, incubation, sickness, recover, true, agentNum, asymtomatic,
                                false, longTermHealthIssues);
                        allAgents.add(agent);
                        agentNum++;
                    }
                    // initial immune agent is added to grid
                    else if(allAgentsIndexRandomized.get(index) == 3){
                        Agent agent = new Agent((int)(j * singleColumnWidth), (int)(height - (i * singleRowHeight)),
                                exposureDistance, incubation, sickness, recover, false, agentNum, asymtomatic,
                                true, longTermHealthIssues);
                        allAgents.add(agent);
                        agentNum++;
                    }
                    index++;
                }
            }
        }
        findAgentsInProximity(exposureDistance);

    }

    /**
     * startAgents starts sick agents.
     *
     * @Parameters
     * void
     *
     * @return
     * void
     */
    public void startAgents() {

        System.out.println("\nStarting agents...\n");
        for(int i = 0; i < allAgents.size(); i++) {
            allAgents.get(i).start();
        }

        for (int i = 0; i < allAgents.size(); i++) {
            if (allAgents.get(i).sick) {
                allAgents.get(i).in.add(1);
            }
        }
    }

    /**
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

                // adding agents
                if(totalDistance <= exposureDistance){
                    allAgents.get(i).agentsInExposureDistance.add(allAgents.get(j));
                    allAgents.get(j).agentsInExposureDistance.add(allAgents.get(i));
                }
            }
        }
    }

    // stops all agents
    public void stopAgents(ArrayList<Agent> allAgents){
        for(int i = 0; i < allAgents.size(); i++){
            allAgents.get(i).stop();
        }
    }
}