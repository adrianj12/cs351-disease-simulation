package DiseaseSimulation;

public class main {

    // Field object that gui will be constantly calling
    public static Field field;
    public static String filePath;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("There should be a single command line argument that is the path to config file.");
            System.out.println("Please refer to README to correctly run program.");
            System.exit(1);
        } else {
            filePath = args[0];
            // CALL TO GUI IS MADE HERE
        }
        startSimulation();
    }

    // GUI WILL CALL THIS TO START SIMULATION
    public static void startSimulation() {
        field = new Field(filePath);
    }
}