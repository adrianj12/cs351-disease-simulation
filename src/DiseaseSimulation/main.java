/*************************************************************************
 * Daniel Morales-Garcia
 *
 * Main.java is the main function of the program. Main is used as a connection
 * between the field and GUI.
 *
 *************************************************************************/


package DiseaseSimulation;

import java.time.Clock;

public class main {

    public static long startTime;

    // Field object that GUI will be constantly calling
    public static Field field;

    // the config file path passed in through command line argument
    public static String filePath;

    /*
     * Main verifies that a command line argument is passed in, then assigns the path to
     * a global variable String.
     *
     * @Parameters
     * String[] args
     *     receives full path of txt config file on system.
     *
     * @return
     * void
     */
    public static void main(String[] args) {

        if (args.length != 1) {

            System.out.println("There should be a single command line argument that is the path to config file.");
            System.out.println("Please refer to README to correctly run program.");
            System.exit(1);

        } else {

            filePath = args[0];
            field = new Field(filePath);

            // GUI creation
            GUI gui = new GUI();
            gui.open(field);

        }

    }

    public static void newGUI(){
        field = new Field(filePath);

        // GUI creation
        GUI gui = new GUI();
        gui.open(field);
    }

}