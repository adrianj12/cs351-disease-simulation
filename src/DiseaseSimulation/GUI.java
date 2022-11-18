/**
 *
 * @author Adrian Abeyta <ajabeyta@unm.edu>
 * @description Encapsulates the GUI. Provides Start, Stop, and Restart buttons to control the state of the
 *  simulation. Reads the Field and updates canvas display accordingly.
 * @name GUI
 * @usage Instantiate to create a window with simulation field and appropriate buttons, then run open() method
 *  with Field object as argument.
 *
 */
package DiseaseSimulation;

// JavaFX mechanics
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;

// JavaFX graphics
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// JavaFX nodes/components
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GUI extends Application {

    // Reference to simulation field object
    private static Field field;

    // Canvas field to be modified by multiple methods
    private static Canvas simField;
    private static GraphicsContext gc;

    // Agent state colors
    private static final Color healthy = Color.LAWNGREEN;
    private static final Color vulnerable = Color.YELLOW;
    private static final Color sick = Color.RED;
    private static final Color immune = Color.LIGHTBLUE;
    private static final Color dead = Color.BLACK;
    private static final Color asymptomatic = Color.ORANGE;

    public GUI() { }

    /**
     *
     * @description Creates JavaFX GUI components and displays initial simulation field
     * @param field Simulation field object containing all agent nodes
     *
     */
    public void open(Field field) {

        this.field = field;

        launch();

    }

    /**
     *
     * @description JavaFX parent method overridden to provide GUI components
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @TODO add log window pane to show agent within GUI
     * @TODO add color legend to help identify agent states to user
     *
     */
    @Override
    public void start(Stage stage) {

        stage.setTitle("Disease Simulation - by Adrian Abeyta and Daniel Morales-Garcia");
        stage.setResizable(false);

        BorderPane layout = new BorderPane();

        // Top title text
        VBox header = new VBox();
        Text title = new Text("Disease Simulation");
        title.setFont(Font.font(null, FontWeight.BOLD, 16));

        Text subtitle = new Text("press Start Simulation button to begin");

        header.getChildren().addAll(title, subtitle);
        BorderPane.setAlignment(header, Pos.CENTER);
        layout.setTop(header);

        // Set up blank white simulation canvas background
        simField = new Canvas(field.width + 10, field.height + 10);
        gc = simField.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, field.width + 10, field.height + 10);
        layout.setCenter(simField);

        // Set up the agents on the sim field canvas before sim is started
        drawAgents();

        // Bottom buttons
        HBox buttonBar = new HBox();
        Button stopButton = new Button("Stop");
        Button startButton = new Button("Start Simulation");
        Button restartButton = new Button("Reset");

        // Start button
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startButton.setDisable(true);
                stopButton.setDisable(false);
                main.startTime = System.currentTimeMillis();
                field.startAgents();
                timer time = new timer();
                time.start();
            }
        });

        // Reset/restart button
        restartButton.setDisable(true);
        restartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.stopAgents(field.allAgents);
                startButton.setDisable(false);
                stopButton.setDisable(true);
                // setting new canvas
                simField = new Canvas(field.width+10, field.height+10);
                gc = simField.getGraphicsContext2D();
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, field.width+10, field.height+10);
                layout.setCenter(simField);
                try {
                    main.newSimulation();
                    stage.close();
                } catch(IllegalStateException e){}
            }
        });

        // Stop button
        stopButton.setDisable(true);
        stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.stopAgents(field.allAgents);
                stopButton.setDisable(true);
                restartButton.setDisable(false);
            }
        });

        // Set up bottom buttons
        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        buttonBar.getChildren().addAll(startButton, stopButton, restartButton);
        layout.setBottom(buttonBar);

        // Set up scene
        Scene scene = new Scene(layout, 800, 800);
        stage.setScene(scene);
        stage.show();

    }

    /**
     *
     * @description helper method to call JavaFX GUI components to redraw
     *
     */
    private class timer extends AnimationTimer {
        @Override
        public void handle(long now) {
            drawAgents();
        }
    }

    /**
     *
     * @description re/draws all agents on the simulation canvas/field
     *
     */
    private void drawAgents() {

        for(Agent current : field.allAgents) {

            drawAgent(current);

        }

    }

    /**
     *
     * @description helper method to draw a singular agent with appropriate coloring; color is customizable thru class
     *  variables at top of class body
     * @param agent reference to agent object to draw onto canvas
     *
     */
    private void drawAgent(Agent agent) {

        State state = State.Normal;
        if (agent.dead) {
            state = State.Dead;
        } else if (agent.immune) {
            state = State.Immune;
        } else if (agent.vulnerable) {
            state = State.Vulnerable;
        }else if (agent.asymptomatic) {
            state = State.Asymptomatic;
        } else if (agent.sick) {
            state = State.Sick;
        }

        gc.setFill(state.color);
        gc.fillOval(agent.positionX, agent.positionY, 10, 10);

    }

    /**
     *
     * @description helper enum to encapsulate agent state associated with color
     *
     */
    private enum State {

        Normal(healthy),
        Vulnerable(vulnerable),
        Sick(sick),
        Asymptomatic(asymptomatic),
        Dead(dead),
        Immune(immune);

        public final Color color;

        State(Color color) {

            this.color = color;

        }

    }

}
